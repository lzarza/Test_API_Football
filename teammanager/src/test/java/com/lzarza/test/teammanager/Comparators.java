package com.lzarza.test.teammanager;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.lzarza.test.teammanager.data.Player;
import com.lzarza.test.teammanager.data.Team;
import com.lzarza.test.teammanager.dto.PlayerDTO;
import com.lzarza.test.teammanager.dto.TeamDTO;

public class Comparators {
	public static void compareTeamToTeamDto(Team team, TeamDTO teamDto, boolean compareIds, boolean compareAcronyms) {
		assertEquals(team.getAcronym(), teamDto.getAcronym());
		assertEquals(team.getName(), teamDto.getName());
		assertTrue(team.getTeamBudget().compareTo( teamDto.getTeamBudget()) == 0);
		assertEquals(team.getTeamId(), teamDto.getTeamId());
		assertEquals(team.getPlayers().size(), teamDto.getPlayers().size());
		for(Player p : team.getPlayers()) {
			PlayerDTO pResult = teamDto.getPlayers().stream().filter(dto -> dto.getPlayerId().equals(p.getPlayerId())).findFirst().orElse(null);
			assertNotNull(pResult);
			assertEquals(p.getName(), pResult.getName());
			assertTrue(p.getPlayerBudget().compareTo(pResult.getPlayerBudget()) == 0);
			assertEquals(p.getPosition(), pResult.getPosition());
			if(compareAcronyms) assertEquals(p.getPlayerTeam().getAcronym(), pResult.getTeamAcronym());
		}
	}
	
	public static void compareTeamDtos(TeamDTO expected, TeamDTO teamDto, boolean compareIds, boolean compareAcronyms) {
		if(compareIds) {
			assertEquals(expected.getTeamId(), teamDto.getTeamId());
		}
		assertEquals(expected.getAcronym(), teamDto.getAcronym());
		assertEquals(expected.getName(), teamDto.getName());
		assertTrue(expected.getTeamBudget().compareTo( teamDto.getTeamBudget()) == 0);
		
		assertEquals(expected.getPlayers().size(), teamDto.getPlayers().size());
		for(PlayerDTO p : expected.getPlayers()) {
			PlayerDTO pResult = teamDto.getPlayers().stream().filter(dto -> dto.getPlayerId().equals(p.getPlayerId())).findFirst().orElse(null);
			assertNotNull(pResult);
			assertEquals(p.getName(), pResult.getName());
			assertTrue(p.getPlayerBudget().compareTo(pResult.getPlayerBudget()) == 0);
			assertEquals(p.getPosition(), pResult.getPosition());
			if(compareAcronyms) assertEquals(p.getTeamAcronym(), pResult.getTeamAcronym());
		}
	}
	
	public static void comparePlayerDTOs(PlayerDTO expected, PlayerDTO result, boolean compareIds) {
		assertNotNull(result);
		if(compareIds) {
			assertEquals(expected.getPlayerId(), result.getPlayerId());
		}
		assertEquals(expected.getName(), result.getName());
		assertTrue(expected.getPlayerBudget().compareTo(result.getPlayerBudget()) == 0);
		assertEquals(expected.getPosition(), result.getPosition());
		assertEquals(expected.getTeamAcronym(), result.getTeamAcronym());
	}
	
	public static void comparePlayerToPlayerDTO(Player expected, PlayerDTO result, boolean compareIds) {
		assertNotNull(result);
		if(compareIds) {
			assertEquals(expected.getPlayerId(), result.getPlayerId());
		}
		assertEquals(expected.getName(), result.getName());
		assertTrue(expected.getPlayerBudget().compareTo(result.getPlayerBudget()) == 0);
		assertEquals(expected.getPosition(), result.getPosition());
		if(expected.getPlayerTeam() != null) {
			assertEquals(expected.getPlayerTeam().getAcronym(), result.getTeamAcronym());
		}else {
			assertNull(result.getTeamAcronym());
		}
	}
}
