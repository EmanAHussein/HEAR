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
    document.getElementById('addUserForm').addEventListener('submit', handleAddUser);
    document.getElementById('addCourseForm').addEventListener('submit', handleAddCourse);
    document.getElementById('addClassForm').addEventListener('submit', handleAddClass);
    document.getElementById('editProfileForm').addEventListener('submit', handleEditProfile);
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
    menuItems += '<div class="menu-item" onclick="showSection(\'coursesManagementSection\')">📚 Courses</div>';
    menuItems += '<div class="menu-item" onclick="showSection(\'classesManagementSection\')">🏫 Classes</div>';

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
        'coursesSection': 'My Courses',
        'coursesManagementSection': 'Courses Management',
        'classesManagementSection': 'Classes Management'
    };
    document.getElementById('pageTitle').textContent = titles[sectionId] || 'Dashboard';

    // Load section data
    if (sectionId === 'studentsSection') loadStudents();
    if (sectionId === 'facultySection') loadFaculty();
    if (sectionId === 'profileSection') loadProfile();
    if (sectionId === 'scheduleSection') loadSchedule();
    if (sectionId === 'coursesSection') loadCourses();
    if (sectionId === 'adminDashboard') loadDashboardStats();
    if (sectionId === 'coursesManagementSection') loadAllCourses();
    if (sectionId === 'classesManagementSection') loadAllClasses();
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
    document.getElementById('addCourseModal').classList.add('active');
    loadCoursesForDropdown();
}

function showAddClassModal() {
    document.getElementById('addClassModal').classList.add('active');
    loadCoursesAndFacultyForClass();
}

async function showEditProfileModal() {
    document.getElementById('editProfileModal').classList.add('active');
    
    // Load current profile data
    try {
        const response = await fetch(`${API_BASE_URL}/user/profile/get`, {
            headers: { 'Authorization': `Bearer ${authToken}` }
        });

        if (response.ok) {
            const profile = await response.json();
            
            document.getElementById('editProfileName').value = profile.name || currentUser.name;
            document.getElementById('editProfileEmail').value = currentUser.email;
            document.getElementById('editProfilePhone').value = currentUser.phone || '';
            
            if (currentUser.role === 'STUDENT') {
                document.getElementById('editStudentProfileFields').classList.remove('hidden');
                document.getElementById('editFacultyProfileFields').classList.add('hidden');
                document.getElementById('editStudentCode').value = profile.studentCode || '';
                document.getElementById('editCurrentLevel').value = profile.currentLevel || '';
                document.getElementById('editStudentDepartment').value = profile.department || 'GENERAL';
            } else if (currentUser.role === 'FACULTYMEMBER') {
                document.getElementById('editFacultyProfileFields').classList.remove('hidden');
                document.getElementById('editStudentProfileFields').classList.add('hidden');
                
                const facultyResponse = await fetch(`${API_BASE_URL}/faculty_member/profile/${currentUser.id}/get`, {
                    headers: { 'Authorization': `Bearer ${authToken}` }
                });
                
                if (facultyResponse.ok) {
                    const facultyProfile = await facultyResponse.json();
                    document.getElementById('editJobTitle').value = facultyProfile.jobTitle || '';
                    document.getElementById('editScientificDegree').value = facultyProfile.scientificDegree || '';
                    document.getElementById('editFacultyDepartment').value = facultyProfile.department || 'GENERAL';
                    document.getElementById('editBio').value = facultyProfile.bio || '';
                }
            }
        }
    } catch (error) {
        console.error('Failed to load profile for editing:', error);
    }
}

function toggleNewUserStudentFields() {
    const role = document.getElementById('newUserRole').value;
    const studentFields = document.getElementById('newUserStudentFields');
    studentFields.classList.toggle('hidden', role !== 'STUDENT');
}

async function handleAddUser(e) {
    e.preventDefault();
    const role = document.getElementById('newUserRole').value;
    
    const userData = {
        name: document.getElementById('newUserName').value,
        email: document.getElementById('newUserEmail').value,
        phone: document.getElementById('newUserPhone').value,
        password: document.getElementById('newUserPassword').value,
        role: role,
        admin: false
    };

    if (role === 'STUDENT') {
        userData.profile = {
            studentCode: parseInt(document.getElementById('newStudentCode').value),
            currentLevel: document.getElementById('newCurrentLevel').value,
            department: document.getElementById('newStudentDepartment').value
        };
    }

    try {
        const response = await fetch(`${API_BASE_URL}/admin/user/add`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${authToken}`
            },
            body: JSON.stringify(userData)
        });

        if (response.ok) {
            showModalAlert('addUserAlert', 'User added successfully!', 'success');
            document.getElementById('addUserForm').reset();
            setTimeout(() => {
                closeModal('addUserModal');
                loadStudents();
                loadFaculty();
                loadDashboardStats();
            }, 1500);
        } else {
            showModalAlert('addUserAlert', 'Failed to add user', 'error');
        }
    } catch (error) {
        showModalAlert('addUserAlert', 'Failed to add user', 'error');
    }
}

async function handleAddCourse(e) {
    e.preventDefault();
    
    const courseData = {
        name: document.getElementById('newCourseName').value,
        code: document.getElementById('newCourseCode').value,
        creditHours: document.getElementById('newCourseCreditHours').value,
        description: document.getElementById('newCourseDescription').value
    };

    try {
        const response = await fetch(`${API_BASE_URL}/admin/course/add`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${authToken}`
            },
            body: JSON.stringify(courseData)
        });

        if (response.ok) {
            showModalAlert('addCourseAlert', 'Course added successfully!', 'success');
            document.getElementById('addCourseForm').reset();
            setTimeout(() => {
                closeModal('addCourseModal');
                loadAllCourses();
                loadDashboardStats();
            }, 1500);
        } else {
            showModalAlert('addCourseAlert', 'Failed to add course', 'error');
        }
    } catch (error) {
        showModalAlert('addCourseAlert', 'Failed to add course', 'error');
    }
}

