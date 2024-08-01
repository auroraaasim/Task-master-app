package _G02.trello.board.controller;


import _G02.trello.board.model.BoardModel;
import _G02.trello.board.service.IBoardService;
import _G02.trello.task.model.TaskModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/board")
@CrossOrigin(origins = "*")
public class BoardController {

    private final IBoardService boardService;

    @Autowired
    public BoardController(IBoardService boardService) {this.boardService = boardService;}

    @PostMapping("/save")
    public BoardModel addBoard(@RequestBody BoardModel boardModel) throws Exception {
        return boardService.createBoard(boardModel);
    }

    @PutMapping("/editName/{boardId}")
    public BoardModel editBoardName(@PathVariable Long boardId, @RequestParam String newName) throws Exception {
        return boardService.changeBoardName(boardId, newName);
    }

    @PutMapping("/addTask/{boardId}")
    public BoardModel addTaskToBoard(@PathVariable Long boardId, @RequestParam Long taskId) throws Exception {
        return boardService.addTaskToBoard(boardId, taskId);
    }

    @DeleteMapping("/deleteTask/{boardId}")
    public BoardModel deleteTaskFromBoard(@PathVariable Long boardId, @RequestParam Long taskId) throws Exception {
        return boardService.deleteTaskFromBoard(boardId, taskId);
    }

    @DeleteMapping("/delete/{boardId}")
    //@CrossOrigin(origins = "http://localhost:3000")
    public void deleteBoard(@PathVariable Long boardId) {boardService.deleteBoard(boardId);}

    /*
    @DeleteMapping("/board/delete/{boardId}")
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<String> deleteBoard(@PathVariable Long boardId) {
        try {
            boardService.deleteBoard(boardId);
            return ResponseEntity.ok("Board deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete board");
        }
    }

     */

    @GetMapping("/getAll")
    public List<BoardModel> getAllBoards() {return boardService.getAllBoards();}

    @GetMapping("/get/{boardId}")
    public BoardModel getBoardById(@PathVariable Long boardId) {return boardService.findBoardByID(boardId);}

    @GetMapping("/getTasks/{boardId}")
    public List<TaskModel> getTasksOfBoard(@PathVariable Long boardId) {return boardService.getTasksByBoardId(boardId);}

}
