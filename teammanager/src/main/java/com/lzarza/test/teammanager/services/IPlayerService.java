package com.lzarza.test.teammanager.services;

import java.util.List;

import com.lzarza.test.teammanager.dto.PlayerDTO;
import com.lzarza.test.teammanager.exception.ServiceException;

public interface IPlayerService {

	/**
	 * Find a player
	 * @param playerId
	 * @return the player found or null
	 * @throws ServiceException if id is null
	 */
	PlayerDTO getById(Long playerId) throws ServiceException;

	/**
	 * Try to create a player
	 * @param player
	 * @return the create player
	 * @throws ServiceException if parameter is null or have an id
	 */
	PlayerDTO createPlayer(PlayerDTO player) throws ServiceException;

	/**
	 * Try to update a player
	 * @param player
	 * @return the updated player
	 * @throws ServiceException if parameter is null or doesn't have id or if player is not found
	 */
	PlayerDTO updatePlayer(PlayerDTO player) throws ServiceException;

	/**
	 * Disable a player and remove him from its team
	 * @param playerId
	 * @throws ServiceException if player id is null or player is not found
	 */
	void disablePlayer(Long playerId) throws ServiceException;

	/**
	 * Return the list of active players
	 */
	List<PlayerDTO> getActivePlayers();
}