levelUp = function(id) {
    var classID = 11;
    $.ajax({
        type:'GET',
        url: "/levelUp"+id+"_"+classID,
        complete: function(){
            window.location.reload(true);
        }
    });
}