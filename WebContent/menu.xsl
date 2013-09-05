<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:template name="menu">
	
		<style type="text/css">
			body { font-family:Fantasy, "Comic Sans MS"}
			a img{ border:0; }
			a:link { color:navy }
			a:visited { color:navy }
			a:hover { color:navy }
			a:active { color:navy } 
		</style>
	
		<div id="header" >
		<div id="menu" style="float:left;">
		
			<img src="grafica/logo.png"/>
		</div>
		
		<div style="float:right; color:white;">
			<a href="Controller?pag=home">
				<img src="grafica/home.png"/>
			</a>
			.		
			<a href="Controller?pag=upload">
				<img src="grafica/upload.png"/>
			</a>
			.
			<a href="Controller?pag=search">
				<img src="grafica/ricerca.png"/>
			</a>
			.
			<a href="Controller?pag=searchShots">
				<img src="grafica/shots.png"/>
			</a>
			.
			<a href="documentazione/javadoc/index.html">
				<img src="grafica/javadoc.png"/>
			</a>
		</div>
	</div>
	<div style="clear:both;"><hr style="height:3px;border-width:5;color:red;background-color:black" /></div>
			
		
	</xsl:template>
</xsl:stylesheet>