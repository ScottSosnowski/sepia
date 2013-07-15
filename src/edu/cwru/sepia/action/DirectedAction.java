
package edu.cwru.sepia.action;

import edu.cwru.sepia.model.state.Direction;

/**
 * A sub-type of {@code Action} which includes PrimitiveMove, PrimitiveGather, PrimitiveDeposit.
 * In addition to the base attributes found in Action, this class also contains a
 * direction in which the action should be performed. For actions that target tiles more than
 * one away, see {@code TargetedAction} or {@code LocatedAction}. 
 *
 */
public class DirectedAction extends Action {	
	private static final long serialVersionUID = -8274872242806705391L;
	
	protected final Direction direction;
	public DirectedAction(int unitid, ActionType type, Direction direction)
	{
		super(unitid, type);
		this.direction = direction;
	}
	public Direction getDirection()
	{
		return direction;
	}
	@Override
	public String toString() 
	{
		return "DirectedAction [direction=" + direction + ", type=" + type
				+ ", unitId=" + unitId + "]";
	}
	@Override 
	public boolean equals(Object other)
	{
		if (this == other)
		{
			return true;
		}
		else if (other == null || !this.getClass().equals(other.getClass()))
		{
			return false;
		}
		else
		{			
			DirectedAction aother = (DirectedAction)other;
			return aother.type == type && aother.unitId == unitId && aother.direction == direction;
		}
	}
	@Override 
	public int hashCode()
	{
		int prime = 61;
		return prime * prime * direction.hashCode() + prime * type.hashCode() + unitId;
	}
}
