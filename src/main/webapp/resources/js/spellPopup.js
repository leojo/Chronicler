$(function() {

	$.fn.extend({
		// Post: Makes array of span elements editable.
		makeSpellOffer: function () {

			// Make sure no spell offer is open
			$('.spellOffer').hide();
			var spellOfferOpen = false;
			$(this).each(function () {
				var $spellSlot = $(this);
				var $resetBtn = $spellSlot.find('.spellResetBtn');
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
					if(!spellOfferOpen) {
						$spellOffer.show();
						spellOfferOpen = true;
						$spells.click(function() {
							$spellID = $(this).attr('spell_id');
							console.log("does spell ID look like a jquery object");

							$spellSlotInput.val($spellID);
							$spellSlot.find('span').replaceWith($(this).html());
							$spellSlot.removeClass('unprepared');
							$spellSlot.makeSpendable();
							$('.spellOffer').hide();
							spellOfferOpen  = false;
							submitChanges();
						});
					} else return;

				});
			});
			return this;
		},

		makeSpendable: function() {
			$(this).each(function() {
				var $spellSlot = $(this);
				var $spellSlotStatus = $spellSlot.find('.spellSlotStatus');
				console.log($spellSlotStatus);
				var submitSpend = function () {
					if ($spellSlotStatus.val() !== '') {
						console.log("submitting form!");
						$('#sheetForm').ajaxSubmit({url: 'updateSpellslot', type: 'post'});
						$spellSlot.unbind('click');

					}
				};

				$spellSlot.click(function() {
					$(this).removeClass('available');
					$(this).addClass('spent');
					console.log("This is the spellSlotStatus we're about to change");
					console.log($spellSlotStatus);
					console.log("Value before"+$spellSlotStatus.val());
					$spellSlotStatus.val('spent');
					console.log("Value after"+$spellSlotStatus.val());
					console.log($spellSlotStatus)
					submitSpend();
				});
			});

		},

		makeResetable: function() {
			$(this).each(function() {
				var $spellSlot = $(this);
				$spellSlot.find('.spellResetBtn').click(function(e) {
					console.log("HITTING RESET BUTTON");
					e.stopPropagation();
					console.log("this is the spellslot");
					console.log($spellSlot);
					console.log("and its html");
					console.log($spellSlot.html());
					$spellSlot.find('span').replaceWith('<span class="content unprepped"><p class="spellName">Unprepared spell</p><p class="spellDescr">Click to select a spell</p></span>');
					console.log($spellSlot.html());
					$spellSlot.addClass('unprepared');
					$spellSlot.makeSpellOffer();
				});
			});
		},

		resetSpells: function() {
			$spellSlot.removeClass('spent');
			$spellSlot.addClass('available');
		}
	});
	//$('.available').parent().parent().addClass('available');
 	$('.unprepped').parent().parent().makeSpellOffer();
	$('.available').makeSpendable();
	$('.spellSlot').makeResetable();


});


