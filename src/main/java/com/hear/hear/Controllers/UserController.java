package com.hear.hear.Controllers;

import com.hear.hear.Mappers.FavouriteMaterialsMapping;
import com.hear.hear.Mappers.RegisterUserRequest;
import com.hear.hear.Repositories.UserRepository;
import com.hear.hear.entities.User;
import com.hear.hear.services.AuthService;
import com.hear.hear.services.ClassService;
import com.hear.hear.services.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/user")
@AllArgsConstructor
public class UserController {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RegisterUserRequest registerRequest;
    private final AuthService authService;
    private final FavouriteMaterialsMapping favouriteMaterialsMapping;
    private final ClassService classService;
    private final UserService userService;


    @GetMapping("/getUserProfile")
    public ResponseEntity<?> getUserProfile() {
        var user=authService.getCurrentUser();
       var userProfile= userService.getUserProfile(user);
        if(userProfile.isEmpty()){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok(userProfile);
    }





@GetMapping("/getFavouriteMaterials")
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

    @GetMapping("/getAllClassesByUserId")
    public ResponseEntity<?> getAllClassesByUserId() {
        var userId=authService.getCurrentUser().getId();
        var user= userRepository.findById(userId);
        var classes=classService.getAllClassesByUserId(user.get());
        if(classes.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(classes);
    }

    @GetMapping("/getAllClassesByUserIdAndDay")
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



}
