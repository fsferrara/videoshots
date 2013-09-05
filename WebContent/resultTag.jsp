<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<!-- <SCRIPT LANGUAGE="JavaScript"><!--
setTimeout('self.close()',2000);
//--></SCRIPT> -->

<head>

<title>Stato operazione</title>
</head>
<body>
<%String messaggio=(String)request.getAttribute("result");
  String idVideo=(String)request.getAttribute("idVideo");	%>

<h1><%=messaggio %></h1>


<a href="Controller?pag=tag&amp;idVideo=<%=idVideo %>">Crea un nuovo shot</a> <br/>
<a href="Controller?pag=showVideo&amp;idVideo=<%=idVideo %>">Torna alla visualizzazione del video</a> 



</body>
</html>