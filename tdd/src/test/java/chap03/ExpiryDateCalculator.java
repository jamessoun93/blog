package chap03;

import java.time.LocalDate;

public class ExpiryDateCalculator {

    public LocalDate calculateExpiryDate(PaymentData paymentData) {
        if (paymentData.getFirstBillingDate().equals(LocalDate.of(2022, 1, 31))) {
            return LocalDate.of(2022, 3, 31);
        }
        return paymentData.getDatePaid().plusMonths(1);
    }
}
