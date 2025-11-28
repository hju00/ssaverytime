# SsaveryTime Vue 프로젝트

이 프로젝트는 `ssavery-time-ui-design` React 프로젝트의 UI/UX를 Vue 3로 재현하기 위해 제작된 Vue.js 애플리케이션입니다. Vite를 통한 Vue 3, 스타일링을 위한 Tailwind CSS, UI 컴포넌트를 위한 shadcn-vue, 내비게이션을 위한 Vue Router, 국제화를 위한 `vue-i18n`을 활용합니다.

## 사용된 기술

*   **Vue.js 3**: 점진적 자바스크립트 프레임워크.
*   **Vite**: 개발 서버 및 빌드에 사용되는 차세대 프론트엔드 툴링.
*   **Vue Router**: Vue.js 공식 라우터.
*   **Tailwind CSS**: 사용자 정의 디자인을 빠르게 구축하기 위한 유틸리티 우선 CSS 프레임워크.
*   **shadcn-vue**: Radix Vue 및 Tailwind CSS를 사용하여 구축된 재사용 가능한 UI 컴포넌트.
*   **vue-i18n**: Vue.js를 위한 국제화 플러그인.
*   **Lucide 아이콘**: SVG 아이콘 컬렉션.

## 사전 준비 사항

시작하기 전에 시스템에 다음이 설치되어 있는지 확인하십시오:

*   **Node.js**: (LTS 버전 권장)
*   **npm** (Node Package Manager) 또는 **Yarn** 또는 **pnpm**

## 설정 및 설치

로컬 시스템에서 프로젝트를 설정하고 실행하려면 다음 단계를 따르십시오:

1.  **저장소 클론:**
    ```bash
    git clone <YOUR_REPOSITORY_URL>
    cd ssavery-time
    ```
    *(`<YOUR_REPOSITORY_URL>`를 실제 Git 저장소 URL로 대체하십시오.)*

2.  **의존성 설치:**
    아직 `ssavery-time` 디렉토리로 이동하지 않았다면 이동한 후 프로젝트 의존성을 설치하십시오:
    ```bash
    npm install
    # 또는 Yarn 사용 시
    # yarn install
    # 또는 pnpm 사용 시
    # pnpm install
    ```

3.  **개발 서버 시작:**
    의존성 설치가 완료되면 개발 서버를 시작할 수 있습니다:
    ```bash
    npm run dev
    ```
    애플리케이션은 일반적으로 `http://localhost:5173`에서 사용할 수 있습니다.

## 페이지 접속

(개발 서버가 `http://localhost:5173`에서 실행 중이라고 가정할 때) 다음 URL을 사용하여 애플리케이션의 다양한 페이지에 접속할 수 있습니다:

*   **홈 (인증) 페이지**: `http://localhost:5173/`
*   **게시판 페이지**: `http://localhost:5173/board`
    *   여기에서 개별 게시물을 클릭하여 상세 페이지로 이동할 수 있습니다.
*   **게시물 상세 페이지**: `http://localhost:5173/post/:id` (예: `http://localhost:5173/post/1`)
*   **식단 페이지**: `http://localhost:5173/diet`
*   **프로필 페이지**: `http://localhost:5173/profile`

## 국제화 (i18n)

이 프로젝트는 `vue-i18n`을 사용하도록 구성되어 있으며, 한국어(`ko`)를 기본 로케일로, 영어(`en`)를 대체 로케일로 설정합니다. `src/main.js`에서 기본 로케일을 변경하거나, 필요한 경우 언어 전환기를 구현할 수 있습니다.

## 기여

프로젝트에 기여하는 것을 환영합니다. 기존 코드 스타일 및 규칙을 따라주십시오.
