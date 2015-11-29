$(function() {

	$.fn.extend({
		// Post: Makes array of span elements editable.
		makeSpellOffer: function () {

			// Make sure no spell offer is open
			$('.spellOffer').hide();
			var spellOfferOpen = false;
			$(this).each(function () {
				var $spellSlot = $(this);
				var $spellSlotInput = $spellSlot.find(".spellSlotInput");
				var $slotLevel = $spellSlot.parent().parent().attr('class').slice(-1);
				var $spellOffer = $($('.spellOffer'+$slotLevel)[0]);
				var $spells = $spellOffer.find('.offerSpell');
				// Submit the edited values for the spans:
				var submitChanges = function () {
					if ($spellSlotInput.val() !== '') {
						console.log("submitting form!");
						$('#sheetForm').ajaxSubmit({url: 'updateSpellslot', type: 'post'});
						$spells.unbind('click');

					}
				};
				$spellOffer.click(function (event) {
					event.stopPropagation();
				});

				// Hide spans and show editable fields in their place
				$spellSlot.click(function (e) {

					console.log("click detected!");
					if(!spellOfferOpen) {
						$spellOffer.show();
						spellOfferOpen = true;
						$spells.click(function() {
							$spellID = $(this).attr('spell_id');
							$spellSlotInput.val($spellID);
							$spellSlot.find('span').replaceWith($(this).html());
							$spellSlot.addClass('prepped');
							$('.spellOffer').hide();
							spellOfferOpen  = false;
							submitChanges();
						});
					} else return;

				});
			});
			return this;
		}
	});
 	$('.spellSlot').makeSpellOffer();
});


