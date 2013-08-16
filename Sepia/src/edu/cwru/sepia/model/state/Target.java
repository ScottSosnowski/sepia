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
package edu.cwru.sepia.model.state;

import java.io.Serializable;

import edu.cwru.sepia.util.DeepEquatable;

/**
 * The base class for anything that can be the target of an {@code edu.cwru.sepia.action.Action}.
 * @author Tim
 *
 */
public abstract class Target implements Serializable, DeepEquatable {
	public static final long serialVersionUID = 310562678386330058l;
	public final int id;
	public Target(int id)
	{
		this.id = id;
	}
}
