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
	EClass eClass = (EClass) request.getAttribute("eclass");
	String creatorUri = (String) request.getAttribute("creatorUri");
	List<EAttribute> eAttributes = (List<EAttribute>) eClass
			.getEAllAttributes();
	List<String> possibleBlocks = (List<String>) request.getAttribute("possibleBlocks");
	List<String> possibleParts = (List<String>) request.getAttribute("possibleParts");
	List<String> possibleRoles = (List<String>) request.getAttribute("possibleRoles");

	
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html;charset=utf-8">
<title>MagicDraw OSLC Adapter: Resource Creator</title>

</head>
<body style="padding: 10px;">
	<h1>
		SysML
		<%=eClass.getName()%>
		Creation
	</h1>
	<div>
		<form id="Create" action=<%=creatorUri%> method="POST">
			


			<table style="clear: both;">

				<%
					for (EAttribute eAttribute : eAttributes) {
				%>
				<tr>
				<th class="field_label required"><%=eAttribute.getName()%>:</th>				
				<td><input name="<%=eAttribute.getName()%>" class="required text_input"
					type="text" style="width: 400px id=" <%=eAttribute.getName()%>" /></td>
				</tr>
				<%
					}
				%>

				
				<tr>
					<th class="field_label">Connector End 1 Role</th>
					<td><select name="role1">
							<%
								for (String c : possibleRoles) {
							%>
							<option value="<%=c%>"><%=c%></option>
							<%
								}
							%>
					</select></td>
				</tr>
				
				<tr>
					<th class="field_label">Connector End 1 Part With Port</th>
					<td><select name="partWithPort1">
							<%
								for (String c : possibleParts) {
							%>
							<option value="<%=c%>"><%=c%></option>
							<%
								}
							%>
					</select></td>
				</tr>
				
				<tr>
					<th class="field_label">Connector End 2 Role</th>
					<td><select name="role2">
							<%
								for (String c : possibleRoles) {
							%>
							<option value="<%=c%>"><%=c%></option>
							<%
								}
							%>
					</select></td>
				</tr>

				<tr>
					<th class="field_label">Connector End 2 Part With Port</th>
					<td><select name="partWithPort2">
							<%
								for (String c : possibleParts) {
							%>
							<option value="<%=c%>"><%=c%></option>
							<%
								}
							%>
					</select></td>
				</tr>

				<tr>
					<th class="field_label">Owner</th>
					<td><select name="ownerElement">
							<%
								for (String c : possibleBlocks) {
							%>
							<option value="<%=c%>"><%=c%></option>
							<%
								}
							%>
					</select></td>
				</tr>

				

				<tr>
					<td></td>
					<td><input type="submit" value="Create <%=eClass.getName()%>">
					</td>
				</tr>
			</table>

			<div style="width: 500px;"></div>

		</form>

		<div style="clear: both;"></div>
	</div>
</body>
</html>

