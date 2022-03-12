# Java Primitive Types

## Integer (32 bits)

```java
public class Main {

    public static void main(String[] args) {

        System.out.println(Integer.MAX_VALUE); // 2147483647
        System.out.println(Integer.MIN_VALUE); // -2147483648

        System.out.println(Integer.MAX_VALUE + 1); // overflow -> -2147483648
        System.out.println(Integer.MIN_VALUE - 1); // underflow -> 2147483647

        int maxInt = 2147483647;
        int maxIntPlusOne = 2147483648; // BAD: Integer number too large

        int maxInt = 2_147_483_647; // 이것도 가능 for readability
    }
}
```

## Byte (8 bits)

```java
public class Main {

    public static void main(String[] args) {

        byte myMinByteVal = Byte.MIN_VALUE;
        byte myMaxByteVal = Byte.MAX_VALUE;

        System.out.println(myMinByteVal); // -128
        System.out.println(myMaxByteVal); // 127
    }
}
```

## Short (16 bits)

```java
public class Main {

    public static void main(String[] args) {

        short myMinShortVal = Short.MIN_VALUE;
        short myMaxShortVal = Short.MAX_VALUE;

        System.out.println(myMinShortVal); // -32768
        System.out.println(myMaxShortVal); // 32767
    }
}
```

## Long (64 bits)

```java
public class Main {

    public static void main(String[] args) {

        long myLongValue = 100L; // forces java to treat this number as long
        System.out.println(myLongValue);

        long myMinLongValue = Long.MIN_VALUE;
        long myMaxLongValue = Long.MAX_VALUE;
        System.out.println(myMinLongValue); // -9223372036854775808
        System.out.println(myMaxLongValue); // 9223372036854775807

        long bigLongLiteralValue = 2_147_483_647_234; // Integer number too large
        long bigLongLiteralValue = 2_147_483_647_234L; // works now
    }
}
```

## Casting

```java
public class Main {

    public static void main(String[] args) {

        int myTotal = (myMinIntValue/2); // works
        byte myNewByteVal = (myMinIntValue/2); // doesn't work (int -> byte)
        byte myNewByteVal = (byte) (myMinByteVal/2); // casting to byte
        short myNewShortVal = (short) (myMinShortVal/2); // casting to short
    }
}

```

## Float / Double

- double은 float보다 두배의 크기를 잡아먹지만 double이 더 빠르다.
- 더 precise하고 더 큰 수를 다룰 수 있기 때문에 많은 자바 라이브러리들에서 double로 처리하고 double을 리턴하게끔 되어있다.
- double이 float보다는 낫지만 그래도 floating point numbers가 저장되는 방식의 한계로 인해 precision 문제가 발생한다.
- BigDecimal을 써야한다.

```java
public class Main {

    public static void main(String[] args) {

        int myIntValue = 5 / 3; // 1
        float myFloatValue = 5f / 3f; // 1.6666666
        double myDoubleValue = 5d / 3d; // 1.6666666666666667 (double has more precision)
        double myDoubleValue2 = 5.00 / 3.00; // java treats floating point numbers as double as default
    }
}
```

## Char / Boolean

```java
public class Main {

    public static void main(String[] args) {

        char myChar = 'D'; // 16 bits (NOT single byte) because of Unicode characters
        char myUnicodeChar = '\u0044'; // D
        char myCopyrightChar = '\u00A9';

        System.out.println(myUnicodeChar);
        System.out.println(myCopyrightChar);

        boolean a = true;
    }
}
```
