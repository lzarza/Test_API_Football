package com.lzarza.test.teammanager.dto;

import java.math.BigDecimal;
import java.util.List;

public class TeamDTO {
	private Long teamId;
	private String name;
	private String acronym;
	private BigDecimal teamBudget;
	private List<PlayerDTO> players;
	
	public Long getTeamId() {
		return teamId;
	}
	public void setTeamId(Long teamId) {
		this.teamId = teamId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAcronym() {
		return acronym;
	}
	public void setAcronym(String acronym) {
		this.acronym = acronym;
	}
	public BigDecimal getTeamBudget() {
		return teamBudget;
	}
	public void setTeamBudget(BigDecimal teamBudget) {
		this.teamBudget = teamBudget;
	}
	public List<PlayerDTO> getPlayers() {
		return players;
	}
	public void setPlayers(List<PlayerDTO> players) {
		this.players = players;
	}
}
