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
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SendGridMailServiceImpl implements SendGridMailService {

    private final String MAIL_ENDPOINT = "mail/send";

    private final String MAIL_CONTENT_TYPE = "text/plain";

    private final String RECEIVER_MAIL = "feedback@benetech.org";

    private final String SUBJECT = "Benetech ServiceNet Feedback";

    @Autowired
    private SendGrid sendGrid;

    @Override
    public void sendMail(String from, String to, String subj, String text) {
        Email fromWho = new Email(this.getValidEmailAddress(from));
        Email toWho = new Email(to);
        Content content = new Content(MAIL_CONTENT_TYPE, text);
        Mail mail = new Mail(fromWho, subj, toWho, content);

        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint(MAIL_ENDPOINT);
            request.setBody(mail.build());
            Response response = sendGrid.api(request);
            log.debug(response.getStatusCode() + " " + response.getBody() + "" + response.getHeaders());
        } catch (IOException ex) {
            log.error(ex.getMessage(), ex);
        }
    }

    @Override
    public void sendFeedBackMail(String from, String text) {
        this.sendMail(from, RECEIVER_MAIL, SUBJECT, text);
    }

    private String getValidEmailAddress(String email) {
        if (StringUtils.isNotEmpty(email)) {
            try {
                InternetAddress emailAddr = new InternetAddress(email);
                emailAddr.validate();
            } catch (AddressException ex) {
                return RECEIVER_MAIL;
            }
        } else {
            return RECEIVER_MAIL;
        }

        return email;
    }
}
