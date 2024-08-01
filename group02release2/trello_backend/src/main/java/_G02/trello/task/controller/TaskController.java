package _G02.trello.task.controller;

import _G02.trello.signup.model.UserModel;
import _G02.trello.signup.repository.UserRepository;
import _G02.trello.signup.service.UserService;
import _G02.trello.task.model.TaskModel;
import _G02.trello.task.repository.TaskRepository;
import _G02.trello.task.service.ITaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/task")
@CrossOrigin(origins = "*")
public class TaskController {

    private final ITaskService taskService;

    private final UserService userService;


    @Autowired
    public TaskController(ITaskService taskService, UserService userService) {

        this.taskService = taskService;
        this.userService = userService;
    }

    @PostMapping("/save")
    public TaskModel addTask(@RequestBody TaskModel taskModel) throws Exception {
        return taskService.createTask(taskModel);
    }

    @GetMapping("/getAll")
    public List<TaskModel> getAllTasks() {return taskService.getAllTasks();}

    @GetMapping("/get/{taskId}")
    public TaskModel getTaskById(@PathVariable Long taskId) {return taskService.findTaskByID(taskId);}

    @DeleteMapping("/delete/{taskId}")
    public void deleteTask(@PathVariable Long taskId) {taskService.deleteTask(taskId);}

    @PutMapping("/update")
    public TaskModel editTask(@RequestBody TaskModel taskModel) throws Exception {
        Long id = taskModel.getId();
        if (id == null) {
            throw new Exception("Task ID is null");
        }
        return taskService.updateTask(taskModel);
    }

    @PutMapping("/assign")
    public TaskModel assignMemberToTask(@RequestBody TaskUpdate task){
        Long taskId = task.getTaskId();
        int userId = task.getUserId();

        TaskModel taskModel = taskService.findTaskByID(taskId);
        UserModel user = userService.findUserById(userId);

        return taskService.assignTaskToMember(taskModel,user);
    }

    public static class TaskUpdate {
        private Long taskId;
        private int userId;

        public Long getTaskId() {
            return taskId;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public void setTaskId(Long taskId) {
            this.taskId = taskId;
        }

    }

    }
