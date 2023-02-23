package com.hth.service;

public interface EmailService {
    void sendRegisterEmail(String toEmail) ;
    void sendPsw(String email,String psw) ;
}