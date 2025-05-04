# haohao_journal_server

## Task Time Management Endpoints

### Update Task Estimated Hours

- **PUT** `/api/tasks/{id}/estimated-hours`
- Updates the estimated hours for a task
- Query Parameters:
  - `hours`: Float - The new estimated hours value
- Returns: Updated Task object

### Update Task Actual Hours

- **PUT** `/api/tasks/{id}/actual-hours`
- Updates the actual hours spent on a task
- Query Parameters:
  - `hours`: Float - The new actual hours value
- Returns: Updated Task object

### Get Estimated Hours by Sprint and Status

- **GET** `/api/tasks/sprint/{sprintId}/status/{status}/estimated-hours`
- Gets the sum of estimated hours for all tasks in a sprint with a specific status
- Path Parameters:
  - `sprintId`: Long - The ID of the sprint
  - `status`: TaskStatus - The status of the tasks (e.g., IN_PROGRESS, DONE)
- Returns: Float - Sum of estimated hours

### Get Actual Hours by Sprint and Status

- **GET** `/api/tasks/sprint/{sprintId}/status/{status}/actual-hours`
- Gets the sum of actual hours for all tasks in a sprint with a specific status
- Path Parameters:
  - `sprintId`: Long - The ID of the sprint
  - `status`: TaskStatus - The status of the tasks (e.g., IN_PROGRESS, DONE)
- Returns: Float - Sum of actual hours
