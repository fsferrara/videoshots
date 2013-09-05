<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/xml; charset=utf-8"
    pageEncoding="UTF-8"%>
<?xml-stylesheet type="text/xsl" href="showVideo.xsl"?>

<%@ page import="gestionexml.*,java.util.*"%> 

<% VideoFile video=(VideoFile) request.getAttribute("video");
   TemporalDecomposition td=(TemporalDecomposition) request.getAttribute("shot");
%>
<video>
	<id><%=video.getVideoId()%></id>
	<titolo><%=video.getVideoTitle()%></titolo>
	<autore><%=video.getVideoAuthor()%></autore>
	<nomeFile><%=video.getVideoFileName()%></nomeFile>
	<descrizione><%=video.getVideoFreeTextAnnotation()%></descrizione>
	<inizio><%=request.getParameter("inizio")%></inizio>
	<durata><%=request.getParameter("durata")%></durata>
	<%=request.getAttribute("xmlShots")%>

<% if (td!=null){ %>
	<shotShow>
		<descrizione><%=td.getFreeTextAnnotation()%></descrizione>
		<hrTimePoint><%=TemporalDecomposition.hrMediaTimePoint(td.getMediaTimePoint())%></hrTimePoint>
		<hrDuration><%=TemporalDecomposition.hrMediaDuration(td.getMediaDuration())%></hrDuration>
	</shotShow>
<% }%>	
	<videoCorrelati>
<% ArrayList<VideoFile> related=video.getRelatedVideoFile(); 
   for (VideoFile rel:related){
%>

		<videoCorrelato>
			<id><%=rel.getVideoId()%></id>
			<titolo><%=rel.getVideoTitle()%></titolo>
			<autore><%=rel.getVideoAuthor()%></autore>
			<immagine uri="<%="file/video/"+rel.getImageFileName()%>"/>
		</videoCorrelato>
<% }
%>
	</videoCorrelati>
</video>