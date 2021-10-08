package com.inforsecure.fiuapi.repository;

import com.inforsecure.fiuapi.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Repository
public interface UserRepository extends JpaRepository<User,UUID> {

   Optional<User> findByEmail(String email);

   Boolean existsByUserName(String userName);

   Boolean existsByEmail(String email);

   @Query(nativeQuery = true, value = "SELECT * FROM users ORDER BY wealth_score DESC LIMIT 20;")
   List<User> findTop10Users();

}
