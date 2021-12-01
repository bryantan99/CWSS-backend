package com.chis.communityhealthis.service.sms;

public interface SmsService {
    void sendSms(String to, String msg) throws Exception;
}
