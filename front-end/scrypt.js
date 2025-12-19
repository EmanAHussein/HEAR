
const API_BASE_URL = 'http://localhost:8080';
let currentUser = null;
let authToken = null;

// Initialize
document.addEventListener('DOMContentLoaded', () => {
    checkAuth();
    setupEventListeners();
});

function setupEventListeners() {
    document.getElementById('loginForm').addEventListener('submit', handleLogin);
    document.getElementById('registerForm').addEventListener('submit', handleRegister);
    document.getElementById('showRegister').addEventListener('click', showRegisterPage);
    document.getElementById('showLogin').addEventListener('click', showLoginPage);
    document.getElementById('logoutBtn').addEventListener('click', handleLogout);
    document.getElementById('registerRole').addEventListener('change', toggleStudentFields);
}

function toggleStudentFields() {
    const role = document.getElementById('registerRole').value;
    const studentFields = document.getElementById('studentFields');
    studentFields.classList.toggle('hidden', role !== 'STUDENT');
}

function showRegisterPage(e) {
    e.preventDefault();
    document.getElementById('loginPage').classList.add('hidden');
    document.getElementById('registerPage').classList.remove('hidden');
}

function showLoginPage(e) {
    e.preventDefault();
    document.getElementById('registerPage').classList.add('hidden');
    document.getElementById('loginPage').classList.remove('hidden');
}

async function handleLogin(e) {
    e.preventDefault();
    const email = document.getElementById('loginEmail').value;
    const password = document.getElementById('loginPassword').value;

    try {
        const response = await fetch(`${API_BASE_URL}/auth/login`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ email, password })
        });

        if (response.ok) {
            const data = await response.json();
            authToken = data.token;
            localStorage.setItem('authToken', authToken);
            await loadUserProfile();
            showDashboard();
        } else {
            showAlert('loginAlert', 'Invalid credentials', 'error');
        }
    } catch (error) {
        showAlert('loginAlert', 'Login failed. Please try again.', 'error');
    }
}

async function handleRegister(e) {
    e.preventDefault();
    const role = document.getElementById('registerRole').value;
    
    const userData = {
        name: document.getElementById('registerName').value,
        email: document.getElementById('registerEmail').value,
        phone: document.getElementById('registerPhone').value,
        password: document.getElementById('registerPassword').value,
        role: role,
        admin: false
    };

    if (role === 'STUDENT') {
        userData.profile = {
            studentCode: parseInt(document.getElementById('studentCode').value),
            currentLevel: document.getElementById('currentLevel').value,
            department: document.getElementById('studentDepartment').value
        };
    }

    try {
        const response = await fetch(`${API_BASE_URL}/auth/register`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(userData)
        });

        if (response.ok) {
            showAlert('registerAlert', 'Registration successful! Please login.', 'success');
            setTimeout(showLoginPage, 2000);
        } else {
            showAlert('registerAlert', 'Registration failed. Please try again.', 'error');
        }
    } catch (error) {
        showAlert('registerAlert', 'Registration failed. Please try again.', 'error');
    }
}

async function loadUserProfile() {
    try {
        const response = await fetch(`${API_BASE_URL}/auth/me`, {
            headers: { 'Authorization': `Bearer ${authToken}` }
        });

        if (response.ok) {
            currentUser = await response.json();
        }
    } catch (error) {
        console.error('Failed to load user profile:', error);
    }
}

function showDashboard() {
    document.getElementById('loginPage').classList.add('hidden');
    document.getElementById('registerPage').classList.add('hidden');
    document.getElementById('dashboard').classList.add('active');

    document.getElementById('dashboardUserName').textContent = currentUser.name;
    document.getElementById('dashboardUserRole').textContent = currentUser.role;
    document.getElementById('topBarUserName').textContent = currentUser.name;
    document.getElementById('topBarUserEmail').textContent = currentUser.email;
    document.getElementById('userAvatar').textContent = currentUser.name.charAt(0).toUpperCase();

    setupMenu();
    showSection('adminDashboard');
}

