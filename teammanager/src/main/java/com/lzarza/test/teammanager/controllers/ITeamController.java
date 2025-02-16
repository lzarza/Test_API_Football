package com.lzarza.test.teammanager.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import com.lzarza.test.teammanager.dto.TeamDTO;

public interface ITeamController {

	/**
	 * Get the list of the active teams
	 * @return the list of the active teams
	 * ApiResponses(code=200 message="OK")
	 */
	ResponseEntity<Object> getAllTeams();

	/**
	 * Return a team based on its id
	 * @param teamId
	 * @return the id found
	 * ApiResponses(code=200 message="OK")
	 * ApiResponses(code=403 message="id required")
	 * ApiResponses(code=422 message="team not found")
	 */
	ResponseEntity<Object> getTeam(Long teamId);

	/**
	 * Create a new team
	 * @param team the team to save, it must not have id
	 * @return the team saved with its new id
	 * ApiResponses(code=200 message="OK")
	 * ApiResponses(code=403 message="missing data")
	 */
	ResponseEntity<Object> createTeam(TeamDTO team);

	/**
	 * Update a team
	 * @param team the team to save
	 * @return the team saved
	 * ApiResponses(code=200 message="OK")
	 * ApiResponses(code=403 message="missing data")
	 * ApiResponses(code=422 message="team not found")
	 */
	ResponseEntity<Object> updateTeam(Long teamId, TeamDTO team);

	/**
	 * Delete a team from the list of active teams
	 * @param teamId
	 * @return
	 * ApiResponses(code=200 message="OK")
	 * ApiResponses(code=403 message="team id missing")
	 * ApiResponses(code=422 message="team not found")
	 */
	ResponseEntity<Object> deleteTeam(Long teamId);

	/**
	 * Add a player to a team
	 * @param teamId
	 * @param playerId
	 * @return the modified team
	 * ApiResponses(code=200 message="OK")
	 * ApiResponses(code=403 message="missing data")
	 * ApiResponses(code=422 message="team or player not found")
	 */
	ResponseEntity<Object> addTeamPlayer(Long teamId, Long playerId);

	/**
	 * Remove a player from a team
	 * @param teamId
	 * @param playerId
	 * @return the modified team
	 * ApiResponses(code=200 message="OK")
	 * ApiResponses(code=403 message="missing data")
	 * ApiResponses(code=422 message="team or player not found")
	 */
	ResponseEntity<Object> removeTeamPlayer(Long teamId, Long playerId);

}