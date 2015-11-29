levelUp = function(id) {
    var className = $("#classSelector").val();
    console.log("Leveling up as a "+className);
    $.ajax({
        type:'GET',
        url: "/levelUp"+id+"_"+className,
        complete: function(){
            window.location.reload(true);
        }
    });
}