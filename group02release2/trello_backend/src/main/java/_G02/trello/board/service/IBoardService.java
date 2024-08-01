package _G02.trello.board.service;



import _G02.trello.board.model.BoardModel;
import _G02.trello.task.model.TaskModel;

import java.util.List;

public interface IBoardService {
        BoardModel createBoard(BoardModel boardModel) throws Exception;
        BoardModel findBoardByID(Long boardId);
        BoardModel addTaskToBoard(Long boardId, Long taskId) throws Exception;
        void deleteBoard(Long boardId);
        List<BoardModel> getAllBoards();
        List<TaskModel> getTasksByBoardId(Long workspaceId);
        BoardModel deleteTaskFromBoard(Long boardId, Long taskId) throws Exception;
        BoardModel changeBoardName(Long boardId, String newName) throws Exception;
}
