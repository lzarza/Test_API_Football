package com.lzarza.test.teammanager.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.lzarza.test.teammanager.Comparators;
import com.lzarza.test.teammanager.DataGenerator;
import com.lzarza.test.teammanager.data.Player;
import com.lzarza.test.teammanager.data.Team;
import com.lzarza.test.teammanager.dto.PlayerDTO;
import com.lzarza.test.teammanager.exception.ServiceException;
import com.lzarza.test.teammanager.repository.PlayerRepository;
import com.lzarza.test.teammanager.services.IPlayerService;
import com.lzarza.test.teammanager.services.impl.PlayerServiceImpl;

public class PlayerServiceTest {
	
	@Mock
	PlayerRepository playerRepository;
	
	IPlayerService playerService;
	
	@BeforeEach
	void setup() {
		//auto mock save methods to return themselves
		MockitoAnnotations.initMocks(this);
		when(playerRepository.save(any())).thenAnswer(i -> DataGenerator.savePlayerAction(i.getArguments()[0]));
		playerService = new PlayerServiceImpl(playerRepository);
	}
	
	@Test
	void testGetAll() {
		List<Player> players = new ArrayList<Player>();
		players.add(DataGenerator.generatePlayer(1L, "Quincini", 100d, "MILIEU"));
		players.add(DataGenerator.generatePlayer(2L, "Testino", 150d, "GARDIEN"));
		players.add(DataGenerator.generatePlayer(3L, "Mandoli", 200d, "ATTAQUANT"));
		when(playerRepository.findByActiveTrue()).thenReturn(players);
		List<PlayerDTO> result = playerService.getActivePlayers();
		assertEquals(players.size(), result.size());
		for(Player p : players) {
			PlayerDTO pResult = result.stream().filter(dto -> dto.getPlayerId().equals(p.getPlayerId())).findFirst().orElse(null);
			assertNotNull(result);
			Comparators.comparePlayerToPlayerDTO(p, pResult, true);
		}
	}
	
	@Test
	void getById() throws ServiceException {
		Player p = DataGenerator.generatePlayer(1L, "Quincini", 100d, "MILIEU");
		when(playerRepository.findById(any())).thenReturn(Optional.of(p));
		PlayerDTO result = playerService.getById(1L);
		Comparators.comparePlayerToPlayerDTO(p, result, true);
	}
	
	@Test
	void getByIdWithTeam() throws ServiceException {
		Player p = DataGenerator.generatePlayer(1L, "Quincini", 100d, "MILIEU");
		Team team = DataGenerator.generateTeam(1L, "Test", "TST", 0d);
		p.setPlayerTeam(team);
		when(playerRepository.findById(any())).thenReturn(Optional.of(p));
		PlayerDTO result = playerService.getById(1L);
		Comparators.comparePlayerToPlayerDTO(p, result, true);
	}
	
	@Test
	void getById_ErrorIdNull() {
		Player p = DataGenerator.generatePlayer(1L, "Quincini", 100d, "MILIEU");
		when(playerRepository.findById(any())).thenReturn(Optional.of(p));
		try {
			playerService.getById(null);
		} catch (ServiceException e) {
			assertEquals(e.getErrorCode(), ServiceException.MISSING_PLAYER_ID);
			return;
		}
		fail("No exception");
	}
	
	@Test
	void createPlayer() throws ServiceException {
		PlayerDTO test = DataGenerator.generatePlayerDTO(null, "Quincini", 100d, "MILIEU");
		PlayerDTO result = playerService.createPlayer(test);
		Comparators.comparePlayerDTOs(test, result, false);
	}
	
	@Test
	void createPlayer_ErrorId() {
		PlayerDTO test = DataGenerator.generatePlayerDTO(1L, "Quincini", 100d, "MILIEU");
		try {
			playerService.createPlayer(test);
		} catch (ServiceException e) {
			assertEquals(e.getErrorCode(), ServiceException.CANNOT_CREATE_PLAYER);
			return;
		}
		fail("No exception");
	}
	
	@Test
	void updatePlayer() throws ServiceException {
		PlayerDTO test = DataGenerator.generatePlayerDTO(1L, "Quincini", 100d, "MILIEU");
		Player p = DataGenerator.generatePlayer(1L, "Mandoli", 200d, "Attaquant");
		when(playerRepository.findById(any())).thenReturn(Optional.of(p));
		PlayerDTO result = playerService.updatePlayer(test);
		Comparators.comparePlayerDTOs(test, result, true);
	}
	
	@Test
	void updatePlayer_ErrorId() {
		PlayerDTO test = DataGenerator.generatePlayerDTO(null, "Quincini", 100d, "MILIEU");
		Player p = DataGenerator.generatePlayer(1L, "Mandoli", 200d, "Attaquant");
		when(playerRepository.findById(any())).thenReturn(Optional.of(p));
		try {
			playerService.updatePlayer(test);
		} catch (ServiceException e) {
			assertEquals(e.getErrorCode(), ServiceException.MISSING_PLAYER_ID);
			return;
		}
		fail("No exception");

	}
	
	@Test
	void updatePlayer_NotFound() {
		PlayerDTO test = DataGenerator.generatePlayerDTO(1L, "Quincini", 100d, "MILIEU");
		when(playerRepository.findById(any())).thenReturn(Optional.empty());
		try {
			playerService.updatePlayer(test);
		} catch (ServiceException e) {
			assertEquals(e.getErrorCode(), ServiceException.PLAYER_NOT_FOUND);
			return;
		}
		fail("No exception");
	}
	
	@Test
	void disablePlayer() throws ServiceException {
		Player p = DataGenerator.generatePlayer(1L, "Mandoli", 200d, "Attaquant");
		when(playerRepository.findById(any())).thenReturn(Optional.of(p));
		playerService.disablePlayer(1L);
		assertFalse(p.isActive());
	}
	
	@Test
	void disablePlayer_ErrorId() {
		Player p = DataGenerator.generatePlayer(1L, "Mandoli", 200d, "Attaquant");
		when(playerRepository.findById(any())).thenReturn(Optional.of(p));
		try {
			playerService.disablePlayer(null);
		} catch (ServiceException e) {
			assertEquals(e.getErrorCode(), ServiceException.MISSING_PLAYER_ID);
			return;
		}
		fail("No exception");
	}
	
	@Test
	void disablePlayer_NotFound() {
		when(playerRepository.findById(any())).thenReturn(Optional.empty());
		try {
			playerService.disablePlayer(1L);
		} catch (ServiceException e) {
			assertEquals(e.getErrorCode(), ServiceException.PLAYER_NOT_FOUND);
			return;
		}
		fail("No exception");
	}
	
	
}
