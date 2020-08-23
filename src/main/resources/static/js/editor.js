$(document).ready(function () {

    $('#json').hide();
    $('.editor-section').hide();
    changeTab('location-direction-tab', 'location-direction-section');

    //Load the linked scenes for the default location
    getLinkedLocations();

    $("#location-direction-form").submit(function (event) {
        event.preventDefault();
        addMovementLocation();
    });

    $("#action-target-form").submit(function (event) {
        event.preventDefault();
        addActionTarget();
        clearInputs();
    });

    $("#location-form").submit(function (event) {
        event.preventDefault();
        addLocation();
        clearInputs();
    });

    $("#target-form").submit(function (event) {
        event.preventDefault();
        addTarget();
        clearInputs();
    });

    $("#action-form").submit(function (event) {
        event.preventDefault();
        addAction();
        clearInputs();
    });

    $("#direction-form").submit(function (event) {
        event.preventDefault();
        addDirection();
        clearInputs();
    });

    $("#response-port").on("click", '#json', function () {
        copyJson();
    });

    $("#location-direction-tab").on("click", function () {
        $('label[for=current-location], #current-location').attr('disabled', false);
        changeTab($(this).attr('id'), 'location-direction-section');
    });
    $("#action-target-tab").on("click", function () {
        $('label[for=current-location], #current-location').attr('disabled', false);
        changeTab($(this).attr('id'), 'action-target-section');
    });
    $("#location-tab").on("click", function () {
        $('label[for=current-location], #current-location').attr('disabled', true);
        changeTab($(this).attr('id'), 'location-section');
    });
    $("#target-tab").on("click", function () {
        $('label[for=current-location], #current-location').attr('disabled', true);
        changeTab($(this).attr('id'), 'target-section');
    });
    $("#action-tab").on("click", function () {
        $('label[for=current-location], #current-location').attr('disabled', true);
        changeTab($(this).attr('id'), 'action-section');
    });

    $("#direction-tab").on("click", function () {
        $('label[for=current-location], #current-location').attr('disabled', true);
        changeTab($(this).attr('id'), 'direction-section');
    });


    $("#current-location").change(function () {
        getLinkedLocations();
    });

    $(function () {
        $('[data-toggle="tooltip"]').tooltip();
    })
});


function changeTab(tabId, sectionId) {
    $('.editor-section').hide();
    $('.nav-link').removeClass('active');
    $('#' + tabId).addClass('active');
    $('#' + sectionId).show();
}

function clearInputs() {
    $('input').val('');
}

function getLinkedLocations() {
    $.ajax({
        type: "GET",
        contentType: "application/json",
        url: "/editor/linked-locations/" + $('#current-location').val(),
        dataType: 'json',
        timeout: 600000,
        success: function (data) {

            setSceneImage(data.currentLocation, 'current');
            setSceneImage(data.locationNorth, 'north');
            setSceneImage(data.locationSouth, 'south');
            setSceneImage(data.locationEast, 'east');
            setSceneImage(data.locationWest, 'west');

            setLocationActionTargets(data);

            setPadImages();

            //Set the scene assets
            setSceneAssets(data.currentLocation, 'current');
            setSceneAssets(data.locationNorth, 'north');
            setSceneAssets(data.locationSouth, 'south');
            setSceneAssets(data.locationEast, 'east');
            setSceneAssets(data.locationWest, 'west');


        },
        error: function (error) {
            $('#feedback').html(error.responseText);
        }
    });
}

function setLocationActionTargets(data) {

    let currentLocation = data.currentLocation;
    let actionTargets = data.actionTargets;
    let locationDescription = 'Location [<keyword>' + currentLocation.description + '</keyword>]<br>';

    // Create a list of actions / targets for the supplied location.
    let actionTargetsList = locationDescription + 'No actions / targets exist for this location.';

    if (actionTargets != null && actionTargets.length != 0) {
        actionTargetsList = locationDescription;
        actionTargetsList += '<ul>';
        actionTargets.forEach((actionTarget) => {
            // actionTargetsList += '<li>' + actionTarget.description + '</li>';
            actionTargetsList += '<li>' + actionTarget + '</li>';
        });
        actionTargetsList += '</ul>';
    }
    $('#action-targets').html(actionTargetsList);
}

function setSceneAssets(data, direction) {

    //Reset assets
    $('#assets-' + direction).html('');

    if (data) {
        let assets = '';
        data.assets.forEach((asset, key, arr) => {
            if (Object.is(arr.length - 1, key)) {
                assets += '<img src="' + asset + '" class="img-fluid"/>'
            } else {
                assets += '<img src="' + asset + '" class="img-fluid scene-asset"/>';
            }
        });
        $('#assets-' + direction).html(assets);
    }
}

