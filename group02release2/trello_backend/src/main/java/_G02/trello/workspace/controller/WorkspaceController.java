package _G02.trello.workspace.controller;


import _G02.trello.board.model.BoardModel;
import _G02.trello.signup.model.UserModel;
import _G02.trello.workspace.model.WorkspaceModel;
import _G02.trello.workspace.service.IWorkspaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/workspace")
public class WorkspaceController {
    private final IWorkspaceService workspaceServiceImpl;

    @Autowired
    public WorkspaceController(IWorkspaceService workspaceServiceImpl){this.workspaceServiceImpl = workspaceServiceImpl;}

    @PostMapping("/save")
    public WorkspaceModel createWorkspace(@RequestBody WorkspaceModel workspaceModel) throws Exception {
        return workspaceServiceImpl.createWorkspace(workspaceModel);
    }

    @GetMapping("/get/{workspaceId}")
    public WorkspaceModel findWorkspaceByID(@PathVariable Long workspaceId) throws Exception {
        return workspaceServiceImpl.findWorkspaceByID(workspaceId);
    }

    @PutMapping("/addBoard/{workspaceId}")
    public WorkspaceModel addBoardToWorkspace(@PathVariable Long workspaceId, @RequestParam Long boardId) throws Exception {
        return workspaceServiceImpl.addBoardToWorkspace(workspaceId, boardId);
    }

    @PutMapping("/deleteBoard/{workspaceId}")
    public WorkspaceModel deleteBoardFromWorkspace(@PathVariable Long workspaceId, @RequestParam Long boardId) throws Exception {
        return workspaceServiceImpl.deleteBoardFromWorkspace(workspaceId, boardId);
    }

    @GetMapping("/getAllMembers/{workspaceId}")
    public List<UserModel> getAllMembers(@PathVariable Long workspaceId) throws Exception {
        return workspaceServiceImpl.getAllMembers(workspaceId);
    }

    @GetMapping("/getAllBoards/{workspaceId}")
    public List<BoardModel> getAllBoardsByWorkspaceId(@PathVariable Long workspaceId) throws Exception {
        return workspaceServiceImpl.getAllBoardsByWorkspaceId(workspaceId);
    }

    @DeleteMapping("/delete/{workspaceId}")
    public void deleteWorkspace(@PathVariable Long workspaceId) throws Exception {
        workspaceServiceImpl.deleteWorkspace(workspaceId);
    }

    @GetMapping("/getAllByUserId/{userId}")
    public List<WorkspaceModel> getAllWorkspacesByUserId(@PathVariable int userId) {
        return workspaceServiceImpl.getAllWorkspacesByUserId(userId);
    }

    @GetMapping("/checkOwner/{workspaceId}/{userId}")
    public boolean checkOwner(@PathVariable Long workspaceId, @PathVariable int userId) {
        return workspaceServiceImpl.checkOwner(workspaceId, userId);
    }

    @PutMapping("/addMember/{workspaceId}")
    public WorkspaceModel addMember(@PathVariable Long workspaceId, @RequestParam int userId) throws Exception {
        return workspaceServiceImpl.addMember(workspaceId, userId);
    }

    @PutMapping("/removeMember/{workspaceId}")
    public WorkspaceModel removeMember(@PathVariable Long workspaceId, @RequestParam int userId) throws Exception {
        return workspaceServiceImpl.removeMember(workspaceId, userId);
    }

    @GetMapping("/getAllMembersId/{workspaceId}")
    public int[] getAllMembersId(@PathVariable Long workspaceId) {
        return workspaceServiceImpl.getAllMembersId(workspaceId);
    }

    @PutMapping("/changeDetails/{workspaceId}")
    public WorkspaceModel changeWorkspaceDetails(@PathVariable Long workspaceId, @RequestParam String newName, @RequestParam String newDescription) throws Exception {
        return workspaceServiceImpl.changeWorkspaceDetails(workspaceId, newName, newDescription);
    }

    @GetMapping("/getAll")
    public List<WorkspaceModel> getAllWorkspaces() {
        return workspaceServiceImpl.getAllWorkspaces();
    }
}
