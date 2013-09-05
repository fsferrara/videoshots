<?xml version="1.0" encoding="ISO-8859-1" ?>
<%@ page language="java" contentType="text/xml; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<?xml-stylesheet type="text/xsl" href="tag.xsl"?>

<%@ page import="gestionexml.*,java.util.*"%> 

<% VideoFile video=(VideoFile) request.getAttribute("video");
   //ArrayList<TemporalDecomposition> shots=video.getTemporalDecomposition();
%>


<tag>
	<titolo><%=video.getVideoTitle()%></titolo>
	<nomeVideo><%=video.getVideoFileName()%></nomeVideo>
	<nomeFile><%=video.getVideoFileName()%></nomeFile>
	<inizio><%=request.getParameter("inizio")%></inizio>
	<durata><%=request.getParameter("durata")%></durata>
	<id><%=video.getVideoId()%></id>
<!-- Qui ci andrebbero le informazioni dell'albero dei tag -->
<!-- 	<shots>
		<shot id="1" descrizione="asd">
			<shot id="2" descrizione="asd2"></shot>
			<shot id="pippo" descrizione="asd2">
				<shot id="ahah" descrizione="asd2"></shot>
			</shot>
		</shot>
		<shot id="3" descrizione="asd3"></shot>
	</shots>
 -->
<%=request.getAttribute("xmlShots")%>

</tag>