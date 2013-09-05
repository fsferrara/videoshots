<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="refresh" content="5;url=Controller?pag=home">
<title>Stato operazione</title>
</head>
<body>
<%String messaggio=(String)request.getAttribute("result"); %>

<h1><%=messaggio %></h1>
</body>
</html>