function syntaxHighlight(json) {
    json = json.replace(/&/g, '&amp;').replace(/</g, '&lt;').replace(/>/g, '&gt;');
    return json.replace(/("(\\u[a-zA-Z0-9]{4}|\\[^u]|[^\\"])*"(\s*:)?|\b(true|false|null)\b|-?\d+(?:\.\d*)?(?:[eE][+\-]?\d+)?)/g, function (match) {
        var cls = 'number';
        if (/^"/.test(match)) {
            if (/:$/.test(match)) {
                cls = 'key';
            } else {
                cls = 'string';
            }
        } else if (/true|false/.test(match)) {
            cls = 'boolean';
        } else if (/null/.test(match)) {
            cls = 'null';
        }
        return '<span class="' + cls + '">' + match + '</span>';
    });
}

function copyJson() {
    let json = document.getElementById('json');
    navigator.clipboard.writeText(json.textContent);
}

function addMovementLocation() {

    let currentLocation = $('#current-location').val();
    let destinationLocation = $('#destination-location').val();
    let direction = $("#direction").val();

    $('.alert').remove();
    $('#json').html('').hide();
    $('#scene-images').hide();
    $('#current-location-image').hide();
    $('#destination-location-image').hide();

    $.ajax({
        type: "POST",
        contentType: "application/json",
        url: "/editor/location-direction",
        data: JSON.stringify({
            "currentLocationId": currentLocation,
            "destinationLocationId": destinationLocation,
            "directionId": direction
        }),
        dataType: 'json',
        cache: false,
        timeout: 600000,
        success: function (data) {

            let locationDirection = {
                "description": data.description,
                "currentLocationId": data.currentLocation.locationId,
                "destinationLocationId": data.destinationLocation.locationId,
                "directionId": data.directionId
            }
            formatJson(locationDirection);

            //Set scene images
            $('#scene-images').show();

            setSceneImage(data.currentLocation, 'current', data.actionTargetsCurrent);
            setSceneImage(data.locationNorth, 'north', data.actionTargetsNorth);
            setSceneImage(data.locationSouth, 'south', data.actionTargetsSouth);
            setSceneImage(data.locationEast, 'east', data.actionTargetsEast);
            setSceneImage(data.locationWest, 'west', data.actionTargetsWest);
        },
        error: function (error) {
            $('#response-port').append(
                '<div class="alert" role="alert">' +
                '<i class="fas fa-bomb error-alert-icon"></i>' + error.responseText + '</div>'
            ).fadeIn();
        }
    });
}

function addLocation() {

    let description = $('#location-description').val();
    let response = $('#location-response').val();
    let imageName = $('#location-image').val();

    $('.alert').remove();
    $('#json').html('').hide();
    $('#scene-images').hide();

    $.ajax({
        type: "POST",
        contentType: "application/json",
        url: "/editor/location",
        data: JSON.stringify({
            "description": description,
            "response": response,
            "imageName": imageName
        }),
        dataType: 'json',
        cache: false,
        timeout: 600000,
        success: function (data) {

            let location = {
                "locationId": data.location.locationId,
                "description": data.description,
                "response": data.response,
                "imagePath": data.imagePath
            }
            formatJson(location);

            //Set scene images
            $('#scene-images').hide();
        },
        error: function (error) {
            $('#response-port').append(
                '<div class="alert" role="alert">' +
                '<i class="fas fa-bomb error-alert-icon"></i>' + error.responseText + '</div>'
            ).fadeIn();
        }
    });
}

function addTarget() {

    let description = $('#target-description').val();

    $('.alert').remove();
    $('#json').html('').hide();
    $('#scene-images').hide();

    $.ajax({
        type: "POST",
        contentType: "application/json",
        url: "/editor/target",
        data: JSON.stringify({
            "description": description
        }),
        dataType: 'json',
        cache: false,
        timeout: 600000,
        success: function (data) {

            let target = {
                "targetId": data.targetId,
                "description": data.description
            }
            formatJson(target);

            //Set scene images
            $('#scene-images').hide();
        },
        error: function (error) {
            $('#response-port').append(
                '<div class="alert" role="alert">' +
                '<i class="fas fa-bomb error-alert-icon"></i>' + error.responseText + '</div>'
            ).fadeIn();
        }
    });
}

