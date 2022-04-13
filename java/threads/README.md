```java
class MyThread implements Runnable {

    public void run() {
        int i;
        for(i=1; i<=200; i++) {
            System.out.println(i + "\t");
        }
    }
}

public class ThreadTest {
    public static void main(String[] args) {
        System.out.println(Thread.currentThread() + "start");

        // MyThread th1 = new MyThread();
        // MyThread th2 = new MyThread();

        // th1.start();
        // th2.start();

        MyThread runnable = new MyThread();

        Thread th1 = new Thread(runnable);
        Thread th2 = new Thread(runnable);

        th1.start();
        th2.start();

        System.out.println(Thread.currentThread() + "end");
    }
}
```
