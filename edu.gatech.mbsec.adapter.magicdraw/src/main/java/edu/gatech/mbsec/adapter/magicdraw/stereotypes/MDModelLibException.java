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
 *	   Sebastian Herzig (sebastian.herzig@gatech.edu)		- initial implementation       
 *******************************************************************************************/
package edu.gatech.mbsec.adapter.magicdraw.stereotypes;

/**
 * MDModelLibException is the main class to be used for raising exceptions within
 * the scope of the library. It should be used for any exceptions that do not fit
 * into the category of commonly raised exceptions (e.g.
 * {@link IllegalArgumentException})
 * 
 * @author Sebastian Herzig
 * @version 0.1
 */
public class MDModelLibException extends Exception {

	private static final long serialVersionUID = 5316387091620198576L;

	public MDModelLibException() {
		
	}

	public MDModelLibException(String arg0) {
		super(arg0);
	}

	public MDModelLibException(Throwable arg0) {
		super(arg0);
	}

	public MDModelLibException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

}
