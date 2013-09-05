<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	
	
<xsl:template match="listashots">
<html>
	<head>
		<title>Risultati Ricerca Shots</title>
	</head>
	<body>
		<xsl:call-template name="menu"/>
		
		<table>
		<xsl:for-each select="shot">
			<tr>
				<!-- PER INSERIRE E COMMERCIALI BISOGNA USARE &amp; -->
				<td><a href="Controller?pag=showVideo&amp;idVideo={id/text()}&amp;idShot={idShot/text()}&amp;inizio={mediaTimePoint/text()}&amp;durata={mediaDuration/text()}"><img WIDTH="120" HEIGHT="100" SRC="{immagine/@uri}"/> </a>
				</td>
				
				<td>
					<p><a href="Controller?pag=showVideo&amp;idVideo={id/text()}&amp;idShot={idShot/text()}&amp;inizio={mediaTimePoint/text()}&amp;durata={mediaDuration/text()}"><h3><xsl:value-of select="titolo"/></h3></a>
					Autore: <xsl:value-of select="autore"/><br/>
					Descrizione: <xsl:value-of select="freeTextAnnotation"/><br/>
					Inizio: <xsl:value-of select="hrTimePoint"/><br/>
					Durata: <xsl:value-of select="hrDuration"/><br/>
					</p>
				</td>
			</tr>
		</xsl:for-each>
		
		</table>
		
		<xsl:call-template name="footer"/>
	</body>
</html>

</xsl:template>

<xsl:template match="noShots">
<html>
	<head>
		<title>Risultati Ricerca Shots</title>
	</head>
	<body>
		<xsl:call-template name="menu"/>
		<div align="center" style="height: 100%; width:100%">
		La ricerca non ha prodotto risultati.<br/>Premere <a href="Controller?pag=searchShots">qui</a> per effettuare una nuova ricerca
		</div>
		<xsl:call-template name="footer"/>
	</body>
</html>
</xsl:template>

<xsl:include href="menu.xsl"/>
<xsl:include href="footer.xsl"/>
	
</xsl:stylesheet>