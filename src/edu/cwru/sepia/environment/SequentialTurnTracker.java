/**
 *  Strategy Engine for Programming Intelligent Agents (SEPIA)
    Copyright (C) 2012 Case Western Reserve University

    This file is part of SEPIA.

    SEPIA is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    SEPIA is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with SEPIA.  If not, see <http://www.gnu.org/licenses/>.
 */
package edu.cwru.sepia.environment;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

import edu.cwru.sepia.agent.Agent;
/**
 * A class that gives priority to a single player at a time.  This is a cycle with order randomly generated each episode.
 * @author The Condor
 *
 */
public class SequentialTurnTracker implements TurnTracker {

	private ArrayList<Integer> players;
	private HashSet<Integer> playersWhoHaveHadTurns;
	private boolean currentIndexRemoved=false;
	private Random r;
	private int currentIndex;
	public SequentialTurnTracker(Random r) {
		players = new ArrayList<Integer>();
		playersWhoHaveHadTurns = new HashSet<Integer>();
		this.r = r;
	}
	@Override
	public void addPlayer(Integer playerNumber) {
		if (!players.contains(playerNumber))
		{
			players.add(playerNumber);
		}
	}

	@Override
	public void removePlayer(Integer playerNumber) {
		//Find where that player is in the list
		int placeInList = players.indexOf(playerNumber);
		if (placeInList != -1) //if it is in the list at all
		{
			//if the current counter is at or beyond that agent, decrement it
			if (currentIndex == placeInList)
				currentIndexRemoved = true;
			if (currentIndex >= placeInList)
			{
				currentIndex--;
			}
			//actually remove the player
			players.remove(placeInList);
		}
	}

	@Override
	public void newEpisodeAndStep() {
		//Shuffle the list and start at the beginning
		java.util.Collections.shuffle(players, r);
		playersWhoHaveHadTurns.clear();
		currentIndex = 0;
		currentIndexRemoved = false;
	}

	@Override
	public void newStep() {
		//Mark the last guy to have a turn as having moved
		//But not if the thing that was the current index was removed, because if so, the thing at currentindex isn't guarenteed to have moved.
		if (!currentIndexRemoved)
			playersWhoHaveHadTurns.add(players.get(currentIndex));
		
		//Move the index circularly
		currentIndex = (currentIndex+1)%players.size();
		currentIndexRemoved = false;
	}

	@Override
	public boolean isAgentsTurn(Agent agent) {
		return agent == null ? false : isPlayersTurn(agent.getPlayerNumber());
	}
	
	@Override
	public boolean hasHadTurnBefore(int playerNumber) {
		return playersWhoHaveHadTurns.contains(playerNumber);
	}
	@Override
	public boolean isPlayersTurn(int playerNumber) {
		if (playerNumber == Agent.OBSERVER_ID)
			return true;
		return playerNumber == players.get(currentIndex);
	}

}
