package com.lzarza.test.teammanager.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.lzarza.test.teammanager.data.Player;
import com.lzarza.test.teammanager.data.Team;
import com.lzarza.test.teammanager.dto.PlayerDTO;
import com.lzarza.test.teammanager.dto.TeamDTO;
import com.lzarza.test.teammanager.exception.ServiceException;
import com.lzarza.test.teammanager.repository.PlayerRepository;
import com.lzarza.test.teammanager.repository.TeamRepository;
import com.lzarza.test.teammanager.services.ITeamService;

@Service
public class TeamServiceImpl implements ITeamService {
	
	private final TeamRepository teamRepository;
	private final PlayerRepository playerRepository;
	private final ModelMapper modelMapper;
	
	private final Logger logger = LoggerFactory.getLogger(TeamServiceImpl.class);
	
	public TeamServiceImpl(TeamRepository teamRepository, PlayerRepository playerRepository) {
		this.teamRepository = teamRepository;
		this.playerRepository = playerRepository;
		this.modelMapper = new ModelMapper();
	}

	@Override
	public TeamDTO findById(Long teamId) throws ServiceException {
		verifyId(teamId, ServiceException.MISSING_TEAM_ID);
		TeamDTO result = null;
		Optional<Team> dbTeam = teamRepository.findById(teamId);
		if(dbTeam.isPresent()) {
			result = mapTeam(dbTeam.get());
		}
		return result;
	}
	
	@Override
	public List<TeamDTO> getAllActive(Integer page, Integer size, String sort){
		List<TeamDTO> result = new ArrayList<TeamDTO>();
		
		Pageable pagging = getTeamPageable(page, size, sort);
		List<Team> teams = null;
		if(pagging == null) {
			teams = teamRepository.findByActiveTrue();
		}else {
			teams = teamRepository.findByActiveTrue(pagging);
		}
		for(Team team : teams) {
			result.add(mapTeam(team));
		}
		return result;
	}
	
	@Override
	public TeamDTO addPlayer(Long teamId, Long playerId) throws ServiceException {
		verifyId(teamId, ServiceException.MISSING_TEAM_ID);
		verifyId(playerId, "Cannot find player without id");
		Team team = getTeamById(teamId);
		Player player = getPlayerById(playerId);
		player.setPlayerTeam(team);
		playerRepository.save(player);
		//reload team since player is associated to be sure change is done
		return mapTeam(getTeamById(teamId));
	}
	
	@Override
	public TeamDTO removePlayer(Long teamId, Long playerId) throws ServiceException {
		verifyId(teamId, ServiceException.MISSING_TEAM_ID);
		verifyId(playerId, "Cannot find player without id");
		Player player = getPlayerById(playerId);
		if(player.getPlayerTeam() != null && player.getPlayerTeam().getTeamId().equals(teamId)) {
			player.setPlayerTeam(null);
		}
		playerRepository.save(player);
		//reload team since player is no more associated to be sure change is done
		return mapTeam(getTeamById(teamId));
	}
	
	@Override
	public TeamDTO createTeam(TeamDTO base) throws ServiceException {
		verifyTeam(base,false);
		if(base.getTeamId() != null) {
			throw new IllegalArgumentException("cannot create a team with an id");
		}
		Team toSave = modelMapper.map(base, Team.class);
		toSave = teamRepository.save(toSave);
		//At creation a team may have some players inside
		if(!CollectionUtils.isEmpty(base.getPlayers())){
			for(PlayerDTO player : base.getPlayers()) {
				addPlayer(toSave.getTeamId(), player.getPlayerId());
			}
		}
		
		//force reload to have a result compliant to database
		return this.findById(toSave.getTeamId());
	}
	
	@Override
	public TeamDTO updateTeam(TeamDTO changes) throws ServiceException {
		verifyTeam(changes,true);
		Team toChange = getTeamById(changes.getTeamId());
		toChange.setAcronym(changes.getAcronym());
		toChange.setName(changes.getName());
		toChange.setTeamBudget(changes.getTeamBudget());
		toChange = teamRepository.save(toChange);
		return mapTeam(toChange);
	}
	
	@Override
	public boolean disableTeam(Long teamId) throws ServiceException {
		verifyId(teamId, ServiceException.MISSING_TEAM_ID);
		Team toChange = getTeamById(teamId);
		toChange.setActive(false);
		//When a team is disabled, the players are removed
		for(Player player : toChange.getPlayers()) {
			player.setPlayerTeam(null);
			playerRepository.save(player);
		}
		return true;
	}
	
	/**
	 * Mep a team using the model mapper then add the player mapped as dto
	 * @param team
	 * @return
	 */
	private TeamDTO mapTeam(Team team) {
		TeamDTO result = modelMapper.map(team, TeamDTO.class);
		result.setPlayers(new ArrayList<PlayerDTO>());
		if(!CollectionUtils.isEmpty(team.getPlayers())) {
			for(Player p : team.getPlayers()) {
				result.getPlayers().add(modelMapper.map(p, PlayerDTO.class));
			}
		}
		return result;
	}
	
	/**
	 * Verify the id is not null
	 * @param id
	 * @param errorCode
	 * @throws ServiceException if id is null
	 */
	private void verifyId(Long id, String errorCode) throws ServiceException {
		if(id == null) {
			throw new ServiceException(errorCode);
		}
	}
	
	private Pageable getTeamPageable(Integer number, Integer size, String sortColumn) {
		Pageable result = null;
		Sort sorting = null;
		if(sortColumn != null) {
			sorting = Sort.by(sortColumn);
		}
		if(number != null && size != null) {
			if(sorting == null) {
				result = PageRequest.of(number.intValue(), size.intValue());
			}else {
				result = PageRequest.of(number.intValue(), size.intValue(), sorting);
			}
		}
		return result;
	}
	
	/**
	 * Get the team by id
	 * @param teamId
	 * @return the team entity
	 * @throws ServiceException if team is not found
	 */
	private Team getTeamById(Long teamId) throws ServiceException {
		Optional<Team> dbTeam = teamRepository.findById(teamId);
		if(dbTeam.isEmpty()) {
			throw new ServiceException(ServiceException.TEAM_NOT_FOUND);
		}
		return dbTeam.get();
	}
	
	/**
	 * Get a player by id
	 * @param playerId
	 * @return a player
	 * @throws ServiceException if player is not found
	 */
	private Player getPlayerById(Long playerId) throws ServiceException {
		Optional<Player> dbPlayer = playerRepository.findById(playerId);
		if(dbPlayer.isEmpty()) {
			throw new ServiceException(ServiceException.PLAYER_NOT_FOUND);
		}
		return dbPlayer.get();
	}
	
	/**
	 * Verify if team data exist and optional if its id exist
	 * @param dto
	 * @param verifyId
	 * @throws ServiceException if checks are wrong
	 */
	private void verifyTeam(TeamDTO dto, boolean verifyId) throws ServiceException {
		if(dto == null) {
			logger.error("team dto missing");
			throw new ServiceException(ServiceException.MISSING_TEAM_ID);
		}
		if(verifyId && dto.getTeamId() == null) {
			logger.error("team id missing");
			throw new ServiceException(ServiceException.MISSING_TEAM_ID);
		}
	}
	
}
