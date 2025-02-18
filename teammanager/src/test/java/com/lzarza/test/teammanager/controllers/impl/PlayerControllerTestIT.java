package com.lzarza.test.teammanager.controllers.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

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
import com.lzarza.test.teammanager.repository.PlayerRepository;
import com.lzarza.test.teammanager.repository.TeamRepository;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-integrationtest.properties")
public class PlayerControllerTestIT {

	@Autowired TeamRepository teamRepository;
	
	@Autowired PlayerRepository playerRepository;
	
	@Autowired PlayerController playerController;
	
	ModelMapper mapper = new ModelMapper();
	
	Team t1;
	Player p1, p2, p3;
	
	@BeforeEach
	void setUp() throws Exception {
		playerRepository.deleteAll();
		teamRepository.deleteAll();
		
		t1 = teamRepository.save(DataGenerator.generateTeam(null, "Test", "TST", 3000d));
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
	void getAll() {
		ResponseEntity<Object> response = playerController.getAllPlayers();
		assertEquals(HttpStatus.OK, response.getStatusCode()); 
		List<PlayerDTO> dtos = (List<PlayerDTO>) response.getBody();
		assertEquals(3, dtos.size());
		foundAndTestPlayer(p1, dtos, true);
		foundAndTestPlayer(p2, dtos, true);
		foundAndTestPlayer(p3, dtos, true);
	}
	
	@Test
	void getById() {
		ResponseEntity<Object> response = playerController.getPlayer(p1.getPlayerId());
		assertEquals(HttpStatus.OK, response.getStatusCode()); 
		PlayerDTO dto = (PlayerDTO) response.getBody();
		Comparators.comparePlayerToPlayerDTO(p1, dto, true);
	}
	
	@Test
	void getById_NotFound() {
		ResponseEntity<Object> response = playerController.getPlayer(99999L);
		assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatusCode()); 
	}
	
	@Test
	void createPlayer() {
		PlayerDTO player = DataGenerator.generatePlayerDTO(null, "Marciano", 10000d, "Buteur");
		ResponseEntity<Object> response = playerController.createPlayer(player);
		assertEquals(HttpStatus.OK, response.getStatusCode()); 
		PlayerDTO dto = (PlayerDTO) response.getBody();
		Comparators.comparePlayerDTOs(player, dto, false);
	}
	
	@Test
	void createPlayer_ErrorId() {
		PlayerDTO player = DataGenerator.generatePlayerDTO(9999L, "Marciano", 10000d, "Buteur");
		ResponseEntity<Object> response = playerController.createPlayer(player);
		assertEquals(HttpStatus.CONFLICT, response.getStatusCode()); 
	}
	
	@Test
	void createPlayer_ErrorName() {
		PlayerDTO player = DataGenerator.generatePlayerDTO(null, null, 10000d, "Buteur");
		ResponseEntity<Object> response = playerController.createPlayer(player);
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode()); 
	}
	
	@Test
	void createPlayer_ErrorPosition() {
		PlayerDTO player = DataGenerator.generatePlayerDTO(null, "Marciano", 10000d, null);
		ResponseEntity<Object> response = playerController.createPlayer(player);
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode()); 
	}
	
	@Test
	void updatePlayer() {
		PlayerDTO player = DataGenerator.generatePlayerDTO(p3.getPlayerId(), "Marciano", 10000d, "Buteur");
		ResponseEntity<Object> response = playerController.updatePlayer(p3.getPlayerId(), player);
		assertEquals(HttpStatus.OK, response.getStatusCode()); 
		PlayerDTO dto = (PlayerDTO) response.getBody();
		Comparators.comparePlayerDTOs(player, dto, false);
	}
	
	@Test
	void updatePlayer_ErrorId() {
		PlayerDTO player = DataGenerator.generatePlayerDTO(null, "Marciano", 10000d, "Buteur");
		ResponseEntity<Object> response = playerController.updatePlayer(null, player);
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode()); 
	}
	
	@Test
	void updatePlayer_ErrorName() {
		PlayerDTO player = DataGenerator.generatePlayerDTO(p3.getPlayerId(), null, 10000d, "Buteur");
		ResponseEntity<Object> response = playerController.updatePlayer(p3.getPlayerId(), player);
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode()); 
	}
	
	@Test
	void updatePlayer_ErrorPosition() {
		PlayerDTO player = DataGenerator.generatePlayerDTO(p3.getPlayerId(), "Marciano", 10000d, null);
		ResponseEntity<Object> response = playerController.updatePlayer(p3.getPlayerId(), player);
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode()); 
	}
	
	@Test 
	void disablePlayer() {
		ResponseEntity<Object> response = playerController.deletePlayer(p1.getPlayerId());
		assertEquals(HttpStatus.OK, response.getStatusCode()); 
		
		ResponseEntity<Object> responseGet = playerController.getAllPlayers();
		List<PlayerDTO> dtos = (List<PlayerDTO>) responseGet.getBody();
		assertEquals(2, dtos.size());
		
		Player p = playerRepository.findById(p1.getPlayerId()).get();
		assertFalse(p.isActive());
		assertNull(p.getPlayerTeam());
	}
	
	@Test 
	void disablePlayer_ErrorId() {
		ResponseEntity<Object> response = playerController.deletePlayer(99999L);
		assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatusCode()); 
		
		ResponseEntity<Object> responseGet = playerController.getAllPlayers();
		List<PlayerDTO> dtos = (List<PlayerDTO>) responseGet.getBody();
		assertEquals(3, dtos.size());
	}
	
	private void foundAndTestPlayer(Player expected, List<PlayerDTO> result, boolean checkId) {
		PlayerDTO tested = null;
		if(checkId) {
			tested = result.stream().filter(dto->dto.getPlayerId().equals(expected.getPlayerId())).findFirst().orElse(null);
		}else {
			tested = result.stream().filter(dto->dto.getName().equals(expected.getName())).findFirst().orElse(null);
		}
		assertNotNull(tested);
		Comparators.comparePlayerToPlayerDTO(expected, tested, checkId);
	}
	
}
