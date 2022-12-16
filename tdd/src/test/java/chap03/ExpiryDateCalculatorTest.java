package chap03;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ExpiryDateCalculatorTest {

    @Test
    void 만원_납부시_한달뒤가_만료일() {
        final PaymentData pd1 = PaymentData.builder()
                .datePaid(LocalDate.of(2022, 1, 1))
                .paidAmount(10_000)
                .build();
        final PaymentData pd2 = PaymentData.builder()
                .datePaid(LocalDate.of(2022, 3, 1))
                .paidAmount(10_000)
                .build();

        assertExpiryDate(pd1, LocalDate.of(2022, 2, 1));
        assertExpiryDate(pd2, LocalDate.of(2022, 4, 1));
    }

    @Test
    void 납부일과_한달_뒤_날짜가_같지_않음() {
        final PaymentData pd1 = PaymentData.builder()
                .datePaid(LocalDate.of(2022, 3, 31))
                .paidAmount(10_000)
                .build();
        final PaymentData pd2 = PaymentData.builder()
                .datePaid(LocalDate.of(2022, 8, 31))
                .paidAmount(10_000)
                .build();
        final PaymentData pd3 = PaymentData.builder()
                .datePaid(LocalDate.of(2022, 1, 31))
                .paidAmount(10_000)
                .build();
        final PaymentData pd4 = PaymentData.builder()
                .datePaid(LocalDate.of(2024, 1, 31))
                .paidAmount(10_000)
                .build();

        assertExpiryDate(pd1, LocalDate.of(2022, 4, 30));
        assertExpiryDate(pd2, LocalDate.of(2022, 9, 30));
        assertExpiryDate(pd3, LocalDate.of(2022, 2, 28));
        assertExpiryDate(pd4, LocalDate.of(2024, 2, 29));
    }

    @Test
    void 첫_납부일과_만료일_일자가_다를때_만원_납부() {
        final PaymentData pd1 = PaymentData.builder()
                .firstBillingDate(LocalDate.of(2022, 1, 31))
                .datePaid(LocalDate.of(2022, 2, 28))
                .paidAmount(10_000)
                .build();
        final PaymentData pd2 = PaymentData.builder()
                .firstBillingDate(LocalDate.of(2022, 1, 30))
                .datePaid(LocalDate.of(2022, 2, 28))
                .paidAmount(10_000)
                .build();
        final PaymentData pd3 = PaymentData.builder()
                .firstBillingDate(LocalDate.of(2022, 5, 31))
                .datePaid(LocalDate.of(2022, 6, 30))
                .paidAmount(10_000)
                .build();

        assertExpiryDate(pd1, LocalDate.of(2022, 3, 31));
        assertExpiryDate(pd2, LocalDate.of(2022, 3, 30));
        assertExpiryDate(pd3, LocalDate.of(2022, 7, 31));
    }

    @Test
    void 이만원_이상_납부시_비례해서_납부일_계산() {
        final PaymentData pd1 = PaymentData.builder()
                .datePaid(LocalDate.of(2022, 3, 1))
                .paidAmount(20_000)
                .build();
        final PaymentData pd2 = PaymentData.builder()
                .datePaid(LocalDate.of(2022, 3, 1))
                .paidAmount(30_000)
                .build();
        final PaymentData pd3 = PaymentData.builder()
                .datePaid(LocalDate.of(2022, 3, 1))
                .paidAmount(50_000)
                .build();
        final PaymentData pd4 = PaymentData.builder()
                .datePaid(LocalDate.of(2022, 3, 1))
                .paidAmount(70_000)
                .build();

        assertExpiryDate(pd1, LocalDate.of(2022, 5, 1));
        assertExpiryDate(pd2, LocalDate.of(2022, 6, 1));
        assertExpiryDate(pd3, LocalDate.of(2022, 8, 1));
        assertExpiryDate(pd4, LocalDate.of(2022, 10, 1));
    }

    @Test
    void 첫_납부일과_만료일_일자가_다를때_이만원_이상_납부() {
        final PaymentData pd1 = PaymentData.builder()
                .firstBillingDate(LocalDate.of(2022, 1, 31))
                .datePaid(LocalDate.of(2022, 2, 28))
                .paidAmount(20_000)
                .build();
        final PaymentData pd2 = PaymentData.builder()
                .firstBillingDate(LocalDate.of(2022, 1, 31))
                .datePaid(LocalDate.of(2022, 2, 28))
                .paidAmount(40_000)
                .build();
        final PaymentData pd3 = PaymentData.builder()
                .firstBillingDate(LocalDate.of(2022, 3, 31))
                .datePaid(LocalDate.of(2022, 4, 30))
                .paidAmount(30_000)
                .build();

        assertExpiryDate(pd1, LocalDate.of(2022, 4, 30));
        assertExpiryDate(pd2, LocalDate.of(2022, 6, 30));
        assertExpiryDate(pd3, LocalDate.of(2022, 7, 31));
    }

    @Test
    void 십만원을_납부하면_1년_제공() {
        final PaymentData pd1 = PaymentData.builder()
                .datePaid(LocalDate.of(2022, 1, 28))
                .paidAmount(100_000)
                .build();

        assertExpiryDate(pd1, LocalDate.of(2023, 1, 28));
    }

    private void assertExpiryDate(PaymentData paymentData, LocalDate expectedExpDate) {
        ExpiryDateCalculator calc = new ExpiryDateCalculator();
        LocalDate actualExpDate = calc.calculateExpiryDate(paymentData);
        assertEquals(expectedExpDate, actualExpDate);
    }
}
