# SSH란 무엇일까요?

## SSH란 무엇일까? SSH를 왜 사용할까? SSH의 장단점은? GitHub 계정 연동까지!

다들 한 번쯤 SSH라는 단어를 들어보거나 본 적이 있을 것입니다.

GitHub에서 repository를 clone 받을 때도 등장하고, 배포용으로 생성한 AWS 서버에 연결할 때도 등장합니다.

보통은 SSH에 대한 이해도가 있어야 사용 가능할 것이라 생각하겠지만, 세상에는 너무나도 멋지고 유능한 사람들이 많아서 SSH 가 무엇인지 잘 몰라도 Google 검색을 통해 충분히 사용이 가능합니다.

하지만 잠깐 시간을 내어 SSH 가 무엇인지, 왜 사용하는지 정도만 알고 넘어가도, 앞으로 SSH를 사용할 때 조금 더 의미가 있게 사용하게 되지 않을까 하는 마음에 이번 포스팅을 작성해 봅니다.

그리고 마지막에는 나의 GitHub 계정과 내 컴퓨터 사이에 SSH 커넥션을 여는 방법을 다뤄보겠습니다.

---

## ✔️ SSH 란?
SSH 란 Secure Shell의 줄임말로, 두 컴퓨터 간 통신을 할 수 있게 해주는 하나의 protocol입니다.

💡 Protocol 이란, 서로 다른 통신 장비 간 주고받는 데이터의 양식과 규칙입니다.

인터넷 연결만 되어있어도 내 컴퓨터의 terminal을 통해 다른 지역에 있는 컴퓨터 혹은 서버를 관리할 수 있게 해 주고, 파일도 공유할 수 있습니다.

비슷한 protocol의 예시로 우리가 알고 있는 HTTP, HTTPS, FTP 등이 있습니다.

브라우저가 웹페이지를 보여주기 위해 서버와 통신할 때 HTTPS protocol을 사용하는 것과 같이, 서로 다른 컴퓨터들이 shell을 통해 통신하기 위한 protocol 이 필요했고, Rlogin, Telnet 등을 거쳐 지금 가장 많이 사용되는 것이 SSH입니다.

---

## ✔️ SSH의 장점: 암호화된 통신
HTTPS에서 통신 간 데이터가 암호화되어 있는 것과 같이, SSH를 이용한 통신에서는 Client와 Host의 통신이 암호화되어 있습니다.

💡 Client: Host에 접속하려고 하는 컴퓨터
💡 Host: 접속 대상 (ex. remote 서버)

모든 데이터가 암호화되어 전송되기 때문에 굉장히 안전하다는 점이 SSH 가 갖는 가장 큰 장점이며, 널리 사용되고 있는 이유입니다.

## ✔️ SSH의 Encryption/Decryption 과정
SSH 통신에서 데이터가 암호화되는 과정에는 크게 3가지 스텝이 존재합니다.

대칭 암호화 (Symmetric Encryption)
비대칭 암호화 (Asymmetric Encryption)
해쉬 함수 (Hashing)
대칭 암호화 (Symmetric Encryption)
대칭 암호화란 1개의 공통된 Secret Key를 가지고 양쪽에서 데이터를 암호화 및 복호화할 수 있게 하는 방법입니다.

### 대칭 암호화 (Symmetric Encryption)
서로 같은 Secret Key 만 있으면 데이터를 암호화/복호화할 수 있다는 장점이 있지만, 혹시나 Secret Key 가 유출되었을 경우 암호화된 모든 통신이 노출된다는 치명적인 단점이 존재합니다.

이런 이유 때문에 안전하게 Key를 exchange 하는 방법으로 비대칭 암호화 (Asymmetric Encryption) 알고리즘이 존재합니다.

비대칭 암호화 (Asymmetric Encryption)
비대칭 암호화 방식에서는 송신자, 수신자 각각 Public Key와 Private Key를 가지게 됩니다. 송신자의 Public Key로 암호화된 데이터는 송신자의 Private Key를 사용해야만 복호화가 가능합니다. 그래서 서로의 Public Key를 교환하여 암호화하는 데 사용하게 됩니다.

정리해보자면,

