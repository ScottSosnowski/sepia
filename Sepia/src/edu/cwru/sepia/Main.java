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
package edu.cwru.sepia;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import org.apache.commons.configuration.CombinedConfiguration;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.HierarchicalConfiguration;
import org.apache.commons.configuration.HierarchicalINIConfiguration;
import org.apache.commons.configuration.XMLConfiguration;

import edu.cwru.sepia.agent.Agent;
import edu.cwru.sepia.model.persistence.generated.XmlState;
import edu.cwru.sepia.model.state.StateCreator;
import edu.cwru.sepia.model.state.XmlStateCreator;
import edu.cwru.sepia.runner.Runner;

/**
 * An entry point into Sepia that takes an XML configuration file as defined in data/schema/config.xsd.
 * @author tim
 *
 */
public final class Main {
	private static final Logger logger = Logger.getLogger(Main.class.getCanonicalName());

	public static void main(String[] args) {
		if(args.length == 0)
		{
			System.out.println("You must specify a configuration file.");
			return;
		}
				
		HierarchicalConfiguration config = null;
		try 
		{
			config = getConfiguration(args[0]);
		} 
		catch (ConfigurationException ex) 
		{
			logger.log(Level.SEVERE, args[0] + " is not a valid XML or INI configuration file.", ex);
			return;
		}
		
		StateCreator stateCreator;
		try
		{
			stateCreator = getStateCreator(config);
		}
		catch(JAXBException ex) 
		{
			logger.log(Level.SEVERE, config.getString("map") + " is not a valid map file.", ex);
			return;
		}
		
		Agent[] agents = getAgents(config);
		if(agents == null)
			return;
		
		Runner runner;
		try
		{
			runner = getRunner(config, stateCreator, agents);
		}
		catch (Exception ex)
		{
			logger.log(Level.SEVERE, "Unable to instantiate episode runner " + config.getString("runner.class"), ex);
			return;
		}
		
		runner.run();
	}
	
	private static StateCreator getStateCreator(Configuration config) throws JAXBException {
		String mapFilename = config.getString("Map");
		JAXBContext context = JAXBContext.newInstance(XmlState.class);
		XmlState state = (XmlState)context.createUnmarshaller().unmarshal(new File(mapFilename));
		return new XmlStateCreator(state);
	}
	
	private static Agent[] getAgents(HierarchicalConfiguration config) {
		String[] participatingAgents = config.getStringArray("agents.ParticipatingAgents");
		Agent[] agents = new Agent[participatingAgents.length];
		
		for(int i = 0; i < participatingAgents.length; i++)
		{
			String key = "agents."+participatingAgents[i];
			HierarchicalConfiguration agentConfig = config.configurationAt(key);
			String className = agentConfig.getString("ClassName");
			int playerId = agentConfig.getInt("PlayerId");
			String[] arguments = agentConfig.getStringArray("ConstructorArguments");
			try {
				Class<?> classDef = Class.forName(className);
				Agent agent = null;
				try {
					if(arguments != null && arguments.length != 0)
						agent = (Agent) classDef.getConstructor(int.class, String[].class).newInstance(playerId, arguments);
				} catch (Exception e) {
				}
				if(agent == null) {
					@SuppressWarnings("unchecked")
					Constructor<? extends Agent> constructor = (Constructor<? extends Agent>) classDef.getConstructor(int.class); 
					agent = (Agent)constructor.newInstance(playerId);
				}
				agents[i] = agent;
			} catch(Exception ex) {
				String errorMessage = String.format("Unable to instantiate %s with playerId %d%s.", className, playerId,
						(arguments == null ? "" : " and additional arguments " + Arrays.toString(arguments)));
				logger.log(Level.SEVERE, errorMessage, ex);
				throw new RuntimeException(ex);//make sure the program fails and the error gets propagated
			}
			agents[i].setConfiguration(agentConfig);
		}
		
		return agents;
	}
	
	private static HierarchicalConfiguration getConfiguration(String filename) throws ConfigurationException {
		HierarchicalConfiguration configuration = null;
		try {
			configuration = new XMLConfiguration(new File(filename));
		} catch(Exception ex) {}
		if(configuration == null || configuration.isEmpty()) {
			try {
				configuration = new HierarchicalINIConfiguration(new File(filename));
			} catch(Exception ex) {}
		}
		if(configuration == null)
			throw new ConfigurationException("Unable to load configuration file as XML, or INI.");
		return configuration;
	}
	
	private static Runner getRunner(HierarchicalConfiguration globalConfig, StateCreator stateCreator, Agent[] agents) throws ClassNotFoundException, 
													IllegalArgumentException, SecurityException, InstantiationException, 
													IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		Class<?> runnerClass = Class.forName(globalConfig.getString("runner.ClassName"));
		CombinedConfiguration config = new CombinedConfiguration();
		config.addConfiguration(globalConfig.configurationAt("model"), "model", "model");
		config.addConfiguration(globalConfig.configurationAt("runner.properties"), "runner.properties", "runner.properties");
		return (edu.cwru.sepia.runner.Runner)runnerClass.getConstructor(Configuration.class, StateCreator.class, Agent[].class)
														.newInstance(config, stateCreator, agents);
	}
	
	private Main() {}
}
