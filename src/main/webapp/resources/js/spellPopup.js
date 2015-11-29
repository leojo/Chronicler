$(function() {

	$.fn.extend({
		// Allows user to open up spell offer by clicking on spellslot.
		// This is unbound from the spell slot once it has filled again.
		makeSpellOffer: function () {

			// Make sure no spell offer is open
			$('.spellOffer').hide();
			var spellOfferOpen = false;

			$(this).each(function () {
				var $spellSlot = $(this);
				$spellSlot.addClass('unprepared');
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

				// Hide spans and show editable fields in their place
				$spellSlot.click(function (e) {
					e.stopPropagation();
					if(!spellOfferOpen) {


						$spellOffer.show();

						console.log("spell offer offset");
						console.log($spellOffer.offset());
						$spellOffer.offset({top: 0, left: 0});
						console.log("spell offer offset 0");
						console.log($spellOffer.offset());
						console.log("spell SLOT offset");
						console.log($spellSlot.offset());
						$spellOffer.offset($spellSlot.offset());
						console.log("spell offer offset 1");
                        console.log($spellOffer.offset());

						spellOfferOpen = true;
						$spells.click(function(evt) {
							evt.stopPropagation();
							$spellID = $(this).attr('spell_id');
							console.log("does spell ID look like a jquery object");

							$spellSlotInput.val($spellID);
							$spellSlot.find('span').replaceWith($(this).html());
							$spellSlot.removeClass('unprepared');
							$spellSlot.makeSpendable();
							$('.spellOffer').hide();
							spellOfferOpen  = false;
							$spellSlot.unbind('click');
							$spellSlot.makeSpendable();
							submitChanges();
						});
					} else return;

				});
			});
			return this;
		},

		// Allows user to use up spell by clicking on it.
		// Becomes despendable again when the user has clicked.
		makeSpendable: function() {
			$(this).each(function() {
				var $spellSlot = $(this);
				var $spellSlotStatus = $spellSlot.find('.spellSlotStatus');
				console.log($spellSlotStatus);
				var submitSpend = function () {
					if ($spellSlotStatus.val() !== '') {
						$('#sheetForm').ajaxSubmit({url: 'updateSpellslot', type: 'post'});
						$spellSlot.unbind('click');

					}
				};

				$spellSlot.click(function() {
					$(this).removeClass('available');
					$(this).addClass('spent');
					$spellSlotStatus.val('spent');
					submitSpend();
					$spellSlot.unbind('click');
				});
			});

		},

		makeResetable: function() {
			$(this).each(function() {
				var $spellSlot = $(this);
				$spellSlot.find('.spellResetBtn').click(function(e) {
					e.stopPropagation();
					$spellSlot.find('span').replaceWith('<span class="content unprepped"><p class="spellName">Unprepared spell</p><p class="spellDescr">Click to select a spell</p></span>');
					$spellSlot.addClass('unprepared');
					$spellSlot.unbind('click');
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
 	$('.unprepped').parent().parent().addClass('unprepared');
 	$('.prepped').parent().parent().addClass('prepared');
 	$('.unprepared').makeSpellOffer();
	$('.prepared').makeSpendable();
	$('.spellSlot').makeResetable();


});


