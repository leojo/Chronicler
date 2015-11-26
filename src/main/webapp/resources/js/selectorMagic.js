$(function() {

    $('.classFluff').html($('.classSelect').val().toUpperCase());
    $('.raceFluff').html($('.raceSelect').val().toUpperCase());
    $('.createCharBtn').prop('disabled', true);

    $('.classSelect').change(function() {
        $('.classFluff').html($(this).val().toUpperCase());
    });

    $('.raceSelect').change(function() {
        $('.raceFluff').html($(this).val().toUpperCase());
    });

    $('.nameSelect').keyUp(function() {
        $('.nameSelect').change(function() {
                console.log("CHANGED!");
                console.log($(this).val());
                $('.createCharBtn').val('Make '+$(this).val());
                $('.createCharBtn').prop('disabled', false);
            });
    });

})

