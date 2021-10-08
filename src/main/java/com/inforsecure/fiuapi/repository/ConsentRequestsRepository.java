package com.inforsecure.fiuapi.repository;

import com.inforsecure.fiuapi.domain.ConsentRequests;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

import java.util.UUID;


@Repository
public interface ConsentRequestsRepository extends JpaRepository<ConsentRequests,UUID> {

   @Query(nativeQuery = true, value = "SELECT * FROM consent_requests where user_id = ?1 ORDER BY created_at DESC LIMIT 30;")
   List<ConsentRequests> findTop30ConsentData(String userId);

}