나는 상대방에서 나의 Public Key를 보내주고, 나는 상대방의 Public Key를 받는다.
내가 보내고 싶은 데이터를 내가 가진 상대방의 Public Key로 암호화 한 뒤 전송한다.
상대방이 내가 전송한 암호화된 데이터를 받아 본인의 Private Key로 복호화한다.

### 비대칭 암호화 (Asymmetric Encryption)

그렇다면 SSH는 온전히 비대칭 암호화를 사용하겠구나!라고 생각할 수 있지만 실제로는 그렇지 않습니다. 비대칭 암호화는 송/수신자 간 대칭 암호화에서 필요했던 공통의 Secret Key를 생성하는 과정에서 사용합니다.

그렇다면, 서로가 서로의 Public Key를 공유할 때는 해커가 해당 데이터를 가로챌 위험성은 없을까? 당연히 있습니다!

그래서 등장하는 알고리즘이 디피-헬먼 키 교환 알고리즘 (Diffie-Hellman Key Exchange Algorithm)입니다. 디피-헬먼 알고리즘에 대해서는 수학적인 개념이 많고, 자료가 많으니 따로 짚고 넘어가진 않겠습니다. 여기서 중요한 정보는 이 키 교환 알고리즘은 암호화 알고리즘이 아니며, 서로 간 안전하게 키를 공유할 수 있게 해주는 알고리즘이라는 점입니다.

### 해쉬 함수 (Hashing)
지금 까지 대칭 암호화, 비대칭 암호화, 디피-헬먼 키 교환 알고리즘이 SSH에서 송/수신자 간 안전하게 암호화된 데이터를 주고받는지에 대해 알아봤습니다.

하지만, SSH 커넥션이 성공적으로 생기기 전 이미 누군가 (해커) 송/수신자 (Client 혹은 Host)를 위장하여 잠입해, 모든 정보를 조작할 가능성도 있습니다. 이런 경우 송/수신자는 아무것도 모른 채 악의적으로 변화된 데이터를 지속적으로 받아보게 될 수도 있습니다.

이를 방지하기 위해 SSH에서는 MAC (Message Authentication Code)을 통한 데이터 무결성을 (송신자가 보낸 메시지가 변조되지 않았는지) 확인하는 절차가 존재합니다.

Hash 함수가 전송되는 메시지, 송/수신자가 공유하는 Secret Key, 그리고 Packet Sequence Number를 가지고 MAC 값을 출력하여 수신자 (Host) 에게 보내줍니다. 수신자도 같은 정보를 가지고 있기 때문에, 같은 정보를 가지고 똑같은 Hash 함수를 돌려 나온 MAC 값이 일치하는지 확인하는 방식입니다.

💡 Hashing 은 기본적으로 단방향으로 이루어집니다. 단방향으로 이루어지는 이유는 복호화를 염두에 두고 사용하는 암호화 과정이 아니기 때문입니다.

---

## ✔️ Authentication
이렇게 해서 송/수신자 간 안전한 connection 이 열리게 되면, 이제 접속을 시도하는 user 가 접속 권한이 있는 사람인지 확인하는 절차가 필요합니다. 만약 이런 절차가 없다면 누구나 언제든 서버에 접속할 수 있기 때문입니다.

SSH에서 User를 인증하는 방법은 크게 2가지가 있습니다.

Password (username/password — username 은 보통 root)
RSA (password 없이 identity 확인)

### Password
우선 비밀번호를 입력하는 방법입니다.

이미 여러 가지 절차를 통해 secure connection 이 열린 상태이기 때문에 password를 입력해도 크게 문제 될 건 없겠지만 권장되는 방법은 아닙니다. 왜냐하면 이 방법은 Brute-force 공격에 취약하다는 단점이 있기 때문입니다.

💡 Brute-force Attack: 키 전수조사 또는 무차별 대입 공격을 통해 조합 가능한 모든 문자열을 하나씩 대입해 보는 방식으로 암호를 해독하는 방법이다.

그래서 비밀번호 입력 없이 나의 identity 가 확인되는 방법이 바로 RSA입니다.

### RSA
요약하자면, 내 컴퓨터에 SSH 키를 생성하여 나의 Public Key를 host의 인가 목록에 추가해 자동으로 나를 인증하는 방법입니다. 이 방법을 이용하면 해당 Public Key와 매칭 되는 Private Key 가 존재하는 나의 컴퓨터를 사용하는 한 비밀번호 없이 인증이 가능합니다.

