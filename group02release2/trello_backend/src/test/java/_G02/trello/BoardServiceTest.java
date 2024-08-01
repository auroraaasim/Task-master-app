package _G02.trello;

import _G02.trello.board.model.BoardModel;
import _G02.trello.board.repository.BoardRepository;
import _G02.trello.board.service.BoardServiceImpl;
import _G02.trello.task.model.TaskModel;
import _G02.trello.task.service.ITaskService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.*;
import org.mockito.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class BoardServiceTest {

    @Mock
    private BoardRepository boardRepository;

    @Mock
    private ITaskService taskService;

    private BoardServiceImpl boardServiceImpl;

    @BeforeEach
    public void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        boardServiceImpl = new BoardServiceImpl(boardRepository, taskService);
    }

    @Test
    public void testCreateBoard() throws Exception {
        BoardModel boardModel = new BoardModel();
        boardModel.setBoardName("Test Board");
        when(boardRepository.save(boardModel)).thenReturn(boardModel);
        BoardModel result = boardServiceImpl.createBoard(boardModel);
        assertNotNull(result);
        assertEquals("Test Board", result.getBoardName());
    }

    @Test
    public void testFindBoardByID() {
        Long boardId = 1L;
        BoardModel boardModel = new BoardModel();
        boardModel.setId(boardId);
        when(boardRepository.findById(boardId)).thenReturn(Optional.of(boardModel));
        BoardModel result = boardServiceImpl.findBoardByID(boardId);
        assertNotNull(result);
        assertEquals(boardId, result.getId());
    }

    @Test
    public void testFindBoardByID_NotFound() {
        Long boardId = 1L;
        when(boardRepository.findById(boardId)).thenReturn(Optional.empty());
        BoardModel result = boardServiceImpl.findBoardByID(boardId);
        assertNull(result);
    }

    @Test
    public void testAddTaskToBoard() throws Exception {
        Long boardId = 1L;
        Long taskId = 2L;
        TaskModel taskModel = new TaskModel();
        taskModel.setId(taskId);

        BoardModel boardModel = new BoardModel();
        boardModel.setId(boardId);
        boardModel.setTasks(new ArrayList<>());

        when(taskService.findTaskByID(taskId)).thenReturn(taskModel);
        when(boardRepository.findById(boardId)).thenReturn(Optional.of(boardModel));
        when(boardRepository.save(boardModel)).thenReturn(boardModel);

        BoardModel result = boardServiceImpl.addTaskToBoard(boardId, taskId);

        assertNotNull(result);
        assertEquals(1, result.getTasks().size());
        assertEquals(taskId, result.getTasks().get(0).getId());
    }

    @Test
    public void testGetTasksByBoardId() {
        Long boardId = 1L;
        TaskModel task1 = new TaskModel();
        task1.setId(1L);
        TaskModel task2 = new TaskModel();
        task2.setId(2L);

        BoardModel boardModel = new BoardModel();
        boardModel.setId(boardId);
        boardModel.setTasks(Arrays.asList(task1, task2));

        when(boardRepository.findById(boardId)).thenReturn(Optional.of(boardModel));

        List<TaskModel> result = boardServiceImpl.getTasksByBoardId(boardId);

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    public void testDeleteTaskFromBoard() throws Exception {
        Long boardId = 1L;
        Long taskId = 2L;

        TaskModel task1 = new TaskModel();
        task1.setId(taskId);

        BoardModel boardmodel = new BoardModel();
        boardmodel.setId(1L);
        List<TaskModel> tasks = new ArrayList<>();
        tasks.add(task1);
        boardmodel.setTasks(tasks);

        when(taskService.findTaskByID(taskId)).thenReturn(task1);
        when(boardRepository.findById(1L)).thenReturn(Optional.ofNullable(boardmodel));
        doNothing().when(taskService).deleteTask(taskId);
        when(boardRepository.save(boardmodel)).thenReturn(boardmodel);

        BoardModel result = boardServiceImpl.deleteTaskFromBoard(1L, taskId);

        assertNotNull(result);
        assertEquals(0, result.getTasks().size());
    }

    @Test
    public void testDeleteTaskFromBoard_NotFoundTask() throws Exception {
        Long boardId = 1L;
        Long taskId = 2L;

        BoardModel boardmodel = new BoardModel();
        boardmodel.setId(1L);
        List<TaskModel> tasks = new ArrayList<>();
        boardmodel.setTasks(tasks);

        when(taskService.findTaskByID(taskId)).thenReturn(null);
        when(boardRepository.findById(1L)).thenReturn(Optional.ofNullable(boardmodel));

        BoardModel result = boardServiceImpl.deleteTaskFromBoard(1L, taskId);
        assertEquals(boardmodel, result); // boardModel is not changed
    }

    @Test
    public void testChangeBoardName() {
        Long boardId = 1L;
        String newName = "New Name";

        BoardModel boardModel = new BoardModel();
        boardModel.setId(boardId);
        boardModel.setBoardName("Old Name");

        when(boardRepository.findById(boardId)).thenReturn(Optional.of(boardModel));
        when(boardRepository.save(boardModel)).thenReturn(boardModel);

        BoardModel result = boardServiceImpl.changeBoardName(boardId, newName);

        assertNotNull(result);
        assertEquals(newName, result.getBoardName());
    }

    @Test
    public void testChangeBoardName_NameNotEmpty() {
        Long boardId = 1L;
        String newName = "";

        BoardModel boardModel = new BoardModel();
        boardModel.setId(boardId);
        boardModel.setBoardName("Old Name");

        when(boardRepository.findById(boardId)).thenReturn(Optional.of(boardModel));

        assertThrows(IllegalArgumentException.class, () -> boardServiceImpl.changeBoardName(boardId, newName));
    }

    @Test
    public void testDeleteBoard() {
        Long boardId = 1L;
        BoardModel boardModel = new BoardModel();
        boardModel.setId(boardId);
        when(boardRepository.findById(boardId)).thenReturn(Optional.of(boardModel));
        doNothing().when(boardRepository).deleteById(boardId);
        boardServiceImpl.deleteBoard(boardId);
        verify(boardRepository, times(1)).deleteById(boardId);
    }

    @Test
    public void testDeleteBoard_NotFound() {
        Long boardId = 1L;
        when(boardRepository.findById(boardId)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> boardServiceImpl.deleteBoard(boardId));
    }

    @Test
    public void testGetAllBoards() {
        List<BoardModel> boards = Arrays.asList(new BoardModel(), new BoardModel());
        when(boardRepository.findAll()).thenReturn(boards);
        List<BoardModel> result = boardServiceImpl.getAllBoards();
        assertNotNull(result);
        assertEquals(2, result.size());
    }
}

