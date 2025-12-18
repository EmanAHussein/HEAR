package com.hear.hear.Controllers;

import com.hear.hear.Mappers.FavouriteMaterialsMapping;
import com.hear.hear.Mappers.UserMapping;
import com.hear.hear.Repositories.UserRepository;
import com.hear.hear.authentication.AuthService;
import com.hear.hear.entities.User;
import com.hear.hear.services.ClassService;
import com.hear.hear.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/user")
@AllArgsConstructor
public class UserController {
    UserRepository userRepository;
    PasswordEncoder passwordEncoder;
    UserMapping registerRequest;
    private final AuthService authService;
    private final UserService userService;
    private final ClassService classService;
    private final FavouriteMaterialsMapping favouriteMaterialsMapping;

//    @GetMapping("/get_users")
//    public List<User> getUsers() {
//        return userRepository.findAll();
//    }

    @GetMapping("/Profile/get")
    public ResponseEntity<?> getUserProfile() {
        var user=authService.getCurrentUser();
        var userProfile= userService.getUserProfile(user);
        if(userProfile.isEmpty()){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok(userProfile);
    }

    @GetMapping("/classes/get")
    public ResponseEntity<?> getAllClassesByUserId() {
        var userId=authService.getCurrentUser().getId();
        var user= userRepository.findById(userId);
        var classes=classService.getAllClassesByUserId(user.get());
        if(classes.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(classes);
    }

    @GetMapping("/classes/day/get")
    public ResponseEntity<?> getAllClassesByUserIdAndDay() {
        var userId=authService.getCurrentUser().getId();
        var user= userRepository.findById(userId).orElse(null);
        var day = LocalDate.now().getDayOfWeek();
        assert user != null;
        var classes=classService.getAllClassesByUserIdAndDay(user,day);
        if(classes.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(classes);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> handleException(RuntimeException ex){
        return ResponseEntity.badRequest().body(ex.getMessage());
    }





    //---------------------------------------------------------
    @GetMapping("/materials/favorites/get")
    public ResponseEntity<?> getFavoriteMaterials() {
        var user=authService.getCurrentUser().getId();

        if(user==null){
            return ResponseEntity.notFound().build();
        }
        var favouriteMaterials=userRepository.findMaterialsForUser(user);
        if (favouriteMaterials.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(favouriteMaterialsMapping.getFavouriteMaterials(favouriteMaterials));
    }

//    @GetMapping("/me")
//    public ResponseEntity<ProfileDto> getCurrentUser(){
//        Integer userId = 1;
//        var user = userRepository.findById(userId).orElse(null);
//        if(user == null){
//            return ResponseEntity.notFound().build();
//        }
//        FIGURED OUT I NEED TO IMPLEMENT Tokens & AUTH FIRST :(
//    }

}
