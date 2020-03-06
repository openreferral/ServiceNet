package org.benetech.servicenet.service;

public interface SendGridMailService {
    void sendMail(String from, String to, String subject, String text);

    void sendFeedBackMail(String from, String text);
}
