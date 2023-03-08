package com.hth.log;


import com.alibaba.fastjson.JSON;
import com.hth.entity.Msg;
import com.hth.dto.RegisterDto;
import com.hth.entity.Syslogger;
import com.hth.entity.User;
import com.hth.mapper.SysloggerMapper;
import com.hth.util.IpUtil;
import com.hth.util.JWTUtil;
import org.apache.shiro.SecurityUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.Map;

/**
 * 日志
 * 在controller中，添加了 @LogWeb的注解，都会走这里
 */
@Aspect 
@Component 
public class LogAspect
{ 
//    private static final Logger logger = LoggerFactory.getLogger(SysAspect.class);
    @Autowired
    SysloggerMapper logMapper;
    //线程本地化startTime，每个线程的startTime不会相互影响
    ThreadLocal<Long> startTime = new ThreadLocal<>(); 

    private Syslogger webLogger;
    //定义切点，为标注了Logweb注解的方法
    @Pointcut("@annotation(com.hth.log.Logweb)")
    public void LogPointcut(){}

    //运行到切点前的操作
    @Before("LogPointcut()") 
    public void doBefore(JoinPoint joinPoint) throws Exception
    {

        webLogger=new Syslogger();
        startTime.set(System.currentTimeMillis()); 

        ServletRequestAttributes attribute = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        HttpServletRequest request = attribute.getRequest();
        //Log注解
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature)signature;
        Method method = methodSignature.getMethod();
        String descr = method.getAnnotation(Logweb.class).value();
        webLogger.setDescription(descr);
        webLogger.setCreateTime(new Date());
        webLogger.setIp(IpUtil.getIpAddr(request));

        webLogger.setUrl(request.getRequestURL().toString());
        webLogger.setClassMethod(signature.getDeclaringTypeName() + "." + signature.getName());
        webLogger.setRequestType(request.getMethod());

        String methodName=signature.getName();
//        System.out.println(methodName);
        if(methodName.equals("register"))
        {
            Object[] args = joinPoint.getArgs();
            RegisterDto dto= (RegisterDto) args[0];
            webLogger.setParams("email:"+dto.getEmail()+",userName:"+dto.getUsername());
        }
        else if(methodName.equals("login")||methodName.equals("updatePassword")){
            webLogger.setParams("");
        }
        else if(joinPoint.getArgs()!=null){
            Object[] args = joinPoint.getArgs();
            Object[] arguments = new Object[args.length];
            for (int i = 0; i < args.length; i++) {
                //将不能序列化的请求，响应以及上传的文件排除掉
                if (args[i] instanceof ServletRequest || args[i] instanceof ServletResponse || args[i] instanceof MultipartFile) {
                    //ServletRequest不能序列化，从入参里排除，否则报异常：java.lang.IllegalStateException: It is illegal to call this method if the current request is not in asynchronous mode (i.e. isAsyncStarted() returns false)
                    //ServletResponse不能序列化 从入参里排除，否则报异常：java.lang.IllegalStateException: getOutputStream() has already been called for this response
                    continue;
                }
                arguments[i] = args[i];
            }
            if (arguments != null){
                webLogger.setParams(JSON.toJSONString(arguments));
            }

        }

        else
            webLogger.setParams("");

        webLogger.setLogType(1);


    }
    //抛出异常的处理
    @AfterThrowing(throwing="ex" ,pointcut = "LogPointcut()")
    void ExceptionPrintLog(Throwable ex){
        //设置用户id
        String token=(String) SecurityUtils.getSubject().getPrincipal();
        if(token!=null&&!token.equals(""))
        {
            Integer userId = JWTUtil.getUserId(token);
            if(userId!=null)
                webLogger.setUid(userId);
        }
        webLogger.setLogType(2);//异常的type为2
        webLogger.setExcTime((int) (System.currentTimeMillis()-startTime.get()));
        webLogger.setResponse(JSON.toJSONString(ex));
        logMapper.insert(webLogger);
    }

    @AfterReturning(returning = "ret", pointcut = "LogPointcut()") 
    public void doAfterReturning(Object ret)
    {   //设置用户id
        String token=(String) SecurityUtils.getSubject().getPrincipal();
        if(token!=null&&!token.equals(""))
        {
            Integer userId = JWTUtil.getUserId(token);
            if(userId!=null)
                webLogger.setUid(userId);
        }
        //返回类型为Msg才保存
        if(ret instanceof Msg){
            Map<String, Object> data = ((Msg) ret).getData();
            if(data.containsKey("token")){
                data.remove(token);
                User user= (User) data.get("user");
                webLogger.setUid(user.getId());
            }
            ((Msg) ret).setData(data);
        }
        webLogger.setResponse(JSON.toJSONString(ret)+"");
        webLogger.setExcTime((int) (System.currentTimeMillis()-startTime.get()));
        System.out.println(webLogger);
        logMapper.insert(webLogger);
    } 
} 
