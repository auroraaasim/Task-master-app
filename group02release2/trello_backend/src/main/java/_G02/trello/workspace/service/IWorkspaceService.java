package _G02.trello.workspace.service;

import _G02.trello.board.model.BoardModel;
import _G02.trello.signup.model.UserModel;
import _G02.trello.workspace.model.WorkspaceModel;

import java.util.List;

public interface IWorkspaceService {
    WorkspaceModel createWorkspace(WorkspaceModel workspaceModel);
    WorkspaceModel findWorkspaceByID(Long workspaceId);
    WorkspaceModel addBoardToWorkspace(Long workspaceId, Long boardId) throws Exception;
    WorkspaceModel deleteBoardFromWorkspace(Long workspaceId, Long boardId) throws Exception;
    void deleteWorkspace(Long workspaceId);
    List<WorkspaceModel> getAllWorkspaces();
    List<WorkspaceModel> getAllWorkspacesByUserId(int userId);
    List<UserModel> getAllMembers(Long workspaceId);
    List<BoardModel> getAllBoardsByWorkspaceId(Long workspaceId);
    boolean checkOwner(Long workspaceId, int userId);
    WorkspaceModel addMember(Long workspaceId, int userId);
    WorkspaceModel removeMember(Long workspaceId, int userId);
    int[] getAllMembersId(Long workspaceId);
    WorkspaceModel changeWorkspaceDetails(Long workspaceId, String newName, String newDescription);

}
