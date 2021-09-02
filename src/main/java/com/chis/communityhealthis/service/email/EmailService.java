package com.chis.communityhealthis.service.email;

import com.chis.communityhealthis.model.email.MailRequest;

import java.util.Map;

public interface EmailService {
   void sendEmailWithTemplate(MailRequest mailRequest, Map<String, Object> model);
}
