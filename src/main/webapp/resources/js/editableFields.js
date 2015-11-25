// Used to make certain span elements of the character sheet editable on double click.
// Based on JQuery example.

/*
update = function() {
    $.ajax({
    type:'POST',
    url: "/character"
    });

    console.log("ajax should have updated via the controller");
    }*/

    $(function() {

$.fn.extend({
    // Post: Makes array of span elements editable.
	makeEditable: function () {
		$(this).each(function () {
			var $spans = $(this),

			// Create edit field input in place of span
			/*$editField = ($spans.hasClass('numberField') ?
						$('<input type="number"></input>').addClass($spans.attr('class'))
						: $('<input type="text"></input>').addClass($spans.attr('class'))),*/
			$editField = $($spans.parent().find('.spanInput')[0]),

			// Submit the edited values for the spans:
			submitChanges = function () {
				if ($editField.val() !== '') {
					$('#sheetForm').ajaxSubmit({url: 'updateCharacter', type: 'post'});
					$spans.html($editField.val());
					$spans.show();
					$spans.trigger('editsubmit', [$spans.html()]);
					$(document).unbind('click', submitChanges);
					$editField.hide();
					console.log("DO WE HAVE A NAME");
					console.log($spans.html());

				}
			},
			tempVal;
			$editField.click(function (event) {
				event.stopPropagation();
			});

            // Hide spans and show editable fields in their place
			$spans.dblclick(function (e) {
			    console.log("double click detected!");
				tempVal = $spans.html();
				$editField.val(tempVal).show().css('display', 'block')   //$editField.val(tempVal).insertBefore(this)
                .bind('keypress', function (e) {
					var code = (e.keyCode ? e.keyCode : e.which);
					if (code == 13) {
						submitChanges();
					}
				}).select();
				$spans.hide();
				$(document).click(submitChanges);
			});
		});
		return this;
	}
});

 $('.editable-span').makeEditable();



});


