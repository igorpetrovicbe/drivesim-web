package com.be.drivesim.repository;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.be.drivesim.model.BrisqueScore;
public interface BrisqueScoreRepository extends JpaRepository<BrisqueScore, Long> {
	Optional<BrisqueScore> findById(Integer id);
}