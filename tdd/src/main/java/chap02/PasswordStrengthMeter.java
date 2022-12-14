package chap02;

public class PasswordStrengthMeter {
    public PasswordStrength meter(String s) {
        if (s == null || s.isEmpty()) return PasswordStrength.INVALID;

        int strengthCount = getStrengthCount(s);

        if (strengthCount <= 1) return PasswordStrength.WEAK;
        if (strengthCount == 2) return PasswordStrength.NORMAL;
        return PasswordStrength.STRONG;
    }

    private int getStrengthCount(String s) {
        int strengthCount = 0;

        if (s.length() >= 8) strengthCount++;
        if (meetsContainingNumberCriteria(s)) strengthCount++;
        if (meetsContainingCapitalLetterCriteria(s)) strengthCount++;

        return strengthCount;
    }

    private boolean meetsContainingNumberCriteria(String s) {
        for (char ch : s.toCharArray()) {
            if (ch >= '0' && ch <= '9') {
                return true;
            }
        }
        return false;
    }

    private boolean meetsContainingCapitalLetterCriteria(String s) {
        for (char ch : s.toCharArray()) {
            if (Character.isUpperCase(ch)) {
                return true;
            }
        }
        return false;
    }
}
