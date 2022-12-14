package chap03;

import java.time.LocalDate;

public class PaymentData {
    private LocalDate firstBillingDate;
    private LocalDate datePaid;
    private int paidAmount;

    private PaymentData() {}

    public PaymentData(LocalDate firstBillingDate, LocalDate datePaid, int paidAmount) {
        this.firstBillingDate = firstBillingDate;
        this.datePaid = datePaid;
        this.paidAmount = paidAmount;
    }

    public LocalDate getFirstBillingDate() {
        return firstBillingDate;
    }

    public LocalDate getDatePaid() {
        return datePaid;
    }

    public int getPaidAmount() {
        return paidAmount;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private PaymentData paymentData = new PaymentData();

        public Builder firstBillingDate(LocalDate firstBillingDate) {
            paymentData.firstBillingDate = firstBillingDate;
            return this;
        }
        public Builder datePaid(LocalDate datePaid) {
            paymentData.datePaid = datePaid;
            return this;
        }

        public Builder paidAmount(int paidAmount) {
            paymentData.paidAmount = paidAmount;
            return this;
        }

        public PaymentData build() {
            return paymentData;
        }
    }
}
