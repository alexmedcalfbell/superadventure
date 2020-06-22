$(document).ready(function () {

    $("#game").submit(function (event) {
        event.preventDefault();
        postAction();
    });
});

function postAction() {

    $("#go").prop("disabled", true);

    $.ajax({
        type: "POST",
        contentType: "application/json",
        url: "/game/action",
        data: JSON.stringify($("#action").val()),
        dataType: 'json',
        cache: false,
        timeout: 600000,
        success: function (data) {
            $('#feedback').html(data);
            $("#go").prop("disabled", false);
        },
        error: function (e) {
            console.log(e);
            $("#go").prop("disabled", false);

        }
    });

}

