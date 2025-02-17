package com.lzarza.test.teammanager.controllers.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;

import com.lzarza.test.teammanager.Comparators;
import com.lzarza.test.teammanager.DataGenerator;
import com.lzarza.test.teammanager.data.Player;
import com.lzarza.test.teammanager.data.Team;
import com.lzarza.test.teammanager.dto.PlayerDTO;
import com.lzarza.test.teammanager.dto.TeamDTO;
import com.lzarza.test.teammanager.repository.PlayerRepository;
import com.lzarza.test.teammanager.repository.TeamRepository;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-integrationtest.properties")
public class TeamControllerTestIT {

	@Autowired TeamRepository teamRepository;
	
	@Autowired PlayerRepository playerRepository;
	
	@Autowired TeamController teamController;
	
	ModelMapper mapper = new ModelMapper();
	
	private Team t1;
	private Team t2;
	private Player p1;
	private Player p2;
	private Player p3;
	
	@BeforeEach
	void setUp() throws Exception {
		playerRepository.deleteAll();
		teamRepository.deleteAll();
		t1 = teamRepository.save(DataGenerator.generateTeam(null, "Test", "TST", 1000d));
		t2 = teamRepository.save(DataGenerator.generateTeam(null, "Test2", "TST2", 2000d));
		p1 = playerRepository.save(DataGenerator.generatePlayer(null, "Quincini", 100d, "MILIEU"));
		p2 = playerRepository.save(DataGenerator.generatePlayer(null, "Testino", 150d, "GARDIEN"));
		p3 = playerRepository.save(DataGenerator.generatePlayer(null, "Mandoli", 200d, "ATTAQUANT"));
		p1.setPlayerTeam(t1);
		p2.setPlayerTeam(t1);
		p1 = playerRepository.save(p1);
		p2 = playerRepository.save(p2);
	}
	
	@Test
	void createTeam() {
		TeamDTO t3 = DataGenerator.generateTeamDTO(null, "Test3", "TST3", 2000d);
		ResponseEntity<Object> response = teamController.createTeam(t3);
		assertEquals(HttpStatus.OK, response.getStatusCode()); 
		TeamDTO result = (TeamDTO) response.getBody();
		Comparators.compareTeamDtos(t3, result, false, false);
	}
	
	@Test
	void createTeamWithPlayers() {
		PlayerDTO player3 = mapper.map(p3, PlayerDTO.class);
		TeamDTO t3 = DataGenerator.generateTeamDTO(null, "Test3", "TST3", 2000d, player3);
		
		ResponseEntity<Object> response = teamController.createTeam(t3);
		assertEquals(HttpStatus.OK, response.getStatusCode()); 
		TeamDTO result = (TeamDTO) response.getBody();
		Comparators.compareTeamDtos(t3, result, false, false);
	}
	
	@Test
	void createTeam_MissingName() {
		TeamDTO t3 = DataGenerator.generateTeamDTO(null, null, "TST3", 2000d);
		ResponseEntity<Object> response = teamController.createTeam(t3);
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode()); 
	}
	
	@Test
	void createTeam_MissingAcronym() {
		TeamDTO t3 = DataGenerator.generateTeamDTO(null, "Test3", null, 2000d);
		ResponseEntity<Object> response = teamController.createTeam(t3);
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode()); 
	}
	
	@Test
	void createTeam_MissingBudget() {
		TeamDTO t3 = DataGenerator.generateTeamDTO(null, "Test3", "TST3", null);
		ResponseEntity<Object> response = teamController.createTeam(t3);
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode()); 
	}
}
