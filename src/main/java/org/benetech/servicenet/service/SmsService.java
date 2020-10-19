package org.benetech.servicenet.service;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.benetech.servicenet.errors.InternalServerErrorException;
import org.benetech.servicenet.service.dto.SmsMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class SmsService {
    private final Logger log = LoggerFactory.getLogger(SmsService.class);

    @Value("#{systemEnvironment['TWILIO_ACCOUNT_SID']}")
    private String acccountSid;

    @Value("#{systemEnvironment['TWILIO_AUTH_TOKEN']}")
    private String authToken;

    @Value("#{systemEnvironment['TWILIO_PHONE_NUMBER']}")
    private String fromNumber;

    public void send(SmsMessage sms) {
        log.debug("Sending SMS message to " + sms.getTo());
        if (StringUtils.isEmpty(acccountSid)) {
            log.warn("Could not send a message to " + sms.getTo() + " - Twilio credentials are missing.");
            return;
        }
        Twilio.init(acccountSid, authToken);

        Message message = Message.creator(new PhoneNumber(sms.getTo()), new PhoneNumber(fromNumber), sms.getMessage())
            .create();
        if (StringUtils.isEmpty(message.getErrorMessage())) {
            log.info("SMS Message sent to " + sms.getTo() + ", id: " + message.getSid());
        } else {
            throw new InternalServerErrorException("Could not send message: " + message.getErrorMessage());
        }
    }
}
