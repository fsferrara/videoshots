<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0"
xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:template match="ricerca">
<html>
	<head>
		<title>Ricerca</title>
	</head>
	<body>
		
		<xsl:call-template name="menu"/>
		<h3>Selezionare i parametri di ricerca:</h3>
		
		<form method="post" action="Controller?pag=findVideo">					
			<table>
				<tr>
					<td>Autore:</td>
					<td>
						<select name="aut">
							<option value=""></option> 
							<xsl:for-each select="menuAutori/autore">
								<option value="{text()}"><xsl:value-of select="text()"></xsl:value-of></option>
							</xsl:for-each>
						</select>
					</td>
				</tr>
				<tr>
					<td>Genere:</td>
					<td>
						<select name="genre">
							<option value=""></option> 
							<xsl:for-each select="menuGeneri/genere">
								<option value="{@codice}"><xsl:value-of select="text()"></xsl:value-of></option>
							</xsl:for-each>
						</select>
					</td>
				</tr>
				<tr>
					<td>Location:</td>
					<td>
						<select name="loc">
							<option value=""></option> 
							<xsl:for-each select="menuLocation/location">
								<option value="{text()}"><xsl:value-of select="text()"></xsl:value-of></option>
							</xsl:for-each>
						</select>
					</td>
				</tr>			
			</table>
			<input type="submit" value="Cerca"/>
			
		</form>
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

<xsl:include href="menu.xsl"/>
<xsl:include href="footer.xsl"/>
</xsl:stylesheet>