function setupMenu() {
    const menu = document.getElementById('sidebarMenu');
    let menuItems = '';

    // Common menu items
    menuItems += '<div class="menu-item" onclick="showSection(\'profileSection\')">👤 My Profile</div>';
    menuItems += '<div class="menu-item" onclick="showSection(\'scheduleSection\')">📅 My Schedule</div>';

    // Role-specific menu items
    if (currentUser.role === 'STUDENT') {
        menuItems += '<div class="menu-item" onclick="showSection(\'coursesSection\')">📚 My Courses</div>';
    } else if (currentUser.role === 'FACULTYMEMBER') {
        // Faculty specific items
    }

    // Admin menu (if user is admin)
    menuItems += '<div class="menu-item" onclick="showSection(\'adminDashboard\')">📊 Dashboard</div>';
    menuItems += '<div class="menu-item" onclick="showSection(\'studentsSection\')">👥 Students</div>';
    menuItems += '<div class="menu-item" onclick="showSection(\'facultySection\')">👨‍🏫 Faculty</div>';

    menu.innerHTML = menuItems;
}

function showSection(sectionId) {
    // Hide all sections
    document.querySelectorAll('.content-section').forEach(section => {
        section.classList.remove('active');
    });

    // Remove active class from all menu items
    document.querySelectorAll('.menu-item').forEach(item => {
        item.classList.remove('active');
    });

    // Show selected section
    document.getElementById(sectionId).classList.add('active');

    // Update page title
    const titles = {
        'adminDashboard': 'Dashboard',
        'studentsSection': 'Students Management',
        'facultySection': 'Faculty Management',
        'profileSection': 'My Profile',
        'scheduleSection': 'My Schedule',
        'coursesSection': 'My Courses'
    };
    document.getElementById('pageTitle').textContent = titles[sectionId] || 'Dashboard';

    // Load section data
    if (sectionId === 'studentsSection') loadStudents();
    if (sectionId === 'facultySection') loadFaculty();
    if (sectionId === 'profileSection') loadProfile();
    if (sectionId === 'scheduleSection') loadSchedule();
    if (sectionId === 'coursesSection') loadCourses();
    if (sectionId === 'adminDashboard') loadDashboardStats();
}

async function loadDashboardStats() {
    try {
        const [students, faculty] = await Promise.all([
            fetch(`${API_BASE_URL}/admin/students/all/get`, {
                headers: { 'Authorization': `Bearer ${authToken}` }
            }).then(r => r.json()),
            fetch(`${API_BASE_URL}/admin/faculty_members/all/get`, {
                headers: { 'Authorization': `Bearer ${authToken}` }
            }).then(r => r.json())
        ]);

        document.getElementById('totalStudents').textContent = students.length;
        document.getElementById('totalFaculty').textContent = faculty.length;
    } catch (error) {
        console.error('Failed to load stats:', error);
    }
}

async function loadStudents() {
    try {
        const response = await fetch(`${API_BASE_URL}/admin/students/all/get`, {
            headers: { 'Authorization': `Bearer ${authToken}` }
        });

        if (response.ok) {
            const students = await response.json();
            const tbody = document.getElementById('studentsTableBody');
            
            if (students.length === 0) {
                tbody.innerHTML = '<tr><td colspan="6" style="text-align: center;">No students found</td></tr>';
                return;
            }

            tbody.innerHTML = students.map(student => `
                <tr>
                    <td>${student.name}</td>
                    <td>${student.email}</td>
                    <td>${student.studentCode}</td>
                    <td>${student.currentLevel}</td>
                    <td><span class="badge badge-student">${student.department}</span></td>
                    <td>
                        <button class="btn btn-danger btn-small" onclick="deleteUser(${student.userId})">Delete</button>
                    </td>
                </tr>
            `).join('');
        }
    } catch (error) {
        console.error('Failed to load students:', error);
    }
}

async function loadFaculty() {
    try {
        const response = await fetch(`${API_BASE_URL}/admin/faculty_members/all/get`, {
            headers: { 'Authorization': `Bearer ${authToken}` }
        });

        if (response.ok) {
            const faculty = await response.json();
            const tbody = document.getElementById('facultyTableBody');
            
            if (faculty.length === 0) {
                tbody.innerHTML = '<tr><td colspan="6" style="text-align: center;">No faculty members found</td></tr>';
                return;
            }

            tbody.innerHTML = faculty.map(member => `
                <tr>
                    <td>${member.name}</td>
                    <td>${member.email}</td>
                    <td>${member.jobTitle || 'N/A'}</td>
                    <td>${member.scientificDegree || 'N/A'}</td>
                    <td><span class="badge badge-faculty">${member.department}</span></td>
                    <td>
                        <button class="btn btn-danger btn-small" onclick="deleteUser(${member.userId})">Delete</button>
                    </td>
                </tr>
            `).join('');
        }
    } catch (error) {
        console.error('Failed to load faculty:', error);
    }
}

