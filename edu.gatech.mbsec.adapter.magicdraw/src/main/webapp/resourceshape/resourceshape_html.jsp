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
<%@ page import="org.eclipse.lyo.oslc4j.core.model.ResourceShape" %>
<%@ page import="org.eclipse.lyo.oslc4j.core.model.Property" %>
	
<%

ResourceShape resource = (ResourceShape)request.getAttribute("resource");
String requestURL = (String)request.getAttribute("requestURL");
%>
<html>
<head>

<meta http-equiv="Content-Type" content="text/html;charset=utf-8">
<title>MagicDraw OSLC Adapter: Resource Shape</title>
<link rel="stylesheet" type="text/css"
	href="<%=request.getContextPath()%>/css/simple.css">
<link href='http://fonts.googleapis.com/css?family=Open+Sans:400,700'
	rel='stylesheet' type='text/css'>
<link rel="shortcut icon" href="<%=request.getContextPath()%>/images/100px_white-oslc-favicon.ico">


</head>
<body onload="">

	<!-- header -->
	<p id="title">MagicDraw OSLC Adapter: Resource Shape</p>

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
				Identity: org.eclipse.lyo.adapter.simulink
			</p>
		</div>
		<br>

		<!-- resource type and name -->
		<h1><span id="metainfo">Resource Shape </span><%= resource.getTitle() %></h1>
		
		<% if(resource.getDescribes().length > 0) { %>
			<% if(resource.getDescribes().length > 1) { %>
				<p><span id="metainfo">Types of resources associated with this shape</span></p>
				<% for (URI uri : resource.getDescribes()) { %>
					<p><a href="<%= uri %>"> <%=uri%></a></p>
				<% } %>			
			<% } else {%>	
				<% URI uri = resource.getDescribes()[0]; %>		
				<p><span id="metainfo">Type of resource associated with this shape: </span><a href="<%= uri %>"> <%= uri %></a></p>
			<% } %>
		<% } %>
		
		
		
		<br>
		<br>
		<br>
		
		<% if(resource.getProperties().length > 0) { %>
			<!--  <p><span id="metainfo">List of properties expected to be contained in resources associated with this shape</span></p> -->
			
				<% for (Property property : resource.getProperties()) { %>
					<h2><span id="metainfo">OSLC Property </span><%= property.getName() %></h2>
					
					<% if(property.getDescription() != null) { %>
						<p><span id="metainfo">Description: </span><%= property.getDescription() %></p>
					<% } %>
					<% if(property.getTitle() != null) { %>
						<p><span id="metainfo">Title: </span><%= property.getTitle() %></p>
					<% } %>
					<% if(property.getPropertyDefinition() != null) { %>
						<p><span id="metainfo">Definition: </span><%= property.getPropertyDefinition() %></p>
					<% } %>
					<% if(property.getRepresentation() != null) { %>
						<p><span id="metainfo">Representation: </span><%= property.getRepresentation() %></p>
					<% } %>
					<% if(property.getRange().length > 0) { %>
						
						<% if(property.getRange().length > 1) { %>
												
							<p><span id="metainfo">Range: </span></p>
							<% for (URI uri : property.getRange()) { %>
								<p><%= uri %></p>
							<% } %>	
						
						<% } else {%>
							<% URI uri = property.getRange()[0]; %>	
							<p><span id="metainfo">Range: </span> <%= uri %></p>
						<% } %>
						
					<% } %>
					<% if(property.getOccurs() != null) { %>
						<p><span id="metainfo">Occurs: </span><%= property.getOccurs() %></p>
					<% } %>
					
				<% } %>			
			
		<% } %>
		
		 
		
		
		
		
		
				
		
		

	</div>


	<!-- footer -->
	<p id="footer">OSLC MagicDraw Adapter 1.1</p>
	 
</body>
</html>


<!-- Java functions -->
<%!
	public String getElementName(URI uri){
		String[] names = uri.getPath().split("::");
	    String last_name = names[names.length - 1]; 	    	
	    return last_name; 
	}
%>

<%!
	public String getElementQualifiedName(URI uri){
		String[] names = uri.getPath().split("/");
	    String last_name = names[names.length - 1]; 	    	
	    return last_name; 
	}
%>