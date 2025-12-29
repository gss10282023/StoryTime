var stompClient = null;

function connect() {
    var socket = new SockJS('/ws');
    stompClient = Stomp.over(socket);

    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);

        stompClient.subscribe('/topic/image', function (message) {
            showImage(message.body);
        });

        stompClient.subscribe('/topic/error', function (message) {
            showError(message.body);
        });

        stompClient.subscribe('/topic/videoGenerationId', function (message) {
            var generationId = message.body;
            // Start polling for video result
            pollVideoResult(generationId);
        });

        stompClient.subscribe('/topic/videoResult', function (message) {
            showVideo(message.body);
        });

        stompClient.subscribe('/topic/videoProgress', function (message) {
            document.getElementById('status').innerText = message.body;
        });

    }, function (error) {
        console.log('WebSocket connection error: ' + error);
        setTimeout(connect, 3000); // Reconnect after 3 seconds
    });
}

function showImage(base64Image) {
    document.getElementById('status').innerText = 'Image generated:';
    var img = document.getElementById('result');
    var outputFormat = document.getElementById('output_format').value;
    img.src = 'data:image/' + outputFormat + ';base64,' + base64Image;
    img.style.display = 'block';

    // If the user wants to convert to video
    if (document.getElementById('convertToVideo').checked) {
        startVideoGeneration(base64Image);
    }
}

function startVideoGeneration(base64Image) {
    document.getElementById('status').innerText = 'Starting video generation...';

    var formData = new FormData();
    var cfgScale = 10; // You can get this value from user input
    var motionBucketId = 180; // You can get this value from user input

    // Convert base64 image to Blob
    var byteCharacters = atob(base64Image);
    var byteNumbers = new Array(byteCharacters.length);
    for (var i = 0; i < byteCharacters.length; i++) {
        byteNumbers[i] = byteCharacters.charCodeAt(i);
    }
    var byteArray = new Uint8Array(byteNumbers);
    var blob = new Blob([byteArray], { type: 'image/png' });

    formData.append('image', blob, 'image.png');
    formData.append('cfg_scale', cfgScale);
    formData.append('motion_bucket_id', motionBucketId);

    fetch('/startVideoGeneration', {
        method: 'POST',
        body: formData
    }).then(response => response.text())
        .then(data => {
            console.log(data);
            // The server will send the generation ID via WebSocket
        });
}

function pollVideoResult(generationId) {
    fetch('/fetchVideoResult/' + generationId)
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.text();
        })
        .then(data => {
            console.log(data);
            // The actual video data will be sent via WebSocket
        })
        .catch(error => {
            console.error('Error fetching video result:', error);
        });

    // Set timeout to poll again after 10 seconds
    setTimeout(function () {
        pollVideoResult(generationId);
    }, 10000);
}

function showVideo(base64Video) {
    document.getElementById('status').innerText = 'Video generated:';

    var videoElement = document.getElementById('generatedVideo');

    if (!videoElement) {
        videoElement = document.createElement('video');
        videoElement.id = 'generatedVideo';
        videoElement.controls = true;
        document.body.appendChild(videoElement);
    }

    videoElement.src = 'data:video/mp4;base64,' + base64Video;
}


function showError(errorMessage) {
    document.getElementById('status').innerText = 'Error: ' + errorMessage;
}

document.getElementById('generationForm').addEventListener('submit', function (event) {
    event.preventDefault();

    var formData = new FormData(document.getElementById('generationForm'));
    document.getElementById('status').innerText = 'Generating image, please wait...';
    document.getElementById('result').style.display = 'none';

    fetch('/generate', {
        method: 'POST',
        body: formData
    }).then(response => {
        if (response.ok) {
            console.log('Request successful');
        } else {
            console.log('Request failed with status ' + response.status);
        }
        return response.text();
    }).then(data => {
        console.log(data);
    });
});

connect();