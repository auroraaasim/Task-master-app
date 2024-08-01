package _G02.trello.signup.repository;

import _G02.trello.signup.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserModel, Integer> {
    boolean existsByEmail(String email);
    @Query(value ="SELECT password FROM user_model u WHERE u.email = ?1", nativeQuery = true)
    String findByEmail(String email);

    @Query(value ="SELECT id FROM user_model u WHERE u.email = ?1", nativeQuery = true)
    Integer findIdByEmail(String email);

    UserModel getById(Integer id);
    UserModel findByUsername(String username);


}