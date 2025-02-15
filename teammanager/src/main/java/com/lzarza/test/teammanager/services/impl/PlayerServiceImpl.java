package com.lzarza.test.teammanager.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.lzarza.test.teammanager.data.Player;
import com.lzarza.test.teammanager.dto.PlayerDTO;
import com.lzarza.test.teammanager.exception.ServiceException;
import com.lzarza.test.teammanager.repository.PlayerRepository;
import com.lzarza.test.teammanager.services.IPlayerService;

@Service
public class PlayerServiceImpl implements IPlayerService {

	private final PlayerRepository playerRepository;
	
	private final ModelMapper modelMapper;
	private final Logger logger = LoggerFactory.getLogger(PlayerServiceImpl.class);
	
	public PlayerServiceImpl(PlayerRepository playerRepository) {
		this.playerRepository = playerRepository;
		this.modelMapper = new ModelMapper();
	}
	
	@Override
	public PlayerDTO getById(Long playerId) throws ServiceException {
		PlayerDTO result = null;
		verifyPlayerId(playerId);
		Optional<Player> player = playerRepository.findById(playerId);
		if(player.isPresent()) {
			result = mapPlayer(player.get());
		}
		return result;
	}
	
	@Override
	public PlayerDTO createPlayer(PlayerDTO player) throws ServiceException {
		verifyPlayer(player,false);
		if(player.getPlayerId() != null) {
			logger.error("Create player failure, existing id");
			throw new ServiceException(ServiceException.CANNOT_CREATE_PLAYER);
		}
		Player entity = modelMapper.map(player, Player.class);
		entity = playerRepository.save(entity);
		return mapPlayer(entity);
	}
	
	@Override
	public PlayerDTO updatePlayer(PlayerDTO player) throws ServiceException {
		verifyPlayer(player,true);
		Player toChange = getPlayerById(player.getPlayerId());	
		toChange.setName(player.getName());
		toChange.setPosition(player.getPosition());
		toChange.setPlayerBudget(player.getPlayerBudget());
		toChange = playerRepository.save(toChange);
		return mapPlayer(toChange);
	}
	
	@Override
	public void disablePlayer(Long playerId) throws ServiceException {
		verifyPlayerId(playerId);
		Player toChange = getPlayerById(playerId);
		toChange.setActive(false);
		toChange.setPlayerTeam(null);
		playerRepository.save(toChange);
	}
	
	@Override
	public List<PlayerDTO> getActivePlayers() {
		List<PlayerDTO> result = new ArrayList<PlayerDTO>();
		List<Player> actives = playerRepository.findByActiveTrue();
		if(actives != null) {
			for(Player player : actives) {
				result.add(mapPlayer(player));
			}
		}
		return result;
	}
	
	/**
	 * Map basic player data using model mapper and adapt getting the acronym on team data
	 * @param entity
	 * @return
	 */
	private PlayerDTO mapPlayer(Player entity) {
		PlayerDTO result = modelMapper.map(entity, PlayerDTO.class);
		if(entity.getPlayerTeam() != null) {
			result.setTeamAcronym(entity.getPlayerTeam().getAcronym());
		}
		return result;
	}
	
	/**
	 * Verify if the given id is not null
	 * @param playerId
	 * @throws ServiceException if id is null
	 */
	private void verifyPlayerId(Long playerId) throws ServiceException {
		if(playerId == null) {
			throw new ServiceException(ServiceException.MISSING_PLAYER_ID);
		}
	}
	
	/**
	 * Get a player by its id
	 * @param playerId
	 * @return a player
	 * @throws ServiceException if player is not found
	 */
	private Player getPlayerById(Long playerId) throws ServiceException {
		Optional<Player> dbPlayer = playerRepository.findById(playerId);
		if(dbPlayer.isEmpty()) {
			logger.error("Player " + playerId + " not found");
			throw new ServiceException(ServiceException.PLAYER_NOT_FOUND);
		}
		return dbPlayer.get();
	}
	
	/**
	 * Verify if the given player is null and optional its id isn't too
	 * @param dto
	 * @param verifyId
	 * @throws ServiceException if verifications are wrong
	 */
	private void verifyPlayer(PlayerDTO dto, boolean verifyId) throws ServiceException {
		if(dto == null) {
			logger.error("Player dto missing");
			throw new ServiceException(ServiceException.MISSING_PLAYER_ID);
		}
		if(verifyId && dto.getPlayerId() == null) {
			logger.error("Player id missing");
			throw new ServiceException(ServiceException.MISSING_PLAYER_ID);
		}
	}

	
	
}
