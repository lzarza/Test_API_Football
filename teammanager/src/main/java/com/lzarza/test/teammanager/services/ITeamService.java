package com.lzarza.test.teammanager.services;

import java.util.List;

import com.lzarza.test.teammanager.dto.TeamDTO;
import com.lzarza.test.teammanager.exception.ServiceException;

public interface ITeamService {

	/**
	 * Return a team using its id
	 * @param teamId
	 * @return
	 * @throws ServiceException if team id is null
	 */
	TeamDTO findById(Long teamId) throws ServiceException;

	/**
	 * Get all active teams
	 * @return
	 */
	List<TeamDTO> getAllActive(Integer page, Integer size, String sort);

	/**
	 * Add a player to a team
	 * @param teamId
	 * @param playerId
	 * @throws ServiceException if one id is null and if a data is not found
	 */
	void addPlayer(Long teamId, Long playerId) throws ServiceException;

	/**
	 * Remove a player from the team
	 * @param teamId
	 * @param playerId
	 * @throws ServiceException if one id is null and if a data is not foun
	 */
	void removePlayer(Long teamId, Long playerId) throws ServiceException;

	/**
	 * Create a team
	 * @param base
	 * @return
	 * @throws ServiceException if parameter is null or if it has an id set
	 */
	TeamDTO createTeam(TeamDTO base) throws ServiceException;

	/**
	 * Update a team
	 * @param changes
	 * @return
	 * @throws ServiceException if parameter is null or doesn't have id or if the team is not found
	 */
	TeamDTO updateTeam(TeamDTO changes) throws ServiceException;

	/**
	 * Disable a team and remove it from its players
	 * @param teamId
	 * @return
	 * @throws ServiceException if id is null or team is not found
	 */
	boolean disableTeam(Long teamId) throws ServiceException;

	

}