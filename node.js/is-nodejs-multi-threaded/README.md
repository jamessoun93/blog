# Is Node.js Multi-Threaded?

JS는 synchronous 언어인데 node.js에서는 어떻게 비동기 처리가 가능할까?

node.js는 js를 기반 런타임이니 js 동작방식대로 동작할 수 밖에 없지 않나?

---

우선 processes vs threads

Processes

- top-level execution container (like an application)
- separate memory space
  - OS가 해당 프로세스에 정해진 메모리 공간을 할당함.
- lot of safety

Threads

- Runs inside a process
- Every thread has a parent process that it is attached to
  - a single process can have multiple threads
- Shared memory space
  - all of these multiple threads share the same memory.
  - you don't have to do anything to share data back and forth btw two diff threads.
  - really performant

all javascript, v8, and the event loop run in one thread (called the main thread)

synchronous c++ backed methods are run in the main thread

asynchronous c++ backed methods sometimes don't run in the main thread

example: Crypto

very cpu-intensive task (does a lot of math)

https://blog.logrocket.com/a-complete-guide-to-threads-in-node-js-4fa3898fe74f/

---

main thread 가 있고 이 하나의 thread가 v8 엔진, node apis, libuv를 동작시킴.

이 중 blocking 함수가 있다면 전체 퍼포먼스에 영향을 미침.

그래서 node.js는 libuv 라는 라이브러리를 사용하여 비동기 처리를 가능하게 해줍니다.

Node.js는 libuv를 I/O-intensive한 작업과 CPU-intensive한 작업에 사용합니다.

I/O-intensive한 작업의 예시로는 DNS 쿼리와 File System을 다루는 작업정도를 들 수 있고, CPU-intensive한 작업은 crypto 라이브러리를 통한 암호화 및 해싱 정도를 예로 들 수 있습니다.

즉, DNS 쿼리를 통해 호스트네임을 ip로 resolve하는 작업, 비동기 파일 처리, 암호화 등등 작업에 있어서 node.js는 해당 작업들을 libuv를 사용하여 thread를 사용합니다. (thread 관련 내용은 아래에서 조금 더 자세히 설명합니다.)

event loop가 비동기 처리를 가능하게 해줌.

이벤트 루프는 비동기 코드를 실행시켜주는 libuv내 코드.

노드에서 비동기 함수를 호출할때마다 이벤트 루프에 할당됌.

모든 자바스크립트 코드는 메인 쓰레드에서 실행되고, 모든 비동기 처리는 이벤트 루프를 통해 처리됌.

이벤트 루프가 OS에게 일을 시키고 결과가 준비됐을때 전달받는 방식.

이벤트 루프는 직장 상사와 같음. 해당 업무가 진행돼야함을 알리고 완료 시 보고 받는 형태.

계속 새로운 이벤트를 처리할 수 있게 대기하고 있으며, 해당 이벤트가 다른 코드를 block하지 않도록 작업을 위임하는 것.

그렇다면 이벤트 루프에 올려지는 작업은 어디서 진행될까? 직원들은 누구일까?

두 가지 옵션이 존재.

1. OS
2. Thread Pool

서버와의 통신같은 네트워크 태스크는 OS가 처리함.

파일시스템을 활용하여 파일을 읽는 태스크 같은 것들은 thread pool.

libuv 내에는 여러개의 쓰레드를 갖고 있는 thread pool 이라는게 존재함 (미리 준비되어 있음)

libuv는 c++ 기반이라 쓰레드가 존재함.

thread pool의 thread는 process내 thread와 같음

v8과 이벤트루프를 실행하는 메인 쓰레드가 있고 추가로 가용 가능한 4개의 thread를 제공함 (thread pool)

여기서 default가 4개이고 thread를 많이 사용한다는 것을 알고 있으면 `process.env.UV_THREADPOOL_SIZE=6;` 의 형태로 thread pool의 사이즈를 직접 지정해줘 thread pool의 크기를 늘려주는 방법도 있습니다.

당연히 thread 하나는 한번에 하나의 task만 처리 가능하고 재사용됌.

CPU가 새로운 task가 있을때 매번 쓰레드를 생성하고 작업을 마친 뒤 쓰레드를 없애주는 작업을 안해도 되게 만들어줌.

당장 가용 가능한 쓰레드가 없다면 기다려야 함.

libuv가 특정 쓰레드에 작업을 할당하면 해당 쓰레드는 독립적으로 작업을 진행함 (다른 쓰레드 혹은 작업에 영향을 안준다는 뜻)

작업을 마치면 이벤트 루프가 해당 작업에 대한 결과를 받아 callback을 실행함.

하지만 모든 비동기 작업이 쓰레드 풀을 사용하진 않음 (쓰레드는 복잡하고 제공되는 리소스가 정해져 있기 때문에)

libuv는 가능하다면 OS 커널을 사용하게끔 함.

OS 커널을 사용해서 직접 컴퓨터의 하드웨어를 사용하게끔 함 (컴퓨터 자체 쓰레드가 존재하기 때문)

커널은 다른 컴퓨터와의 통신 같은 네트워크 작업을 효율적으로 처리할 수 있고, 이렇게 OS에서 처리하면 더 효율적인 작업들은 OS를 통해서 처리하도록 하는 것. (결과적으로 정해진 자원을 사용하는 쓰레드 풀의 자원을 아끼는 효과)

OS에서 처리하는 비동기 작업들은 쓰레드풀을 건너뛰고 바로 이벤트루프에 처리 결과를 알리고 이벤트 루프가 필요한 callback 함수를 실행함.

JS는 single thread로 synchronous하게 돌지만, 노드는 내부적으로 이런식으로 작동하기 때문에 개발자가 쓰레드를 직접 만질 필요가 없고 non-blocking I/O가 가능하게 해줌.

---

그렇다면 thread를 언제 사용할까요?
