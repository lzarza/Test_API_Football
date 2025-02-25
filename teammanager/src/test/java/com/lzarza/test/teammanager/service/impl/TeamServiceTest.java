package com.lzarza.test.teammanager.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import com.lzarza.test.teammanager.Comparators;
import com.lzarza.test.teammanager.DataGenerator;
import com.lzarza.test.teammanager.data.Player;
import com.lzarza.test.teammanager.data.Team;
import com.lzarza.test.teammanager.dto.TeamDTO;
import com.lzarza.test.teammanager.exception.ServiceException;
import com.lzarza.test.teammanager.repository.PlayerRepository;
import com.lzarza.test.teammanager.repository.TeamRepository;
import com.lzarza.test.teammanager.services.ITeamService;
import com.lzarza.test.teammanager.services.impl.TeamServiceImpl;

class TeamServiceTest {

	@Mock
	TeamRepository teamRepository;
	
	@Mock
	PlayerRepository playerRepository;
	
	ITeamService teamService;
	
	ModelMapper mapper = new ModelMapper();
	
	@BeforeEach
	void setup() {
		//auto mock save methods to return themselves
		MockitoAnnotations.initMocks(this);
		when(teamRepository.save(any())).thenAnswer(i -> DataGenerator.saveTeamAction(i.getArguments()[0]));
		teamService = new TeamServiceImpl(teamRepository, playerRepository);
	}
	
	@Test
	void testGetAll() {
		List<Team> testResult = new ArrayList<Team>();
		testResult.add(DataGenerator.generator.nextObject(Team.class));
		testResult.add(DataGenerator.generator.nextObject(Team.class));
		when(teamRepository.findByActiveTrue(any())).thenReturn(testResult);
		List<TeamDTO> result = teamService.getAllActive(0, 1, null);
		assertNotNull(result);
		assertEquals(result.size(), testResult.size());
	}
	
	@Test
	void testFindById() throws ServiceException {
		Player p1 = DataGenerator.generatePlayer(1L, "Quincini", 100d, "MILIEU");
		Player p2 = DataGenerator.generatePlayer(2L, "Testino", 100d, "GARDIEN");
		Team testTeam = DataGenerator.generateTeam(1L, "Test", "TST", 10000d, p1, p2);
		when(teamRepository.findById(any())).thenReturn(Optional.of(testTeam));
		TeamDTO result = teamService.findById(1L);
		Comparators.compareTeamToTeamDto(testTeam, result, false, false);
	}
	
	@Test
	void testFindById_IdNull() {
		try {
			Player p1 = DataGenerator.generatePlayer(1L, "Quincini", 100d, "MILIEU");
			Player p2 = DataGenerator.generatePlayer(2L, "Testino", 100d, "GARDIEN");
			Team testTeam = DataGenerator.generateTeam(1L, "Test", "TST", 10000d, p1, p2);
			when(teamRepository.findById(any())).thenReturn(Optional.of(testTeam));
			teamService.findById(null);
		} catch (ServiceException e) {
			assertEquals(e.getErrorCode(), ServiceException.MISSING_TEAM_ID);
			return;
		}
		fail("No exception");
	}
	
	@Test
	void testFindById_NotFound() throws ServiceException {
		when(teamRepository.findById(any())).thenReturn(Optional.empty());
		TeamDTO result = teamService.findById(1L);
		assertNull(result);
	}
	
	@Test
	void testInsert() throws ServiceException {
		TeamDTO testTeamDTO = DataGenerator.generateTeamDTO(null, "Test", "TST", 10000d);
		TeamDTO result = teamService.createTeam(testTeamDTO);
		Comparators.compareTeamDtos(testTeamDTO, result, false, true);
	}
	
