// Source: http://www.w3schools.com/ajax/
function doAddCart(item, path, object)
{
	var $this = object;
	var xmlhttp;
	if (window.XMLHttpRequest)
	{// code for IE7+, Firefox, Chrome, Opera, Safari
		xmlhttp=new XMLHttpRequest();
	}
	else
	{// code for IE6, IE5
		xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
	}
	xmlhttp.onreadystatechange = function(){
		if (xmlhttp.readyState==4 && xmlhttp.status==200)
		{
			console.log("Ajax successful! Added = " + item);
			console.log("Response:\n" + xmlhttp.responseText);
			console.log($this);
			
		}
	}
	
	xmlhttp.open("POST", path + "/e/Cart?add=" + item,true);
	xmlhttp.send();
	
	
}