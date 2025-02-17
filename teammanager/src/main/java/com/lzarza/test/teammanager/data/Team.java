package com.lzarza.test.teammanager.data;

import java.math.BigDecimal;
import java.util.List;

import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
@Table(name = "TEAMS")
public class Team {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="TEAM_ID")
	private Long teamId;
	
	@Column(name="NAME")
	private String name;
	
	@Column(name="ACRONYM")
	private String acronym;
	
	@Column(name="BUDGET")
	private BigDecimal teamBudget;
	
	@OneToMany(mappedBy = "playerTeam", fetch = FetchType.EAGER)
	private List<Player> players;
	
	@Column(name="ACTIVE")
	private boolean active;

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

	public List<Player> getPlayers() {
		return players;
	}

	public void setPlayers(List<Player> players) {
		this.players = players;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}
}
