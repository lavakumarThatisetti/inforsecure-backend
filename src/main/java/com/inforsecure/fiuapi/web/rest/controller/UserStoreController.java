package com.inforsecure.fiuapi.web.rest.controller;


import com.inforsecure.fiuapi.domain.User;
import com.inforsecure.fiuapi.repository.UserRepository;
import com.inforsecure.fiuapi.service.UserService;
import com.inforsecure.fiuapi.web.rest.resource.MessageResponse;
import com.inforsecure.fiuapi.web.rest.resource.TopUsers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;


@RestController
@RequestMapping("/api/user")
public class UserStoreController {

    @Autowired
    private UserRepository userRepository;


    private UserService userDetailsService;

    @Autowired
    public UserStoreController(UserService userDetailsService){
        this.userDetailsService = userDetailsService;

    }

    @PostMapping(produces = "application/json",value = "/registerUser")
    public ResponseEntity<?> registerUser(@Valid @RequestBody User user) {
        if (userRepository.existsByUserName(user.getUserName())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }

        if (userRepository.existsByEmail(user.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already in use!"));
        }

        return ResponseEntity.ok(userDetailsService.createUser(user));
    }


    @PutMapping(produces = "application/json", value = "/updateUser/{userId}")
    public ResponseEntity<User> updateUser(@NotBlank @PathVariable String userId,
                                                 @Valid @RequestBody User userBase, BindingResult bindingResult){
        try {
            if (bindingResult.hasErrors()) {
                throw new Exception(bindingResult.getFieldErrors().iterator().next().getDefaultMessage());
            }
        }catch (Exception e) {
            ResponseEntity.ok(e.getMessage());
        }

        return ResponseEntity.ok(userDetailsService.updateUser(userId, userBase));
    }


    @GetMapping(produces = "application/json", value = "/getUser/{userId}")
    public ResponseEntity<User> getUser(@NotBlank @PathVariable String userId){

        return ResponseEntity.ok(userDetailsService.getUser(userId));
    }

    @GetMapping(produces = "application/json", value = "/getUserByEmail/{emailId}")
    public ResponseEntity<User> getUserByEmail(@NotBlank @PathVariable String emailId){

        return ResponseEntity.ok(userDetailsService.getUserByEmail(emailId));
    }

    @GetMapping(produces = "application/json", value = "/getTop10Users")
    public ResponseEntity<List<TopUsers>> getTop10UsersBasedOnWealthScore(){

        return ResponseEntity.ok(userDetailsService.getTop10ByWealthScore());
    }

}
