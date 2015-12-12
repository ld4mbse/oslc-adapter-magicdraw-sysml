/*********************************************************************************************
 * Copyright (c) 2014 Model-Based Systems Engineering Center, Georgia Institute of Technology.
 *
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  and Eclipse Distribution License v. 1.0 which accompanies this distribution.
 *  
 *  The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 *  and the Eclipse Distribution License is available at
 *  http://www.eclipse.org/org/documents/edl-v10.php.
 *  
 *  Contributors:
 *  
 *	   Axel Reichwein (axel.reichwein@koneksys.com)		- initial implementation       
 *******************************************************************************************/
package edu.gatech.mbsec.adapter.magicdraw.clients;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import edu.gatech.mbsec.adapter.magicdraw.application.MagicDrawManager;

/**
 * The main() method of HTTPClient4GettingResourceRepresentation sends an 
 * HTTP request to receive an RDF/XML representation of a SysML Block as provided by the 
 * OSLC MagicDraw SysML adapter. The RDF/XML representation is saved in a file.
 * 
 * @author Axel Reichwein (axel.reichwein@koneksys.com)
 */
public class HTTPClient4GettingResourceRepresentation {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		URL magicdrawAdapter;
		try {
			magicdrawAdapter = new URL(MagicDrawManager.baseHTTPURI 
				+ "/services/Wired_Camera_Example/blocks/Blocks::Wired_Camera");
			URLConnection magicDrawConnection = magicdrawAdapter.openConnection();
			magicDrawConnection.addRequestProperty("Authorization", "Basic YXhlbDpvc2xj");
			magicDrawConnection.addRequestProperty("Accept", "application/rdf+xml");
	        BufferedReader htmlRepresentation = new BufferedReader(
	                                new InputStreamReader(
	                                magicDrawConnection.getInputStream()));
	        String inputLine;
	        while ((inputLine = htmlRepresentation.readLine()) != null) 
	            System.out.println(inputLine);
	        htmlRepresentation.close();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}        
	}

}
