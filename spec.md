## 1. Application Overview

**Application Name**: **Haohao Journal**

**Concept**:
"Record your daily life carefully and nurture it"
Manage tasks in a weekly unit (sprint) and review at the end of each day.
Flutter (mobile app) + Spring Boot (backend API) configuration.

---

## 2. Target Platforms

- Flutter (iOS / Android)

- Spring Boot (Kotlin) backend API

- Database: PostgreSQL (hosted on Railway)

---

## 3. Main Features

| Feature                          | Description                                                                                                                                            |
| -------------------------------- | ------------------------------------------------------------------------------------------------------------------------------------------------------ |
| Sprint Management                | Automatically starts on Monday at 0:00 and ends on Sunday at 23:59. No manual creation required.                                                       |
| Task Registration and Management | Manage titles, memos, Epic (classification tag), scheduled date, status (TODO/DOING/DONE).                                                             |
| Task List Display                | Display task lists by Epic and scheduled date.                                                                                                         |
| Task Edit and Delete             | Edit and delete tasks in the task details screen.                                                                                                      |
| Today's Task List                | Display only the tasks scheduled for today on the home screen.                                                                                         |
| Warning for Overdue Tasks        | Red line/badge display for unfinished tasks past the scheduled date.                                                                                   |
| Daily Review (DailyReview)       | Record doneSummary (what was done) and feelingSummary (thoughts and learning).                                                                         |
| Sprint Review (SprintReview)     | Summarize "I want to boast about this" and "I want to thank this" and "I want to continue this" and "I'm stuck" and "I want to change this" on Sunday. |
| Calendar View                    | Display calendar during sprint period, transition to daily review on date tap.                                                                         |

---

## 4. Screen List (Phase 1)

| Screen Name                  | Main Function                                             |
| ---------------------------- | --------------------------------------------------------- |
| Home Screen                  | Today's task management + task addition + review guidance |
| Task List Screen             | Display task lists by Epic and date                       |
| Task New Creation Screen     | Register a new task                                       |
| Task Details and Edit Screen | Edit and delete existing tasks                            |
| Summary Screen               | Display Calendar View                                     |
| Daily Details Screen         | Task progress for today and selected date + review entry  |

## 5. Data Model Overview

```
Sprint
- id
- startDate
- endDate

Epic
- id
- name
- color (optional)

Task
- id
- sprintId
- epicId
- title
- memo
- plannedDate
- completedDate (nullable)
- status (TODO / DOING / DONE)
- createdAt
- updatedAt

DailyReview
- id
- sprintId
- date
- doneSummary（やったこと）
- feelingSummary（所感・学び）
- createdAt

SprintReview
- id
- sprintId
- doyaSummary（ドヤりたいこと）
- thanksSummary（感謝したいこと）
- continueSummary（続けたいこと）
- stuckSummary（詰まっていること）
- changeSummary（変えたいこと）
- createdAt

```

---

## 6. Navigation Structure

```
Home Screen
 ├─→ Task New Creation Screen
 ├─→ Task Details and Edit Screen
 ├─→ Daily Details Screen (Today)

Task List Screen
 ├─→ Task New Creation Screen
 ├─→ Task Details and Edit Screen

Summary Screen
 ├─→ Daily Details Screen (Selected Date)

```

---

## 7. Technical Stack (Planned)

- Flutter 3.22+

- Dart 3.x

- table_calendar（Calendar display package）

- Riverpod or StateNotifier（Lightweight state management）

- Material3 Design

- Spring Boot 3.x

- PostgreSQL (Railway)

- Railway Deployment（API & DB）

---

## 8. MVP Scope (1-week Development Goal)

- Phase 1 feature (task management, automatic sprint generation, daily review recording)

- UI design focuses on simplicity, modernity, and warmth

- Minimum release in a form that can save and reference data
