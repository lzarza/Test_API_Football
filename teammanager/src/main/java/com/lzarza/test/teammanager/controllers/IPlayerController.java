package com.lzarza.test.teammanager.controllers;

import org.springframework.http.ResponseEntity;

import com.lzarza.test.teammanager.dto.PlayerDTO;

public interface IPlayerController {

	/**
	 * Get the list of the active players
	 * @return the list of the active players
	 * ApiResponses(code=200 message="OK")
	 */
	ResponseEntity<Object> getAllPlayers();

	/**
	 * Return a player based on its id
	 * @param playerId
	 * @return the id found
	 * ApiResponses(code=200 message="OK")
	 * ApiResponses(code=403 message="id required")
	 * ApiResponses(code=422 message="player not found")
	 */
	ResponseEntity<Object> getPlayer(Long playerId);

	/**
	 * Delete a player from the list of active players
	 * @param playerId
	 * @return
	 * ApiResponses(code=200 message="OK")
	 * ApiResponses(code=403 message="player id missing")
	 * ApiResponses(code=422 message="player not found")
	 */
	ResponseEntity<Object> deletePlayer(Long playerId);

	/**
	 * Update a player
	 * @param player the player to save
	 * @return the player saved
	 * ApiResponses(code=200 message="OK")
	 * ApiResponses(code=403 message="missing data")
	 * ApiResponses(code=422 message="player not found")
	 */
	ResponseEntity<Object> updatePlayer(Long playerId, PlayerDTO player);

	/**
	 * Create a new player
	 * @param player the player to save, it must not have id
	 * @return the player saved with its new id
	 * ApiResponses(code=200 message="OK")
	 * ApiResponses(code=403 message="missing data")
	 */
	ResponseEntity<Object> createPlayer(PlayerDTO player);


}
