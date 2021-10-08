package com.inforsecure.fiuapi.repository;

import com.inforsecure.fiuapi.domain.FiDataResponses;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Repository
public interface FiResponseRepository extends JpaRepository<FiDataResponses,UUID> {

   Optional<FiDataResponses> findByFiHash(String fiHash);

   List<FiDataResponses> findAllByUserId(String userId);

}
