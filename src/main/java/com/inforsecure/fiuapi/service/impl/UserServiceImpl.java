package com.inforsecure.fiuapi.service.impl;


import com.inforsecure.fiuapi.domain.User;
import com.inforsecure.fiuapi.repository.UserRepository;
import com.inforsecure.fiuapi.service.UserService;
import com.inforsecure.fiuapi.web.rest.resource.TopUsers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository){
        this.userRepository=userRepository;
    }

    @Override
    public User createUser(User user) {

        try {
            //Save User
            user = userRepository.save(user);

        }catch (DataAccessException ex){
            System.out.println("Exception "+ ex.getMessage());
        }
        return user;
    }

    @Override
    public User updateUser(String userId, User user) {
        User updsteUser = null;
        try {
            //Get Password from DB
            Optional<User> userBase = userRepository.findById(UUID.fromString(userId));
            if(userBase.isPresent()){
                User updateUser = constructUser(user);
                //Setting Original Values
                updateUser.setId(userBase.get().getId());
                updateUser.setCreatedAt(userBase.get().getCreatedAt());
                updateUser.setVersion(userBase.get().getVersion());
                updateUser.setWealthScore(userBase.get().getWealthScore());
                //Save the updated User
                updsteUser = userRepository.save(updateUser);
            }
        }catch (DataAccessException ex){
            System.out.println("Exception "+ ex.getMessage());
        }
        return updsteUser;
    }

    @Override
    public User getUser(String userId) {
        Optional<User> userBase = userRepository.findById(UUID.fromString(userId));
        if(userBase.isPresent()){
            System.out.println(userBase.get().getEmail());
            return userBase.get();
        }
        return null;
    }

    @Override
    public User getUserByEmail(String email) {
        Optional<User> userBase = userRepository.findByEmail(email);
        return userBase.orElse(null);
    }

    @Override
    public List<TopUsers> getTop10ByWealthScore() {
        List<User> topUsers = userRepository.findTop10Users();
        List<TopUsers> topUsersList =  new ArrayList<>();
        topUsers.forEach(user -> topUsersList.add(new TopUsers(user.getUserName(),user.getWealthScore())));
        return topUsersList;
    }

    private User constructUser(User updateUser){
        return User.builder()
                .firstName(updateUser.getFirstName())
                .lastName(updateUser.getLastName())
                .userName(updateUser.getUserName())
                .email(updateUser.getEmail())
                .phoneNo(updateUser.getPhoneNo())
                .build();
    }
}
