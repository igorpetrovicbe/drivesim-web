package com.be.drivesim.repository;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.be.drivesim.model.VerificationToken;

@Repository
public interface TokenRepository extends JpaRepository<VerificationToken, Long> {
	Optional<VerificationToken> findByToken(String token);
}
