package org.benetech.servicenet.service;

public interface SendGridMailService {
    void sendMail(String from, String to, String subject, String content);

    void sendFeedback(String from, String content);
}