async function loadProfile() {
    try {
        const response = await fetch(`${API_BASE_URL}/user/profile/get`, {
            headers: { 'Authorization': `Bearer ${authToken}` }
        });

        if (response.ok) {
            const profile = await response.json();
            const content = document.getElementById('profileContent');
            
            let html = `
                <div style="display: grid; gap: 15px;">
                    <div><strong>Name:</strong> ${profile.name || currentUser.name}</div>
                    <div><strong>Email:</strong> ${currentUser.email}</div>
            `;

            if (profile.studentCode !== undefined) {
                html += `
                    <div><strong>Student Code:</strong> ${profile.studentCode}</div>
                    <div><strong>Current Level:</strong> ${profile.currentLevel}</div>
                    <div><strong>Department:</strong> ${profile.department}</div>
                `;
            }

            html += '</div>';
            content.innerHTML = html;
        }
    } catch (error) {
        document.getElementById('profileContent').innerHTML = '<p>Failed to load profile</p>';
    }
}

async function loadSchedule() {
    try {
        const response = await fetch(`${API_BASE_URL}/user/classes/get`, {
            headers: { 'Authorization': `Bearer ${authToken}` }
        });

        if (response.ok) {
            const schedule = await response.json();
            const content = document.getElementById('scheduleContent');
            
            if (schedule.length === 0) {
                content.innerHTML = '<p>No classes scheduled</p>';
                return;
            }

            content.innerHTML = schedule.map(cls => `
                <div class="schedule-item">
                    <h4>${cls.name}</h4>
                    <p><strong>Day:</strong> ${cls.day}</p>
                    <p><strong>Time:</strong> ${cls.startTime} - ${cls.endTime}</p>
                    <p><strong>Room:</strong> ${cls.room}</p>
                    <p><strong>Type:</strong> ${cls.type}</p>
                </div>
            `).join('');
        }
    } catch (error) {
        document.getElementById('scheduleContent').innerHTML = '<p>Failed to load schedule</p>';
    }
}

async function loadCourses() {
    try {
        const response = await fetch(`${API_BASE_URL}/student/courses/get`, {
            headers: { 'Authorization': `Bearer ${authToken}` }
        });

        if (response.ok) {
            const courses = await response.json();
            const tbody = document.getElementById('coursesTableBody');
            
            if (courses.length === 0) {
                tbody.innerHTML = '<tr><td colspan="4" style="text-align: center;">No courses enrolled</td></tr>';
                return;
            }

            tbody.innerHTML = courses.map(course => `
                <tr>
                    <td>${course.name}</td>
                    <td>${course.code}</td>
                    <td>${course.creditHours}</td>
                    <td>${course.description || 'N/A'}</td>
                </tr>
            `).join('');
        }
    } catch (error) {
        document.getElementById('coursesTableBody').innerHTML = '<tr><td colspan="4">Failed to load courses</td></tr>';
    }
}

async function deleteUser(userId) {
    if (!confirm('Are you sure you want to delete this user?')) return;

    try {
        const response = await fetch(`${API_BASE_URL}/admin/user/${userId}/delete`, {
            method: 'DELETE',
            headers: { 'Authorization': `Bearer ${authToken}` }
        });

        if (response.ok) {
            alert('User deleted successfully');
            loadStudents();
            loadFaculty();
        }
    } catch (error) {
        alert('Failed to delete user');
    }
}

function showAddUserModal() {
    document.getElementById('addUserModal').classList.add('active');
}

function showAddCourseModal() {
    alert('Add Course modal - coming soon!');
}

function showAddClassModal() {
    alert('Add Class modal - coming soon!');
}

function closeModal(modalId) {
    document.getElementById(modalId).classList.remove('active');
}

function handleLogout() {
    localStorage.removeItem('authToken');
    authToken = null;
    currentUser = null;
    document.getElementById('dashboard').classList.remove('active');
    document.getElementById('loginPage').classList.remove('hidden');
}

function checkAuth() {
    const token = localStorage.getItem('authToken');
    if (token) {
        authToken = token;
        loadUserProfile().then(() => {
            if (currentUser) showDashboard();
        });
    }
}

function showAlert(elementId, message, type) {
    const alertDiv = document.getElementById(elementId);
    alertDiv.innerHTML = `<div class="alert alert-${type === 'error' ? 'error' : 'success'}">${message}</div>`;
    setTimeout(() => alertDiv.innerHTML = '', 5000);
}

// Close modal when clicking outside
window.onclick = function(event) {
    if (event.target.classList.contains('modal')) {
        event.target.classList.remove('active');
    }
}