package com.lzarza.test.teammanager.controllers.impl;

import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lzarza.test.teammanager.controllers.IPlayerController;
import com.lzarza.test.teammanager.dto.PlayerDTO;
import com.lzarza.test.teammanager.exception.ServiceException;
import com.lzarza.test.teammanager.services.IPlayerService;

@RestController
@RequestMapping("api/v1/player")
public class PlayerController implements IPlayerController {
	
	private final IPlayerService playerService;
	
	public PlayerController(IPlayerService playerService) {
		this.playerService = playerService;
	}
	
	
	/**
	 * Get the list of the active players
	 * @return the list of the active players
	 * ApiResponses(code=200 message="OK")
	 */
	@Override
	@GetMapping
	public ResponseEntity<Object> getAllPlayers(){
		return ResponseEntity.ok(playerService.getActivePlayers());
	}
	
	/**
	 * Return a player based on its id
	 * @param playerId
	 * @return the id found
	 * ApiResponses(code=200 message="OK")
	 * ApiResponses(code=403 message="id required")
	 * ApiResponses(code=422 message="player not found")
	 */
	@Override
	@GetMapping("/{playerId}")
	public ResponseEntity<Object> getPlayer(@PathVariable Long playerId){
		if(playerId == null) return ResponseEntity.badRequest().body("player id required");
		PlayerDTO player = null;
		try {
			player = playerService.getById(playerId);
		} catch (ServiceException e) {
			return ServiceExceptionHandler.exceptionToResponse(e);
		}
		
		if(player == null) return ResponseEntity.unprocessableEntity().body("player not found");
		return ResponseEntity.ok(player);
	}
	
	/**
	 * Create a new player
	 * @param player the player to save, it must not have id
	 * @return the player saved with its new id
	 * ApiResponses(code=200 message="OK")
	 * ApiResponses(code=403 message="missing data")
	 */
	@Override
	@PostMapping
	public ResponseEntity<Object> createPlayer(PlayerDTO player){
		if(isMisformedData(player, false)) return ResponseEntity.badRequest().body("missing fields");
		PlayerDTO result = null;
		try {
			result = playerService.createPlayer(player);
		} catch (ServiceException e) {
			return ServiceExceptionHandler.exceptionToResponse(e);
		}
		return ResponseEntity.ok(result);
	}
	
	/**
	 * Update a player
	 * @param player the player to save
	 * @return the player saved
	 * ApiResponses(code=200 message="OK")
	 * ApiResponses(code=403 message="missing data")
	 * ApiResponses(code=422 message="player not found")
	 */
	@Override
	@PutMapping("/{playerId}")
	public ResponseEntity<Object> updatePlayer(@PathVariable Long playerId, PlayerDTO player){
		if(isMisformedData(player, false)) return ResponseEntity.badRequest().body("missing fields");
		PlayerDTO result = null;
		try {
			result = playerService.updatePlayer(player);
		} catch (ServiceException e) {
			return ServiceExceptionHandler.exceptionToResponse(e);
		}
		return ResponseEntity.ok(result);
	}

	/**
	 * Delete a player from the list of active players
	 * @param playerId
	 * @return
	 * ApiResponses(code=200 message="OK")
	 * ApiResponses(code=403 message="player id missing")
	 * ApiResponses(code=422 message="player not found")
	 */
	@Override
	@DeleteMapping("/{playerId}")
	public ResponseEntity<Object> deletePlayer(@PathVariable Long playerId){
		if(playerId == null) return ResponseEntity.badRequest().body("player id required");
		try {
			playerService.disablePlayer(playerId);
		} catch (ServiceException e) {
			return ServiceExceptionHandler.exceptionToResponse(e);
		}
		return ResponseEntity.ok(null);
	}
	
	/**
	 * Verify if mandatory fields are present
	 * @param player
	 * @param checkId
	 * @return
	 */
	private boolean isMisformedData(PlayerDTO player, boolean checkId) {
		return !StringUtils.hasLength(player.getName()) ||
				!StringUtils.hasLength(player.getPosition()) ||
				(checkId && (player.getPlayerId() == null || player.getPlayerId() <= 0));
	}
}
