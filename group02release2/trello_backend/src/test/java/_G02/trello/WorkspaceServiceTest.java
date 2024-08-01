package _G02.trello;

import _G02.trello.board.model.BoardModel;
import _G02.trello.board.service.IBoardService;
import _G02.trello.signup.model.UserModel;
import _G02.trello.signup.repository.UserRepository;
import _G02.trello.workspace.model.WorkspaceModel;
import _G02.trello.workspace.repository.WorkspaceRepository;
import _G02.trello.workspace.service.WorkspaceServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class WorkspaceServiceTest {

    @Mock
    private WorkspaceRepository workspaceRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private IBoardService boardService;

    private WorkspaceServiceImpl workspaceServiceImpl;

    @BeforeEach
    public void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        workspaceServiceImpl = new WorkspaceServiceImpl(workspaceRepository, userRepository, boardService);
    }

    @Test
    public void testCreateWorkspace() {
        WorkspaceModel workspaceModel = new WorkspaceModel();
        workspaceModel.setWorkspaceName("Test Workspace");
        when(workspaceRepository.save(workspaceModel)).thenReturn(workspaceModel);
        WorkspaceModel result = workspaceServiceImpl.createWorkspace(workspaceModel);
        assertNotNull(result);
        assertEquals("Test Workspace", result.getWorkspaceName());
    }

    @Test
    public void testCheckOwner_Owner() {
        Long workspaceId = 1L;
        int userId = 1;

        WorkspaceModel workspaceModel = new WorkspaceModel();
        workspaceModel.setId(workspaceId);
        workspaceModel.setOwner(userId);

        when(workspaceRepository.findById(workspaceId)).thenReturn(Optional.of(workspaceModel));

        boolean result = workspaceServiceImpl.checkOwner(workspaceId, userId);

        assertTrue(result);
    }

    @Test
    public void testCheckOwner_NotOwner() {
        Long workspaceId = 1L;
        int userId = 1;

        WorkspaceModel workspaceModel = new WorkspaceModel();
        workspaceModel.setId(workspaceId);
        workspaceModel.setOwner(2);

        when(workspaceRepository.findById(workspaceId)).thenReturn(Optional.of(workspaceModel));

        boolean result = workspaceServiceImpl.checkOwner(workspaceId, userId);

        assertFalse(result);
    }

    @Test
    public void testAddMember() {
        Long workspaceId = 1L;
        int userId = 1;

        UserModel userModel = new UserModel();
        userModel.setId(userId);
        userModel.setWorkspaces(new ArrayList<>());

        WorkspaceModel workspaceModel = new WorkspaceModel();
        workspaceModel.setId(workspaceId);
        workspaceModel.setUsers(new ArrayList<>());

        when(userRepository.findById(userId)).thenReturn(Optional.of(userModel));
        when(workspaceRepository.findById(workspaceId)).thenReturn(Optional.of(workspaceModel));
        when(workspaceRepository.save(workspaceModel)).thenReturn(workspaceModel);

        WorkspaceModel result = workspaceServiceImpl.addMember(workspaceId, userId);

        assertNotNull(result);
        assertEquals(1, result.getUsers().size());
        assertEquals(userId, result.getUsers().get(0).getId());
    }

    @Test
    public void testRemoveMember() {
        Long workspaceId = 1L;
        int userId = 1;

        UserModel userModel = new UserModel();
        userModel.setId(userId);
        userModel.setWorkspaces(new ArrayList<>());

        WorkspaceModel workspaceModel = new WorkspaceModel();
        workspaceModel.setId(workspaceId);
        List<UserModel> users = new ArrayList<>();
        users.add(userModel);
        workspaceModel.setUsers(users);

        when(userRepository.findById(userId)).thenReturn(Optional.of(userModel));
        when(workspaceRepository.findById(workspaceId)).thenReturn(Optional.of(workspaceModel));
        when(workspaceRepository.save(workspaceModel)).thenReturn(workspaceModel);

        WorkspaceModel result = workspaceServiceImpl.removeMember(workspaceId, userId);

        assertNotNull(result);
        assertEquals(0, result.getUsers().size());
    }

    @Test
    public void testGetAllMembersId() {
        Long workspaceId = 1L;
        UserModel user1 = new UserModel();
        user1.setId(1);
        UserModel user2 = new UserModel();
        user2.setId(2);

        WorkspaceModel workspaceModel = new WorkspaceModel();
        workspaceModel.setId(workspaceId);
        workspaceModel.setUsers(Arrays.asList(user1, user2));

        when(workspaceRepository.findById(workspaceId)).thenReturn(Optional.of(workspaceModel));

        int[] result = workspaceServiceImpl.getAllMembersId(workspaceId);

        assertNotNull(result);
        assertEquals(2, result.length);
        assertArrayEquals(new int[]{1, 2}, result);
    }

    @Test
    public void testChangeWorkspaceDetails() {
        Long workspaceId = 1L;
        String newName = "New Name";
        String newDescription = "New Description";

        WorkspaceModel workspaceModel = new WorkspaceModel();
        workspaceModel.setId(workspaceId);
        workspaceModel.setWorkspaceName("Old Name");
        workspaceModel.setWorkspaceDescription("Old Description");

        when(workspaceRepository.findById(workspaceId)).thenReturn(Optional.of(workspaceModel));
        when(workspaceRepository.save(workspaceModel)).thenReturn(workspaceModel);

        WorkspaceModel result = workspaceServiceImpl.changeWorkspaceDetails(workspaceId, newName, newDescription);

        assertNotNull(result);
        assertEquals(newName, result.getWorkspaceName());
        assertEquals(newDescription, result.getWorkspaceDescription());
    }

    @Test
    public void testFindWorkspaceByID() {
        Long workspaceId = 1L;
        WorkspaceModel workspaceModel = new WorkspaceModel();
        workspaceModel.setId(workspaceId);
        when(workspaceRepository.findById(workspaceId)).thenReturn(Optional.of(workspaceModel));
        WorkspaceModel result = workspaceServiceImpl.findWorkspaceByID(workspaceId);
        assertNotNull(result);
        assertEquals(workspaceId, result.getId());
    }

    @Test
    public void testFindWorkspaceByID_NotFound() {
        Long workspaceId = 1L;
        when(workspaceRepository.findById(workspaceId)).thenReturn(Optional.empty());
        WorkspaceModel result = workspaceServiceImpl.findWorkspaceByID(workspaceId);
        assertNull(result);
    }

    @Test
    public void testGetAllMembers() {
        Long workspaceId = 1L;
        UserModel user1 = new UserModel();
        user1.setId(1);
        UserModel user2 = new UserModel();
        user2.setId(2);

        WorkspaceModel workspaceModel = new WorkspaceModel();
        workspaceModel.setId(workspaceId);
        workspaceModel.setUsers(Arrays.asList(user1, user2));

        when(workspaceRepository.findById(workspaceId)).thenReturn(Optional.of(workspaceModel));

        List<UserModel> result = workspaceServiceImpl.getAllMembers(workspaceId);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(1, result.get(0).getId());
        assertEquals(2, result.get(1).getId());
    }

    @Test
    public void testGetAllBoardsByWorkspaceId() {
        Long workspaceId = 1L;
        BoardModel board1 = new BoardModel();
        board1.setId(1L);
        BoardModel board2 = new BoardModel();
        board2.setId(2L);

        WorkspaceModel workspaceModel = new WorkspaceModel();
        workspaceModel.setId(workspaceId);
        workspaceModel.setBoards(Arrays.asList(board1, board2));

        when(workspaceRepository.findById(workspaceId)).thenReturn(Optional.of(workspaceModel));

        List<BoardModel> result = workspaceServiceImpl.getAllBoardsByWorkspaceId(workspaceId);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals(2L, result.get(1).getId());
    }

    @Test
    public void testGetAllWorkspacesByUserId() {
        int userId = 1;
        WorkspaceModel workspace1 = new WorkspaceModel();
        workspace1.setId(1L);
        WorkspaceModel workspace2 = new WorkspaceModel();
        workspace2.setId(2L);

        when(workspaceRepository.findAllByUsers_Id(userId)).thenReturn(Arrays.asList(workspace1, workspace2));

        List<WorkspaceModel> result = workspaceServiceImpl.getAllWorkspacesByUserId(userId);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals(2L, result.get(1).getId());
    }

    @Test
    public void testAddBoardToWorkspace() throws Exception {
        Long workspaceId = 1L;
        Long boardId = 2L;

        BoardModel boardModel = new BoardModel();
        boardModel.setId(boardId);

        WorkspaceModel workspaceModel = new WorkspaceModel();
        workspaceModel.setId(workspaceId);
        workspaceModel.setBoards(new ArrayList<>());

        when(boardService.findBoardByID(boardId)).thenReturn(boardModel);
        when(workspaceRepository.findById(workspaceId)).thenReturn(Optional.of(workspaceModel));
        when(workspaceRepository.save(workspaceModel)).thenReturn(workspaceModel);

        WorkspaceModel result = workspaceServiceImpl.addBoardToWorkspace(workspaceId, boardId);

        assertNotNull(result);
        assertEquals(1, result.getBoards().size());
        assertEquals(boardId, result.getBoards().get(0).getId());
    }

    @Test
    public void testDeleteBoardFromWorkspace() throws Exception {
        Long workspaceId = 1L;
        Long boardId = 2L;

        BoardModel board1 = new BoardModel();
        board1.setId(boardId);

        WorkspaceModel workspaceModel = new WorkspaceModel();
        workspaceModel.setId(workspaceId);
        List<BoardModel> boards = new ArrayList<>();
        boards.add(board1);
        workspaceModel.setBoards(boards);

        when(boardService.findBoardByID(boardId)).thenReturn(board1);
        when(workspaceRepository.findById(workspaceId)).thenReturn(Optional.of(workspaceModel));
        doNothing().when(boardService).deleteBoard(boardId);

        WorkspaceModel result = workspaceServiceImpl.deleteBoardFromWorkspace(workspaceId, boardId);

        assertNotNull(result);
        assertEquals(0, result.getBoards().size());
    }

    @Test
    public void testDeleteWorkspace() {
        Long workspaceId = 1L;
        BoardModel board1 = new BoardModel();
        board1.setId(1L);

        WorkspaceModel workspaceModel = new WorkspaceModel();
        workspaceModel.setId(workspaceId);
        workspaceModel.setBoards(Arrays.asList(board1));

        when(workspaceRepository.findById(workspaceId)).thenReturn(Optional.of(workspaceModel));
        doNothing().when(boardService).deleteBoard(1L);

        workspaceServiceImpl.deleteWorkspace(workspaceId);

        verify(workspaceRepository, times(1)).deleteById(workspaceId);
    }

    @Test
    public void testDeleteWorkspace_NotFound() {
        Long workspaceId = 1L;
        when(workspaceRepository.findById(workspaceId)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> workspaceServiceImpl.deleteWorkspace(workspaceId));
    }


    @Test
    public void testGetAllWorkspaces() {
        WorkspaceModel workspace1 = new WorkspaceModel();
        workspace1.setId(1L);
        WorkspaceModel workspace2 = new WorkspaceModel();
        workspace2.setId(2L);

        when(workspaceRepository.findAll()).thenReturn(Arrays.asList(workspace1, workspace2));

        List<WorkspaceModel> result = workspaceServiceImpl.getAllWorkspaces();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals(2L, result.get(1).getId());
    }
}

