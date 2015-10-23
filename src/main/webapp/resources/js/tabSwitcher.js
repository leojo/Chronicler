$(function() {
    $('.tabs').each(function(){
        var $tabIDs = $(this).find('a');
        $selected = $($tabIDs.filter('[href="'+location+'"]')[0] || $tabIDs[0]);
        $content = $($selected[0].hash);

        // Add 'selected' to parent class to update UI.
        $selected.parent().addClass('selected');

        $tabIDs.not($selected).each(function () {
            $(this.hash).hide();
        });

        // On click:
        $(this).on('click', 'a', function(e){
            // Remove 'selected' from old tab and hide content.
            $selected.parent().removeClass('selected');
            $content.hide();

            // Update clicked tab to 'selected' and show it's contents.
            $selected = $(this);
            $content = $(this.hash);
            $selected.parent().addClass('selected');
            $content.show();
        });
    });

});