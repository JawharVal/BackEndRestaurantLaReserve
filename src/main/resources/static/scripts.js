// Create a double buffer
let buffer = document.createElement('div');
buffer.id = 'buffer';
document.querySelector('body').appendChild(buffer);

// Define a function to fetch and display the menu items for the "MENU" section by default
function fetchMenuItems(section) {
    buffer.innerHTML = ''; // Clear the buffer
    document.querySelector('main').classList.add('loading'); // Add loading class
    fetch('/api/menuItems/section/' + section) // Fetch all menu items for the specified section
        .then(response => response.json())
        .then(data => {
            document.querySelector('main').style.backgroundColor = 'gold'; // Set the default background color
            document.querySelector('main').style.transition = 'background-color 0.5s ease'; // Apply transition property
            document.querySelector('main').classList.remove('loading'); // Remove loading class
            data.forEach((item, i) => { // Add 'i' to get the index of the item
                if (item.section === section) { // Only display items that belong to the specified section
                    let html = `<div class="menuItem styled-text hidden" style="--i: ${i};">
                                    <h2>${item.name}</h2>
                                    <h4>${item.description}</h4>
                                    <h5>Price: ${item.price.toString()} USD</h5>
                                    <img src="${item.imageUrl}">
                                </div>`;
                    // Append the new menu item to the buffer
                    buffer.innerHTML += html;
                }
            });
            // Swap the buffer and the menuItems
            document.getElementById('menuItems').innerHTML = buffer.innerHTML;
            buffer.innerHTML = '';
            // Remove the 'hidden' class after a delay to trigger the transition
            setTimeout(() => {
                let menuItems = document.querySelectorAll('.menuItem.hidden');
                menuItems.forEach(item => item.classList.remove('hidden'));
                document.querySelector('body').style.opacity = 1;
            }, 100); // Adjust delay as needed
        })
        .catch(error => console.error('Error:', error));
}

// Call the function immediately when the script runs to fetch and display menu items for the "MENU" section
fetchMenuItems('MENU');

// Also call the function when the "Menu" button is clicked to fetch and display menu items for the "MENU" section
document.getElementById('menuButton').addEventListener('click', function() {
    fetchMenuItems('MENU');
});

// Define a function to fetch and display the menu items for the "SPECIALS" section when the "Specials" button is clicked
function fetchSpecials() {
    buffer.innerHTML = ''; // Clear the buffer
    document.querySelector('main').classList.add('loading'); // Add loading class
    fetch('/api/menuItems/section/SPECIALS') // Fetch all menu items for the "SPECIALS" section
        .then(response => response.json())
        .then(data => {
            document.querySelector('main').style.backgroundColor = 'white'; // Set the background color
            document.querySelector('main').style.transition = 'background-color 0.5s ease'; // Apply transition property
            document.querySelector('main').classList.remove('loading'); // Remove loading class
            data.forEach((item, i) => { // Add 'i' to get the index of the item
                let html = `<div class="menuItem styled-text hidden" style="--i: ${i};">
                                <h2>${item.name}</h2>
                                <h4>${item.description}</h4>
                                <h5>Price: ${item.price.toString()} USD</h5>
                                <img src="${item.imageUrl}">
                            </div>`;
                // Append the new menu item to the buffer
                buffer.innerHTML += html;
            });
            // Swap the buffer and the menuItems
            document.getElementById('menuItems').innerHTML = buffer.innerHTML;
            buffer.innerHTML = '';
            // Remove the 'hidden' class after a delay to trigger the transition
            setTimeout(() => {
                let menuItems = document.querySelectorAll('.menuItem.hidden');
                menuItems.forEach(item => item.classList.remove('hidden'));
                document.querySelector('body').style.opacity = 1;
            }, 100); // Adjust delay as needed
        })
        .catch(error => console.error('Error:', error));

}

// Event listener for the "Specials" button to fetch and display menu items for the "SPECIALS" section
document.getElementById('specialsButton').addEventListener('click', function() {
    fetchSpecials();
});

document.getElementById('menuButton').addEventListener('click', function() {
    this.classList.add('active');
    document.getElementById('specialsButton').classList.remove('active');
});

document.getElementById('specialsButton').addEventListener('click', function() {
    this.classList.add('active');
    document.getElementById('menuButton').classList.remove('active');
});

// Add event listeners for navigation links to toggle the 'selected' class
let navLinks = document.querySelectorAll('.nav-link');
navLinks.forEach(link => {
    link.addEventListener('click', function(event) {
        // Remove the 'selected' class from all links
        navLinks.forEach(otherLink => otherLink.classList.remove('selected'));

        // Add the 'selected' class to the clicked link
        this.classList.add('selected');
    });
});

window.addEventListener('load', function() {
    document.body.style.display = 'block';
});

window.addEventListener('scroll', function() {
    var header = document.querySelector('header');
    if (window.pageYOffset > 0) {
        header.style.height = '50px'; /* adjust as needed */
    } else {
        header.style.height = '100px'; /* adjust as needed */
    }
});

