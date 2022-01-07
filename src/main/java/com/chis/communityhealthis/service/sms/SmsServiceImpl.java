package com.chis.communityhealthis.service.sms;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SmsServiceImpl implements SmsService{

    @Value("${twilio.account.sid}")
    private String ACCOUNT_SID;

    @Value("${twilio.auth.token}")
    private String AUTH_TOKEN;

    public static final String FROM_PHONE_NO = "+13253355815";
    public static final String MALAYSIA_COUNTRY_CODE = "MY";

    @Override
    public void sendSms(String toPhoneNo, String msg) throws Exception {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
        try {
            String e614FormattedPhoneNo = toE614Format(toPhoneNo);
            PhoneNumber toPhoneNumber = new PhoneNumber(e614FormattedPhoneNo);
            PhoneNumber fromPhoneNumber = new PhoneNumber(FROM_PHONE_NO);
            Message message = Message.creator(toPhoneNumber, fromPhoneNumber, msg).create();
            System.out.println(message.getSid());
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    private String toE614Format(String toPhoneNo) throws Exception {
        PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();
        try {
            Phonenumber.PhoneNumber malaysiaPhoneNo = phoneNumberUtil.parse(toPhoneNo, MALAYSIA_COUNTRY_CODE);
            return phoneNumberUtil.format(malaysiaPhoneNo, PhoneNumberUtil.PhoneNumberFormat.E164);
        } catch (NumberParseException e) {
            throw new Exception(e.toString());
        }
    }
}
