package chap02;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 검사할 규칙
 * - 길이가 8글자 이상
 * - 0부터 9 사이의 숫자를 포함
 * - 대문자 포함
 * - 세 규칙을 모두 충족하면 암호를 강함이다.
 * - 2개의 규칙을 충족하면 암호는 보통이다.
 * - 1개 이하의 규칙을 충족하면 암호는 약함이다.
 * <p>
 * 첫 번째 테스트는 가장 쉽거나 가장 예외적인 상황을 선택해라
 * 쉬운 케이스: 모든 규칙을 충족하는 경우
 * 예외 케이스: 모든 조건을 충족하지 않는 경우
 */
public class PasswordStrengthMeterTest {

    private PasswordStrengthMeter meter = new PasswordStrengthMeter();

    private void assertStrength(String password, PasswordStrength expStr) {
        PasswordStrength result = meter.meter(password);
        assertEquals(expStr, result);
    }


    @Test
    @DisplayName("인풋이 NULL 인 경우: INVALID")
    void nullInput_Then_Invalid() {
        assertStrength(null, PasswordStrength.INVALID);
    }

    @Test
    @DisplayName("인풋이 비었을 경우: INVALID")
    void emptyInput_Then_Invalid() {
        assertStrength("", PasswordStrength.INVALID);
    }

    @Test
    @DisplayName("모든 조건을 충족하는 경우: STRONG")
    void meetsAllCriteria_Then_Strong() {
        assertStrength("ab12!@AB", PasswordStrength.STRONG);
        assertStrength("abc1!Add", PasswordStrength.STRONG);
    }

    @Test
    @DisplayName("길이 제외 전부 충족하는 경우: NORMAL")
    void meetsOtherCriteria_except_for_Length_Then_Normal() {
        assertStrength("ab12!@A", PasswordStrength.NORMAL);
        assertStrength("Ab12!c", PasswordStrength.NORMAL);
    }

    @Test
    @DisplayName("숫자 제외 전부 충족하는 경우: NORMAL")
    void meetsOtherCriteria_except_for_number_Then_Normal() {
        assertStrength("ab!@ABqwer", PasswordStrength.NORMAL);
    }

    @Test
    @DisplayName("대문자 제외 전부 충족하는 경우: NORMAL")
    void meetsOtherCriteria_except_for_Capital_Letter_Then_Normal() {
        assertStrength("dsndjfj222ss", PasswordStrength.NORMAL);
    }

    @Test
    @DisplayName("8글자 이상인 조건만 충족하는 경우: WEAK")
    void meetsOnlyLengthCriteria_Then_Weak() {
        assertStrength("zzzzzzzzzzzzzzz", PasswordStrength.WEAK);
    }

    @Test
    @DisplayName("숫자 포함 조건만 충족하는 경우: WEAK")
    void meetsOnlyNumberCriteria_Then_Weak() {
        assertStrength("12345", PasswordStrength.WEAK);
    }

    @Test
    @DisplayName("대문자 포함 조건만 충족하는 경우: WEAK")
    void meetsOnlyCapitalLetterCriteria_Then_Weak() {
        assertStrength("AADDC", PasswordStrength.WEAK);
    }

    @Test
    @DisplayName("아무 조건도 충족하지 못한 경우: WEAK")
    void meetsNoCriteria_Then_Weak() {
        assertStrength("abc", PasswordStrength.WEAK);
    }
}