function addAction() {

    let description = $('#action-description').val();

    $('.alert').remove();
    $('#json').html('').hide();
    $('#scene-images').hide();

    $.ajax({
        type: "POST",
        contentType: "application/json",
        url: "/editor/action",
        data: JSON.stringify({
            "description": description
        }),
        dataType: 'json',
        cache: false,
        timeout: 600000,
        success: function (data) {

            let action = {
                "actionId": data.actionId,
                "description": data.description
            }
            formatJson(action);

            //Set scene images
            $('#scene-images').hide();
        },
        error: function (error) {
            $('#response-port').append(
                '<div class="alert" role="alert">' +
                '<i class="fas fa-bomb error-alert-icon"></i>' + error.responseText + '</div>'
            ).fadeIn();
        }
    });
}

function addDirection() {

    let description = $('#direction-description').val();

    $('.alert').remove();
    $('#json').html('').hide();
    $('#scene-images').hide();

    $.ajax({
        type: "POST",
        contentType: "application/json",
        url: "/editor/direction",
        data: JSON.stringify({
            "description": description
        }),
        dataType: 'json',
        cache: false,
        timeout: 600000,
        success: function (data) {

            let action = {
                "directionId": data.directionId,
                "description": data.description
            }
            formatJson(action);

            //Set scene images
            $('#scene-images').hide();
        },
        error: function (error) {
            $('#response-port').append(
                '<div class="alert" role="alert">' +
                '<i class="fas fa-bomb error-alert-icon"></i>' + error.responseText + '</div>'
            ).fadeIn();
        }
    });
}

function addActionTarget() {

    let currentLocation = $('#current-location').val();
    let action = $('#action').val();
    let target = $("#target").val();
    let response = $('#location-action-target-response').val();
    let imageName = $('#location-action-target-image').val();

    $('.alert').remove();
    $('#json').html('').hide();
    $('#scene-images').hide();

    $.ajax({
        type: "POST",
        contentType: "application/json",
        url: "/editor/action-target",
        data: JSON.stringify({
            "currentLocationId": currentLocation,
            "actionId": action,
            "targetId": target,
            "response": response,
            "imageName": imageName
        }),
        dataType: 'json',
        cache: false,
        timeout: 600000,
        success: function (data) {

            let actionTarget = {
                "description": data.description,
                "locationId": data.location.locationId,
                "actionId": data.actionId,
                "targetId": data.targetId,
                "response": data.response,
                "imagePath": data.imagePath
            }
            formatJson(actionTarget);

            //Set scene images
            $('#scene-images').show();

            // setSceneImage(data.currentLocation, 'current');

        },
        error: function (error) {
            $('#response-port').append(
                '<div class="alert" role="alert">' +
                '<i class="fas fa-bomb error-alert-icon"></i>' + error.responseText + '</div>'
            ).fadeIn();
        }
    });
}

function formatJson(json) {
    //Show the generated json
    $('#json').html(syntaxHighlight(JSON.stringify(json, undefined, 4))).fadeIn();

    //Add the copy button
    $('#json').append('<button id="copy" type="button" class="btn btn-primary"><i class="fas fa-copy "></i></button>');
}

function setSceneImageDescription(location, direction) {

    let imageDescription = location !== null ? location.description : 'No linked location';

    $('#' + direction + '-image').append('<figcaption class="scene-image-description current">' + imageDescription + '</figcaption>');
}

function setPadImages() {
    $('.pad-image').html(
        '<img src="/images/no-image.jpg" alt="Location image" class="img-fluid"/>'
    ).fadeIn();
}

function setSceneImage(location, direction) {

    let imagePath = location !== null ? location.imagePath : '/images/no-image.jpg';

    $('#' + direction + '-image').html(
        '<img src="' + imagePath + '" alt="Location image" class="img-fluid"/>'
    ).fadeIn();

    setSceneImageDescription(location, direction);
}

function postAction() {

    $("#go").prop("disabled", true);

    let location = $("#history > li") == null ? $("#history > li").get(0).id : 1;
    let command = $("#action").val();

    $.ajax({
        type: "POST",
        contentType: "application/json",
        url: "/action",
        data: JSON.stringify({
            "location": location,
            "command": command
        }),
        dataType: 'json',
        cache: false,
        timeout: 600000,
        success: function (data) {

            $('#action').val('');

            //Set response
            $('#feedback').html(data.response);

            //Set scene image
            $('#scene-image')
                .css('background-image', 'url("' + data.image + '")')
                .css({width: '1000px', height: '550px', 'background-size': '100%'});

            //Update command history
            $('#history').prepend('<li id="' + data.location + '" class="list-group-item">' + data.command + '</li>');
            $('#command').hide().fadeIn();

            $("#go").prop("disabled", false);
        },
        error: function (error) {
            $('#feedback').html(error.responseText);
            $("#go").prop("disabled", false);
        }
    });
}

function highlightMovementKeywords(response) {

}

