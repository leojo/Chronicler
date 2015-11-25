$(function() {
    $('.remove')
    .click(
         function ()
         {
            console.log("should hdie this");
             $(this).parent().hide();
         }
    );
});

deleteCharacter = function(id) {
    $.ajax({
    type:'GET',
    url: "/deleteCharacter"+id
    });

    console.log($(this));
    console.log($(this).parent());
    $(this).parent().hide();
    console.log("deleting character");
}

deleteCampaign = function(id) {
    $.ajax({
    type:'GET',
    url: "/deleteCampaign"+id
    });

    console.log("deleting campaign");
    }