async function handleAddClass(e) {
    e.preventDefault();
    
    const classData = {
        name: document.getElementById('newClassName').value,
        courseId: parseInt(document.getElementById('newClassCourse').value),
        facultyMemberId: parseInt(document.getElementById('newClassFaculty').value),
        type: document.getElementById('newClassType').value,
        day: document.getElementById('newClassDay').value,
        startTime: document.getElementById('newClassStartTime').value,
        endTime: document.getElementById('newClassEndTime').value,
        room: document.getElementById('newClassRoom').value
    };

    try {
        const response = await fetch(`${API_BASE_URL}/admin/class/add`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${authToken}`
            },
            body: JSON.stringify(classData)
        });

        if (response.ok) {
            showModalAlert('addClassAlert', 'Class added successfully!', 'success');
            document.getElementById('addClassForm').reset();
            setTimeout(() => {
                closeModal('addClassModal');
                loadAllClasses();
            }, 1500);
        } else {
            showModalAlert('addClassAlert', 'Failed to add class', 'error');
        }
    } catch (error) {
        showModalAlert('addClassAlert', 'Failed to add class', 'error');
    }
}

async function handleEditProfile(e) {
    e.preventDefault();
    
    try {
        // Update user info
        const userData = {
            name: document.getElementById('editProfileName').value,
            email: document.getElementById('editProfileEmail').value,
            phone: document.getElementById('editProfilePhone').value
        };
        
        const password = document.getElementById('editProfilePassword').value;
        if (password) {
            userData.newPassword = password;
        }

        // Update profile based on role
        if (currentUser.role === 'STUDENT') {
            const profileData = {
                studentCode: parseInt(document.getElementById('editStudentCode').value),
                currentLevel: document.getElementById('editCurrentLevel').value,
                department: document.getElementById('editStudentDepartment').value
            };
            
            // Note: Using faculty endpoint as template - adjust if you have student-specific endpoint
            await fetch(`${API_BASE_URL}/user/profile/update`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${authToken}`
                },
                body: JSON.stringify(profileData)
            });
        } else if (currentUser.role === 'FACULTYMEMBER') {
            const profileData = {
                jobTitle: document.getElementById('editJobTitle').value,
                department: document.getElementById('editFacultyDepartment').value,
                scientificDegree: document.getElementById('editScientificDegree').value,
                bio: document.getElementById('editBio').value
            };
            
            await fetch(`${API_BASE_URL}/faculty_member/profile/update`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${authToken}`
                },
                body: JSON.stringify(profileData)
            });
        }

        showModalAlert('editProfileAlert', 'Profile updated successfully!', 'success');
        setTimeout(() => {
            closeModal('editProfileModal');
            loadProfile();
        }, 1500);
    } catch (error) {
        showModalAlert('editProfileAlert', 'Failed to update profile', 'error');
    }
}

async function loadCoursesAndFacultyForClass() {
    try {
        // Load all courses (you may need to create this endpoint or use existing one)
        const courseSelect = document.getElementById('newClassCourse');
        courseSelect.innerHTML = '<option value="">Loading...</option>';
        
        // Load faculty
        const facultyResponse = await fetch(`${API_BASE_URL}/admin/faculty_members/all/get`, {
            headers: { 'Authorization': `Bearer ${authToken}` }
        });
        
        if (facultyResponse.ok) {
            const faculty = await facultyResponse.json();
            const facultySelect = document.getElementById('newClassFaculty');
            facultySelect.innerHTML = '<option value="">Select Faculty</option>' +
                faculty.map(f => `<option value="${f.facultyMemberId}">${f.name}</option>`).join('');
        }
        
        // For courses - you may need to implement a get all courses endpoint
        courseSelect.innerHTML = '<option value="">Select Course (Implement endpoint)</option>';
    } catch (error) {
        console.error('Failed to load data for class form:', error);
    }
}

async function loadAllCourses() {
    // Note: You may need to implement this endpoint in your backend
    // For now, showing placeholder
    const tbody = document.getElementById('coursesManagementTableBody');
    tbody.innerHTML = '<tr><td colspan="5" style="text-align: center;">Course management endpoint needed</td></tr>';
}

async function loadAllClasses() {
    // Note: You may need to implement this endpoint in your backend
    // For now, showing placeholder
    const tbody = document.getElementById('classesManagementTableBody');
    tbody.innerHTML = '<tr><td colspan="6" style="text-align: center;">Class management endpoint needed</td></tr>';
}

function showModalAlert(elementId, message, type) {
    const alertDiv = document.getElementById(elementId);
    alertDiv.innerHTML = `<div class="alert alert-${type === 'error' ? 'error' : 'success'}">${message}</div>`;
    setTimeout(() => alertDiv.innerHTML = '', 5000);
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