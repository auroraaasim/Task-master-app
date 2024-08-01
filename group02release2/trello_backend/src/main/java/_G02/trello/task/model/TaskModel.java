package _G02.trello.task.model;

import _G02.trello.signup.model.UserModel;
import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class TaskModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    String taskName;
    String description;
    int owner;
    int status;
    int project;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime updateTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime dueTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime taskCurrentTime;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserModel assignedUser;
    int listIndex;

    public TaskModel(String taskName, String description, int owner, int status, int project, LocalDateTime dueTime, LocalDateTime currentTime, int listIndex) {
        this.taskName = taskName;
        this.description = description;
        this.owner = owner;
        this.status = status;   // 0: to do, 1: doing, 2: done
        this.project = project;
        this.dueTime = dueTime;
        this.taskCurrentTime = currentTime;
        this.listIndex = listIndex;
        this.updateTime = currentTime;
    }


    public TaskModel(){}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getOwner() {
        return owner;
    }

    public void setOwner(int owner) {
        this.owner = owner;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getProject() {
        return project;
    }

    public void setProject(int project) {
        this.project = project;
    }

    public LocalDateTime getDueTime() {
        return dueTime;
    }

    public void setDueTime(LocalDateTime dueTime) {
        this.dueTime = dueTime;
    }

    public LocalDateTime getTaskCurrentTime() {
        return taskCurrentTime;
    }

    public void setTaskCurrentTime(LocalDateTime currentTime) {
        this.taskCurrentTime = currentTime;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    public int getListIndex() {
        return listIndex;
    }

    public void setListIndex(int listIndex) {
        this.listIndex = listIndex;
    }

}
