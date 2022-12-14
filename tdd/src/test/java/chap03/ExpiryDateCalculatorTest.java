package chap03;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ExpiryDateCalculatorTest {

    @Test
    void 만원_납부시_한달뒤가_만료일() {
        assertExpiryDate(
                PaymentData.builder()
                        .datePaid(LocalDate.of(2022, 1, 1))
                        .paidAmount(10_000)
                        .build(),
                LocalDate.of(2022, 2, 1));
        assertExpiryDate(
                PaymentData.builder()
                        .datePaid(LocalDate.of(2022, 3, 1))
                        .paidAmount(10_000)
                        .build(),
                LocalDate.of(2022, 4, 1));
    }

    @Test
    void 납부일과_한달_뒤_날짜가_같지_않음() {
        assertExpiryDate(
                PaymentData.builder()
                        .datePaid(LocalDate.of(2022, 3, 31))
                        .paidAmount(10_000)
                        .build(),
                LocalDate.of(2022, 4, 30));
        assertExpiryDate(
                PaymentData.builder()
                        .datePaid(LocalDate.of(2022, 8, 31))
                        .paidAmount(10_000)
                        .build(),
                LocalDate.of(2022, 9, 30));
        assertExpiryDate(
                PaymentData.builder()
                        .datePaid(LocalDate.of(2022, 1, 31))
                        .paidAmount(10_000)
                        .build(),
                LocalDate.of(2022, 2, 28));
        assertExpiryDate(
                PaymentData.builder()
                        .datePaid(LocalDate.of(2024, 1, 31))
                        .paidAmount(10_000)
                        .build(),
                LocalDate.of(2024, 2, 29));
    }

    @Test
    void 첫_납부일과_만료일_일자가_다를때_만원_납부() {
        assertExpiryDate(
                PaymentData.builder()
                        .firstBillingDate(LocalDate.of(2022, 1, 31))
                        .datePaid(LocalDate.of(2022, 2, 28))
                        .paidAmount(10 + 000)
                        .build(),
                LocalDate.of(2022, 3, 31)
        );
    }

    private void assertExpiryDate(PaymentData paymentData, LocalDate expectedExpDate) {
        ExpiryDateCalculator calc = new ExpiryDateCalculator();
        LocalDate actualExpDate = calc.calculateExpiryDate(paymentData);
        assertEquals(expectedExpDate, actualExpDate);
    }
}
