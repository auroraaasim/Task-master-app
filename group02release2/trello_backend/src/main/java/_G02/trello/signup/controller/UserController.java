package _G02.trello.signup.controller;

import _G02.trello.signup.model.UserModel;
import _G02.trello.signup.service.UserService;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import _G02.trello.signup.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;
    @Autowired
    UserRepository userRepository;

    @PostMapping("/register")
    public String userRegistration(@RequestBody UserModel user) {
        return userService.userRegistration(user);
    }

    @GetMapping("/getAllUser")
    public List<UserModel> getAllUser() {
        return userService.getAllUser();
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserModel userModel) {
        // Get User's password and email.
        String email = userModel.getEmail();
        String password = userModel.getPassword();

        UserModel user = userService.loginUser(email, password);

        if (user == null) {
            // User authentication failed, return an error response
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid email or password");
        }

        // User authentication is successful, you can generate a token or set a session, etc.

        // Return a successful response
        return ResponseEntity.ok("Login successful");
    }

    @PutMapping("/updatePassword/{userId}")
    public boolean updatePassword(@PathVariable("userId") int userId, @RequestParam String password) {
        Optional<UserModel> user = userRepository.findById(userId);
        if (user.isPresent()){
            UserModel userModel = user.get();
            userModel.setPassword(password);
            userRepository.save(userModel);
            return true;
        }
        return false;
    }

    @GetMapping("/searchByEmail")
    public int searchByEmail(@RequestParam String email) {
        return userRepository.findIdByEmail(email);
    }

    @GetMapping("/getQuestionList/{userId}")
    public String[] getQuestionList(@PathVariable("userId") int userId) {
        return userService.getQuestionList(userId);
    }

    @PutMapping("/addQuestionAndAnswer/{userId}")
    public boolean addQuestionAndAnswer(@PathVariable("userId") int userId, @RequestParam String question, @RequestParam String answer) {
        return userService.addQuestionAndAnswer(userId, question, answer);
    }

    @GetMapping("/checkAnswer/{userId}")
    public boolean checkAnswer(@PathVariable("userId") int userId, @RequestParam String question, @RequestParam String answer) {
        return userService.checkAnswer(userId, question, answer);
    }

    @PostMapping("/reset-password")
    public String resetPassword(@RequestParam("username") String username,
                                @RequestParam("securityAnswer") String securityAnswer,
                                @RequestParam("newPassword") String newPassword,
                                Model model) {
        return userService.resetPassword(username, securityAnswer, newPassword, model);
    }

    @PostMapping("/update-password")
    public String updatePassword(@RequestParam("securityAnswer") String securityAnswer,
                                 @RequestParam("newPassword") String newPassword,
                                 Model model) {
        // Verify the security answer and the password reset token
        // Update the user's password
        // Redirect the user to the login page or a success page with appropriate feedback
        return userService.updatePassword(securityAnswer,newPassword, model);

    }
}