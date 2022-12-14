package chap02;

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
    void 인풋이_NULL_인_경우_INVALID() {
        assertStrength(null, PasswordStrength.INVALID);
    }

    @Test
    void 인풋이_비었을_경우_INVALID() {
        assertStrength("", PasswordStrength.INVALID);
    }

    @Test
    void 모든_조건을_충족하는_경우_STRONG() {
        assertStrength("ab12!@AB", PasswordStrength.STRONG);
        assertStrength("abc1!Add", PasswordStrength.STRONG);
    }

    @Test
    void 길이_제외_전부_충족하는_경우_NORMAL() {
        assertStrength("ab12!@A", PasswordStrength.NORMAL);
        assertStrength("Ab12!c", PasswordStrength.NORMAL);
    }

    @Test
    void 숫자_제외_전부_충족하는_경우_NORMAL() {
        assertStrength("ab!@ABqwer", PasswordStrength.NORMAL);
    }

    @Test
    void 대문자_제외_전부_충족하는_경우_NORMAL() {
        assertStrength("dsndjfj222ss", PasswordStrength.NORMAL);
    }

    @Test
    void 글자_길이가_8이상인_조건만_충족하는_경우_WEAK() {
        assertStrength("zzzzzzzzzzzzzzz", PasswordStrength.WEAK);
    }

    @Test
    void 숫자_포함_조건만_충족하는_경우_WEAK() {
        assertStrength("12345", PasswordStrength.WEAK);
    }

    @Test
    void 대문자_포함_조건만_충족하는_경우_WEAK() {
        assertStrength("AADDC", PasswordStrength.WEAK);
    }

    @Test
    void 아무_조건도_충족하지_못한_경우_WEAK() {
        assertStrength("abc", PasswordStrength.WEAK);
    }
}
