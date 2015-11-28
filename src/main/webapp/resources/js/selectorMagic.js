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


    $('.nameSelect').keyup(function() {
        if($(this).val() !== "") {
            $('.createCharBtn').val('Make '+$(this).val());
            $('.createCharBtn').prop('disabled', false);
        } else {
            $('.createCharBtn').val('Name Your Character');
            $('.createCharBtn').prop('disabled', true);
        }
    })
})

