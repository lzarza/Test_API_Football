package com.lzarza.test.teammanager.controllers.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
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
	
	private Team t1, t2, t3;
	private Player p1, p2, p3;
	
	@BeforeEach
	void setUp() throws Exception {
		playerRepository.deleteAll();
		teamRepository.deleteAll();
		t1 = teamRepository.save(DataGenerator.generateTeam(null, "Test", "ZTST", 3000d));
		t2 = teamRepository.save(DataGenerator.generateTeam(null, "ZTest2", "TST", 2000d));
		t3 = teamRepository.save(DataGenerator.generateTeam(null, "ZTest3", "ZTST2", 1000d));
		p1 = playerRepository.save(DataGenerator.generatePlayer(null, "Quincini", 100d, "MILIEU"));
		p2 = playerRepository.save(DataGenerator.generatePlayer(null, "Testino", 150d, "GARDIEN"));
		p3 = playerRepository.save(DataGenerator.generatePlayer(null, "Mandoli", 200d, "ATTAQUANT"));
		p1.setPlayerTeam(t1);
		p2.setPlayerTeam(t1);
		p1 = playerRepository.save(p1);
		p2 = playerRepository.save(p2);
		t1 = teamRepository.findById(t1.getTeamId()).get(); //to reload with players
	}
	
	@Test
	void createTeam() {
		TeamDTO test = DataGenerator.generateTeamDTO(null, "TestCreate", "TSTCRT", 2000d);
		ResponseEntity<Object> response = teamController.createTeam(test);
		assertEquals(HttpStatus.OK, response.getStatusCode()); 
		TeamDTO result = (TeamDTO) response.getBody();
		Comparators.compareTeamDtos(test, result, false, false);
	}
	
	@Test
	void createTeamWithPlayers() {
		PlayerDTO player3 = mapper.map(p3, PlayerDTO.class);
		TeamDTO test = DataGenerator.generateTeamDTO(null, "TestCreate", "TSTCRT", 2000d, player3);
		
		ResponseEntity<Object> response = teamController.createTeam(test);
		assertEquals(HttpStatus.OK, response.getStatusCode()); 
		TeamDTO result = (TeamDTO) response.getBody();
		Comparators.compareTeamDtos(test, result, false, false);
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
	
	@Test
	void getAllSortName() {
		ResponseEntity<Object> response = teamController.getAllTeams(0, 3, "name");
		List<TeamDTO> dtos = (List<TeamDTO>) response.getBody();
		Comparators.compareTeamToTeamDto(t1, dtos.get(0), true, true);
		Comparators.compareTeamToTeamDto(t2, dtos.get(1), true, true);
		Comparators.compareTeamToTeamDto(t3, dtos.get(2), true, true);
	}
	
	@Test
	void getAllSortAcronym() {
		ResponseEntity<Object> response = teamController.getAllTeams(0, 3, "acronym");
		List<TeamDTO> dtos = (List<TeamDTO>) response.getBody();
		Comparators.compareTeamToTeamDto(t2, dtos.get(0), true, true);
		Comparators.compareTeamToTeamDto(t1, dtos.get(1), true, true);
		Comparators.compareTeamToTeamDto(t3, dtos.get(2), true, true);
	}
	
	@Test
	void getAllSortBudget() {
		ResponseEntity<Object> response = teamController.getAllTeams(0, 3, "budget");
		List<TeamDTO> dtos = (List<TeamDTO>) response.getBody();
		Comparators.compareTeamToTeamDto(t3, dtos.get(0), true, true);
		Comparators.compareTeamToTeamDto(t2, dtos.get(1), true, true);
		Comparators.compareTeamToTeamDto(t1, dtos.get(2), true, true);
	}
	
	@Test
	void getAllSortNamePagging() {
		ResponseEntity<Object> response = teamController.getAllTeams(1, 1, "name");
		List<TeamDTO> dtos = (List<TeamDTO>) response.getBody();
		Comparators.compareTeamToTeamDto(t2, dtos.get(0), true, true);
	}
	
	@Test
	void testUpdate() {
		TeamDTO test = DataGenerator.generateTeamDTO(t3.getTeamId(), "TestUpdate", "TST3", 2000d);
		ResponseEntity<Object> response = teamController.updateTeam(t3.getTeamId(), test);
		assertEquals(HttpStatus.OK, response.getStatusCode()); 
		TeamDTO result = (TeamDTO) response.getBody();
		Comparators.compareTeamDtos(test, result, true, true);
	}
	
	@Test
	void updateTeam_MissingName() {
		TeamDTO test = DataGenerator.generateTeamDTO(t3.getTeamId(), null, "TST4", 2000d);
		ResponseEntity<Object> response = teamController.updateTeam(t3.getTeamId(), test);
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode()); 
	}
	
	@Test
	void updateTeam_MissingAcronym() {
		TeamDTO test = DataGenerator.generateTeamDTO(t3.getTeamId(), "TestUpdate", null, 2000d);
		ResponseEntity<Object> response = teamController.updateTeam(t3.getTeamId(), test);
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode()); 
	}
	
	@Test
	void updateTeam_MissingBudget() {
		TeamDTO test = DataGenerator.generateTeamDTO(t3.getTeamId(), "TestUpdate", "TST4", null);
		ResponseEntity<Object> response = teamController.updateTeam(t3.getTeamId(), test);
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode()); 
	}
	
	@Test
	void testUpdate_NotFound() {
		TeamDTO test = DataGenerator.generateTeamDTO(999L, "TestUpdate", "TST4", 2000d);
		ResponseEntity<Object> response = teamController.updateTeam(t3.getTeamId(), test);
		assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatusCode()); 
	}
	
	@Test
	void testAddPlayer() {
		ResponseEntity<Object> response = teamController.addTeamPlayer(t1.getTeamId(), p3.getPlayerId());
		TeamDTO result = (TeamDTO) response.getBody();
		assertEquals(3, result.getPlayers().size());
	}
	
	@Test
	void testAddPlayer_TeamNotFound() {
		ResponseEntity<Object> response = teamController.addTeamPlayer(99999L, p3.getPlayerId());
		assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatusCode());
	}
	
	@Test
	void testAddPlayer_PlayerNotFound() {
		ResponseEntity<Object> response = teamController.addTeamPlayer(t1.getTeamId(), 99999L);
		assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatusCode());
	}
	
	@Test
	void testRemovePlayer() {
		ResponseEntity<Object> response = teamController.removeTeamPlayer(t1.getTeamId(), p1.getPlayerId());
		TeamDTO result = (TeamDTO) response.getBody();
		assertEquals(1, result.getPlayers().size());
	}
	
	@Test
	void testRemovePlayer_PlayerNotFound() {
		ResponseEntity<Object> response = teamController.removeTeamPlayer(t1.getTeamId(), 99999L);
		assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatusCode());
	}
	
	@Test
	void removeTeam() {
		ResponseEntity<Object> response = teamController.deleteTeam(t1.getTeamId());
		ResponseEntity<Object> response2 = teamController.getAllTeams(0, 3, "name");
		List<TeamDTO> dtos = (List<TeamDTO>) response2.getBody();
		assertEquals(2, dtos.size());
		Comparators.compareTeamToTeamDto(t2, dtos.get(0), true, true);
		Comparators.compareTeamToTeamDto(t3, dtos.get(1), true, true);
	}
	
	@Test
	void removeTeam_NotFound() {
		ResponseEntity<Object> response = teamController.deleteTeam(99999L);
		assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatusCode());
		ResponseEntity<Object> response2 = teamController.getAllTeams(0, 3, "name");
		List<TeamDTO> dtos = (List<TeamDTO>) response2.getBody();
		assertEquals(3, dtos.size());
	}
}
