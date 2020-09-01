package org.benetech.servicenet.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

import io.github.jhipster.config.JHipsterProperties;
import java.io.ByteArrayOutputStream;
import javax.mail.Multipart;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import org.benetech.servicenet.ServiceNetApp;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ServiceNetApp.class)
public class MailServiceIntTest {

    @Autowired
    private JHipsterProperties jHipsterProperties;

    @Spy
    private JavaMailSenderImpl javaMailSender;

    @Captor
    private ArgumentCaptor<MimeMessage> messageCaptor;

    private MailService mailService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        doNothing().when(javaMailSender).send(any(MimeMessage.class));
        mailService = new MailService(jHipsterProperties, javaMailSender);
    }

    @Test
    public void testSendEmail() throws Exception {
        MimeMessage message = sendMail(false);
        assertThat(message.getDataHandler().getContentType()).isEqualTo("text/plain; charset=UTF-8");
    }

    @Test
    public void testSendHtmlEmail() throws Exception {
        MimeMessage message = sendMail(true);
        assertThat(message.getDataHandler().getContentType()).isEqualTo("text/html;charset=UTF-8");
    }

    @Test
    public void testSendMultipartEmail() throws Exception {
        MimeBodyPart part = sendMultipartEmail(false);
        assertThat(part.getDataHandler().getContentType()).isEqualTo("text/plain; charset=UTF-8");
    }

    @Test
    public void testSendMultipartHtmlEmail() throws Exception {
        MimeBodyPart part = sendMultipartEmail(true);
        assertThat(part.getDataHandler().getContentType()).isEqualTo("text/html;charset=UTF-8");
    }

    @Test
    public void testSendEmailWithException() {
        doThrow(MailSendException.class).when(javaMailSender).send(any(MimeMessage.class));
        mailService.sendEmail("john.doe@example.com", "testSubject", "testContent", false, false);
    }

    private MimeMessage sendMail(boolean isHtml) throws Exception {
        mailService.sendEmail("john.doe@example.com", "testSubject", "testContent", false, isHtml);
        verify(javaMailSender).send(messageCaptor.capture());
        MimeMessage message = messageCaptor.getValue();
        assertThat(message.getSubject()).isEqualTo("testSubject");
        assertThat(message.getAllRecipients()[0].toString()).isEqualTo("john.doe@example.com");
        assertThat(message.getFrom()[0].toString()).isEqualTo("test@localhost");
        assertThat(message.getContent()).isInstanceOf(String.class);
        assertThat(message.getContent().toString()).isEqualTo("testContent");

        return message;
    }

    private MimeBodyPart sendMultipartEmail(boolean isHtml) throws Exception {
        mailService.sendEmail("john.doe@example.com", "testSubject", "testContent", true, isHtml);
        verify(javaMailSender).send(messageCaptor.capture());
        MimeMessage message = messageCaptor.getValue();
        MimeMultipart mp = (MimeMultipart) message.getContent();
        MimeBodyPart part = (MimeBodyPart) ((MimeMultipart) mp.getBodyPart(0).getContent()).getBodyPart(0);
        ByteArrayOutputStream aos = new ByteArrayOutputStream();
        part.writeTo(aos);
        assertThat(message.getSubject()).isEqualTo("testSubject");
        assertThat(message.getAllRecipients()[0].toString()).isEqualTo("john.doe@example.com");
        assertThat(message.getFrom()[0].toString()).isEqualTo("test@localhost");
        assertThat(message.getContent()).isInstanceOf(Multipart.class);
        assertThat(aos.toString()).isEqualTo("\r\ntestContent");

        return part;
    }
}
