# [객체 지향 프로그래밍 #2] 다형성과 추상화

- [1. 다형성](#1-다형성)
- [2. 추상화](#2-추상화)
- [3. 타입 추상화](#3-타입-추상화)
- [4. 추상 타입 사용 장점: 유연함](#4-추상-타입-사용-장점-유연함)
- [5. 추상화는 언제 하는게 좋을까?](#5-추상화는-언제-하는게-좋을까)
- [6. 추상화를 잘 하려면?](#6-추상화를-잘-하려면)
- [7. 예시](#7-예시)

## 1. 다형성

다형성이란 **한 객체가 여러 타입을 갖는 것, 즉 한 객체가 여러 타입의 기능을 제공한다는 뜻**이다.

보통 객체 지향에서 다형성은 타입 상속으로 구현하게 된다.

객체를 상속하게 되면 하위 타입은 상위 타입도 된다.

간단한 예를 보자.

```java
public class Timer {
    public void start() { .. }
    public void stop() { .. }
}

public interface Rechargeable {
    void charge();
}
```

```java
public class IotTimer extends Timer implements Rechargeable {
    public void charge() {
        ...
    }
}
```

위 예시에서 `IotTimer` 클래스는 `Timer` 클래스를 상속하고 `Rechargeable` 인터페이스를 구현했다.

`IotTimer` 는 이제 `Timer` 타입도 되고 `Rechargeable` 타입도 된다.

그래서 아래와 같이 `IotTimer` 인스턴스를 `Timer` 타입에도, `Rechargeable` 타입에도 할당이 가능하게 되고 해당 타입들이 제공하는 기능들을 사용할 수 있게 된다.

```java
IotTimer it = new IotTimer();
it.start();
it.stop();

Timer t = it;
t.start();
t.stop();

Rechargeable r = it;
r.charge();
```

## 2. 추상화

추상화란 **데이터나 프로세스 등을 의미가 비슷한 개념이나 의미 있는 표현으로 정의하는 과정**이다.

추상화는 크게 두 가지 방식으로 구현 가능하다.

첫 번째로 **특정한 성질을 가지고 추상화** 하는 방식이 있다.

- 유저 정보의 id, name, email 같은 필드를 뽑아내서 User 테이블로 추상화 할 수 있다.
- 돈과 관련된 여러 특성 중 currency, amount 같은 필드를 뽑아내서 Money 클래스로 추상화 할 수 있다.

두 번째로 **공통 성질을 가지고 일반화** 하는 방식이 있다.

- 애플 맥북 프로 모델과 LG 그램 모델의 공통점을 뽑아 Laptop 으로 추상화 할 수 있다.
- 지포스, 라데온의 공통점을 뽑아 GPU 로 추상화 할 수 있다.

추상화는 이렇게 특정한 성질이나 공통점을 뽑아내는 과정이다.

그렇다면 다형성은 추상화랑 무슨 관련이 있을까?

**다형성은 추상화의 공통 성질을 뽑아내는 과정과 관련이 있다.**

## 3. 타입 추상화

**타입 추상화**란 **서로 다른 구현의 공통된 특징을 가지고 대표하는 상위 타입을 도출하는 과정**을 말한다.

보통 인터페이스로 추상화하며 도출한 추상 타입과 개별 구현은 타입 상속으로 연결된다.

내 서비스에 사용자에게 알림을 보내는 기능이 있다고 해보자.

서비스 규모가 커지면서 다양한 방식의 알림 기능이 만들어질텐데 예를 들어 이메일, 문자, 카카오톡 알림 기능들이 존재한다고 해보자.

그렇다면 개별 기능들은 `EmailNotifier`, `SMSNotifier`, `KakaoNotifier` 의 형태로 구현되어 있을텐데, 이 셋은 알림 기능에 대한 개별 구현체라는 공통점이 존재한다.

이럴 때 `Notifier` 라는 인터페이스를 만들면 기능에 대한 의미를 제공하면서 내부 구현을 제공할 필요가 없고 어떻게 구현할지도 몰라도 된다.

이렇게 인터페이스를 이용해 추상 타입을 도출했다면 추상 타입을 가지고 코드를 작성할 수 있다.

```java
Notifier notifier = getNotifier(...);
notifier.notify(...)
```

위와 같이 코드를 작성하면 여러가지 알림 기능 구현체들 중 어떤 구현체가 `notifier` 에 할당되도 상관이 없게 된다.

추상 타입을 활용하면 이렇게 구현을 감추면서 기능의 구현보다 의도를 더 잘 드러낼 수 있게 된다.

## 4. 추상 타입 사용 장점: 유연함

추상 타입을 사용하지 않고 개별 구현체를 직접 사용하게 되면 아래와 같이 코드를 작성하게 된다.

```java
private SmsSender smsSender;

public void cancel(String orderNo) {
    ... 주문 취소 처리

    smsSender.sendSms(...);
}
```

여기서 카카오톡 알림 기능이 추가된다면?

```java
private SmsSender smsSender;
private KakaoPush kakaoPush;

public void cancel(String orderNo) {
    ... 주문 취소 처리

    if (pushEnabled) {
        kakaoPush.push(...);
    } else {
        smsSender.sendSms(...);
    }
}
```

여기서 또 이메일 알림 기능이 추가된다면?

```java
private SmsSender smsSender;
private KakaoPush kakaoPush;
private MailService mailService;

public void cancel(String orderNo) {
    ... 주문 취소 처리

    if (pushEnabled) {
        kakaoPush.push(...);
    } else {
        smsSender.sendSms(...);
    }

    mailService.sendMail(...);
}
```

요구 사항의 변경/추가에 따라 주문 취소 코드도 함께 변경이 되는 것을 볼 수 있다.

주문 취소 로직은 변한게 없는데도 알림 방식이 추가됨에따라 코드를 변경할 수 밖에 없게 된다.

즉, 해당 기능 자체에 대한 변경사항이 없는데도 다른 변경사항 때문에 코드를 변경하게 된다.

여기서 추상 타입을 이용해본다면?

```java
public void cancel(String orderNo) {
    ... 주문 취소 처리

    Notifier notifier = getNotifier(...);
    notifier.notify(...);
}

private Notifier getNotifier(...) {
    if (pushEnabled) {
        return new KakaoNotifier();
    } else {
        return new SmsNotifier();
    }
}
```

위와 같이 추상 타입을 이용하면 이렇게 상황에 맞는 알림 기능을 코드 변경없이 가져올 수 있게 된다.

여기서 더 나아가 `getNotifier(...)` 메서드가 하고 있는 일을 보면, 인자로 넘어온 값에 따라 상황에 맞는 구현체를 생성한다.

이 부분도 아래와 같이 추가로 추상화를 해볼 수 있다.

```java
public void cancel(String orderNo) {
    ... 주문 취소 처리

    Notifier notifier = NotifierFactory.instance().getNotifier(...);
    notifier.notify(...);
}

public interface NotifierFactory {
    Notifier getNotifier(...);

    static NotifierFactory instance() {
        return new DefaultNotifierFactory();
    }
}

public class DefaultNotifierFactory implements NotifierFactory {
    public Notifier getNotifier(...) {
        if (pushEnabled) return new KakaoNotifier();
        else return new SmsNotifier();
    }
}
```

이렇게 추상화를 하면 알림 기능에 변경사항이 생겼을 때 주문 취소 처리 로직을 그대로 둔 채 `DefaultNotifierFactory` 만 수정하면 된다.

알림 기능에 변경사항이 생겨도 주문 취소 처리 로직은 바뀌지 않는다는 것이 핵심이다.

이런 유연함을 제공하는게 바로 추상 타입을 사용하는 이유다.

## 5. 추상화는 언제 하는게 좋을까?

**무턱대로 처음부터 추상화를 하는 것은 바람직하지 않다.**

추상화를 한다는 것은 새로운 추상 타입이 생긴다는 것을 의미한다.

타입이 늘어나게되면 그만큼 프로그램은 점점 복잡해진다.

그렇다보니 아직 존재하지 않는 기능에 대한 이른 추상화는 주의할 필요가 있다. 잘못된 추상화를 하게 될 가능성이 있고 복잡도만 증가하기 때문이다.

**추상화는 실제 변경 혹은 확장이 발생할 때 시도하는게 좋다.**

```java
public class OrderService {
    private MailSender sender;

    public void order(...) {
        ... 주문 처리

        sender.send(msg);
    }
}
```

최초에는 이메일 알림 기능만 있기 때문에 위와 같이 구현했는데, 새로운 요구사항으로 문자 알림을 추가해야 한다면 이 때가 바로 추상화를 시도하기 좋은 시점이 될 수 있다.

```java
public class OrderService {
    private MailSender sender;
    private SmsService smsService;

    public void order(...) {
        ... 주문 처리

        sender.send(msg);
        smsService.send(smsMsg);
    }
}
```

위와 같이 하기보다 아래와 같이 추상화를 시도해본다.

```java
public class OrderService {
    private Notifier notifier;

    public void order(...) {
        ... 주문 처리

        notifier.notify(msg);
    }
}
```

이렇게 필요한 시점에 추상화를 시도하게 되면 프로그램의 복잡도를 증가시키지 않으면서 추상화를 통해서 얻을 수 있는 유연함을 가질 수 있게 된다.

## 6. 추상화를 잘 하려면?

추상화를 잘 하려면 기능을 구현한 이유가 무엇 때문인지 생각해봐야 한다.

그 이유가 명확해지면서 공통점을 도출할 수 있게 되면 원하는 방식으로 추상화가 가능해진다.

## 7. 예시

코드 예시를 통해 추상화의 이점을 살펴보자.

예시로 클라우드 파일 통합 관리 기능을 개발한다고 해보자.

현재 서비스에서 사용중인 클라우드는 드롭박스와 구글드라이브가 있고, 주요 기능으로는 각 클라우드의 파일 목록 조회, 다운로드, 업로드, 삭제, 그리고 검색 기능이 있다.

먼저 추상화를 하지 않은 상태의 파일 목록 조회 기능의 예시를 살펴보자.

```java
public enum CloudId {
    DROPBOX,
    GOOGLE_CLOUD
}
```

```java
pubic class FileInfo {
    private CloudId cloudId;
    private String fileId;
    private String name;
    private long length;

    ...
}
```

```java
public class CloudFileManager {
    public List<FileInfo> getFiles(CloudId cloudId) {
        if (cloudId == CloudId.DROPBOX) {
            ...
        } else if (cloudId == CloudId.GOOGLE_DRIVE) {
            ...
        }
    }
}
```

이제 추상화를 하지 않은 상태의 파일 다운로드 기능의 예시를 살펴보자.

```java
public void download(FileInfo file, File localTarget) {
    if (file.getCloudId() == CloudId.DROPBOX) {
        ...
    } else if (file.getCloudId() == CloudId.GOOGLE_DRIVE) {
        ...
    }
}
```

추상화를 하지 않은 상태의 경우 다른 모든 기능들이 어떻게 구현되어 있을지 느낌이 온다.

이런 상태에서 기능을 확장해 여러가지 클라우드 서비스를 추가해야 한다면 어떻게 될까?

이미 존재하는 모든 기능들에 추가할 클라우드 서비스 개수만큼 else if 문이 추가된다...

즉, 추상화를 안하면 이런식으로 코드 구조가 길어지고 복잡해질 가능성이 높다.

새로운 클라우드 서비스에 대한 지원을 추가할때마다 모든 메서드에 새로운 if 문이 추가되면서 코드가 점점 읽기 힘들어져 진행 자체가 느려질 수 있게 된다.

관련 코드가 여러 곳으로 분산된다는 문제도 있다. 하나의 클라우드 서비스에 대한 처리 관련 코드가 여러 메서드에 흩어지게 된다.

결과적으로 코드 가독성과 분석 속도가 저하됨에따라 실수할 가능성이 높고 불필요한 디버깅 시간이 증가되는 형태가 된다.

그렇다면 이제 추상화를 해보자.

## 추상화

결국 서비스에서 지원하고 있는 모든 클라우드 서비스를 이용해 공통적으로 하고 싶은 일들이 정해져 있다.

이것을 일반화해서 클라우드 파일시스템 이라는 추상 타입을 도출할 수 있다.

`CloudFileSystem` 이라는 인터페이스를 통해 파일 목록 불러오기, 검색, 파일 불러오기, 파일 추가하기 등 기능을 포함하게 한다.

그리고 해당 기능들에서 쓸 파일 객체를 `CloudFile` 이라는 인터페이스를 두고 파일을 다루는 것과 관련된 기능을 포함하게 한다.

그런 뒤 `CloudFileSystemFactory` 클래스를 생성해 `cloudId` 에 따른 `CloudFileSystem` 객체를 가져오게 한다.

이런 추상 타입들을 이용해서 DROPBOX 용 파일 시스템을 구현해보자.

```java
public class DropBoxFileSystem implements CloudFileSystem {
    prviate DropBoxClient dbClient = new DropBoxClient(...);

    @Override
    public List<CloudFile> getFiles() {
        List<DbFile> dbFiles = dbClient.getFiles();
        List<CloudFile> results = new ArrayList<>(dbFiles.size());
        for (DbFile file : dbFiles) {
            DropBoxCloudFile cf = new DropBoxCloudFile(file, dbClient);
            results.add(cf);
        }
        return results;
    }
}
```

getFiles
