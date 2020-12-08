package org.benetech.servicenet;

import org.benetech.servicenet.errors.BadRequestAlertException;
import org.benetech.servicenet.service.SmsService;
import org.benetech.servicenet.service.dto.SmsMessage;
import org.junit.platform.commons.util.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service class used only for tests, with some methods mocked
 */
@Service
@Transactional
public class TestSmsService extends SmsService {

    public void send(SmsMessage sms) {
        if (StringUtils.isBlank(sms.getTo()) || StringUtils.isBlank(sms.getMessage())) {
            throw new BadRequestAlertException("Invalid phone number or empty message", "sms", "invalidphone");
        }
    }
}
