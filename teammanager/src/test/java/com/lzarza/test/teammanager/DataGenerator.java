package com.lzarza.test.teammanager;

import java.math.BigDecimal;
import java.util.Arrays;

import org.jeasy.random.EasyRandom;

import com.lzarza.test.teammanager.data.Player;
import com.lzarza.test.teammanager.data.Team;
import com.lzarza.test.teammanager.dto.PlayerDTO;
import com.lzarza.test.teammanager.dto.TeamDTO;

public class DataGenerator {
	
	public static EasyRandom generator = new EasyRandom();

	public static Player generatePlayer(long id, String name, double budget, String position) {
		Player result = new Player();
		result.setPlayerId(id);
		result.setName(name);
		result.setPlayerBudget(BigDecimal.valueOf(budget));
		result.setPosition(position);
		result.setActive(true);
		return result;
	}
	
	public static PlayerDTO generatePlayerDTO(long id, String name, double budget, String position) {
		PlayerDTO result = new PlayerDTO();
		result.setPlayerId(id);
		result.setName(name);
		result.setPlayerBudget(BigDecimal.valueOf(budget));
		result.setPosition(position);
		return result;
	}
	
	public static Team generateTeam(Long id, String name, String acronym, double budget, Player...players) {
		Team result = new Team();
		result.setTeamId(id);
		result.setAcronym(acronym);
		result.setName(name);
		result.setTeamBudget(BigDecimal.valueOf(budget));
		result.setPlayers(Arrays.asList(players));
		for(Player teamPlayer : result.getPlayers()) {
			teamPlayer.setPlayerTeam(result);
		}
		result.setActive(true);
		return result;
	}
	
	public static TeamDTO generateTeamDTO(Long id, String name, String acronym, double budget, PlayerDTO...players) {
		TeamDTO result = new TeamDTO();
		result.setTeamId(id);
		result.setAcronym(acronym);
		result.setName(name);
		result.setTeamBudget(BigDecimal.valueOf(budget));
		result.setPlayers(Arrays.asList(players));
		return result;
	}
	
	public static Team saveTeamAction(Object team) {
		Team toSave = (Team)team;
		if(toSave.getTeamId() == null) {
			toSave.setTeamId(generator.nextLong());
		}
		return toSave;
	}
	
	public static Player savePlayerAction(Object player) {
		Player toSave = (Player)player;
		if(toSave.getPlayerId() == null) {
			toSave.setPlayerId(generator.nextLong());
		}
		return toSave;
	}
}
