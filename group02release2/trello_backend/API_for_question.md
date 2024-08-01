**Some apis For UserController**

- `PUT /user/updatePassword/{userId}`: Updates the password of the specified user.
  - Path Parameters:
    - `userId` (integer, required): The ID of the user whose password is to be updated.
  - Query Parameters:
    - `password` (string, required): The new password.
  - Response: A boolean value indicating whether the update was successful.
  - Example:
    - Request:
      ```
      PUT /updatePassword/1?password=newpassword
      ```
    - Response:
      ```
      true
      ```

- `GET /user/searchByEmail`: Searches for a user ID by email address.
  - Query Parameters:
    - `email` (string, required): The email address to search for.
  - Response: An integer representing the ID of the user with the specified email address, or `0` if no such user is found.
  - Example:
    - Request:
      ```
      GET /searchByEmail?email=user@example.com
      ```
    - Response:
      ```
      1
      ```

- `GET /user/getQuestionList/{userId}`: Retrieves the list of security questions for the specified user.
  - Path Parameters:
    - `userId` (integer, required): The ID of the user whose security questions are to be retrieved.
  - Response: An array of strings representing the security questions for the specified user.
  - Example:
    - Request:
      ```
      GET /getQuestionList/1
      ```
    - Response:
      ```
      [
          "What is your mother's maiden name?",
          "What was the name of your first pet?"
      ]
      ```

- `PUT /user/addQuestionAndAnswer/{userId}`: Adds a security question and its answer for the specified user.
  - Path Parameters:
    - `userId` (integer, required): The ID of the user for whom to add a security question and its answer.
  - Query Parameters:
    - `question` (string, required): The security question to add.
    - `answer` (string, required): The answer to the security question.
  - Response: A boolean value indicating whether the addition was successful.
  - Example:
    - Request:
      ```
      PUT /addQuestionAndAnswer/1?question=What%20is%20your%20favorite%20color?&answer=blue
      ```
    - Response:
      ```
      true
      ```

- `GET /user/checkAnswer/{userId}`: Checks whether the answer to a security question for the specified user is correct.
  - Path Parameters:
    - `userId` (integer, required): The ID of the user whose security question answer is to be checked.
  - Query Parameters:
    - `question` (string, required): The security question to check.
    - `answer` (string, required): The answer provided by the user.
  - Response: A boolean value indicating whether the answer is correct.
  - Example:
    - Request:
      ```
      GET /checkAnswer/1?question=What%20is%20your%20favorite%20color?&answer=blue
      ```
    - Response:
      ```
      true
      ```