	@Test
	void testInsert_ErrorId() {
		TeamDTO testTeamDTO = DataGenerator.generateTeamDTO(1L, "Test", "TST", 10000d);
		try {
			teamService.createTeam(testTeamDTO);
		} catch (ServiceException e) {
			assertEquals(e.getErrorCode(), ServiceException.CANNOT_CREATE_TEAM);
			return;
		}
		fail("No exception");
	}
	
	@Test
	void testUpdate() throws ServiceException {
		TeamDTO testTeamDTO = DataGenerator.generateTeamDTO(1L, "Test", "TST", 10000d);
		Team testTeam = DataGenerator.generateTeam(1L, "Testo", "TSTO", 10000d);
		when(teamRepository.findById(any())).thenReturn(Optional.of(testTeam));
		TeamDTO result = teamService.updateTeam(testTeamDTO);
		Comparators.compareTeamDtos(testTeamDTO, result, false, true);
	}
	
	@Test
	void testUpdate_ErrorId() {
		TeamDTO testTeamDTO = DataGenerator.generateTeamDTO(null, "Test", "TST", 10000d);
		try {
			teamService.updateTeam(testTeamDTO);
		} catch (ServiceException e) {
			assertEquals(e.getErrorCode(), ServiceException.MISSING_TEAM_ID);
			return;
		}
		fail("No exception");
	}
	
	@Test
	void testUpdate_NotFound() {
		TeamDTO testTeamDTO = DataGenerator.generateTeamDTO(1L, "Test", "TST", 10000d);
		try {
			when(teamRepository.findById(any())).thenReturn(Optional.empty());
			teamService.updateTeam(testTeamDTO);
		} catch (ServiceException e) {
			assertEquals(e.getErrorCode(), ServiceException.TEAM_NOT_FOUND);
			return;
		}
		fail("No exception");
	}
	
	@Test
	void testAddPlayer() throws ServiceException {
		Team testTeam = DataGenerator.generateTeam(1L, "Testo", "TSTO", 10000d);
		Player p1 = DataGenerator.generatePlayer(1L, "Quincini", 100d, "MILIEU");
		when(playerRepository.save(any())).thenAnswer(i -> assertPlayerTeamId(i.getArguments()[0], testTeam.getTeamId()));
		when(teamRepository.findById(any())).thenReturn(Optional.of(testTeam));
		when(playerRepository.findById(any())).thenReturn(Optional.of(p1));
		teamService.addPlayer(1L, 1L);
	}
	
	@Test
	void testAddPlayer_ErrorTeamNull() {
		try {
			teamService.addPlayer(null, 1L);
		} catch (ServiceException e) {
			assertEquals(e.getErrorCode(), ServiceException.MISSING_TEAM_ID);
			return;
		}
		fail("No exception");
	}
	
	@Test
	void testAddPlayer_ErrorTeamNotFound() {
		when(teamRepository.findById(any())).thenReturn(Optional.empty());
		try {
			teamService.addPlayer(1L, 1L);
		} catch (ServiceException e) {
			assertEquals(e.getErrorCode(), ServiceException.TEAM_NOT_FOUND);
			return;
		}
		fail("No exception");
	}
	
	@Test
	void testAddPlayer_ErrorPlayerNull() {
		try {
			Team testTeam = DataGenerator.generateTeam(1L, "Testo", "TSTO", 10000d);
			when(teamRepository.findById(any())).thenReturn(Optional.of(testTeam));
			teamService.addPlayer(1L, null);
		} catch (ServiceException e) {
			assertEquals(e.getErrorCode(), ServiceException.MISSING_PLAYER_ID);
			return;
		}
		fail("No exception");
	}
	
	@Test
	void testAddPlayer_ErrorPlayerNotFound() {
		Team testTeam = DataGenerator.generateTeam(1L, "Testo", "TSTO", 10000d);
		when(teamRepository.findById(any())).thenReturn(Optional.of(testTeam));
		when(playerRepository.findById(any())).thenReturn(Optional.empty());
		try {
			teamService.addPlayer(1L, 1L);
		} catch (ServiceException e) {
			assertEquals(e.getErrorCode(), ServiceException.PLAYER_NOT_FOUND);
			return;
		}
		fail("No exception");
	}
	
