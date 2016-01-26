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
<%@ page
	import="org.eclipse.lyo.oslc4j.core.model.ServiceProviderCatalog"%>
<%@ page import="org.eclipse.lyo.oslc4j.core.model.ServiceProvider"%>
<%@ page import="org.eclipse.lyo.oslc4j.core.model.Link"%>
<%@ page import="java.net.URI"%>
<%@ page import="java.util.List" %>
<%@ page import="org.eclipse.lyo.oslc4j.core.model.AbstractResource"%>
<%
List<AbstractResource> elements = (List<AbstractResource>) request.getAttribute("elements");
String requestURL = (String)request.getAttribute("requestURL");
String modelName = (String) request.getAttribute("projectId");
%>
<html>
<head>

<meta http-equiv="Content-Type" content="text/html;charset=utf-8">
<title>MagicDraw OSLC Adapter: MagicDraw Connector Ends</title>
<link rel="stylesheet" type="text/css"
	href="<%=request.getContextPath()%>/css/simple.css">
<link href='http://fonts.googleapis.com/css?family=Open+Sans:400,700'
	rel='stylesheet' type='text/css'>
<link rel="shortcut icon" href="<%=request.getContextPath()%>/images/100px_white-oslc-favicon.ico">


</head>
<body onload="">

	<!-- header -->
	<p id="title">MagicDraw OSLC Adapter: MagicDraw Connector Ends</p>

	<!-- main content -->
	<div id="main-body">
		
		<!-- oslc logo and adapter details -->
		<a id="oslclogo" href="http://open-services.net/" target="_blank"><img
			src="<%=request.getContextPath()%>/images/oslcLg.png"></a>
		<div id="adapter-details">
			<p class="word-break">
				This document: <a href="<%= requestURL %>"> <%= requestURL %>
				</a><br> Adapter Publisher: <a class="notfancy"
					href="http://www.mbsec.gatech.edu/research/oslc" target="_blank">Georgia
					Institute of Technology OSLC Tools Project</a><br> Adapter
				Identity: org.eclipse.lyo.adapter.magicDraw
			</p>
		</div>
		<br>

		<!-- resource type and name -->
		<h1><span id="metainfo">MagicDraw Connector Ends </span><%= modelName %></h1>
		<br>

		<!-- resource attributes and relationships -->			
		<% Object[] elementsArray =  elements.toArray();  %>
		<% int elementsSize =  elements.size();  %>
		<% int i =  0;  %>
		<% if( elementsSize > 0) {  %>
		<p><span id="metainfo">Connector Ends</span></p>
		<table>
			<tr>
				<% while(elementsSize > 0) {;  %>
				<% AbstractResource element = (AbstractResource)elementsArray[i]; %>
				<td><a href="<%= element.getAbout() %>"> <%=getElementQualifiedName(element.getAbout())%></a></td>
				<%i++;%>
				<!-- change here maximum number of cells to be displayed in each table row -->
				<% if( i % 3 == 0) {  %>
			</tr>
			<tr>
				<% }  %>
				<%elementsSize--;%>
				<% };  %>
			</tr>
		</table>
		<% } %>
										

	</div>


	<!-- footer -->
	<p id="footer">OSLC MagicDraw Adapter 1.1</p>
	 
</body>
</html>


<!-- Java functions -->
<%!
	public String getElementQualifiedName(URI uri){
		String[] names = uri.getPath().split("/");
	    String last_name = names[names.length - 1]; 	    	
	    return last_name; 
	}
%>