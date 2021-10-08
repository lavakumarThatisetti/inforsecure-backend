package com.inforsecure.fiuapi.service;

import com.inforsecure.fiuapi.domain.User;
import com.inforsecure.fiuapi.web.rest.resource.TopUsers;

import java.util.List;


public interface UserService {

    User createUser(final User user);

    User updateUser(String userId, final User user);

    User getUser(String uid);

    User getUserByEmail(String email);

    List<TopUsers> getTop10ByWealthScore();

}
