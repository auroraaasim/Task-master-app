package _G02.trello.task.service;

import _G02.trello.signup.model.UserModel;
import _G02.trello.task.model.TaskModel;

import java.time.LocalDateTime;
import java.util.List;

public interface ITaskService {
    TaskModel createTask(TaskModel taskModel);
    TaskModel findTaskByID(Long taskId);
    void deleteTask(Long taskId);
    List<TaskModel> getAllTasks();
    public TaskModel updateTask(TaskModel taskModel);

    public TaskModel assignTaskToMember(TaskModel task, UserModel user);

}
