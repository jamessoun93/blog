# HTTPS

일단 HTTP를 사용하면 public wifi 같은 공유 네트워크를 사용할 때 네트워크 공격에 취약함

HTTPS를 사용해서 정보를 암호화하여 주고 받아 방지할 수 있음.

HTTPS를 사용하려면 TLS Certificate이 필요함

TLS Certificate은 Certificate Authorities (CA) 를 통해 발급받을 수 있음.

CA는 클라이언트와 서버 사이에 믿을만한 미들맨이라고 생각하면 됌.

TLS Certificate은 사실 아무나 발급할 수 있지만, 브라우저들은 공개적으로 인증된 CA를 통해 발급한 TLS Certificate만 서포트 함.

TLS Certificate의 종류에는 크게 세 가지가 있음 (DV, OV and EV)

DV는 domain validation의 약자로 해당 certificate 오너가 해당 도메인을 컨트롤하고 있다는 것을 확인해줌.

하지만 DV는 딱 이것만 해주기 때문에 민감한 정보를 다루는 사이트에선 적합하지 않음.

DV Certificate은 주로 Let’s Encrypt 같은 비영리 CA를 통해 발급함.

사용하는데 문제는 없지만 HTTPS는 기본적으로 요청을 주고받는 서버를 인증해주는 기능을 해줘야 안정성이 보장됌.

그래서 로그인이나 결제 서비스를 가지고 있는 사이트의 경우 OV(organization validation)나 EV(extended validation)를 제공하는 유료 TLS Certificate을 발급받는게 좋음.

OV와 EV의 차이는 검증 과정에 있으며 EV가 더 강력함.

하지만 만약 내 도메인에 서브도메인을 추가하고 싶다면 이 경우 해당 서브도메인에 대한 TLS Certificate가 따로 필요함.

그래서 Wildcard TLS Certificate을 쓰면 서브도메인을 무제한으로 secure하게 인증할 수 있음.

—

장점

1. HTTPS는 SEO에도 좋음 (구글 랭킹 등)
2. Referral data를 보존함. HTTPS 에서 HTTP로 이동 시 이전 경로에 대한 referral 정보가 남지 않음. (Google Analytics 같은거에서 안보임) 하지만 HTTPS를 사용하면 해당 데이터가 보존됨.

—

How does TLS work?

symmetric과 asymmetric cryptography를 함께 사용함.

symmetric 암호화는 데이터를 보내는이와 받는이가 공통으로 가지고 있는 secret key를 통해 암호화, 복호화를 할 수 있음.

계산적인 측면에서 볼때는 효율적일 수 있으나 양쪽에서 같은 secret key를 안전하게 가지고 있을 수 있는 방식을 적용해야함.

asymmetric 암호화는 키페어를 사용함. public 키와 private 키.

public key는 수학적으로 private key와 연관되어 있고 길이가 충분하다면 public key를 가지고 private key를 유추해내는 것은 불가능에 가까움.

그래서 받는이가 보내는이에게 public key를 제공하여 보내는이가 public key를 가지고 데이터를 암호화한 뒤 보내면, 오직 받는이의 private key만을 활용해서 데이터를 복호화할 수 있음.

asymmetric 암호화의 장점은 key sharing이 자유롭다는 점. (public key가 노출되어도 상관없기 때문에)

하지만 키가 길면 길수록 안전한데 동시에 길면 길수록 암호화하는 과정의 비용이 커져서 안좋음.

그래서 TLS는 asymmetric 암호화를 통해 session key 라는 것을 생성해서 주고 받고, 해당 session key를 사용하여 데이터를 암호화하고 복호화할 수 있게 함.

그리고 해당 세션이 종료되면 만들어진 세션키는 파기됨.

TLS를 사용하면 서버에 연결하는 클라이언트가 서버의 public 키 소유권을 확인할 수 있는 것이 좋음.

일반적으로 CA가 발급한 X.509 디지털 인증서를 사용하여 public 키의 신뢰성을 보여주게 됨.

이 디지털 인증서는 인증서 소유자의 public key를 인증하고, 소유자가 해당 인증서를 통해 보호되고 있는 도메인에 대한 제어권이 있다는 것을 확인시켜줌.

따라서 CA는 클라이언트에게 검증된 엔티티에 의해 운영되는 서버에 연결된다는 확신을 주는 신뢰할 수 있는 제3자 역할을 함.

—

브라우저에서 특정 HTTPS 사이트에 최초 진입 시 TLS Handshake (TCP Handshake와 비슷함) 라는 과정이 진행됌.

순서

1. client가 “hello” 메세지를 서버로 보냄. 해당 메세지에는 클라이언트에서 서포트하는 TLS 버전과, 사용할 암호화 제품과 클라이언트 랜덤이라는 랜덤 바이트 문자열을 포함함.
2. 서버가 클라이언트의 hello 메세지에 대한 응답으로 서버의 TLS 인증서와 서버에서 선택한 암호화 제품군과 서버 랜덤값을 보냄. 이 과정에서 public key도 함께 공유됨.
3. 클라이언트는 서버에서 제공받은 인증서를 해당 인증서를 발급한 CA에 확인함. 이 과정을 통해 클라이언트는 서버가 실제로 본 서버가 맞는지 확인할 수 있음. (실제로 해당 도메인을 소유하고 있는 오너와 연결을 한건지 확인할 수 있다는 뜻)
4. 클라이언트가 랜덤 바이트 문자열을 서버에서 제공한 public key로 암호화하여 서버에 보냄. (premaster secret)
5. 세션 키 생성: client random, server random, 그리고 premaster secret을 가지고 세션 키를 생성함.
6. 클라이언트는 “finished” 라는 메세지를 해당 세션키로 암호화하여 서버로 보냄
7. 서버도 “finished”라는 메세지를 해당 세션키로 암호화하여 클라이언트에게 보냄
8. 핸드쉐이크 완료. 이후 서로 간의 통신은 해당 세션키를 통해 이루어짐.
