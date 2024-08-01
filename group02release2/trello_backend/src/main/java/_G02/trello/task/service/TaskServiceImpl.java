package _G02.trello.task.service;

import _G02.trello.signup.model.UserModel;
import _G02.trello.signup.repository.UserRepository;
import _G02.trello.task.model.TaskModel;
import _G02.trello.task.repository.TaskRepository;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.config.Task;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.time.LocalDateTime;

@Service
public class TaskServiceImpl implements ITaskService{
    private final TaskRepository taskRepository;

    private final UserRepository userRepository;

    @Autowired
    public TaskServiceImpl(TaskRepository taskRepository,UserRepository userRepository) {
        this.taskRepository=taskRepository;
        this.userRepository = userRepository;
    }

    @Override
    public TaskModel createTask(TaskModel taskModel) {return taskRepository.save(taskModel);}

    @Override
    public TaskModel findTaskByID(Long taskId) {
        TaskModel taskModel = null;

        Optional<TaskModel> optionalTaskModel=  taskRepository.findById(taskId);
        if(optionalTaskModel.isPresent())
        {
            taskModel=optionalTaskModel.get();
        }

        return taskModel;
    }

    @Override
    public TaskModel updateTask(TaskModel taskModel) {
        TaskModel task = null;

        Optional<TaskModel> optionalTaskModel=  taskRepository.findById(taskModel.getId());
        if(optionalTaskModel.isPresent())
        {
            task=optionalTaskModel.get();
            task.setStatus(taskModel.getStatus());
            task.setListIndex(taskModel.getListIndex());
            task.setTaskName(taskModel.getTaskName());
            task.setDescription(taskModel.getDescription());
            task.setDueTime(taskModel.getDueTime());
            // update time
            taskModel.setUpdateTime(LocalDateTime.now());
            taskRepository.save(task);
        } else {
            throw new IllegalStateException("Task with id " + taskModel.getId() + " does not exist");
        }
        return task;
    }

    @Override
    public void deleteTask(Long taskId) {
        // if task not found, throw exception
        Optional<TaskModel> optionalTaskModel =  taskRepository.findById(taskId);
        if(optionalTaskModel.isEmpty()) {throw new IllegalStateException("Task with id " + taskId + " does not exist");}
        taskRepository.deleteById(taskId);
    }

    @Override
    public List<TaskModel> getAllTasks() {return taskRepository.findAll();}


    @Override
    public TaskModel assignTaskToMember(TaskModel task, UserModel user){
        if (task == null || user == null) {
            throw new IllegalArgumentException("Error: Task or user can not be null");
        }


        TaskModel savedTask = null;
        Optional<TaskModel> optionalTaskModel = taskRepository.findById(task.getId());
        Optional<UserModel> optionalUserModel = userRepository.findById(user.getId());

        if(optionalTaskModel.isPresent() && optionalUserModel.isPresent() ) {
            TaskModel newTask= optionalTaskModel.get();
            UserModel newUser = optionalUserModel.get();

            newTask.setOwner(newUser.getId()); //Assign the owner/member to that task
            newUser.setTasks(new ArrayList<>());
            newUser.getTasks().add(newTask); // assign the task to that user
            // save updated task and user
            savedTask = taskRepository.save(newTask);
            userRepository.save(newUser);
        } else {
            throw new IllegalArgumentException("Invalid task ID or user ID");
        }

        return savedTask;

    }
}
