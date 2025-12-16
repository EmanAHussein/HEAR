package com.hear.hear.Repositories;

import com.hear.hear.entities.Course;
import com.hear.hear.entities.Materials;
import com.hear.hear.entities.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.Set;


public interface UserRepository extends JpaRepository<User,Integer> {
    Optional<User> findByEmail(String email);

    boolean existsByEmail(@NotBlank(message = "Email is required") @Email String email);

    @Query("SELECT f FROM Materials f JOIN f.users u WHERE u.id = :userId")
    Set<Materials> findMaterialsForUser(Integer userId);






}
