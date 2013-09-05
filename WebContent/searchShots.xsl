<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0"
xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:template match="ricercaTag">
<html>
	<head>
		<title>Ricerca Tag</title>
	</head>
	<body>
		
		<xsl:call-template name="menu"/>
		<h3>Inserire una breve descrizione:</h3>
		
		<form method="post" action="Controller?pag=findShots">					
			<textarea name="descrizione" rows="5" col="30"/>
			<br/>
			<input type="submit" value="Cerca"/>			
		</form>
		<xsl:call-template name="footer"/>
	</body>
</html>

</xsl:template>

<xsl:include href="menu.xsl"/>
<xsl:include href="footer.xsl"/>
</xsl:stylesheet>
