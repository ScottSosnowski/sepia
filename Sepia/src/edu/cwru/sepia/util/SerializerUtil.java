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
package edu.cwru.sepia.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import edu.cwru.sepia.model.history.History;
import edu.cwru.sepia.model.persistence.HistoryAdapter;
import edu.cwru.sepia.model.persistence.StateAdapter;
import edu.cwru.sepia.model.persistence.generated.XmlHistory;
import edu.cwru.sepia.model.persistence.generated.XmlState;
import edu.cwru.sepia.model.state.State;
import edu.cwru.sepia.model.state.XmlStateCreator;


/**
 * Stores and loads states and histories. Supports both JAXB representation and
 * object serialization (legacy).
 * @author Feng, Tim
 *
 */
public final class SerializerUtil {
	private static final Logger logger = Logger.getLogger(SerializerUtil.class.getCanonicalName());
		
	private SerializerUtil() {}
	
	public static void storeState(String filename, State state) {
		try {
			if(filename.contains(".map")) {
				ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(filename));
				outputStream.writeObject(state);
				outputStream.close();
			} else {
				try {
					JAXBContext context = JAXBContext.newInstance(XmlState.class);
					Marshaller marshaller = context.createMarshaller();
					marshaller.setProperty("jaxb.formatted.output", true);
					StateAdapter adapter = new StateAdapter();
					PrintWriter writer = new PrintWriter(new File(filename));
					marshaller.marshal(adapter.toXml(state), writer);
					writer.close();
				} catch (JAXBException e) {
					logger.log(Level.SEVERE, "Unable to convert state into XML format.", e);
				}
			}
		} catch (IOException e) {
			logger.log(Level.SEVERE, "Unable to write state to " + filename + ".", e);
		}
	}
	
	public static State loadState(String filename) {
		State state = null;
		if (filename.contains(".map")) {
			try {
				ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(filename));
				state = (State) inputStream.readObject();
				inputStream.close();
			} catch (IOException e) {
				logger.log(Level.SEVERE, "Unable to read state from " + filename + ".", e);
			} catch (ClassNotFoundException e) {
				logger.log(Level.SEVERE, "Unable to locate class definition.", e);
			}
		} else {
			try {
				
				JAXBContext context = JAXBContext.newInstance(XmlState.class);
				XmlState xmlState = (XmlState)context.createUnmarshaller().unmarshal(new File(filename));
				state = new XmlStateCreator(xmlState).createState();
			} catch (JAXBException e) {
				logger.log(Level.SEVERE, "Unable to convert state in "+new File(filename).getAbsolutePath()+" into XML format.", e);
			}
		}
		return state;
	}
	
	public static void storeHistory(String filename, History history) {
		try {
			if(filename.contains(".map"))
			{
				ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(filename));
				outputStream.writeObject(history);
				outputStream.close();
			}
			else
			{
				try {
					JAXBContext context = JAXBContext.newInstance(XmlHistory.class);
					Marshaller marshaller = context.createMarshaller();
					marshaller.setProperty("jaxb.formatted.output", true);
					PrintWriter writer = new PrintWriter(new File(filename));
					marshaller.marshal(HistoryAdapter.toXml(history), writer);
					writer.close();
				} catch (JAXBException e) {
					logger.log(Level.SEVERE, "Unable to convert history into XML format.", e);
				}
			}
		} catch(IOException ex) {
			logger.log(Level.SEVERE, "Unable to write history to " + filename + ".", ex);
		}
	}
	
	public static History loadHistory(String filename) {
		History history = null;
		try {
			ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(filename));
			history = (History) inputStream.readObject();
			inputStream.close();
		} catch (IOException e) {
			logger.log(Level.SEVERE, "Unable to read history from " + filename + ".", e);
		} catch (ClassNotFoundException e) {
			logger.log(Level.SEVERE, "Unable to locate class definition.", e);
		}
		return history;
	}
}








