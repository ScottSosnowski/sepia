package edu.cwru.sepia.agent.planner;

public class PlanInfeasibleException extends Exception {
	private static final long serialVersionUID = 1L;

	public PlanInfeasibleException(String message) {
		super(message);
	}
}
