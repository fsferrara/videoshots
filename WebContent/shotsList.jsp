<?xml version="1.0" encoding="ISO-8859-1" ?>
<%@ page language="java" contentType="text/xml; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<?xml-stylesheet type="text/xsl" href="shotsList.xsl"?>

<%@ page import="gestionexml.*" %>


<%@page import="java.util.*"%>



<%  ArrayList<TemporalDecomposition> tDec=(ArrayList<TemporalDecomposition>) request.getAttribute("shots");
	if(tDec.size()==0){ %>
<noShots/>	
<%	}else{ %>

<listashots>
<%		Map<String, VideoFile> videoMap=(Map<String,VideoFile>) request.getAttribute("videoMap");
		for (TemporalDecomposition td:tDec){ 
	   		VideoFile vid=videoMap.get(td.getVideoId()); %>
   		
	<shot>
		<id><%=vid.getVideoId()%></id>
		<idShot><%=td.getId()%></idShot>
		<titolo><%=vid.getVideoTitle()%></titolo>
		<autore><%=vid.getVideoAuthor()%></autore>
		<immagine uri="<%="file/video/"+vid.getImageFileName()%>"/>	
		<freeTextAnnotation><%=td.getFreeTextAnnotation()%></freeTextAnnotation>
<%			if (td.getKeywordsNumber()>0){ %>		
		<keywords>
		
<%				for(int i=0; i<td.getKeywordsNumber();i++){ %>

			<keyword><%=td.getKeywords(i)%></keyword>

<%				} %>

		</keywords>

<%			} %>
		<mediaTimePoint><%=TemporalDecomposition.parseMediaTimePoint(td.getMediaTimePoint())%></mediaTimePoint>
		<mediaDuration><%=TemporalDecomposition.parseMediaDuration(td.getMediaDuration())%></mediaDuration>
		<hrTimePoint><%=TemporalDecomposition.hrMediaTimePoint(td.getMediaTimePoint())%></hrTimePoint>
		<hrDuration><%=TemporalDecomposition.hrMediaDuration(td.getMediaDuration())%></hrDuration>
	</shot>
<%		} %>
</listashots>
<%	} %>