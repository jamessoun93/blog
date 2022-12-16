package chap03;

import java.time.LocalDate;
import java.time.YearMonth;

public class ExpiryDateCalculator {

    public LocalDate calculateExpiryDate(PaymentData paymentData) {
        final int monthToAdd = paymentData.getPaidAmount() == 100_000 ? 12 : paymentData.getPaidAmount() / 10_000;

        if (paymentData.getFirstBillingDate() != null) {
            return getExpiryDateUsingFirstBillingDate(paymentData, monthToAdd);
        }
        return paymentData.getDatePaid().plusMonths(monthToAdd);
    }

    private LocalDate getExpiryDateUsingFirstBillingDate(PaymentData paymentData, int monthToAdd) {
        LocalDate candidateExp = paymentData.getDatePaid().plusMonths(monthToAdd);
        final int dayOfFirstBillingDate = paymentData.getFirstBillingDate().getDayOfMonth();

        if (dayOfFirstBillingDate != candidateExp.getDayOfMonth()) {
            final int lenOfCandidateMonth = YearMonth.from(candidateExp).lengthOfMonth();
            if (lenOfCandidateMonth < dayOfFirstBillingDate) {
                return candidateExp.withDayOfMonth(lenOfCandidateMonth);
            }
            return candidateExp.withDayOfMonth(dayOfFirstBillingDate);
        }

        return candidateExp;
    }
}
