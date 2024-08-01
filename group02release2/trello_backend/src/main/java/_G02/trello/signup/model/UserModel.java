package _G02.trello.signup.model;

import _G02.trello.task.model.TaskModel;
import _G02.trello.workspace.model.WorkspaceModel;
import jakarta.persistence.*;
import java.util.List;

@Entity
public class UserModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String first_name;
    private String last_name;

    @Column(name = "username", unique = true)
    private String username;
    private String password;
    @Column(name = "email", unique = true)
    private String email;
    private String securityQuestion;
    private String securityAnswer;

    // One to Many (UserModel and TaskModel)
    @OneToMany(mappedBy = "assignedUser")
    private List<TaskModel> tasks;

    // Many to Many (UserModel and WorkspaceModel)
    @ManyToMany(targetEntity = WorkspaceModel.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(
            name="t_user_workspace",
            joinColumns=
            @JoinColumn( name="usermodel_id", referencedColumnName="id"),
            inverseJoinColumns=@JoinColumn(name="workspacemodel_id", referencedColumnName="id"))
    private List<WorkspaceModel> workspaces;

    // Constructors
    public UserModel() {
    }

    public UserModel(int id, String first_name, String last_name, String username, String password, String email, String security_question, String securityAnswer) {
        this.id = id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.username = username;
        this.password = password;
        this.email = email;
        this.securityQuestion = security_question;
        this.securityAnswer = securityAnswer;
    }

    //Getter Methods

    public int getId() {
        return id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public String getUserName() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    //Setter Methods

    public void setId(int id) {
        this.id = id;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public void setUsername (String username) {this.username = username;}

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUserName(String username) {
         this.username = username;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public List<WorkspaceModel> getWorkspaces() {
        return workspaces;
    }

    public void setWorkspaces(List<WorkspaceModel> workspaces) {
        this.workspaces = workspaces;
    }



    public void setSecurityQuestion(String security_question) {
        this.securityQuestion = security_question;
    }

    public String getSecurityQuestion() {
        return securityQuestion;
    }
    
    public String getSecurityAnswer() {
        return securityAnswer;
    }

    public void setSecurityAnswer(String securityAnswer) {
        this.securityAnswer = securityAnswer;
    }

    // show tasks of the user
    public List<TaskModel> getTasks(){return tasks;}
    public void setTasks(List<TaskModel> tasks) {this.tasks = tasks;}
}
