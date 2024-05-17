function addUser() {
    const username = document.getElementById('username').value;
    const password = document.getElementById('password').value;
    const userDTO = { username: username, password: password };

    fetch('/admin/user', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(userDTO),
    })
        .then(response => response.json())
        .then(data => console.log(data))
        .catch(error => console.error('Error:', error));
}

function editUser() {
    const userId = document.getElementById('editUserId').value;
    const username = document.getElementById('editUsername').value;
    const password = document.getElementById('editPassword').value;
    const userDTO = { username: username, password: password };

    fetch(`/admin/user/${userId}`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(userDTO),
    })
        .then(response => response.json())
        .then(data => console.log(data))
        .catch(error => console.error('Error:', error));
}

function deleteUser() {
    const userId = document.getElementById('deleteUserId').value;

    fetch(`/admin/user/${userId}`, {
        method: 'DELETE',
    })
        .then(response => {
            if (response.ok) {
                console.log('User deleted successfully');
            } else {
                console.error('Failed to delete user');
            }
        })
        .catch(error => console.error('Error:', error));
}

function getAllUsers() {
    fetch('/admin/users')
        .then(response => response.json())
        .then(data => {
            const userList = document.getElementById('userList');
            userList.innerHTML = '';

            data.forEach(user => {
                const listItem = document.createElement('li');
                listItem.textContent = `ID: ${user.id}, Username: ${user.username}`;
                userList.appendChild(listItem);
            });
        })
        .catch(error => console.error('Error:', error));
}
