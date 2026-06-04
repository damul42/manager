# Project Context: Management System

## 1. Project Overview
**Management System** 은 내부 임직원 관리 및 비용 관리, 프로젝트 관리, 휴가 일정 관리 및 공유, 승인 워크 플로우, 커뮤니케이션 등을 지원하는 내부 그룹웨어 및 관리 시스템이다

## 2. Target Users
*   **직원:** 자기 자신의 정보를 등록하고 수정하거나 할당된 프로젝트 정보를 설정/변경 하고 권한에 따라 휴가나 경비 등을 승인하고 다른 직원과 커뮤니케이션 할 수 있다.
*   **관리자:** 시스템 관리자로 시스템 사용자들을 관리하거나 권한의 설정, 시스템 설정 등을 할 수 있다.

## 3. Key Features & User Journey
1.  **사용자 관리:** 시스템의 사용자를 등록하거나 수정할 수 있다.
2.  **조직 관리:** 조직의 구조를 관리하고 조직별 head를 설정하여 승인 프로세스 등을 돌릴 수 있는 기초 데이터를 제공한다.
3.  **비용 관리:** 직원별 비용이나 인건비, 프로젝트 별 비용 관리를 통해 전체 회사의 PnL 또는 Project 별 PnL을 관리한다.
4.  **휴가 관리:** 직원별로 휴가를 할당하고 신청하며 승인 프로세스에 따라 승인하고 차감하며 휴가 일정을 공유한다.
5.  **커뮤니케이션:** 게시판 및 Chat 형태의 커뮤니케이션 보드를 제공한다.
6.  **기능 관리:** 시스템의 기능을 데이터화 하여 권한에 따라 기능의 접근 여부를 관리한다.
7.  **권한 관리:** 각 기능별 접근 권한을 설정하여 사용자에게 할당하고 이를 통해 사용 및 표출 여부를 관리한다.

## 4. Technical Constraints & Considerations
*   **Security:** JWT 토큰을 사용하여 인증 및 권한 등을 포현한다.
*   **Integration:** Support for various systems (ERP, CRM, WMS, etc.) via Read/Write APIs.

## 5. Visual Identity & UI
*   Professional, PwC-branded aesthetic.
*   Interactive dashboards and clean, wizard-like processes for registration and requirement definition.
*   Report-centric outputs (PDF/Web reports for business stakeholders).

## 6. Reference Documents
*   `requirements/raw/기획.pdf`: Business strategy and positioning.
*   `requirements/raw/phase_definition.pdf`: Rollout phases.
*   `requirements/raw/TalkFile_Full_ui.pdf`: Detailed UI/UX flow and screen mockups.
*   `requirements/raw/Models_List_APAC 2.csv`: Initial list of supported AI models.
