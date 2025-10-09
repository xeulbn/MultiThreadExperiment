## 프로젝트 목적

- I/O 바운드와 CPU 바운드별 성능 비교, 다수 외부 URL 병렬 호출 (동기 vs 비동기) 시간측정, Mailbox PingPong으로 Thread통신과 IPC 통신 비교를 직접 실행해 보는 실험 어플리케이션입니다.


--- 
## HTTP 동기 vs. 비동기 Flow Chart
<img width="2953" height="864" alt="http동기비동기 흐름도" src="https://github.com/user-attachments/assets/e0bbb191-ebaa-42ff-8d55-f21effd1d1d9" />

## CPU Bound & IO Bound Flow Chart
<img width="2760" height="1122" alt="CPU바운드IO바운드" src="https://github.com/user-attachments/assets/dbf2e97b-06c9-4512-8680-288a0d661374" />

## Mailbox vs Process IPC Flow Chart
<img width="2778" height="1534" alt="메일박스핑퐁" src="https://github.com/user-attachments/assets/bc742da7-04b5-4ab3-993c-c2a3e8b4c1de" />


---
## 실험 결과

### IO 바운드 실험
<img width="539" height="480" alt="스크린샷 2025-09-24 오후 2 38 08" src="https://github.com/user-attachments/assets/ff775386-6837-4cb2-a147-4dbc6ace61f2" />

### CPU 바운드 실험
![cpu바운드](https://github.com/user-attachments/assets/ba36ee7c-9f6a-4be1-9d0b-1ced04e5a0d5)

### 동기 vs. 비동기 실험 (HTTP Request)
![Httpreq](https://github.com/user-attachments/assets/84dce3b6-ff57-4bed-a23f-9da1c6e02b9c)

### Thread 통신 vs. IPC 통신 비교 (PingPong)
![process](https://github.com/user-attachments/assets/ea12df74-49c5-48b1-a70c-4ef26e4773c7)



---
## 느낀점

- 가상 스레드는 I/O 대기가 많은 워크로드에서 동시성↑ 비용↓ 효과가 큽니다.
- CallerRunsPolicy는 실패/유실 대신 “진입 속도 조절”을 선택하는 전략으로 대기 시간을 늘리는 대신, 시스템 전체 붕괴를 방지합니다.
