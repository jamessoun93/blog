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

    }
}
```
