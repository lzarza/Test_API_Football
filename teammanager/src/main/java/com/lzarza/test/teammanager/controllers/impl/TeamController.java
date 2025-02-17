package com.lzarza.test.teammanager.controllers.impl;

import java.math.BigDecimal;

import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lzarza.test.teammanager.controllers.ITeamController;
import com.lzarza.test.teammanager.dto.PlayerDTO;
import com.lzarza.test.teammanager.dto.TeamDTO;
import com.lzarza.test.teammanager.exception.ServiceException;
import com.lzarza.test.teammanager.services.ITeamService;

@RestController
@RequestMapping("api/v1/team")
public class TeamController implements ITeamController {
	
	private final ITeamService teamService;
	
	public TeamController(ITeamService teamService) {
		this.teamService = teamService;
	}
	
	/**
	 * Get the list of the active teams
	 * @return the list of the active teams
	 * ApiResponses(code=200 message="OK")
	 */
	@Override
	@GetMapping
	public ResponseEntity<Object> getAllTeams(@RequestParam Integer page, @RequestParam Integer size, @RequestParam(required=false) String sort){
		if(page.intValue()<0 || size.intValue()<1) return ResponseEntity.badRequest().body("invalid pagging data");
		return ResponseEntity.ok(teamService.getAllActive(page,size,sort));
	}
	
	/**
	 * Return a team based on its id
	 * @param teamId
	 * @return the id found
	 * ApiResponses(code=200 message="OK")
	 * ApiResponses(code=403 message="id required")
	 * ApiResponses(code=422 message="team not found")
	 */
	@Override
	@GetMapping("/{teamId}")
	public ResponseEntity<Object> getTeam(@PathVariable Long teamId){
		if(teamId == null) return ResponseEntity.badRequest().body("team id required");
		TeamDTO team = null;
		try {
			team = teamService.findById(teamId);
		} catch (ServiceException e) {
			return ServiceExceptionHandler.exceptionToResponse(e);
		}
		
		if(team == null) return ResponseEntity.unprocessableEntity().body("team not found");
		return ResponseEntity.ok(team);
	}
	
	/**
	 * Create a new team
	 * @param team the team to save, it must not have id
	 * @return the team saved with its new id
	 * ApiResponses(code=200 message="OK")
	 * ApiResponses(code=403 message="missing data")
	 */
	@Override
	@PostMapping
	public ResponseEntity<Object> createTeam(TeamDTO team){
		if(isMisformedData(team, false)) return ResponseEntity.badRequest().body("missing fields");
		TeamDTO result = null;
		try {
			result = teamService.createTeam(team);
			//add players
			if(!CollectionUtils.isEmpty(team.getPlayers())) {
				for(PlayerDTO player : team.getPlayers()) {
					teamService.addPlayer(result.getTeamId(), player.getPlayerId());
				}
				//reload to have player list in result
				result = teamService.findById(result.getTeamId());
			}
		} catch (ServiceException e) {
			return ServiceExceptionHandler.exceptionToResponse(e);
		}
		return ResponseEntity.ok(result);
	}
	
	/**
	 * Update a team
	 * @param team the team to save
	 * @return the team saved
	 * ApiResponses(code=200 message="OK")
	 * ApiResponses(code=403 message="missing data")
	 * ApiResponses(code=422 message="team not found")
	 */
	@Override
	@PutMapping("/{teamId}")
	public ResponseEntity<Object> updateTeam(@PathVariable Long teamId, TeamDTO team){
		if(isMisformedData(team, false)) return ResponseEntity.badRequest().body("missing fields");
		TeamDTO result = null;
		try {
			result = teamService.updateTeam(team);
		} catch (ServiceException e) {
			return ServiceExceptionHandler.exceptionToResponse(e);
		}
		return ResponseEntity.ok(result);
	}
	
	/**
	 * Delete a team from the list of active teams
	 * @param teamId
	 * @return
	 * ApiResponses(code=200 message="OK")
	 * ApiResponses(code=403 message="team id missing")
	 * ApiResponses(code=422 message="team not found")
	 */
	@Override
	@DeleteMapping("/{teamId}")
	public ResponseEntity<Object> deleteTeam(@PathVariable Long teamId){
		if(teamId == null) return ResponseEntity.badRequest().body("team id required");
		try {
			teamService.disableTeam(teamId);
		} catch (ServiceException e) {
			return ServiceExceptionHandler.exceptionToResponse(e);
		}
		return ResponseEntity.ok(null);
	}
	
	/**
	 * Add a player to a team
	 * @param teamId
	 * @param playerId
	 * @return the modified team
	 * ApiResponses(code=200 message="OK")
	 * ApiResponses(code=403 message="missing data")
	 * ApiResponses(code=422 message="team or player not found")
	 */
	@Override
	@PatchMapping("/{teamId}/{playerId}")
	public ResponseEntity<Object> addTeamPlayer(@PathVariable Long teamId, @PathVariable Long playerId){
		if(teamId == null || playerId == null) return ResponseEntity.badRequest().body("team id required");
		TeamDTO result = null;
		try {
			teamService.addPlayer(teamId, playerId);
			result = teamService.findById(teamId);
		} catch (ServiceException e) {
			return ServiceExceptionHandler.exceptionToResponse(e);
		}
		return ResponseEntity.ok(result);
	}
	
	/**
	 * Remove a player from a team
	 * @param teamId
	 * @param playerId
	 * @return the modified team
	 * ApiResponses(code=200 message="OK")
	 * ApiResponses(code=403 message="missing data")
	 * ApiResponses(code=422 message="team or player not found")
	 */
	@Override
	@DeleteMapping("/{teamId}/{playerId}")
	public ResponseEntity<Object> removeTeamPlayer(@PathVariable Long teamId, @PathVariable Long playerId){
		if(teamId == null || playerId == null) return ResponseEntity.badRequest().body("team id required");
		TeamDTO result = null;
		try {
			teamService.removePlayer(teamId, playerId);
			result = teamService.findById(teamId);
		} catch (ServiceException e) {
			return ServiceExceptionHandler.exceptionToResponse(e);
		}
		return ResponseEntity.ok(result);
	}
	
	/**
	 * Verify if mandatory fields are present
	 * @param toCheck the item to check
	 * @param checkId if you check the id is present (not the case for a create)
	 * @return
	 */
	private boolean isMisformedData(TeamDTO toCheck, boolean checkId) {
		return toCheck == null ||
				!StringUtils.hasLength(toCheck.getAcronym()) ||
				!StringUtils.hasLength(toCheck.getName()) ||
				toCheck.getTeamBudget() == null ||
				toCheck.getTeamBudget().compareTo(BigDecimal.ZERO) < 1 || //team budget must be positive
				(checkId && (toCheck.getTeamId() == null || toCheck.getTeamId() <= 0)); //id not null and superior to 0
	}
}
