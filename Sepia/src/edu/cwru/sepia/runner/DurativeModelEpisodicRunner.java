package edu.cwru.sepia.runner;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.configuration.Configuration;

import edu.cwru.sepia.agent.Agent;
import edu.cwru.sepia.environment.Environment;
import edu.cwru.sepia.model.LessSimpleModel;
import edu.cwru.sepia.model.Model;
import edu.cwru.sepia.model.state.StateCreator;

public class DurativeModelEpisodicRunner extends Runner {
	private static final Logger logger = Logger.getLogger(DurativeModelEpisodicRunner.class.getCanonicalName());

	private int seed;
	private int numEpisodes;
	private int episodesPerSave;
	private boolean saveAgents;
	private Environment env;
	
	public DurativeModelEpisodicRunner(Configuration configuration, StateCreator stateCreator, Agent[] agents) {
		super(configuration, stateCreator, agents);
	}

	@Override
	public void run() {
		seed = configuration.getInt("RandomSeed", 6);
		numEpisodes = configuration.getInt("NumEpisodes", 1);
		episodesPerSave = configuration.getInt("EpisodesPerSave", 0);
		saveAgents = configuration.getBoolean("SaveAgents", false);
		
		Model model = new LessSimpleModel(stateCreator.createState(), stateCreator, configuration);
		File baseDirectory = new File("saves");
		baseDirectory.mkdirs();
		env = new Environment(agents ,model, seed);
		if(configuration.getBoolean("LoadAgents", false)) {
			for(Agent agent : agents) {
				try {
					loadAgentData(new File(baseDirectory.getPath() + "/agent" + agent.getPlayerNumber() + 
							"-" + configuration.getInt("LoadFromEpisode")), agent);
				} catch (Exception e) {
					logger.log(Level.WARNING, "Unable to load agent " + agent.getPlayerNumber(), e);
				}
			}
		}
		for(int episode = 0; episode < numEpisodes; episode++) {
			if(logger.isLoggable(Level.FINE))
				logger.fine("=======> Starting episode " + episode);
			try {
				env.forceNewEpisode();
				env.runEpisode();
			} catch (InterruptedException e) {
				logger.log(Level.SEVERE, "Unable to complete episode " + episode, e);
			}
			if(episodesPerSave > 0 && episode % episodesPerSave == 0) {
				saveState(new File(baseDirectory.getPath() + "/state"+episode+".sepiaSave"),env.getModel().getState());
				for(int j = 0; saveAgents && j < agents.length; j++) {
					try {
						File file = new File(baseDirectory.getPath() + "/agent"+j+"-"+episode);
						saveAgentData(file, agents[j]);
						logger.info("Saved agent " + j);
					} catch(Exception ex) {
						logger.log(Level.WARNING, "Unable to save agent " + j, ex);
						
					}
				}
			}
		}
	}

}
