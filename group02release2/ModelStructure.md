# Models Documentation

## UserModel

- `first_name`: String - The first name of the user.
- `last_name`: String - The last name of the user.
- `username`: String - The username of the user.
- `password`: String - The password of the user.
- `email`: String - The email address of the user. This field is unique.
- `securityQuestion`: String - The security question of the user. This field should not be edited or included in requests directly. (Use API)
- `securityAnswer`: String - The security answer of the user. This field should not be edited or included in requests directly. (Use API)

## WorkspaceModel

- `workspaceName`: String - The name of the workspace.
- `owner`: int - The ownerId of the workspace.
- `workspacetype`: WorkspaceType - The type of the workspace.
- `workspaceDescription`: String - The description of the workspace.
- `updateTime`: LocalDateTime - The time when the workspace was last updated. Formatted as "yyyy-MM-dd HH:mm:ss" (String).
- `createTime`: LocalDateTime - The time when the workspace was created. Formatted as "yyyy-MM-dd HH:mm:ss" (String).

## BoardModel

- `boardName`: String - The name of the board.
- `owner`: int - The owner of the board.
- `project`: int - The project associated with the board.
- `updateTime`: LocalDateTime - The time when the board was last updated. Formatted as "yyyy-MM-dd HH:mm:ss" (String).
- `createTime`: LocalDateTime - The time when the board was created. Formatted as "yyyy-MM-dd HH:mm:ss" (String).

## TaskModel

- `taskName`: String - The name of the task.
- `description`: String - The description of the task.
- `owner`: int - The owner of the task.
- `status`: int - The status of the task.
- `project`: int - The project associated with the task.
- `updateTime`: LocalDateTime - The time when the task was last updated. Formatted as "yyyy-MM-dd HH:mm:ss" (String).
- `dueTime`: LocalDateTime - The due time of the task. Formatted as "yyyy-MM-dd HH:mm:ss" (String).
- `taskCurrentTime`: LocalDateTime - The current time of the task. Formatted as "yyyy-MM-dd HH:mm:ss" (String).
- `listIndex`: int - The index of the list in a board. ("list" means the list in the Board, NOT the index of the task in list.)

