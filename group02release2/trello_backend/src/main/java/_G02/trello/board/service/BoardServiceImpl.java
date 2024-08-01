package _G02.trello.board.service;


import _G02.trello.board.model.BoardModel;
import _G02.trello.board.repository.BoardRepository;
import _G02.trello.task.model.TaskModel;
import _G02.trello.task.service.ITaskService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class BoardServiceImpl implements IBoardService {

    private final BoardRepository boardRepository;

    private final ITaskService taskServiceImpl;

    @Autowired
    public BoardServiceImpl(BoardRepository boardRepository, ITaskService taskServiceImpl) {
        this.boardRepository = boardRepository;
        this.taskServiceImpl = taskServiceImpl;
    }

    @Override
    public BoardModel createBoard(BoardModel boardModel) throws Exception
    {
        return boardRepository.save(boardModel);
    }

    @Override
    public BoardModel findBoardByID(Long boardId)
    {
        BoardModel boardModel = null;

        Optional<BoardModel> optionalBoardModel=  boardRepository.findById(boardId);
        if(optionalBoardModel.isPresent())
        {
            boardModel=optionalBoardModel.get();
        }

        return boardModel;
    }

    @Override
    public BoardModel addTaskToBoard(Long boardId, Long taskId) throws Exception
    {
        BoardModel updateBoard = null;
        Optional<BoardModel> optionalBoardModel = null;
        try{
            optionalBoardModel =  boardRepository.findById(boardId);
            if(optionalBoardModel.isPresent())
            {
                updateBoard=optionalBoardModel.get();
                updateBoard.getTasks().add(taskServiceImpl.findTaskByID(taskId));
                boardRepository.save(updateBoard);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return updateBoard;
    }

    @Override
    public List<TaskModel> getTasksByBoardId(Long boardId) {
        Optional<BoardModel> optionalBoardModel = boardRepository.findById(boardId);
        if (optionalBoardModel.isPresent()) {
            BoardModel boardModel = optionalBoardModel.get();
            return boardModel.getTasks();
        } else {
            throw new EntityNotFoundException();
        }
    }

    @Override
    public BoardModel deleteTaskFromBoard(Long boardId, Long taskId) throws Exception
    {
        BoardModel updateBoard = null;
        Optional<BoardModel> optionalBoardModel = null;
        try{
            optionalBoardModel =  boardRepository.findById(boardId);
            if(optionalBoardModel.isPresent())
            {
                updateBoard=optionalBoardModel.get();
                updateBoard.getTasks().remove(taskServiceImpl.findTaskByID(taskId));
                boardRepository.save(updateBoard);
                //remove task from DB
                taskServiceImpl.deleteTask(taskId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return updateBoard;
    }

    @Override
    public BoardModel changeBoardName (Long boardId, String newName){
        if(newName == null || newName.isEmpty()){
            throw new IllegalArgumentException("New name cannot be null or empty");
        }
        Optional<BoardModel> optionalBoardModel = boardRepository.findById(boardId);
        if (optionalBoardModel.isPresent()) {
            BoardModel boardModel = optionalBoardModel.get();
            boardModel.setBoardName(newName);
            boardModel.setUpdateTime(LocalDateTime.now());
            return boardRepository.save(boardModel);
        } else {
            throw new EntityNotFoundException();
        }
    }

    @Override
    public void deleteBoard(Long boardId) {
        // if board does not exist, throw exception
        Optional<BoardModel> optionalBoardModel = boardRepository.findById(boardId);
        if (optionalBoardModel.isEmpty()) {
            throw new EntityNotFoundException();
        }
        boardRepository.deleteById(boardId);
    }

    @Override
    public List<BoardModel> getAllBoards() {return boardRepository.findAll();}

}


