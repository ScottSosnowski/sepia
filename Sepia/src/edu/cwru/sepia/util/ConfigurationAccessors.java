package edu.cwru.sepia.util;

import org.apache.commons.configuration.Configuration;

public final class ConfigurationAccessors {
	
	public static final String TIME_LIMIT = "model.TimeLimit";
	public static final String REQUIRED_WOOD = "model.RequiredWood";
	public static final String REQUIRED_GOLD = "model.RequiredGold";
	public static final String CONQUEST = "model.Conquest";
	public static final String MIDAS = "model.Midas";
	public static final String MANIFEST_DESTINY = "model.ManifestDestiny";
	
	private ConfigurationAccessors() {}
	
	public static boolean isConquest(Configuration configuration) {
		return configuration.getBoolean(CONQUEST, true);
	}
	
	public static boolean isMidas(Configuration configuration) {
		return configuration.getBoolean(MIDAS, false);
	}

	public static boolean isManifestDestiny(Configuration configuration) {
		return configuration.getBoolean(MANIFEST_DESTINY, false);
	}
	
	public static int timeLimit(Configuration configuration) {
		return configuration.getInt(TIME_LIMIT, 1 << 16);
	}

	public static int requiredGold(Configuration configuration) {
		return configuration.getInt(REQUIRED_GOLD, 0);
	}
	
	public static int requiredWood(Configuration configuration) {
		return configuration.getInt(REQUIRED_WOOD, 0);
	}
	
	public static int numEpisodes(Configuration configuration) {
		return configuration.getInt("runner.numEpisodes", 1);
	}
	
	
}
