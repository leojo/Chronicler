$(function() {

	$.fn.extend({
		// Post: Makes array of span elements editable.
		makeSpellOffer: function () {
			$(this).each(function () {
				var $spellSlot = $(this);
				var $spellSlotInput = $spellSlot.find(".spellSlotInput");
				console.log($spellSlot);
				console.log($spellSlotInput);
				var $slotLevel = $spellSlot.parent().parent().attr('class').slice(-1);
				var $spellOffer = $($('.spellOffer'+$slotLevel)[0]);
				var $spells = $spellOffer.find('.offerSpell');
				$spellOffer.hide();
				// Submit the edited values for the spans:
				var submitChanges = function () {
					console.log("What is the spellSlotInput here?");
					console.log($spellSlotInput.val());
					if ($spellSlotInput.val() !== '') {
						console.log("submitting form!");
						$('#spellForm').ajaxSubmit({url: 'updateSpellslot', type: 'post'});
						//$(document).unbind('click', submitChanges);
					}
				};
				$spellOffer.click(function (event) {
					event.stopPropagation();
				});

				// Hide spans and show editable fields in their place
				$spellSlot.click(function (e) {
					console.log("click detected!");
					$spellOffer.show();

					$spells.click(function() {
						console.log("a spell was clicked!");
						$spellID = $(this).attr('spell_id');
						console.log("spells");
						console.log(this);
						console.log("this is the spell ID "+$spellID);
						console.log("and this is the spell slot input");
						$spellSlotInput.val($spellID);
						console.log($spellSlotInput);
						console.log("value of spellslotinput "+$spellSlotInput.val());
						$spellOffer.hide();
						submitChanges();
					});
				});
			});
			return this;
		}
	});
 	$('.spellSlot').makeSpellOffer();
});


