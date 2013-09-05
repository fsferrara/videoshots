<?xml version="1.0" encoding="ISO-8859-1" ?>
<%@ page language="java" contentType="text/xml; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<?xml-stylesheet type="text/xsl" href="search.xsl"?>

<% String aut=(String) request.getAttribute("authors");
   String genre=(String) request.getAttribute("genres");
   String loc=(String) request.getAttribute("locations");
   
   if(!aut.equals("<menuAutori />")||!genre.equals("<menuGeneri />")||!loc.equals("<menuLocation />")){
	   
%>
<ricerca>

<%=aut%>			
<%=genre%>	
<%=loc%>
	
</ricerca>
<% }else{ %>

<noVideoHome/>

<% } %>