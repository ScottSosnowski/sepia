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
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import edu.cwru.sepia.model.persistence.TemplateAdapter;
import edu.cwru.sepia.model.persistence.generated.XmlTemplate;
import edu.cwru.sepia.model.persistence.generated.XmlTemplates;
import edu.cwru.sepia.model.state.IdDistributor;
import edu.cwru.sepia.model.state.Template;

public final class TypeLoader {
	private TypeLoader(){}
	
	public static List<Template<?>> loadFromFile(String filename, int player, IdDistributor idsource) throws FileNotFoundException, JAXBException {
		List<Template<?>> templates = new ArrayList<Template<?>>();

		JAXBContext context = JAXBContext.newInstance(XmlTemplates.class);
		XmlTemplates xmlTemplates = (XmlTemplates)context.createUnmarshaller().unmarshal(new File(filename));
		for(XmlTemplate xml : xmlTemplates.getTemplate()) {
			Template<?> template = TemplateAdapter.fromXml(xml, player);
			template.setID(idsource.nextTemplateId());
			templates.add(template);
		}
			
		return templates;
	}
}
