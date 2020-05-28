package org.benetech.servicenet.service.impl;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import java.io.IOException;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.benetech.servicenet.service.SendGridMailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SendGridMailServiceImpl implements SendGridMailService {

    private static final String MAIL_ENDPOINT = "mail/send";

    private static final String MAIL_CONTENT_TYPE = "text/plain";

    @Value("${feedback.receiver-address:feedback@benetech.org}")
    private String receiverAddress;

    @Value("${feedback.feedback-subject:Benetech ServiceNet Feedback}")
    private String subject;

    @Autowired
    private SendGrid sendGrid;

    @Override
    public void sendMail(String from, String to, String subj, String content) {
        Email fromEmail = new Email(this.getValidEmailAddress(from));
        Email toEmail = new Email(to);
        Content mailContent = new Content(MAIL_CONTENT_TYPE, content);
        Mail mail = new Mail(fromEmail, subj, toEmail, mailContent);

        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint(MAIL_ENDPOINT);
            request.setBody(mail.build());
            Response response = sendGrid.api(request);
            log.debug(response.getStatusCode() + " " + response.getBody() + " " + response.getHeaders());
        } catch (IOException ex) {
            log.error(ex.getMessage(), ex);
        }
    }

    @Override
    public void sendFeedback(String from, String content) {
        this.sendMail(from, receiverAddress, subject, content);
    }

    private String getValidEmailAddress(String email) {
        if (StringUtils.isNotEmpty(email)) {
            try {
                InternetAddress emailAddr = new InternetAddress(email);
                emailAddr.validate();
            } catch (AddressException ex) {
                return receiverAddress;
            }
        } else {
            return receiverAddress;
        }

        return email;
    }
}
