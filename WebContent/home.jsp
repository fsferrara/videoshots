<?xml version="1.0" encoding="ISO-8859-1" ?>
<%@ page language="java" contentType="text/xml; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<?xml-stylesheet type="text/xsl" href="home.xsl"?>
<%@ page import="gestionexml.*" %>


<%@page import="java.util.ArrayList"%>

<% ArrayList<VideoFile> videos=(ArrayList<VideoFile>) request.getAttribute("videos");
	if (videos.size()==0){
		if (request.getParameter("pag").equals("home")){
%>
<noVideoHome/>
<%		}else{ %>
<noVideoFind/>
<%		}
	}else{	
%>
<listavideo>
<%
   		for (VideoFile vid:videos){ %>
	<video>
		<id><%=vid.getVideoId()%></id>
		<titolo><%=vid.getVideoTitle()%></titolo>
		<autore><%=vid.getVideoAuthor()%></autore>
		<immagine uri="<%="file/video/"+vid.getImageFileName()%>"/>		
	</video>
<% 		} %>
</listavideo>
<%	}%>
