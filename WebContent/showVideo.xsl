<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0"
xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:template match="video">
<html>
	<head>
		<title>Visualizzazione</title>
	</head>
	<body>

	
		<xsl:call-template name="menu"/>
		<table border="0" style="width: 100%">
			<tr>
				<td style="vertical-align: top">
					<table border="0" style="vertical-align: top">
						<tr style="vertical-align: top"><td><h2><xsl:value-of select="titolo"/></h2></td></tr>
						<tr>
							<td><h5><xsl:value-of select="autore"/></h5></td>
							<td></td>
						</tr>
						<tr><td>
							<applet code="JMF.TypicalPlayerApplet" width="320" height="300">
								<param name="file" value="file/video/{nomeFile/text()}"></param>
								<param name="inizio" value="{inizio/text()}"></param>
								<param name="durata" value="{durata/text()}"></param>
						
									ERRORE.
							</applet>
						 	
						</td></tr>
						<xsl:apply-templates select="shotShow"/>
						
						<tr><td><p><b>Descrizione video:</b><br/><xsl:value-of select="descrizione"/></p></td></tr>
						<tr><h3><a href="Controller?pag=tag&amp;idVideo={id/text()}" >Crea uno shot per questo video</a></h3>
						</tr>
						
					</table>
					
					
					
				</td>
	
				<td valign="top">
				
					<table border="1" align="right" style="font-size:x-small; width:380px;">
					<tr style="text-align:center"><td colspan="2"><h2>Video Correlati</h2></td></tr>
					<div align="right" style="float:right; overflow-y: scroll; height: 450px; width:400px">
					<table border="1" align="right" style="font-size:x-small; width:380px;">
					<xsl:for-each select="videoCorrelati/videoCorrelato">
						<tr>
							<td><a href="Controller?pag=showVideo&amp;idVideo={id/text()}"><img WIDTH="80" HEIGHT="60" SRC="{immagine/@uri}"/> </a>
							</td>
							<td>
								<h3><a href="Controller?pag=showVideo&amp;idVideo={id/text()}"><xsl:value-of select="titolo"/></a></h3>
								Autore: <xsl:value-of select="autore"/>
							</td>
						</tr>
					</xsl:for-each>
					</table>
					</div>
					</table>
					
					<table border="1" align="right" style="font-size:x-small; width:380px;">
					<tr style="text-align:center"><td colspan="2"><h2>Shot Correlati</h2></td></tr>
					<div align="right" style="float:right; overflow-y: scroll; height: 200px; width:400px">
					<table border="1" align="right" style="font-size:x-small; width:380px;">
					<xsl:apply-templates select="shots/shot"></xsl:apply-templates>
					</table>
					</div>
					</table>
					
				</td>
				
			</tr>
			
		</table>
		<xsl:call-template name="footer"/>
	</body>
</html>



</xsl:template>

<!-- TEMPLATE PER LA VISUALIZZAZIONE DEGLI SHOT CORRELATI -->
<xsl:template match="shot"> 
	<li>
		<p style="text-align:left" >
		<p><a href="Controller?pag=showVideo&amp;idVideo={ancestor::video/id/text()}&amp;idShot={@id}&amp;inizio={mediaTimePoint/text()}&amp;durata={mediaDuration/text()}"><h3><xsl:value-of select="@id"/></h3></a></p>
			<b>Descrizione: </b><xsl:value-of select="@descrizione"></xsl:value-of><br/>
			<b>Inizio: </b><xsl:value-of select="hrTimePoint"/><br/>
			<b>Durata: </b><xsl:value-of select="hrDuration"/><br/>
		</p>
		<ul>
			<xsl:apply-templates select="shot"></xsl:apply-templates>
		</ul>
	</li>
</xsl:template>


<!-- TEMPLATE PER LA VISUALIZZAZIONE DELLE INFO DELLO SHOT CORRENTEMENTE VISUALIZZATO -->
<xsl:template match="shotShow">
	
	<b>Descrizione shot:</b><xsl:value-of select="descrizione"/><br/>
	<b>Inizio shot: </b><xsl:value-of select="hrTimePoint"/><br/>
	<b>Durata shot:</b> <xsl:value-of select="hrDuration"/><br/>
</xsl:template>

<xsl:include href="menu.xsl"/>
<xsl:include href="footer.xsl"/>
</xsl:stylesheet>
