package com.lzarza.test.teammanager.dto;

import java.math.BigDecimal;

/**
 * Working class for a user.
 * We don't return the team nor the active status
 */
public class PlayerDTO {
	private Long playerId;
	private String name;
	private String position;
	private BigDecimal playerBudget;
	private String teamAcronym;
	
	public Long getPlayerId() {
		return playerId;
	}
	public void setPlayerId(Long playerId) {
		this.playerId = playerId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
	public BigDecimal getPlayerBudget() {
		return playerBudget;
	}
	public void setPlayerBudget(BigDecimal playerBudget) {
		this.playerBudget = playerBudget;
	}
	public String getTeamAcronym() {
		return teamAcronym;
	}
	public void setTeamAcronym(String teamAcronym) {
		this.teamAcronym = teamAcronym;
	}
}
