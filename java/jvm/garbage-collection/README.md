# JVM: Garbage Collection

GC가 왜 안좋은지?

- stop the world -> JVM은 application을 멈춰버림
- stw 시간을 줄이는게 포인트

Hotspot JVM에서는 new 영역에 할당을 빠르게 하고 있음.

Serial GC

- CPU 코어 1개일때 쓰기위해 개발

Parallel GC

- 기본구조는 serial이랑 같은데 multi corefmf dldydgotj ej QKfmrp

CMS GC

- parallel이랑 비슷하게 다중으로 하는데 mark & sweep을 concurrent하게 함.
- stw까지 안가게끔 해줌.
- 문제: 속도 때문에 compaction을 안함. 그래서 갑자기 application이 죽어버림

G1 GC

- heap을 균등하게 잘라서 gc
- g1 gc 또한 compaction을 안함.

Solutions

1. JVM을 튜닝하거나
2. 빨리 release 시켜버리는 것 (major gc가 일어나게 하는것)
