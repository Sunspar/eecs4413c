<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:template match="/">
		<html>
		<head> 
			<title>eFoods Purchase Order</title>
		</head>
		<body>
			<xsl:apply-templates />
		</body>
		</html>
	</xsl:template>
	
	<xsl:template match="order">
		Order Number: <xsl:value-of select="@id" /> <br />
		Order Date: <xsl:value-of select="@submitted" /> <br />
		
		
		Subtotal: <xsl:value-of select="./total" /> <br />
		Shipping: <xsl:value-of select="./shipping" /><br />
		HST: <xsl:value-of select="./HST" /><br />
		
		<br />
		Final Total: <xsl:value-of select="./grandTotal" /><br />
		<xsl:apply-templates />
	 </xsl:template>
	 
	 <xsl:template match="customer">
	 	Customer Name: <xsl:value-of select="./name" /> <br />
		Customer Account: <xsl:value-of select="@account" /> <br />
	 </xsl:template>
	 
	 
	 <xsl:template match="items">
	 	<table>
		 	<tr>
		 		<td>Item Number</td>
		 		<td>Item Name</td>
		 		<td>Quantity</td>
		 		<td>Unit Price</td>
		 		<td>Cost</td>
		 	</tr>
		 	<xsl:apply-templates />
		</table>
	 </xsl:template>
	 
	 <xsl:template match="item">
	 	<tr>
	 		<td><xsl:value-of select="@number" /></td>
	 		<td><xsl:value-of select="./name" /></td>
	 		<td><xsl:value-of select="./quantity" /></td>
	 		<td><xsl:value-of select="./price" /></td>
	 		<td><xsl:value-of select="./extended" /></td>
	 	</tr>
	 </xsl:template>
 </xsl:stylesheet>