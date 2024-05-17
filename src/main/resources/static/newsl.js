window.onload = function() {

    // Fade in the body first
    setTimeout(function() {
        document.body.style.transition = 'opacity 0.5s ease-out';
        document.body.style.opacity = 1;
    }, 200); // Adjust delay as needed

    // Then fade in the content
    setTimeout(function() {
        document.getElementById('newsletterForm').style.transition = 'opacity 0.5s ease-out';
        document.getElementById('newsletterForm').style.opacity = 1;
    }, 700); // Adjust delay as needed
};

document.getElementById('newsletterForm').addEventListener('submit', function(event) {
    event.preventDefault();



    var fname = document.getElementById('fname').value;
    var lname = document.getElementById('lname').value;
    var email = document.getElementById('email').value;

    var xhr = new XMLHttpRequest();
    xhr.open('POST', 'http://localhost:8081/subscribe', true);
    xhr.setRequestHeader('Content-Type', 'application/json');
    xhr.send(JSON.stringify({
        firstName: fname,
        lastName: lname,
        email: email
    }));
    xhr.onerror = function () {
        if (xhr.status == 400) {
            alert(xhr.responseText);
        }
    };

    xhr.onload = function () {
        if (xhr.status == 200) {
            var promoCode = JSON.parse(xhr.responseText).promoCode;
            emailjs.send('service_ulut', 'template_bem', {
                to_name: fname + ' ' + lname,
                to_email: email,
                message: 'Congratulations! Your unique promo code is: ' + promoCode
            }, 'UT5KAivb7al')
                .then(function(response) {
                    console.log('SUCCESS!', response.status, response.text);
                }, function(error) {
                    console.error('FAILED...', error);
                });

            // Add transition and change opacity to 1 after the request is complete

        } else {
            document.getElementById('errorMessage').innerText = xhr.responseText;
        }
    };
});


