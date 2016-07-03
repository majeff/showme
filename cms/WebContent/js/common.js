/**
 * @param myform
 * @param e
 * @returns {Boolean}
 */
function submitEnter(myform, e) {
	var keycode;
	if (window.event)
		keycode = window.event.keyCode;
	else if (e)
		keycode = e.which;
	else
		return true;
	if (keycode == 13) {
		myform.submit();
		return false;
	} else
		return true;
}

function show_menuB(numB) {
	for (var j = 10; j < 20; j++) {
		if ($('#Bli0' + j)) {
			if (j != numB) {
				$('#Bli0' + j).hide();
				$('#Bf0' + j).css('background', 'url(/images/01.gif)');
			} else {
				if ($('#Bli0' + numB)) {
					if ($('#Bli0' + numB).css('display') == 'block') {
						$('#Bli0' + numB).hide();
						$('#Bf0' + numB).css('background', 'url(/images/01.gif)');
					} else {
						$('#Bli0' + numB).show();
						$('#Bf0' + numB).css('background', 'url(/images/02.gif)');
					}
				}
			}
		} else {
			break;
		}
	}
}

function go_func(mainFunc, secFunc, url) {
	$.cookie('menu.mainFunc', mainFunc);
	$.cookie('menu.secFunc', secFunc);
	//$("#mainFunc").html(mainFunc);
	//$("#secFunc").html(secFunc);
	document.location=url+'?mainFunc='+mainFunc+"&secFunc="+secFunc;
}

function show_menuC() {
	if ($.cookie('menu.hidden') == 'false') {
		$.cookie('menu.hidden', 'true');
		$("#LeftBox").hide();
		$("#RightBox").css('marginLeft', '0');
		$("#Mobile").css('background', 'url(/images/center.gif)');
	} else {
		$.cookie('menu.hidden', 'false');
		$("#LeftBox").show();
		$("#RightBox").css('marginLeft', '222px');
		$("#Mobile").css('background', 'url(/images/center0.gif)');
	}
}