# DatagramSocket을 이용한 UDP Connection 예시

TCP에서는 스트림 객체를 활용하여 데이터를 주고 받지만, UDP 통신을 할 때에는 스트림을 사용하지 않고 DatagramPacket이라는 클래스를 사용합니다.

```java
// DatagramServerSample.java

import java.net.DatagramSocket;
import java.net.DatagramPacket;

public class DatagramServerSample {
    public static void main(String[] args) {
        DatagramServerSample sample = new DatagramServerSample();
        sample.startServer();
    }

    public void startServer() {
        DatagramSocket server = null;

        try {
            server = new DatagramSocket(9999);
            int bufferLength = 256;
            byte[] buffer = new byte[bufferLength];

            DatagramPacket packet = new DatagramPacket(buffer, bufferLength);

            while (true) {
                System.out.println("Server: Waiting for request....");
                server.receive(packet);

                int dataLength = packet.getLength();
                System.out.println("Server: Data received. Data Length: " + dataLength);

                String data = new String(packet.getData(), 0, dataLength);
                System.out.println("Data received: " + data);

                if (data.equals("EXIT")) {
                    System.out.println("Stop DatagramServer");
                    break;
                }
                System.out.println("-------------------");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (server != null) {
                try {
                    server.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
```

```java
// DatagramClientSample.java

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.DatagramPacket;
import java.util.Date;

public class DatagramClientSample {
    public static void main(String[] args) {
        DatagramClientSample sample = new DatagramClientSample();
        sample.sendDatagramSample();
    }

    public void sendDatagramSample() {
        for (int loop = 0; loop < 3; loop++) {
            sendDatagramData("Sending data at " + new Date());
        }
        sendDatagramData("EXIT");
    }

    public void sendDatagramData(String data) {
        try {
            DatagramSocket client = new DatagramSocket();
            InetAddress address = InetAddress.getByName("127.0.0.1");

            byte[] buffer = data.getBytes();
            DatagramPacket packet = new DatagramPacket(buffer, 0, buffer.length, address, 9999);

            client.send(packet);
            System.out.println("Client: Send data");
            client.close();
            Thread.sleep(1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```

---

TCP와 달리 UDP 커넥션은 데이터가 제대로 전달되었다는 보장을 하지 않아서, 서버가 켜져있지 않은채로 요청을 보내도 별다른 에러가 발생하지 않습니다.