	@Test
	void testRemovePlayer() throws ServiceException {
		Team testTeam = DataGenerator.generateTeam(1L, "Testo", "TSTO", 10000d);
		Player p1 = DataGenerator.generatePlayer(1L, "Quincini", 100d, "MILIEU");
		when(playerRepository.save(any())).thenAnswer(i -> assertPlayerTeamId(i.getArguments()[0], null));
		when(teamRepository.findById(any())).thenReturn(Optional.of(testTeam));
		when(playerRepository.findById(any())).thenReturn(Optional.of(p1));
		teamService.removePlayer(1L, 1L);
	}
	
	@Test
	void testRemovePlayer_ErrorTeamNull() {
		try {
			teamService.removePlayer(null, 1L);
		} catch (ServiceException e) {
			assertEquals(e.getErrorCode(), ServiceException.MISSING_TEAM_ID);
			return;
		}
		fail("No exception");
	}
	
	@Test
	void testRemovePlayer_ErrorPlayerNull() {
		try {
			Team testTeam = DataGenerator.generateTeam(1L, "Testo", "TSTO", 10000d);
			when(teamRepository.findById(any())).thenReturn(Optional.of(testTeam));
			teamService.removePlayer(1L, null);
		} catch (ServiceException e) {
			assertEquals(e.getErrorCode(), ServiceException.MISSING_PLAYER_ID);
			return;
		}
		fail("No exception");
	}
	
	@Test
	void testRemovePlayer_ErrorPlayerNotFound() {
		Team testTeam = DataGenerator.generateTeam(1L, "Testo", "TSTO", 10000d);
		when(teamRepository.findById(any())).thenReturn(Optional.of(testTeam));
		when(playerRepository.findById(any())).thenReturn(Optional.empty());
		try {
			teamService.removePlayer(1L, 1L);
		} catch (ServiceException e) {
			assertEquals(e.getErrorCode(), ServiceException.PLAYER_NOT_FOUND);
			return;
		}
		fail("No exception");
	}
	
	@Test
	void testDisableTeam() throws ServiceException {
		Player p1 = DataGenerator.generatePlayer(1L, "Quincini", 100d, "MILIEU");
		Player p2 = DataGenerator.generatePlayer(2L, "Testino", 100d, "GARDIEN");
		Team testTeam = DataGenerator.generateTeam(1L, "Test", "TST", 10000d, p1, p2);
		when(teamRepository.findById(any())).thenReturn(Optional.of(testTeam));
		teamService.disableTeam(1L);
		//verify if player are null (since its a mocked object the list doesn't change)
		for(Player p : testTeam.getPlayers()) {
			assertPlayerTeamId(p, null);
		}
	}
	
	@Test
	void testDisableTeam_ErrorNull() {
		try {
			teamService.disableTeam(null);
		} catch (ServiceException e) {
			assertEquals(e.getErrorCode(), ServiceException.MISSING_TEAM_ID);
			return;
		}
		fail("No exception");
	}
	
	@Test
	void testDisableTeam_ErrorNotFound() {
		try {
			when(teamRepository.findById(any())).thenReturn(Optional.empty());
			teamService.disableTeam(1L);
		} catch (ServiceException e) {
			assertEquals(e.getErrorCode(), ServiceException.TEAM_NOT_FOUND);
			return;
		}
		fail("No exception");
	}
	
	/**
	 * Compare a DO Object to its DTO
	 * @param team
	 * @param teamDto
	 */
	
	
	private Player assertPlayerTeamId(Object o, Long teamId) {
		Player p = (Player)o;
		if(teamId == null) {
			assertNull(p.getPlayerTeam());
		}else {
			assertEquals(teamId, p.getPlayerTeam().getTeamId());
		}
		return p;
	}
}
