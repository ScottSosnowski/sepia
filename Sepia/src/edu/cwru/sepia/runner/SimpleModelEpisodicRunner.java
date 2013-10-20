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
package edu.cwru.sepia.runner;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.configuration.Configuration;

import edu.cwru.sepia.agent.Agent;
import edu.cwru.sepia.environment.Environment;
import edu.cwru.sepia.model.SimpleModel;
import edu.cwru.sepia.model.state.StateCreator;

/**
 * A {@code Runner} that runs a number of episodes using {@code edu.cwru.sepia.model.SimpleModel}.
 * @author Tim
 *
 */
public class SimpleModelEpisodicRunner extends Runner {
	private static final Logger logger = Logger.getLogger(SimpleModelEpisodicRunner.class.getCanonicalName());

	private int seed;
	private int numEpisodes;
	private int episodesPerSave;
	private boolean saveAgents;
	private Environment env;
	
	public SimpleModelEpisodicRunner(Configuration configuration, StateCreator stateCreator, Agent[] agents) {
		super(configuration, stateCreator, agents);
	}

	@Override
	public void run() {
		seed = configuration.getInt("RandomSeed", 6);
		numEpisodes = configuration.getInt("NumEpisodes", 1);
		episodesPerSave = configuration.getInt("EpisodesPerSave", 0);
		saveAgents = configuration.getBoolean("SaveAgents", false);
		
		SimpleModel model = new SimpleModel(stateCreator.createState(), stateCreator, configuration);
		File baseDirectory = new File("saves");
		baseDirectory.mkdirs();
		env = new Environment(agents ,model, seed, configuration);
		for(int episode = 0; episode < numEpisodes; episode++) {
			if(logger.isLoggable(Level.FINE))
				logger.fine("=======> Starting episode " + episode);
			try {
				env.runEpisode();
			} catch (InterruptedException e) {
				logger.log(Level.SEVERE, "Unable to complete episode " + episode, e);
			}
			if(episodesPerSave > 0 && episode % episodesPerSave == 0) {
				saveState(new File(baseDirectory.getPath() + "/state"+episode+".sepiaSave"),env.getModel().getState());
				for(int j = 0; saveAgents && j < agents.length; j++) {
					try {
						ObjectOutputStream agentOut = new ObjectOutputStream(new FileOutputStream(baseDirectory.getPath() + "/agent"+j+"-"+episode));
						agentOut.writeObject(agents[j]);
						agentOut.close();
					} catch(Exception ex) {
						logger.info("Unable to save agent " + j);
					}
				}
			}
		}
	}
	
}
