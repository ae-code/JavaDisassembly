$(function() {
	var expandables = $(".expandable");
	
	expandables.each(function (i,e) {
		var t = $(e);
		if (t.find("span").text() === "+")
			t.next().toggle();
	});
	
	expandables.click(function() {
		var that = $(this);
		that.next().toggle();
		var span = that.find("span");
		if (span.text() === "-")
			span.text("+");
		else
			span.text("-");
	});
});