package _G02.trello.workspace.service;


import _G02.trello.board.model.BoardModel;
import _G02.trello.signup.model.UserModel;
import _G02.trello.signup.repository.UserRepository;
import _G02.trello.workspace.model.WorkspaceModel;
import _G02.trello.workspace.repository.WorkspaceRepository;
import _G02.trello.board.service.IBoardService;
import org.apache.catalina.User;
import org.hibernate.jdbc.Work;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.config.Task;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class WorkspaceServiceImpl implements IWorkspaceService {

    private final WorkspaceRepository workspaceRepository;
    private final UserRepository userRepository;
    private final IBoardService boardServiceImpl;

    @Autowired
    public WorkspaceServiceImpl(WorkspaceRepository workspaceRepository, UserRepository userRepository, IBoardService boardServiceImpl) {
        this.workspaceRepository = workspaceRepository;
        this.userRepository = userRepository;
        this.boardServiceImpl = boardServiceImpl;
    }

    @Override
    public WorkspaceModel createWorkspace(WorkspaceModel workspaceModel) {
        return workspaceRepository.save(workspaceModel);
    }

    @Override
    public boolean checkOwner(Long workspaceId, int userId) {
        WorkspaceModel workspaceModel = null;
        Optional<WorkspaceModel> optionalWorkspaceModel = workspaceRepository.findById(workspaceId);
        if (optionalWorkspaceModel.isPresent()) {
            workspaceModel = optionalWorkspaceModel.get();
            return workspaceModel.getOwner() == userId;
        }
        return false;
    }


    // Add/Remove user to workspace
    @Override
    public WorkspaceModel addMember(Long workspaceId, int userId){
        WorkspaceModel workspace = findWorkspaceById(workspaceId);
        UserModel user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (!workspace.getUsers().contains(user)) {
            workspace.getUsers().add(user);
            user.getWorkspaces().add(workspace);
            workspaceRepository.save(workspace);
        }
        return workspace;
    }

    private WorkspaceModel findWorkspaceById(Long workspaceId) {
        Optional<WorkspaceModel> optionalWorkspace = workspaceRepository.findById(workspaceId);
        if (optionalWorkspace.isPresent()) {
            return optionalWorkspace.get();
        } else {
            throw new RuntimeException("Workspace not found");
        }
    }
    @Override
    public WorkspaceModel removeMember(Long workspaceId, int userId){
        WorkspaceModel workspace = findWorkspaceById(workspaceId);
        UserModel user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (workspace.getUsers().contains(user)&&workspace.getOwner()!=userId) {
            workspace.getUsers().remove(user);
            user.getWorkspaces().remove(workspace);
            workspaceRepository.save(workspace);
        }
        return workspace;
    }

    @Override
    public int[] getAllMembersId(Long workspaceId){
        WorkspaceModel workspace = findWorkspaceById(workspaceId);
        int[] members = new int[workspace.getUsers().size()];
        for (int i = 0; i < workspace.getUsers().size(); i++) {
            members[i] = workspace.getUsers().get(i).getId();
        }
        return members;
    }

    // Update workspace details
    @Override
    public WorkspaceModel changeWorkspaceDetails(Long workspaceId, String newName, String newDescription){
        WorkspaceModel workspaceModel = null;
        Optional<WorkspaceModel> optionalWorkspaceModel = workspaceRepository.findById(workspaceId);
        if (optionalWorkspaceModel.isPresent()) {
            workspaceModel = optionalWorkspaceModel.get();
            workspaceModel.setWorkspaceName(newName);
            workspaceModel.setWorkspaceDescription(newDescription);
            workspaceModel.setUpdateTime(LocalDateTime.now());
            workspaceRepository.save(workspaceModel);
        }
        return workspaceModel;
    }

    @Override
    public WorkspaceModel findWorkspaceByID(Long workspaceId) {
        WorkspaceModel workspaceModel = null;
        Optional<WorkspaceModel> optionalWorkspaceModel = workspaceRepository.findById(workspaceId);
        if (optionalWorkspaceModel.isPresent()) {
            workspaceModel = optionalWorkspaceModel.get();
        }

        return workspaceModel;
    }

    @Override
    public List<UserModel> getAllMembers(Long workspaceId){
        WorkspaceModel workspaceModel = null;
        Optional<WorkspaceModel> optionalWorkspaceModel = workspaceRepository.findById(workspaceId);
        if (optionalWorkspaceModel.isPresent()) {
            workspaceModel = optionalWorkspaceModel.get();
        }
        assert workspaceModel != null;
        return workspaceModel.getUsers();
    }

    @Override
    public List<BoardModel> getAllBoardsByWorkspaceId (Long workspaceId){
        WorkspaceModel workspaceModel = null;
        Optional<WorkspaceModel> optionalWorkspaceModel = workspaceRepository.findById(workspaceId);
        if (optionalWorkspaceModel.isPresent()) {
            workspaceModel = optionalWorkspaceModel.get();
        }
        assert workspaceModel != null;
        return workspaceModel.getBoards();
    }

    @Override
    public List<WorkspaceModel> getAllWorkspacesByUserId(int userId){
        return workspaceRepository.findAllByUsers_Id(userId);
    }

    @Override
    public WorkspaceModel addBoardToWorkspace(Long workspaceId, Long boardId) throws Exception {
        WorkspaceModel updateWorkspace = null;
        Optional<WorkspaceModel> optionalWorkspaceModel = null;
        try {
            optionalWorkspaceModel = workspaceRepository.findById(workspaceId);
            if (optionalWorkspaceModel.isPresent()) {
                updateWorkspace = optionalWorkspaceModel.get();
                updateWorkspace.getBoards().add(boardServiceImpl.findBoardByID(boardId));
                workspaceRepository.save(updateWorkspace);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return updateWorkspace;
    }

    @Override
    public WorkspaceModel deleteBoardFromWorkspace(Long workspaceId, Long boardId) throws Exception {
        WorkspaceModel updateWorkspace = null;
        Optional<WorkspaceModel> optionalWorkspaceModel = null;
        try {
            optionalWorkspaceModel = workspaceRepository.findById(workspaceId);
            if (optionalWorkspaceModel.isPresent()) {
                updateWorkspace = optionalWorkspaceModel.get();
                updateWorkspace.getBoards().remove(boardServiceImpl.findBoardByID(boardId));
                workspaceRepository.save(updateWorkspace);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return updateWorkspace;
    }

    @Override
    public void deleteWorkspace(Long workspaceId) {
        // if workspace is deleted, all boards in this workspace will be deleted
        WorkspaceModel workspaceModel = null;
        Optional<WorkspaceModel> optionalWorkspaceModel = workspaceRepository.findById(workspaceId);
        if (optionalWorkspaceModel.isPresent()) {
            workspaceModel = optionalWorkspaceModel.get();
        } else {
            throw new RuntimeException("Workspace not found");
        }
        for (BoardModel board : workspaceModel.getBoards()) {
            boardServiceImpl.deleteBoard(board.getId());
        }
        workspaceRepository.deleteById(workspaceId);
    }

    @Override
    public List<WorkspaceModel> getAllWorkspaces() {return workspaceRepository.findAll();}

}
