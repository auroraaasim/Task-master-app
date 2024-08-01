package _G02.trello;

import _G02.trello.signup.model.UserModel;
import _G02.trello.signup.repository.UserRepository;
import _G02.trello.signup.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    private UserService userService;

    @Mock
    private UserModel userModel;


    @BeforeEach
    public void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        userService = new UserService(userRepository);
    }

    @Test
    public void testPasswordValidationIsTrue(){
        String password = "IspasswordV@lid001";
        boolean validation = userService.passwordValidation(password);
        assertTrue(validation,"This password is valid");
    }

    @Test
    public void testPasswordValidationIsFalse(){
        String password = "IspasswordValid";
        boolean validation = userService.passwordValidation(password);
        assertFalse(validation,"This password is invalid");
    }

    @Test
    public void testSuccessfulUserRegistration(){
        userModel = new UserModel(1,"User", "Name", " ","Password@123", "username@gmail.com", "xxx", "xxx" );
        when(userRepository.save(userModel)).thenReturn(userModel);
        String result = userService.userRegistration(userModel);
        assertEquals("User successfully registered", result);
    }

    @Test
    public void testUnsuccessfulUserRegistration(){
        userModel = new UserModel(1,"User", "Name", " ","Username123", "user@gmail.com", "xxx", "xxx" );
        when(userRepository.save(userModel)).thenReturn(userModel);
        String result = userService.userRegistration(userModel);
        assertEquals("Invalid Password", result);
    }

    @Test
    public void testSuccessfulLogin() {

        // Create a mock UserRepository with a test user
        userModel = new UserModel(1,"User", "Name", " ","Username@123", "username@gmail.com", "xxx", "xxx" );
        userRepository.save(userModel);
        Mockito.when(userRepository.existsByEmail("username@gmail.com")).thenReturn(true);
        Mockito.when(userRepository.findByEmail("username@gmail.com")).thenReturn("Username@123"); // Replace "hashedPassword" with the actual hashed password

        // Call the loginUser method with valid credentials
        Mockito.when(userService.loginUser("username@gmail.com", "Username@123")).thenReturn(userModel); // Replace "hashedPassword" with the actual hashed password
        // Assert that the returned UserModel is not null
        assertNotNull(userModel);

        // assert other properties of the UserModel if needed
        assertEquals("username@gmail.com", userModel.getEmail());
    }

    // Security Question Tests
    @Test
    void testGetQuestionList() {
        UserModel testUser = new UserModel();
        testUser.setId(1);
        testUser.setSecurityQuestion("Who is your mom?");
        userRepository.save(testUser);

        when(userRepository.findById(1)).thenReturn(Optional.of(testUser));
        String[] questionList = userService.getQuestionList(testUser.getId());

        assertArrayEquals(new String[]{"Who is your mom?"}, questionList);
    }

    @Test
    void testGetQuestionListWithMultipleQuestions() {
        UserModel testUser = new UserModel();
        testUser.setId(1);
        testUser.setSecurityQuestion("Who is your mom?#Why I did 85% backend code?");
        userRepository.save(testUser);

        when(userRepository.findById(1)).thenReturn(Optional.of(testUser));
        String[] questionList = userService.getQuestionList(testUser.getId());

        assertArrayEquals(new String[]{"Who is your mom?", "Why I did 85% backend code?"}, questionList);
    }

    @Test
    void testAddQuestionAndAnswer() {
        UserModel testUser = new UserModel();
        userRepository.save(testUser);
        testUser.setId(1);

        when(userRepository.findById(1)).thenReturn(Optional.of(testUser));

        boolean result = userService.addQuestionAndAnswer(testUser.getId(), "Why I did 85% backend code?", "Someone didn't do");

        assertTrue(result);

        UserModel updatedUser = userRepository.findById(testUser.getId()).get();
        assertEquals("Why I did 85% backend code?", updatedUser.getSecurityQuestion());
        assertEquals("Someone didn't do", updatedUser.getSecurityAnswer());
    }

    @Test
    void testAddQuestionAndAnswerWithInvalidInput() {
        UserModel testUser = new UserModel();
        userRepository.save(testUser);

        boolean result = userService.addQuestionAndAnswer(testUser.getId(), "What#is#your#favorite#color?", "Discord");

        assertFalse(result);
    }

    @Test
    void testCheckAnswer() {
        UserModel testUser = new UserModel();
        testUser.setId(1);
        testUser.setSecurityQuestion("What is you?");
        testUser.setSecurityAnswer("dump");

        when(userRepository.findById(1)).thenReturn(Optional.of(testUser));
        boolean result = userService.checkAnswer(testUser.getId(), "What is you?", "dump");

        assertTrue(result);
    }

    @Test
    void testCheckAnswerWithIncorrectAnswer() {
        UserModel testUser = new UserModel();
        testUser.setSecurityQuestion("What is your favorite course?");
        testUser.setSecurityAnswer("3151");
        userRepository.save(testUser);

        boolean result = userService.checkAnswer(testUser.getId(), "What is your favorite course?", "3151");

        assertFalse(result);
    }

    // Please! Don't name the method like 'searchByEmail() is using Email to find the pwd' (angry face)
    // use searchPwdByEmail() instead
    @Test
    void searchPwdByEmailTest() {
        String email = "test@example.com";
        String pwd = "testpwd";
        UserModel user = new UserModel();
        user.setEmail(email);
        user.setPassword(pwd);

        when(userRepository.findByEmail(email)).thenReturn(pwd);

        String result = userService.searchByEmail(email);
        assertEquals(pwd, result);
    }


    @Test
    void getAllUserTest() {
        List<UserModel> users = new ArrayList<>();
        users.add(new UserModel());
        users.add(new UserModel());

        when(userRepository.findAll()).thenReturn(users);

        List<UserModel> result = userService.getAllUser();
        assertEquals(users, result);
    }

    @Test
    void existsByEmailTest() {
        String email = "test@example.com";

        when(userRepository.existsByEmail(email)).thenReturn(true);

        Boolean result = userService.existsByEmail(email);
        assertTrue(result);
    }

    @Test
    void findUserByUsernameTest() {
        String username = "quanadadada";
        UserModel user = new UserModel();
        user.setUsername(username);

        when(userRepository.findByUsername(username)).thenReturn(user);

        UserModel result = userService.findUserByUsername(username);
        assertEquals(user, result);
    }

    @Test
    void verifySecurityAnswerTest() {
        UserModel user = new UserModel();
        user.setSecurityAnswer("answer");

        Boolean result = userService.verifySecurityAnswer(user, "answer");
        assertTrue(result);
    }

    @Test
    void shortUpdatePasswordTest() {
        UserModel user = new UserModel();
        user.setPassword("password");

        userService.updatePassword(user, "nnnnnnnnnnnn");
        assertEquals("nnnnnnnnnnnn", user.getPassword());
    }

    @Test
    void findUserByIdTest() {
        int userId = 1;
        UserModel user = new UserModel();
        user.setId(userId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        UserModel result = userService.findUserById(userId);
        assertEquals(user, result);
    }

    @Test
    void updatePasswordTest() {
        String securityAnswer = "answer";
        String newPassword = "nnnnnnnnnnnn";

        UserModel user = new UserModel();
        user.setSecurityAnswer(securityAnswer);

        when(userRepository.findByUsername("")).thenReturn(user);

        Model model = new ExtendedModelMap();
        String result = userService.updatePassword(securityAnswer, newPassword, model);
        assertEquals("redirect:/login", result);
    }

    @Test
    void updatePasswordIncorrectSecurityAnswerTest() {
        String username = "quangggg";
        String securityAnswer = "incorrect";
        String newPassword = "nnnnnnnnnnnn";

        UserModel user = new UserModel();
        user.setUsername(username);
        user.setSecurityAnswer("answer");

        when(userRepository.findByUsername(username)).thenReturn(user);

        Model model = new ExtendedModelMap();
        String result = userService.updatePassword(securityAnswer, newPassword, model);
        assertEquals("reset_password", result);
    }

    @Test
    void resetPasswordTest() {
        String username = "quangggg";
        String securityAnswer = "answer";
        String newPassword = "nnnnnnnnnnnn";

        UserModel user = new UserModel();
        user.setUsername(username);
        user.setSecurityAnswer(securityAnswer);

        when(userRepository.findByUsername(username)).thenReturn(user);

        Model model = new ExtendedModelMap();
        String result = userService.resetPassword(username, securityAnswer, newPassword, model);
        assertEquals("redirect:/login", result);
    }

    @Test
    void resetPasswordIncorrectSecurityAnswerTest() {
        String username = "quangggg";
        String securityAnswer = "incorrect";
        String newPassword = "nnnnnnnnnnnn";

        UserModel user = new UserModel();
        user.setUsername(username);
        user.setSecurityAnswer("answer");

        when(userRepository.findByUsername(username)).thenReturn(user);

        Model model = new ExtendedModelMap();
        String result = userService.resetPassword(username, securityAnswer, newPassword, model);
        assertEquals("forgot_password", result);
    }










}


