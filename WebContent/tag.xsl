<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:template match="tag">
<html>
	<head>
		<title>Crea Shot</title>
	</head>
	<body>
		<xsl:call-template name="menu"/>
		
		<form method="post" action="Controller?pag=doTag">
			<input type="hidden" name="idVideo" value="{id/text()}"/>
			<h3 style="text-align:center">Inserisci nuovo shot per <a href="Controller?pag=showVideo&amp;idVideo={id/text()}"><xsl:value-of select="titolo"/></a></h3>

						
			<table style="width:100%">
			
			<tr><td>
							<applet code="JMF.TypicalPlayerApplet" width="320" height="300">
								<param name="file" value="file/video/{nomeFile/text()}"></param>
								<param name="inizio" value="{inizio/text()}"></param>
								<param name="durata" value="{durata/text()}"></param>
						
									ERRORE.
							</applet>
							
			</td>
			
			<td><b>Inizio</b><br/>
				<input type="text" name="ore" value="0"/> ore <br/>
				<input type="text" name="min" value="0"/> minuti <br/>
				<input type="text" name="sec" value="0"/> secondi <br/>
				<br/>
				<b>Durata</b>(in secondi)<br/>
				<input type="text" name="durata" value="0"/></td>
				<td align="left;"><b>Descrizione</b><br/><textarea name="descrizione" rows="8" col="30"/></td></tr>
			
			
			
			 
			</table>
			
			<br/>
			<b>Selezionare il tag padre</b> [opzionale]
		
			<table style="width:100%" border="1"><tr><td>
				<input type="radio" name="parent" value="" checked="checked" />Nessun Padre
				<ul>
					<xsl:apply-templates select="shots/shot"></xsl:apply-templates>
				</ul>
			</td></tr></table>
			
			
			
			<input type="hidden" name="nomeVideo" value="{nomeVideo/text()}"/>
			<p style="text-align:center">
			<input type="submit" value="Tagga"/>
			</p>
			
		</form>
		
		<xsl:call-template name="footer"/>
	</body>
</html>
</xsl:template>

<xsl:template match="shot">
	<li>
		<p style="text-align:left" >
		<input type="radio" name="parent" value="{@id}" /> <b><xsl:value-of select="@id"></xsl:value-of></b><br/>
			<b>Descrizione: </b><xsl:value-of select="@descrizione"></xsl:value-of><br/>
			<b>Inizio: </b><xsl:value-of select="hrTimePoint"/><br/>
			<b>Durata: </b><xsl:value-of select="hrDuration"/><br/>
		</p>
		<ul>
			<xsl:apply-templates select="shot"></xsl:apply-templates>
		</ul>
	</li>
</xsl:template>

<xsl:include href="menu.xsl"/>
<xsl:include href="footer.xsl"/>

</xsl:stylesheet>