<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%
	String creatorUri = (String) request.getAttribute("creatorUri");
	String deleteUri = (String) request.getAttribute("creatorUri") + "/delete";
	String deleteAllUri = (String) request.getAttribute("creatorUri") + "/deleteall";
	String publishUri = (String) request.getAttribute("creatorUri") + "/publish";
%>

<!DOCTYPE HTML>

<html>
<head>

<meta http-equiv="Content-Type" content="text/html;charset=utf-8">
<title>MagicDraw OSLC Adapter: Subversion File Selector</title>
<link rel="stylesheet" type="text/css"
	href="<%=request.getContextPath()%>/css/simple.css">
<link href='http://fonts.googleapis.com/css?family=Open+Sans:400,700'
	rel='stylesheet' type='text/css'>
<link rel="shortcut icon"
	href="<%=request.getContextPath()%>/images/100px_white-oslc-favicon.ico">


</head>
<body>

	<!-- header -->
	<p id="title">MagicDraw OSLC Adapter: Published Subversion Files</p>

	<div id="main-body">

		<c:set var="svnurlssize" scope="session" value="${svnurls.size()}" />
		


		<!-- oslc logo and adapter details -->
		<a id="oslclogo" href="http://open-services.net/" target="_blank"><img
			src="<%=request.getContextPath()%>/images/oslcLg.png"></a>
		<div id="adapter-details">
			<p class="word-break">
				<br> Adapter Publisher: <a class="notfancy"
					href="http://www.mbsec.gatech.edu/research/oslc" target="_blank">Georgia
					Institute of Technology OSLC Tools Project</a><br> Adapter
				Identity: org.eclipse.lyo.adapter.magicdraw
			</p>
		</div>
		<br>

		<!-- resource type and name -->
		<h1>
			<span id="metainfo">Published Subversion Files </span>MagicDraw
		</h1>
		<br>

		<p>
			<span id="metainfo">Published Subversion Files</span>
		</p>

		<table>

			<form name="d2" action=<%=creatorUri%> method="POST">

				<tr>
					<td><input id="svnfileeditor" type="text" name="svnurl" id="title"
						placeholder="Enter New SVN File URL to publish here" /></td>


					<td><input type="submit" value="Add" /></td>

				</tr>
			</form>

			<c:forEach var="svnurl" items="${svnurls}">



			<tr></tr>

				<tr>
					<td><a href=${svnurl } target="_blank">${svnurl}</a></td>


					<td>
						<form name="d1" action=<%=deleteUri%> method="POST">
							<input type="hidden" name="svnurl" value="${svnurl}"> <input
								id="delete1" type="submit" name="Delete" value="Delete">
						</form>
					</td>

				</tr>
			</c:forEach>
		</table>



		<p>	
		
			<c:if test="${svnurlssize == null}">
   				<c:out value="0" />
			</c:if>
						
			<c:out value="${svnurlssize}" />
			published Subversion files
		<p>
		
		
		<h1>
			<form name="d1" action=<%=deleteAllUri%> method="POST">

				<input id="deleteAll" type="submit" name="DeleteAll"
					value="Delete All">
			</form>
		</h1>

		<h1>
			<form name="p1" action=<%=publishUri%> method="POST">
				<input id="publish1" type="submit" name="Publish" value="Publish">
			</form>
		</h1>

	</div>

	<!-- footer -->
	<p id="footer">OSLC MagicDraw Adapter 1.1</p>
</body>
</html>