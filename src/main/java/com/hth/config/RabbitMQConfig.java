package com.hth.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Fanout 直译为 “扇出” 但是大家更多的会把它叫做广播或者发布与订阅，
 * 它是一种没有路由key的模式，生产者将消息发送给交换机，交换机会把所有消息复制同步到所有与它绑定过的队列上，
 * 而每个队列只能有一个消费者拿到这条消息，如果在一个消费者连接中，创建多个通道，则会出现争抢消息的结果
 * fanout_msg_exchange是rabbitmq自带的交换机，无需创建
 * msg.fanout.queue也是自带的队列，默认绑定fanout_msg_exchange
 */
@Configuration
public class RabbitMQConfig {
    @Bean
    public FanoutExchange fanoutExchange(){
        return new FanoutExchange("fanout_msg_exchange",true,false);
    }
    @Bean
    public Queue msgQueue(){
        return new Queue("msg.fanout.queue",true);
    }
    @Bean
    public Binding msgBinding(){
        return BindingBuilder.bind(msgQueue()).to(fanoutExchange());
    }
}
