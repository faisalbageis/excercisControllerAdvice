package org.example.capstone2.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.capstone2.APi.ApiException;
import org.example.capstone2.APi.ApiResponse;
import org.example.capstone2.Model.User;
import org.example.capstone2.Service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/User")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/get")
    public ResponseEntity<?> getUsers(){
        List<User> users = userService.getUsers();

        return ResponseEntity.status(200).body(users);
    }

    @PostMapping("/add")
    public ResponseEntity<?> addUser(@RequestBody @Valid User user , Errors errors){
        if(errors.hasErrors()){
            String massage = errors.getFieldError().getDefaultMessage();
            throw new ApiException(massage);
        }

        userService.addUser(user);
        return ResponseEntity.status(200).body(new ApiResponse("user added successfully"));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Integer id, @RequestBody @Valid User user , Errors errors){
        if(errors.hasErrors()){
            String massage = errors.getFieldError().getDefaultMessage();
            throw new ApiException(massage);
        }

        userService.updateUser(id, user);

        return ResponseEntity.status(200).body(new ApiResponse("user updated successfully"));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Integer id){
        userService.deleteUser(id);

        return ResponseEntity.status(200).body(new ApiResponse("user deleted successfully"));
    }

    @PostMapping("/login/{email}/{password}")
    public ResponseEntity<?> login(@PathVariable String email,@PathVariable String password){
        userService.login(email,password);

        return ResponseEntity.status(200).body(new ApiResponse("login successfully"));
    }
}