<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:template match="upload">
<html>
	<head>
		<title>Upload</title>
	</head>
	<body>
		<xsl:call-template name="menu"/>
		<form enctype="multipart/form-data" method="post" action="Controller?pag=uploadReq">
			<table>
				<tr><td><xsl:value-of select="msgVideo"/></td><td><input type="file" name="fileVideo" size="30"/></td></tr>
				<tr><td><xsl:value-of select="msgJpg"/></td><td><input type="file" name="fileJpg" size="30"/></td></tr> 
				<tr><td><xsl:value-of select="msgNitf"/></td><td><input type="file" name="fileNitf" size="30"/></td></tr>
				<tr><td><input type="submit" value="Upload File"/></td></tr>
			</table>
		</form>
		<xsl:call-template name="footer"/>
	</body>
</html>

</xsl:template>

<xsl:include href="menu.xsl"/>
<xsl:include href="footer.xsl"/>
</xsl:stylesheet>