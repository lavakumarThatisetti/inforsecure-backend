package com.inforsecure.fiuapi.repository;

import com.inforsecure.fiuapi.domain.ConsentResponses;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;


@Repository
public interface ConsentResponseRepository extends JpaRepository<ConsentResponses,UUID> {

   Optional<ConsentResponses> findByConsentHash(String consentHash);

}
