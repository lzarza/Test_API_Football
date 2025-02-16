package com.lzarza.test.teammanager.controllers.impl;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.lzarza.test.teammanager.repository.PlayerRepository;
import com.lzarza.test.teammanager.repository.TeamRepository;
import com.lzarza.test.teammanager.services.ITeamService;

class TeamServiceTest {

	@Mock
	TeamRepository teamRepository;
	
	@Mock
	PlayerRepository playerRepository;
	
	@InjectMocks ITeamService teamService;
	
	@Test
	void test() {
		fail("Not yet implemented");
	}

}
