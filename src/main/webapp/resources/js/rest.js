/**
 * Created by BjornBjarnsteins on 11/29/15.
 */

rest = function(id) {
    $.ajax({
        type:'GET',
        url: "/rest"+id,
        complete: function(){
            window.location.reload(true);
        }
    });

    console.log("Character "+id+" is taking a nap");
}