--- 

## ✔️ SSH Key 생성 방법 (깃 헙 계정 연동 목적)
🚩 Step 1: SSH Key 존재 유무 확인

```
ls -al ~/.ssh
```

id_rsa.pub이라는 파일이 존재하면 SSH Key 가 이미 있다는 뜻이라 추가로 생성할 필요가 없습니다.

🚩 Step 2: SSH Key 생성

```
ssh-keygen -t rsa -b 4096 -C "깃헙이메일주소"
```
“깃 헙 이메일 주소” 부분에는 당연히 본인의 GitHub 이메일 주소가 들어가야 합니다.

```
> Generating public/private rsa key pair.
```
보이다시피 우리가 알고 있는 Public/Private key pair를 생성한다고 적혀있는 걸 확인할 수 있습니다.

```
> Enter a file in which to save the key (/Users/you/.ssh/id_rsa):
```
위처럼 뜨면 입력 없이 엔터를 눌러 default 위치에 저장하면 됩니다.

```
> Enter passphrase (empty for no passphrase):
> Enter same passphrase again:
```
💡 여기서 passphrase 란,
RSA 방식의 단점인 누군가 악의적으로 내 컴퓨터를 컨트롤할 수 있게 된다면, 내 컴퓨터에 있는 SSH Key를 가진 모든 시스템에 접근 권한이 생기기 때문에 SSH Key에 추가적으로 비밀번호를 추가하는 보안 레이어입니다.

하지만 추가 보안 레이어 없이 그냥 엔터를 눌러 넘어갈 수 있습니다.

🚩 Step 3: GitHub 계정에 SSH Key 추가

SSH Key 복사 (id_rsa.pub 파일 내용 복사 — Public Key)

```
pbcopy < ~/.ssh/id_rsa.pub
```
우측 상단 프로필 사진 버튼을 누른 뒤 Settings로 갑니다.

SSH and GPG keys 클릭

New SSH key 버튼 클릭

Title 에는 본인 식별용으로 이름을 붙여줍니다. (저는 Macbook Pro 16)

Key 부분에는 pbcopy로 복사했던 SSH Key를 붙여 넣기 해줍니다.

붙여 넣기 후 Add SSH key 버튼을 눌러주면 끝입니다.

🚩 Step 4: 제대로 연결이 되었는지 확인할 차례입니다.

아래 커맨드를 통해 새로 추가한 SSH 커넥션이 제대로 연결되었는지 확인합니다.

```
ssh -T git@github.com
```
입력하면 아래와 같은 메시지가 나타날 수도 있습니다.

```
> The authenticity of host 'github.com (IP ADDRESS)' can't be established.
> RSA key fingerprint is 
13:24:35:a1:b2:~~~~.
> Are you sure you want to continue connecting (yes/no)?
```
혹은
```
> The authenticity of host 'github.com (IP ADDRESS)' can't be established.
> RSA key fingerprint is SHA256:sdfasdfasdfhjbasjhdfbashjdbfashdbfajhsbdfashdfb.
> Are you sure you want to continue connecting (yes/no)?
```
fingerprint 가 일치하는지 확인 후 yes를 입력해주면

```
> Hi 본인유저네임! You've successfully authenticated, but GitHub does not
> provide shell access.
```
🚩 완료 🎉🎉🎉

이제 GitHub에서 repository를 clone 받거나, 다른 액션을 수행할 때 GitHub username과 password를 입력해야 하는 번거로움이 사라졌습니다.

아래 사진과 같이 이제 HTTPS 가 아닌 SSH 방식으로 clone 이 가능합니다!

---

## ✔️ 마무리
부족한 필력으로 정보 전달을 하려니 어려움이 많았지만, 앞으로 다양한 주제들을 다루는 포스팅을 추가하면서 점점 더 발전시키려고 합니다.

이해를 돕기 위해 중간중간 등장하는 diagram 들도 직접 draw.io로 만들어봤지만 굉장히 형편없습니다. 이 또한 점점 발전하길!

SSH 가 무엇인지 잘 모른 채로 누가 시키는 대로 혹은 Google 검색을 토대로 무작정 따라 하셨던 분들께 조금이나마 도움이 되었으면 합니다.