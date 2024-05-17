window.onload = function() {
    fetch('/api/homePages')
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(data => {
            var carousel = document.querySelector('.carousel');
            var images, points;
            var intervalID; // Store interval ID
            var currentIndex = 0; // Initialize currentIndex variable

            data.forEach(homePageDto => {
                var div = document.createElement('div');
                div.className = 'carousel-image';

                // Create img element
                var img = document.createElement('img');
                img.src = homePageDto.imageUrls[0]; // Set src attribute to the URL of the first image
                div.appendChild(img); // Append img as a child of div

                // Create overlay with text and button
                var overlay = document.createElement('div');
                overlay.className = 'overlay';
                overlay.innerHTML = `
                    <h2>${homePageDto.title}</h2>
                    <p>${homePageDto.descriptions[0]}</p>
                    <button>${homePageDto.buttonTexts[0]}</button>
                `;
                div.appendChild(overlay);

                carousel.appendChild(div);
            });

            // Set the first image as active
            document.querySelector('.carousel-image').classList.add('active');

            // Initialize images and points arrays after adding new divs
            images = Array.from(document.querySelectorAll('.carousel-image'));
            points = Array.from(document.querySelectorAll('.carousel-point'));

            // Attach event listeners to points
            points.forEach(function(point, index) {
                point.addEventListener('click', function() {
                    clearInterval(intervalID); // Clear the interval
                    images.forEach(function(image) {
                        image.classList.remove('active');
                        image.style.opacity = '0'; // Also hide the image
                    });
                    points.forEach(function(point) { point.classList.remove('active'); });
                    images[index].classList.add('active');
                    images[index].style.opacity = '1'; // Also show the image
                    this.classList.add('active');
                    currentIndex = index; // Update currentIndex to match the clicked point index
                    // Reset automatic transition
                    setTimeout(function() {
                        startAutomaticTransition();
                    }, 1000); // Adjust the delay as needed
                });
            });

            // Function to start automatic transition
            // Function to start automatic transition
            function startAutomaticTransition() {
                intervalID = setInterval(function() {
                    var nextIndex = (currentIndex + 1) % images.length; // Calculate the index of the next image
                    images[currentIndex].style.opacity = '0';
                    images[nextIndex].style.opacity = '1'; // Start fading in the next image immediately
                    points[currentIndex].classList.remove('active');
                    points[nextIndex].classList.add('active');

                    // Add a delay before removing the 'active' class from the previous image
                    setTimeout(function() {
                        images[currentIndex].classList.remove('active');
                        currentIndex = nextIndex; // Update the current index
                    }, 1000); // 1000 milliseconds = 1 second

                }, 6000); // 6000 milliseconds = 6 seconds
            }


            // Start the automatic transition initially
            startAutomaticTransition();

        })
        .catch(error => {
            console.error('There has been a problem with your fetch operation:', error);
        });
};
