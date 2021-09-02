package com.chis.communityhealthis.service.email;

import com.chis.communityhealthis.model.email.MailRequest;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Service
public class EmailServiceImpl implements EmailService{

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private Configuration config;

    @Override
    public void sendEmailWithTemplate(MailRequest mailRequest, Map<String, Object> model) {
        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());

            Template template = config.getTemplate(mailRequest.getTemplateFileName());
            String html = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);

            helper.setTo(mailRequest.getTo());
            helper.setText(html, true);
            helper.setSubject(mailRequest.getSubject());
            helper.setFrom(mailRequest.getFrom());
            mailSender.send(message);

        } catch (MessagingException | TemplateException | IOException e) {
            e.printStackTrace();
        }
    }
}
