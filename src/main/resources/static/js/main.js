$(document).ready(function () {

    //TODO: You could achieve the same by returning the data in the model.
    startGame();

    //Player command
    $("#game").submit(function (event) {
        event.preventDefault();
        postAction();
    });

    //Splash screen
    $("body").on("click", function () {
        closeSplashScreen();
    });
    $("#splash-close").on("click", function () {
        closeSplashScreen();
    });

    //Command history
    $("#history").on("click", 'li', function () {
        $('#action').val($(this).text());
        postAction();
    });

    //Set the last known command in the input on arrow key up.
    $('#action').keydown(function (e) {
        if ((e.which || e.keyCode) == 38) { //38 = arrow key up
            $('#action').val($('#history li').first().text());
        }
    });

    // Reset the game
    $("#start-again").on("click", function () {
        $('#death-modal').modal('hide');
        $('#history').html('');
        $('#action').attr('disabled', false);
        startGame();
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

            //Set the scene assets, with the position absolute style not being set on the last item.
            setSceneAssets(data);
        },
        error: function (error) {
            $('#feedback').html(error.responseText);
        }
    });
}

function postAction() {

    let command = $("#action").val();

    if (command) {

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


                //Set the scene assets, with the position absolute style not being set on the last item.
                setSceneAssets(data);

                if (data.fatal === true) {
                    $('#action').attr('disabled', true);
                    $('#death-modal').modal('show');
                }

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
}

function setSceneAssets(data) {
    let assets = '';
    data.assets.forEach((asset, key, arr) => {
        if (Object.is(arr.length - 1, key)) {
            assets += '<img src="' + asset + '" class="img-fluid"/>'
        } else {
            assets += '<img src="' + asset + '" class="img-fluid scene-asset"/>';
        }
    });
    $('#assets').html(assets);
}

function clearAction() {
    $('#action').val('');
}

