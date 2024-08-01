package _G02.trello;

import _G02.trello.signup.model.UserModel;
import _G02.trello.signup.repository.UserRepository;
import _G02.trello.task.model.TaskModel;
import _G02.trello.task.repository.TaskRepository;
import _G02.trello.task.service.TaskServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TaskServiceTest {
    @Mock
    private TaskRepository taskRepository;

    private TaskServiceImpl taskService;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    public void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        taskService = new TaskServiceImpl(taskRepository, userRepository);
    }

    @Test
    public void testCreateTask() {
        TaskModel taskModel = new TaskModel();
        taskModel.setTaskName("Test Task");

        when(taskRepository.save(taskModel)).thenReturn(taskModel);

        TaskModel result = taskService.createTask(taskModel);

        assertNotNull(result);
        assertEquals("Test Task", result.getTaskName());
    }

    @Test
    public void testFindTaskByID() {
        Long taskId = 1L;
        TaskModel taskModel = new TaskModel();
        taskModel.setId(taskId);
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(taskModel));
        TaskModel result = taskService.findTaskByID(taskId);
        assertNotNull(result);
        assertEquals(taskId, result.getId());
    }

    @Test
    public void testFindTaskByID_NotFound() {
        Long taskId = 1L;
        when(taskRepository.findById(taskId)).thenReturn(Optional.empty());
        TaskModel result = taskService.findTaskByID(taskId);
        assertNull(result);
    }

    @Test
    public void testUpdateTask() {
        Long taskId = 1L;
        int taskStatus = 1;
        int newIndex = 2;
        String newName = "New Name";
        String newDescription = "New Description";
        LocalDateTime newDueDate = LocalDateTime.now();

        TaskModel newTaskModel = new TaskModel();
        newTaskModel.setId(taskId);
        newTaskModel.setTaskName(newName);
        newTaskModel.setDescription(newDescription);
        newTaskModel.setDueTime(newDueDate);

        TaskModel taskModel = new TaskModel();
        taskModel.setId(taskId);
        taskModel.setTaskName("Old Name");
        taskModel.setDescription("Old Description");
        taskModel.setDueTime(LocalDateTime.now().minusDays(1));

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(taskModel));
        when(taskRepository.save(taskModel)).thenReturn(taskModel);

        TaskModel result = taskService.updateTask(newTaskModel);

        assertNotNull(result);
        assertEquals(newName, result.getTaskName());
        assertEquals(newDescription, result.getDescription());
        assertEquals(newDueDate, result.getDueTime());
    }

    @Test
    public void testUpdateTask_NotFound() {
        Long taskId = 1L;
        int taskStatus = 1;
        int newIndex = 2;
        String newName = "New Name";
        String newDescription = "New Description";
        LocalDateTime newDueDate = LocalDateTime.now();

        TaskModel taskModel = new TaskModel();
        taskModel.setId(taskId);
        taskModel.setStatus(taskStatus);
        taskModel.setListIndex(newIndex);
        taskModel.setTaskName(newName);
        taskModel.setDescription(newDescription);
        taskModel.setDueTime(newDueDate);

        when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

        //assertThrows(IllegalStateException.class, () -> taskService.updateTask(taskId, taskStatus, newIndex, newName, newDescription, newDueDate));
        assertThrows(IllegalStateException.class, () -> taskService.updateTask(taskModel));
    }

    @Test
    public void testDeleteTask() {
        Long taskId = 1L;
        TaskModel taskModel = new TaskModel();
        taskModel.setId(taskId);

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(taskModel));
        doNothing().when(taskRepository).deleteById(taskId);

        taskService.deleteTask(taskId);
        verify(taskRepository, times(1)).findById(taskId);
        verify(taskRepository, times(1)).deleteById(taskId);
    }

    @Test
    public void testDeleteTask_NotFound() {
        Long taskId = 1L;
        when(taskRepository.findById(taskId)).thenReturn(Optional.empty());
        assertThrows(IllegalStateException.class, () -> taskService.deleteTask(taskId));
    }

    @Test
    public void testGetAllTasks() {
        List<TaskModel> tasks = Arrays.asList(new TaskModel(), new TaskModel());
        when(taskRepository.findAll()).thenReturn(tasks);
        List<TaskModel> result = taskService.getAllTasks();
        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    public void assignTaskToMember_successful(){
        //use id to link task and user
        //create a task to be set
        TaskModel task = new TaskModel();
        task.setTaskName("first task");
        task.setId(1L);
        task.setStatus(1);
        task.setDueTime(LocalDateTime.now());

        //create a member and add the member to the user list of a workspace
        UserModel member = new UserModel();
        member.setId(100);
        member.setEmail("test@test.com");
        member.setFirst_name("One");
        member.setLast_name("Last");
        member.setPassword("12qwAS!!");
        // WorkspaceModel workspace = new WorkspaceModel();
        // workspace.setUsers(new ArrayList<>());
        // workspace.getUsers().add(member); //add new member to the workspace

        when(this.taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(this.taskRepository.save(task)).thenReturn(task);
        when(this.userRepository.findById(100)).thenReturn(Optional.of(member));
        when(this.userRepository.save(member)).thenReturn(member);

        TaskModel assignTask = taskService.assignTaskToMember(task,member);

        assertEquals(100,assignTask.getOwner()); //check the owner id is same or not
        assertTrue(member.getTasks().contains(task)); // check the member has such task

        verify(this.taskRepository).findById(1L);
        verify(this.userRepository).findById(100);
        verify(this.taskRepository).save(task);
        verify(this.userRepository).save(member);

    }

    @Test
    public void assignTaskToMember_InvalidTask() {
        TaskModel task = new TaskModel();
        task.setTaskName("first task");
        task.setId(999L); // invalid taskID
        task.setStatus(1);
        task.setDueTime(LocalDateTime.now());

        UserModel member = new UserModel();
        member.setId(100);
        member.setEmail("test@test.com");
        member.setFirst_name("One");
        member.setLast_name("Last");
        member.setPassword("12qwAS!!");

        when(this.taskRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> {
            taskService.assignTaskToMember(task, member);
        });
    }

    @Test
    public void assignTaskToMember_InvalidMember(){
        TaskModel task = new TaskModel();
        task.setTaskName("first task");
        task.setId(2L);
        task.setStatus(1);
        task.setDueTime(LocalDateTime.now());

        UserModel member = new UserModel();
        member.setId(-1); // invalid memberID
        member.setEmail("test@test.com");
        member.setFirst_name("One");
        member.setLast_name("Last");
        member.setPassword("12qwAS!!");

        when(this.taskRepository.findById(2L)).thenReturn(Optional.of(task));
        when(this.userRepository.findById(-1)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> {
            taskService.assignTaskToMember(task,member);
        });
    }

    @Test
    public void assginTaskToMember_NullTask(){
        TaskModel task = null;

        UserModel member = new UserModel();
        member.setId(100);

        assertThrows(IllegalArgumentException.class, () -> {
            taskService.assignTaskToMember(task,member);
        });

    }

    @Test
    public void assginTaskToMember_NullMember(){
        TaskModel task = new TaskModel();
        task.setTaskName("first task");
        task.setId(2L);
        task.setStatus(1);
        task.setDueTime(LocalDateTime.now());

        UserModel member = null;

        assertThrows(IllegalArgumentException.class, () -> {
            taskService.assignTaskToMember(task,member);
        });

    }























}

