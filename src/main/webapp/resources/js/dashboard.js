$(function() {
    var $header = $('header'),
        $container = $('.container'),
        $dashboard = $('.dashboard'),
        $content = $('.dashboardContent');

    $('#menuButton').on('click', function(){
        console.log("Clicking burger!");


        if($dashboard.hasClass('closed')) {
            console.log("is closed");
            $dashboard.removeClass('closed');
            $content.show();
            $header.addClass('open');
            $container.addClass('open');
        } else {
            console.log("is open");
            $dashboard.addClass('closed');
            $content.hide();
            $header.removeClass('open');
            $container.removeClass('open');
        }


    });

});