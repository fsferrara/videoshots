<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	
	
<xsl:template match="listavideo">
<html>
	<head>
		<title>VideoShots</title>
	</head>
	<body>
		<xsl:call-template name="menu"/>
		
		<table align="left">
		<xsl:for-each select="video">
			<tr>
				<!-- PER INSERIRE E COMMERCIALI BISOGNA USARE &amp; -->
				<td><a href="Controller?pag=showVideo&amp;idVideo={id/text()}"><img WIDTH="150" HEIGHT="120" SRC="{immagine/@uri}"/> </a>
				</td>
				
				<td>
					<p><a href="Controller?pag=showVideo&amp;idVideo={id/text()}"><h2><xsl:value-of select="titolo"/></h2></a>
					Autore: <xsl:value-of select="autore"/><br/></p>
				</td>
			</tr>
		</xsl:for-each>
		
		</table>
		
		<xsl:call-template name="footer"/>
	</body>
</html>

</xsl:template>

	
<xsl:template match="noVideoHome">
<html>
	<head>
		<title>VideoShots</title>
	</head>
	<body>
		<xsl:call-template name="menu"/>
		<div align="center" style="height: 100%; width:100%">
		Non ci sono video caricati.<br/>Per caricare un video clicca <a href="Controller?pag=upload">qui</a>
		</div>
		<xsl:call-template name="footer"/>
	</body>
</html>
</xsl:template>

<xsl:template match="noVideoFind">
<html>
	<head>
		<title>VideoShots</title>
	</head>
	<body>
		<xsl:call-template name="menu"/>
		<div align="center" style="height: 100%; width:100%">
		La ricerca non ha prodotto risultati.<br/>Premere <a href="Controller?pag=search">qui</a> per effettuare una nuova ricerca
		</div>
		<xsl:call-template name="footer"/>
	</body>
</html>
</xsl:template>

<xsl:include href="menu.xsl"/>
<xsl:include href="footer.xsl"/>
	
</xsl:stylesheet>