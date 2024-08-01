package _G02.trello.workspace.model;

import _G02.trello.board.model.BoardModel;
import _G02.trello.signup.model.UserModel;
import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import java.util.List;
import java.time.LocalDateTime;


@Entity
public class WorkspaceModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String workspaceName;
    private int owner;
    private WorkspaceType workspacetype;
    private String workspaceDescription;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;


    // List of boards
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "workspace_id", referencedColumnName = "id")
    private List <BoardModel> boards;

    // Many to Many (UserModel and WorkspaceModel)
    @JsonIgnore
    @ManyToMany(targetEntity = UserModel.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<UserModel> users;

    public WorkspaceModel(String workspaceName, int owner, WorkspaceType workspacetype, String workspaceDescription, LocalDateTime createTime) {
        this.workspaceName = workspaceName;
        this.owner = owner;
        this.workspacetype = workspacetype;
        this.workspaceDescription = workspaceDescription;
        this.createTime = createTime;
        this.updateTime = createTime;
    }


    public WorkspaceModel() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getWorkspaceName() {
        return workspaceName;
    }

    public void setWorkspaceName(String workspaceName) {
        this.workspaceName = workspaceName;
    }

    public int getOwner() {
        return owner;
    }

    public void setOwner(int owner) {
        this.owner = owner;
    }

    public WorkspaceType getWorkspacetype() {
        return workspacetype;
    }

    public void setWorkspacetype(WorkspaceType workspacetype) {
        this.workspacetype = workspacetype;
    }

    public String getWorkspaceDescription() {
        return workspaceDescription;
    }

    public void setWorkspaceDescription(String workspaceDescription) {
        this.workspaceDescription = workspaceDescription;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public List<BoardModel> getBoards() {
        return boards;
    }

    public void setBoards(List<BoardModel> boards) {
        this.boards = boards;
    }

    public List<UserModel> getUsers() {
        return users;
    }

    public void setUsers(List<UserModel> users) {
        this.users = users;
    }
}
