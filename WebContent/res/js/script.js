/**
 * Fading script to signify item has been added.
 */
function fade(id) {
	var preClass = document.getElementById(id).getAttribute("class");
	document.getElementById(id).classList.add("fadeIn");
	
	setTimeout(function() {
		document.getElementById(id).classList.remove("fadeIn");
	}, 500);
}