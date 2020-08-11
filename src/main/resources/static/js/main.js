$(document).ready(function () {

    //TODO: You could achieve the same by returning the data in the model.
    startGame();

    $("#game").submit(function (event) {
        event.preventDefault();
        postAction();
    });

    $("body").on("click", function () {
        closeSplashScreen();
    });
    $("#splash-close").on("click", function () {
        closeSplashScreen();
    });

    $("#history").on("click", 'li', function () {
        $('#action').val($(this).text());
        postAction();
    });

    $('#action').keydown(function (e) {
        if((e.which || e.keyCode) == 38){ //38 = arrow key up
            $('#action').val($('#history li').first().text());
        }
    });



});

function closeSplashScreen() {
    $("#splash-screen").fadeOut();
    $('#action').focus();
}

function startGame() {

    $.ajax({
        type: "GET",
        contentType: "application/json",
        url: "/start",
        dataType: 'json',
        timeout: 600000,
        success: function (data) {
            //Set response
            $('#feedback').html(data.response);

            //Set scene image
            $('#scene-image').html(
                '<img src="' + data.imagePath + '" alt="Location image" class="img-fluid"/>'
            );
        },
        error: function (error) {
            $('#feedback').html(error.responseText);
        }
    });
}

function postAction() {

    let command = $("#action").val();

    $.ajax({
        type: "POST",
        contentType: "application/json",
        url: "/command",
        data: JSON.stringify({
            "command": command
        }),
        dataType: 'json',
        cache: false,
        timeout: 600000,
        success: function (data) {

            clearAction();

            //Set response
            $('#feedback').html(data.response);

            //Set scene image

            $('#scene-image').html(
                '<img src="' + data.imagePath + '" alt="Location image" class="img-fluid"/>'
            );

            //Update command history
            $('#history').prepend('<li class="list-group-item">' + data.command + '</li>');
            $('#command').hide().fadeIn();
        },
        error: function (error) {
            clearAction();
            $('#feedback').html(error.responseText);
        }
    });
}

function clearAction() {
    $('#action').val('');
}

