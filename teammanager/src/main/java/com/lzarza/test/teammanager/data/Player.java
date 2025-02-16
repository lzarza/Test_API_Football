package com.lzarza.test.teammanager.data;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="PLAYERS")
public class Player {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="PLAYER_ID")
	private Long playerId;
	
	@Column(name="NAME")
	private String name;
	
	@Column(name="POSITION")
	private String position;
	
	@Column(name="BUDGET")
	private BigDecimal playerBudget;
	
	@ManyToOne
	@JoinColumn(name="TEAM_ID")
	private Team playerTeam;
	
	@Column(name="ACTIVE")
	private boolean isActive;
	
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

	public Team getPlayerTeam() {
		return playerTeam;
	}

	public void setPlayerTeam(Team playerTeam) {
		this.playerTeam = playerTeam;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	@Override
	public boolean equals(Object o) {
		if(o != null && o instanceof Player && this.playerId != null) {
			return this.playerId.equals(((Player)o).playerId);
		}
		return false;
	}
}
