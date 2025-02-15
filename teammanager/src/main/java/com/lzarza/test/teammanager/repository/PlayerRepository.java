package com.lzarza.test.teammanager.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lzarza.test.teammanager.data.Player;

public interface PlayerRepository extends JpaRepository<Player, Long> {

	List<Player> findByActiveTrue();
	
}
