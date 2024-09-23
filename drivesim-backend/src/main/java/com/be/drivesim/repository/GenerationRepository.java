package com.be.drivesim.repository;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.be.drivesim.model.Generation;
import com.be.drivesim.model.User;
public interface GenerationRepository extends JpaRepository<Generation, Long> {
	Optional<Generation> findById(Integer id);
}