<!DOCTYPE html>
<%--
/*********************************************************************************************
 * Copyright (c) 2014 Model-Based Systems Engineering Center, Georgia Institute of Technology.
 *                         http://www.mbse.gatech.edu/
 *                  http://www.mbsec.gatech.edu/research/oslc
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
 *	   Axel Reichwein, Koneksys (axel.reichwein@koneksys.com)		
 *******************************************************************************************/
--%>
<%@ page contentType="text/html" language="java" pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="org.eclipse.emf.ecore.EClass"%>
<%@ page import="org.eclipse.emf.ecore.EAttribute"%>
<%
    String elementType = (String) request.getAttribute("elementType");
	String createdElement = (String) request.getAttribute("createdElement");
	String projectId = (String) request.getAttribute("projectId");
	String portNumber = (String) request.getAttribute("portNumber");
	
	
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html;charset=utf-8">
<title>MagicDraw OSLC Adapter: Resource Creator</title>
</head>
<body style="padding: 10px;">
	<h1>
		SysML
		<%=elementType%> <%=createdElement%>
		Created
	</h1>
	<div>
		<form id="Create" action="http://localhost:<%=portNumber%>/oslc4jmagicdraw/services/serviceProviders/<%=projectId%>" method="GET">
			


			<table style="clear: both;">

				<tr>
					<td></td>
					<td><input type="submit" value="Return to Service provider">
					</td>
				</tr>
			</table>

			<div style="width: 500px;"></div>

		</form>

		<div style="clear: both;"></div>
	</div>
</body>
</html>

