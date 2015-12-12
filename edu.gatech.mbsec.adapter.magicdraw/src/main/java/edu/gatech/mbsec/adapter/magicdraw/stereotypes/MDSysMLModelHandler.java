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
 *	   Axel Reichwein (axel.reichwein@koneksys.com)         - adapted for MagicDraw adapter
 *******************************************************************************************/
package edu.gatech.mbsec.adapter.magicdraw.stereotypes;

import com.nomagic.magicdraw.core.Application;
import com.nomagic.magicdraw.core.Project;
import com.nomagic.uml2.ext.jmi.helpers.StereotypesHelper;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Element;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.InstanceSpecification;
import com.nomagic.uml2.ext.magicdraw.mdprofiles.Profile;
import com.nomagic.uml2.ext.magicdraw.mdprofiles.Stereotype;

/**
 * MDSysMLModelHandler is the base class for handling SysML-compliant modeling
 * elements. Part of the functionality includes identifying model elements as
 * elements complying to SysML, other functions include the creation of SysML
 * modeling elements. Note that this class heavily depends on the MagicDraw
 * SysML profile.
 * 
 * TODO: It may be worth removing the pre-checks from the functions and simply
 * performing a pre-check on construction of the class. Think about this
 * 
 * @author Sebastian Herzig
 * @author Axel Reichwein (axel.reichwein@koneksys.com)
 * @version 0.2
 */
public class MDSysMLModelHandler {

	private static String sysMLProfileName_ = "SysML";
	private static String stereotypeBlock_ = "block";

	/**
	 * A concrete implementation of this function should check whether or not
	 * the SysML profile is loaded in the currently active project.
	 * 
	 * @return true if the profile is loaded, false otherwise
	 * @throws MDModelLibException
	 */
	public static boolean isSysMLProfileLoaded() throws MDModelLibException {
		if (StereotypesHelper.getProfile(getActiveProject(), sysMLProfileName_) == null)
			return false;

		return true;
	}

	/**
	 * Helper function that performs a general pre-check, entailing the task of
	 * finding out whether or not the SysML profile is loaded in the current
	 * project.
	 * 
	 * @throws MDModelLibException
	 */
	protected static void isSysMLProfileLoadedPreCheck()
			throws MDModelLibException {
		if (!isSysMLProfileLoaded())
			throw new MDModelLibException(
					"SysML profile not loaded or unable to detect SysML profile");
	}

	/**
	 * Returns an instance of a {@link Profile} representing the SysML profile.
	 * An exception is thrown if the profile was not loaded or could not be
	 * found.
	 * 
	 * @throws MDModelLibException
	 */
	public static Profile getSysMLProfile() throws MDModelLibException {
		isSysMLProfileLoadedPreCheck();

		return StereotypesHelper.getProfile(Application.getInstance()
				.getProject(), sysMLProfileName_);
	}

	/**
	 * Function that checks whether or not a given object is a SysML block. Note
	 * that the type for the argument of this function is {@link Element}. A
	 * pre-check is performed whether or not this particular element is also a
	 * MD UML {@link Class}, as is required to comply with the SysML
	 * specification. If the SysML profile has not been loaded or could not be
	 * located (e.g. in the case when the name of the profile changes), an
	 * exception is thrown.
	 * 
	 * @param e
	 *            An object of type Element (defined in the MD UML meta-model)
	 * @return true if the object is a SysML block, false otherwise
	 * @throws MDModelLibException
	 */
	public static boolean isSysMLBlock(Element e) throws MDModelLibException {
		isSysMLProfileLoadedPreCheck();

		if (e instanceof com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Class
				&& StereotypesHelper
						.hasStereotype(e, getStereotypeSysMLBlock()) == true)
			return true;

		return false;
	}

	public static boolean isSysMLElement(Element element,
			String sysMLStereotypeName) throws MDModelLibException {

		// check regular SysML stereotype
		isSysMLProfileLoadedPreCheck();
		return StereotypesHelper.hasStereotype(element,
				sysMLStereotypeName);
	}

	/**
	 * Helper function that returns the stereotype instance used by any model
	 * elements resembling SysML blocks. An exception is thrown if either the
	 * profile was not loaded or the stereotype could not be found in the
	 * profile.
	 * 
	 * @return The stereotype instance for a SysML block
	 * @throws MDModelLibException
	 */
	private static Stereotype getStereotypeSysMLBlock()
			throws MDModelLibException {
		isSysMLProfileLoadedPreCheck();

		// TODO: Convert this into a find function and then just alias it for
		// the others
		Stereotype s = StereotypesHelper.getStereotype(getActiveProject(),
				stereotypeBlock_, getSysMLProfile());

		if (s == null)
			throw new MDModelLibException("Block stereotype ("
					+ stereotypeBlock_ + ") in the SysML profile was not found");

		return s;
	}

	private static Stereotype getSysMLStereotype(String sysmlStereotypeName)
			throws MDModelLibException {
		isSysMLProfileLoadedPreCheck();

		// TODO: Convert this into a find function and then just alias it for
		// the others
		Stereotype s = StereotypesHelper.getStereotype(getActiveProject(),
				sysmlStereotypeName, getSysMLProfile());

		if (s == null)
			throw new MDModelLibException("Block stereotype ("
					+ sysmlStereotypeName
					+ ") in the SysML profile was not found");

		return s;
	}

	public static Project getActiveProject() throws MDModelLibException {
		if (Application.getInstance().getProject() == null)
			throw new MDModelLibException("No project is currently active");

		return Application.getInstance().getProject();
	}
}
