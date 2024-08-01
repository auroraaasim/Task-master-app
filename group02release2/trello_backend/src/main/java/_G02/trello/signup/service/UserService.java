package _G02.trello.signup.service;


import _G02.trello.signup.model.UserModel;
import _G02.trello.signup.repository.UserRepository;
import _G02.trello.workspace.model.WorkspaceModel;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Service
public class UserService implements UserServiceInterface {

    @Autowired
    UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public boolean passwordValidation(String password) {
        if (password.length() < 8) {
            return false;
        }
        if (!password.matches(".*[A-Z].*")) {
            return false;
        }
        if (!password.matches(".*[a-z].*")) {
            return false;
        }
        if (!password.matches(".*[@#!%$*&?].*")) {
            return false;
        }
        if (!password.matches(".*\\d.*")) {
            return false;
        }
        return true;
    }

    @Override
    public String userRegistration(UserModel user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            return "user is already registered. Try and sign in";
        }
        if (!passwordValidation(user.getPassword())) {
            return "Invalid Password";
        } else {
            userRepository.save(user);
            return "User successfully registered";
        }
    }

    @Override
    public String searchByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public List<UserModel> getAllUser() {
        return userRepository.findAll();
    }

    @Override
    public Boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);

    }

    @Override
    public UserModel loginUser(String email, String password){
        UserModel user = null;
        if (userRepository.existsByEmail(email)) {
            if (userRepository.findByEmail(email).equals(password)) {
                user = userRepository.getById(userRepository.findIdByEmail(email));
            } else {
                return user;
            }
        }
        return user;
    }

    @Override
    public UserModel findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public Boolean verifySecurityAnswer(UserModel user, String securityAnswer) {
        String storedSecurityAnswer = user.getSecurityAnswer();
        return securityAnswer.equals(storedSecurityAnswer);
    }

    @Override
    /* To perform the function of updating the new password and storing that */
    public void updatePassword(UserModel user, String newPassword) {
        user.setPassword(encodePassword(newPassword));
        userRepository.save(user);
    }

    @Override
    public String encodePassword(String password) {
        // encoding the password by reversing the string
        return new StringBuilder(password).reverse().toString();
    }

    @Override
    // Security Question
    public String[] getQuestionList(int userId){
        UserModel userM = null;
        Optional<UserModel> optionalUserModel = userRepository.findById(userId);
        if (optionalUserModel.isPresent()) {
            userM = optionalUserModel.get();
            String question = userM.getSecurityQuestion();
            return question.split("#");
        }
        return null;
    }

    @Override
    public Boolean addQuestionAndAnswer(int userID, String question, String answer){
        UserModel userM = null;
        Optional<UserModel> optionalUserModel = userRepository.findById(userID);
        if(question.contains("#") && answer.contains("#")){
            return false;
        } else if(question.length()==0 || answer.length()==0){
            return false;
        }
        if (optionalUserModel.isPresent()){
            userM = optionalUserModel.get();
            String questionList = userM.getSecurityQuestion();
            String answerList = userM.getSecurityAnswer();
            if (questionList == null){
                userM.setSecurityQuestion(question);
                userM.setSecurityAnswer(answer);
            } else {
                userM.setSecurityQuestion(questionList + "#" + question);
                userM.setSecurityAnswer(answerList + "#" + answer);
            }
            userRepository.save(userM);
            return true;
        }
        return false;
    }

    private Boolean validateAnswerForQuestion(String questionList, String answerList, String question, String answer) {
        if (questionList.contains(question)) {
            String[] questionArray = questionList.split("#");
            String[] answerArray = answerList.split("#");
            for (int i = 0; i < questionArray.length; i++) {
                if (questionArray[i].equals(question)) {
                    if (answerArray[i].equals(answer)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public Boolean checkAnswer(int userID, String question, String answer) {
        if (question.contains("#") && answer.contains("#")) {
            return false;
        } else if (question.isEmpty() || answer.isEmpty()) {
            return false;
        }

        Optional<UserModel> optionalUserModel = userRepository.findById(userID);
        if (optionalUserModel.isPresent()) {
            UserModel userM = optionalUserModel.get();
            String questionList = userM.getSecurityQuestion();
            String answerList = userM.getSecurityAnswer();
            return validateAnswerForQuestion(questionList, answerList, question, answer);
        }

        return false;
    }


    @Override
    public UserModel findUserById(int userId) {
        Optional<UserModel> optionalUser = userRepository.findById(userId);
        if(optionalUser.isPresent()) {
            return optionalUser.get();
        }else {
            // user with the given ID is not found
            throw new IllegalArgumentException("User not found for ID: " + userId);
        }
    }

    @Override
    public String updatePassword( String securityAnswer,String newPassword, Model model) {
        // Verify the security answer and the password reset token
        // Update the user's password
        // Redirect the user to the login page or a success page with appropriate feedback
        String username="";
        UserModel user = findUserByUsername(username);

        if (user == null) {
            model.addAttribute("error", "User not found");
            return "reset_password";
        }

        boolean securityAnswerMatches = verifySecurityAnswer(user, securityAnswer);

        if (!securityAnswerMatches) {
            model.addAttribute("error", "Security answer is incorrect");
            return "reset_password";
        }
        updatePassword(user, newPassword);
        return "redirect:/login"; // Redirect to the login page after password reset

    }
    @Override
    public String resetPassword( String username, String securityAnswer, String newPassword,
                                Model model) {
        UserModel user = findUserByUsername(username);

        if (user == null) {
            model.addAttribute("error", "User not found");
            return "forgot_password";
        }

        boolean securityAnswerMatches = verifySecurityAnswer(user, securityAnswer);

        if (!securityAnswerMatches) {
            model.addAttribute("error", "Security answer is incorrect");
            return "forgot_password";
        }
        updatePassword(user, newPassword);
        return "redirect:/login";
    }
}
