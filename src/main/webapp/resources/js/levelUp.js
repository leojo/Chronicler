levelUp = function(id) {
    $.ajax({
        type:'GET',
        url: "/levelUp"+id
    });

    console.log("Leveling up character");
}