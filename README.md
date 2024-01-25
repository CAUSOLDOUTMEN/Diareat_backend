# 프로젝트명
다이어릿 - Diareat (2023.09 ~ 2023.12)

# Diareat, 사각지대 없는 식습관 관리를 위해!
<img width="1000" alt="image" src="https://github.com/CAUSOLDOUTMEN/.github/blob/main/profile/diareat_main.png">

<다이어릿>은 음식을 촬영하면 칼로리를 대략적으로 계산해주는 기존 서비스에서 영감을 받아, 가공식품의 영양성분표를 촬영하면 칼로리를 추출해주는 OCR 기능을 추가로 결합해 식습관 기록의 사각지대를 해소하는 데 기여하는 Food Capture 서비스입니다.
## Camera

https://github.com/CAUSOLDOUTMEN/Diareat_backend/assets/53044069/f36dba64-3022-4b1b-b811-d9203fb987b5


- 음식이나 영양성분표를 촬영하면 일정 시간 후 영양성분을 추출하실 수 있습니다.
    - 실제 음식의 경우 두잉랩의 foodlens에서 제공하는 외부 API를 사용합니다.
    - 영양성분표의 경우 Pororo OCR을 기반으로 자체 구축한 OCR 서버를 활용합니다.
## Diary
<img width="752" alt="image" src="https://github.com/CAUSOLDOUTMEN/Diareat_backend/assets/53044069/dda543b8-916e-446d-95c3-a5c8921c1f03">


- [오늘의 영양] 화면에서 오늘 촬영한 음식들에 대한 영양분이 실시간으로 결산됩니다.
- [다이어리] 화면에서 오늘 촬영한 음식들에 대한 정보를 확인할 수 있습니다.
- [즐겨찾기] 기능을 통해 자주 먹는 음식을 등록하여 재촬영 없이 편리한 다이어리 추가가 가능합니다.
- [프로필] 화면에서 개인정보 및 목표로 설정된 기준섭취량을 확인하고 직접 수정하실 수 있습니다.

## Analysis
<img width="732" alt="image" src="https://github.com/CAUSOLDOUTMEN/Diareat_backend/assets/53044069/895abda2-e9e6-411d-800c-718601de947b">

- [일기 분석] 화면에서 최근 7일 및 4주 간의 영양소별 섭취 현황을 그래프로 제공합니다.
- [자세히 보기]를 통해 이번 주의 구체적인 식습관 점수 현황, 최근 Best, Worst 음식들을 확인하실 수 있습니다.
- [주간 랭킹] 화면에서 검색 기능을 통해 특정 유저를 팔로우 혹은 팔로우 취소할 수 있고, 자신이 팔로우한 유저들의 주간 식습관 점수를 확인하실 수 있습니다.
- 친구들과 함께 앱을 사용하며 식습관 개선 목표를 달성하기 위해 서로 경쟁해보세요!

# Project Information

## Members
![image](https://github.com/CAUSOLDOUTMEN/Diareat_backend/assets/53044069/f85d298e-9c12-417b-b2b6-bdbfd5b86bbb)

## Tech Stack

### Frontend

- Android
- Kotlin

### Backend

- Java 11
- Springboot
- Redis
- MySQL

### OCR Server

- Python (Pororo OCR)
- OpenCV
- FastAPI
- AWS EC2

### Infra & CI/CD

- Kubernetes
- Argo CD
- Kustomization
- Github actions
- Docker

## Project **Architecture**
![image](https://github.com/CAUSOLDOUTMEN/Diareat_backend/assets/53044069/21f5deb4-8dce-4cc9-a148-6a78e8370c4e)
