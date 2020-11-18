package org.benetech.servicenet.mother;

import org.benetech.servicenet.domain.PaymentAccepted;

public final class PaymentAcceptedMother {

    public static final String PAYMENT_ACCEPTED = "Accepted Payment";

    public static PaymentAccepted createDefault() {
        return new PaymentAccepted()
            .payment(PAYMENT_ACCEPTED);
    }

    private PaymentAcceptedMother() {
    }
}
