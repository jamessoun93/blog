# RDS

RDS란 SQL을 이용하는 DB 엔진들에 대한 관리형 DB 서비스이며 아래 엔진들을 지원함.

- Postgres
- MySQL
- MariaDB
- Oracle
- Microsoft SQL Server
- Aurora

# 직접 EC2에 배포하는 것과 RDS를 이용하는 것의 차이?

RDS는 관리형 DB 서비스로서 데이터베이스 관리에 있어 필요한 많은 서비스를 제공.

- provisioning 및 OS 패치 자동화
- 지속적인 백업과 특정 시점으로 복원 (Point in Time Restore)
- 모니터링 대시보드
- 읽기 성능 향상을 위한 Read Replicas 제공
- Disaster Recovery를 위한 멀티 AZ 셋업
- 업그레이드를 위한 maintenance windows
- 스케일 업 + 아웃 가능
- EBS 기반 스토리지 (gp2 혹은 io1)

하지만 EC2와 달리 SSH 연결을 통한 접근 불가능.

# Storage Auto Scaling

Storage Auto Scaling이란 동적으로 RDS DB 인스턴스의 저장공간을 늘릴 수 있는 기능을 뜻함.

RDS가 사용중인 데이터베이스에 남은 저장공간이 충분치 않은 것을 확인하여 자동으로 저장공간 확장.

예상치 못한 규모의 데이터가 저장되어야 할 때 큰 도움이 되는 기능.

Maximum Storage Threshold 설정을 통해 확장 가능한 최대 limit을 설정할 수 있음.

RDS는 아래 세 가지 조건을 충족할 때 자동으로 저장공간 확장.

1. 이용 가능한 저장공간이 해당 인스턴스에 할당된 총 저장공간의 10% 보다 작을 때
2. 이용 가능한 저장공간이 낮은 상태로 5분이상 지속될 때
3. 마지막 저장공간 변경이 있던 시점부터 6시간 이상 지났을 때

저장공간 오토스케일링 기능은 모든 RDS 엔진을 지원함.

# RDS Read Replicas vs Multi AZ (Disaster Recovery)

우리가 만든 애플리케이션은 지속적으로 데이터베이스와 통신하면서 read 와 write 요청을 수행함.

RDS는 Read Replica를 통해 메인 RDS instance에 read 요청이 과도하게 많은 경우에 대한 대안을 제공함.

이름 그대로 DB 인스턴스의 읽기 전용 복제본을 만들어주는 것.

### RDS Read Replicas - Key Points

- Read Replica는 5개까지 만들 수 있음.
- 같은 AZ, Cross AZ, 혹은 Cross Region에 생성할 수 있음.
- Replication은 비동기(async)로 진행되어 read는 eventually consistent 함. (replication이 진행되기 전에 읽어들이면 old 데이터를 읽어들일 수 있음.)
- 원한다면 Replica를 하나의 자체 DB 인스턴스로 업그레이드 시킬 수 있음.
- 애플리케이션에서 클러스터에 있는 read replica들을 활용하려면 커넥션 설정을 업데이트 해야함.

### RDS Read Replicas - Network Cost

AWS에는 특정 AZ에서 다른 AZ로 데이터를 옮기는데 network cost가 존재함.

RDS Read Replica의 경우 같은 Region인 경우 다른 AZ에 read replica를 두고 replication을 진행해도 network cost가 없음.

us-east-1a 에서 us-east-1b 로의 replication 은 FREE 라는 뜻.

하지만 Cross-Region replication은 fee를 내야함. (ex. us -> eu)

### RDS Multi AZ - Key Points

다른 AZ에 스탠바이 DB를 둬서 가용성을 높이기 위해 사용

- SYNC replication: 다른 AZ에 있는 standby 인스턴스에 replication을 synchronous하게 진행함. 애플리케이션에서 마스터 DB에 write 요청을 하면 같은 write 요청이 스탠바이 DB에도 전달됨.
- 같은 DNS name을 이용하기 때문에 automatic failover를 지원. (가용성이 높아짐)
- 애플리케이션에서는 건들게 없음.
- 스케일링 목적으로 이용하는 서비스가 아님.
- **Read Replicas를 Multi AZ로 구축이 가능함.** (common exam question)

# From Single-AZ to Multi-AZ

- Zero downtime operation (DB를 멈출 필요가 없음)
- "modify" 버튼 누르고 Multi-AZ로 변경하면 됨.
- 자동으로 마스터 DB 인스턴스로부터 synchronous하게 replicated 되는 스탠바이 DB를 생성하고 연결함.
  1. 마스터 DB 인스턴스의 snapshot을 생성.
  2. 해당 snapshot을 이용하여 새로운 DB를 restore
  3. 두 DB 간 synchronization 작업

# RDS Custom for Oracle and Microsoft SQL Server

RDS는 EC2와 다르게 직접 서버 내부에 접속하여 설정을 조작하거나 할 수 없음.

하지만 Oracle과 MS SQL Server 같은 경우 RDS Custom 을 통해 OS와 DB customization이 가능함.

RDS Custom을 사용하면 패치, 설정 조작, SSH 혹은 SSM 세션매니저를 통해 EC2 인스턴스에 접속할 수 있음.

RDS Custom 모드를 활용하기 위해서는 Automation Mode 를 비활성화하고 작업 전 snapshot을 만들어 두는게 좋음. (실수할 수 있기 때문)

### RDS Custom 정리

- RDS: DB와 OS가 전부 AWS에 의해 관리됨.
- RDS Custom: DB와 OS에 어드민 권한을 가지고 여러가지 작업을 수행할 수 있음.

단, Oracle과 MS SQL Server에 한해 이용 가능함.
