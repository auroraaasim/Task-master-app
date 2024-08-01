package _G02.trello.signup.service;

import _G02.trello.signup.model.UserModel;
import org.springframework.ui.Model;

import java.util.List;

public interface UserServiceInterface {

    UserModel loginUser(String email, String password);

    boolean passwordValidation(String password);

    String userRegistration(UserModel user);
    String searchByEmail(String email);

    List<UserModel> getAllUser();

    Boolean existsByEmail(String email);

    UserModel findUserByUsername(String username);

    Boolean verifySecurityAnswer(UserModel user, String securityAnswer);


    void updatePassword(UserModel user, String newPassword);

    String encodePassword(String password);

    String[] getQuestionList(int userId);

    Boolean addQuestionAndAnswer(int userID, String question, String answer);

    Boolean checkAnswer(int userID, String question, String answer);

    UserModel findUserById(int userId);

    String updatePassword( String securityAnswer,String newPassword, Model model);

    String resetPassword( String username, String securityAnswer, String newPassword, Model model);

}
