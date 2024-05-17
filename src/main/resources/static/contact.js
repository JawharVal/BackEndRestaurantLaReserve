window.onload = function() {
    // Initially set the opacity to 0 for both body and content

    // Fade in the body first
    setTimeout(function() {
        document.body.style.transition = 'opacity 0.5s ease-out';
        document.body.style.opacity = 1;
    }, 200); // Adjust delay as needed

    // Then fade in the content
    setTimeout(function() {
        document.getElementById('content').style.transition = 'opacity 0.5s ease-out';
        document.getElementById('content').style.opacity = 1;
    }, 700); // Adjust delay as needed

    // Delay the execution of the JavaScript code
    setTimeout(function() {
        document.getElementById('send-button').addEventListener('click', function() {


            var name = document.getElementById('name').value;
            var email = document.getElementById('email').value;
            var phone = document.getElementById('phone').value;
            var location = document.getElementById('location').value;
            var subject = document.getElementById('subject').value;
            var message = document.getElementById('message').value;

            if (!name || !email || !subject || !message) {
                alert('Please fill in all required fields!');
                return;
            }

            fetch('/contact', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({
                    name: name,
                    email: email,
                    phone: phone,
                    location: location,
                    subject: subject,
                    message: message
                })
            }).then(function(response) {
                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }
                return response.text();
            }).then(function(data) {
                console.log('Form submitted successfully:', data);

                // Add transition and change opacity to 1 after the request is complete

            }).catch(function(error) {
                console.error('There has been a problem with your fetch operation:', error);
            });
        });
    }, 500); // Adjust delay as needed
};
