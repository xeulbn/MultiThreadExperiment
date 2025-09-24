## 프로젝트 목적

- 자바의 다양한 동시성 기법(스레드풀, 가상 스레드, @Async, IPC, mmap, 원자 연산)을 직접 실행해 보는 실험 어플리케이션입니다.

---
## 핵심 키워드
- Backpressure(백프레셔):
  과부하 시 진입 속도를 자연스럽게 늦춰 시스템 붕괴를 막는 설계. 이 프로젝트는 `queue=200 + CallerRunsPolicy`로 구현하였습니다.
- CPU vs I/O 바운드:
  CPU는 코어 한계에, I/O는 대기비용에 민감합니다. 가상 스레드는 I/O 대기에 특히 유리합니다.


--- 
## 전체 동작 순서도 (High-level Flow)
<img width="1184" height="842" alt="스크린샷 2025-09-24 오후 1 24 25" src="https://github.com/user-attachments/assets/8ed42bc4-48c9-47c8-a139-710da5a2fc62" />


## 플랫폼 스레드 vs 가상 스레드 비교 플로우
<img width="1131" height="166" alt="image" src="https://github.com/user-attachments/assets/876825f9-b796-4593-96fc-b01bb1848709" />


## Mailbox vs Process IPC 플로우
<img width="784" height="555" alt="image" src="https://github.com/user-attachments/assets/ac1b3054-f4a9-4c57-b749-28ee145f2846" />


---
## 느낀점

- 가상 스레드는 I/O 대기가 많은 워크로드에서 동시성↑ 비용↓ 효과가 큽니다.
- CallerRunsPolicy는 실패/유실 대신 “진입 속도 조절”을 선택하는 전략으로 대기 시간을 늘리는 대신, 시스템 전체 붕괴를 방지합니다.
- nmap은 항상 빠르지 않으며, 패턴/플랫폼/디스크 상황에 따라 다릅니다.
