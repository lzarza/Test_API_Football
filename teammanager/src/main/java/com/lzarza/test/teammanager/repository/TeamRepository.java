package com.lzarza.test.teammanager.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lzarza.test.teammanager.data.Team;

public interface TeamRepository extends JpaRepository<Team, Long> {

	/**
	 * Get all active teams
	 * @return
	 */
	public List<Team> findByActiveTrue();
}
