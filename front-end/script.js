const API_BASE_URL = 'http://localhost:8080';
let currentUser = null;
let authToken = null;

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
    document.getElementById('editStudentForm').addEventListener('submit', handleEditStudent);
    document.getElementById('editFacultyForm').addEventListener('submit', handleEditFaculty);
    document.getElementById('editCourseForm').addEventListener('submit', handleEditCourse);
    document.getElementById('editClassForm').addEventListener('submit', handleEditClass);
    document.getElementById('enrollStudentForm').addEventListener('submit', handleEnrollStudent);
}

function toggleStudentFields() {
    const role = document.getElementById('registerRole').value;
    document.getElementById('studentFields').classList.toggle('hidden', role !== 'STUDENT');
    document.getElementById('facultyFields').classList.toggle('hidden', role !== 'FACULTYMEMBER');
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
        name: document.getElementById('registerName').value.trim(),
        email: document.getElementById('registerEmail').value.trim(),
        phone: document.getElementById('registerPhone').value.trim(),
        password: document.getElementById('registerPassword').value,
        role: role,
        isAdmin: false
    };

    if (role === 'STUDENT') {
        const studentCodeVal = document.getElementById('studentCode').value.trim();
        userData.profile = {
            studentCode: studentCodeVal ? parseInt(studentCodeVal) : 0,
            currentLevel: document.getElementById('currentLevel').value.trim(),
            department: document.getElementById('studentDepartment').value.trim()
        };
    } else if (role === 'FACULTYMEMBER') {
        userData.profile = {
            jobTitle: document.getElementById('facultyJobTitle').value.trim(),
            department: document.getElementById('facultyDepartment').value.trim(),
            scientificDegree: document.getElementById('facultyDegree').value.trim(),
            bio: document.getElementById('facultyBio').value.trim()
        };
    }

    console.log('Register payload:', JSON.stringify(userData, null, 2));

    try {
        const response = await fetch(`${API_BASE_URL}/auth/register`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(userData)
        });

        if (response.ok) {
            showAlert('registerAlert', 'Registration successful! Please login.', 'success');
            setTimeout(() => {
                document.getElementById('registerForm').reset();
                showLoginPage({ preventDefault: () => {} });
            }, 2000);
        } else {
            const errText = await response.text();
            console.error('Registration failed:', errText);
            showAlert('registerAlert', 'Registration failed. Please try again.', 'error');
        }
    } catch (error) {
        console.error('Register request error:', error);
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

    showSection('scheduleSection');
    console.log('Edit button:', document.getElementById('editProfileBtn'));
    updateProfileUIByRole(); 

}


function setupMenu() {
    const menu = document.getElementById('sidebarMenu');
    let menuItems = '';

    menuItems += '<div class="menu-item" onclick="showSection(\'profileSection\')">👤 My Profile</div>';
    menuItems += '<div class="menu-item" onclick="showSection(\'scheduleSection\')">📅 My Schedule</div>';

    if (currentUser.admin == 'true') {
        menuItems += '<div class="menu-item" onclick="showSection(\'adminDashboard\')">📊 Dashboard</div>';
        menuItems += '<div class="menu-item" onclick="showSection(\'studentsSection\')">👥 Students</div>';
        menuItems += '<div class="menu-item" onclick="showSection(\'facultySection\')">👨‍🏫 Faculty</div>';
        menuItems += '<div class="menu-item" onclick="showSection(\'coursesManagementSection\')">📚 Courses</div>';
        menuItems += '<div class="menu-item" onclick="showSection(\'classesManagementSection\')">🏫 Classes</div>';
    }

    menu.innerHTML = menuItems;
}


function showSection(sectionId) {
    document.querySelectorAll('.content-section').forEach(section => {
        section.classList.remove('active');
    });

    document.querySelectorAll('.menu-item').forEach(item => {
        item.classList.remove('active');
    });

    document.getElementById(sectionId).classList.add('active');

    const titles = {
        'adminDashboard': 'Dashboard',
        'studentsSection': 'Students Management',
        'facultySection': 'Faculty Management',
        'profileSection': 'My Profile',
        'scheduleSection': 'My Schedule',
        'coursesManagementSection': 'Courses Management',
        'classesManagementSection': 'Classes Management'
    };
    document.getElementById('pageTitle').textContent = titles[sectionId] || 'Dashboard';

    if (sectionId === 'studentsSection') loadStudents();
    if (sectionId === 'facultySection') loadFaculty();
    if (sectionId === 'profileSection') loadProfile();
    if (sectionId === 'scheduleSection') loadSchedule();
    if (sectionId === 'adminDashboard') loadDashboardStats();
    if (sectionId === 'coursesManagementSection') loadAllCourses();
    if (sectionId === 'classesManagementSection') loadAllClasses();
}

async function loadDashboardStats() {
    try {
        const [students, faculty, courses] = await Promise.all([
            fetch(`${API_BASE_URL}/admin/students/all/get`, {
                headers: { 'Authorization': `Bearer ${authToken}` }
            }).then(r => r.json()),
            fetch(`${API_BASE_URL}/admin/faculty_members/all/get`, {
                headers: { 'Authorization': `Bearer ${authToken}` }
            }).then(r => r.json()),
            fetch(`${API_BASE_URL}/admin/courses/all/get`, {
                headers: { 'Authorization': `Bearer ${authToken}` }
            }).then(r => r.json()).catch(() => [])
        ]);

        document.getElementById('totalStudents').textContent = students.length;
        document.getElementById('totalFaculty').textContent = faculty.length;
        document.getElementById('totalCourses').textContent = courses.length;
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
                        <button class="btn btn-primary btn-small" onclick="showEditStudentModal(${student.userId}, ${student.studentId})">Edit</button>
                        <button class="btn btn-success btn-small" onclick="showEnrollStudentModal(${student.studentId})">Enroll</button>
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
                        <button class="btn btn-primary btn-small" onclick="showEditFacultyModal(${member.userId}, ${member.facultyMemberId})">Edit</button>
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
            } else if (profile.jobTitle !== undefined) {
                html += `
                    <div><strong>Job Title:</strong> ${profile.jobTitle || 'N/A'}</div>
                    <div><strong>Scientific Degree:</strong> ${profile.scientificDegree || 'N/A'}</div>
                    <div><strong>Department:</strong> ${profile.department}</div>
                    <div><strong>Bio:</strong> ${profile.bio || 'N/A'}</div>
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

async function loadAllCourses() {
    try {
        const response = await fetch(`${API_BASE_URL}/admin/courses/all/get`, {
            headers: { 'Authorization': `Bearer ${authToken}` }
        });

        if (!response.ok) throw new Error('Failed to fetch courses');

        const courses = await response.json();
        const tbody = document.getElementById('coursesManagementTableBody');

        if (!courses || courses.length === 0) {
            tbody.innerHTML =
                '<tr><td colspan="5" style="text-align:center;">No courses found</td></tr>';
            return;
        }

        tbody.innerHTML = courses.map(course => `
            <tr>
                <td>${course.name}</td>
                <td>${course.code}</td>
                <td>${course.creditHours}</td>
                <td>${course.description || 'N/A'}</td>
                <td>
                    <button class="btn btn-primary btn-small"
                        onclick="showEditCourseModal(
                            ${course.id},
                            '${course.name}',
                            '${course.code}',
                            ${course.creditHours},
                            '${course.description || ''}'
                        )">
                        Edit
                    </button>
                    <button class="btn btn-danger btn-small"
                        onclick="deleteCourse(${course.id})">
                        Delete
                    </button>
                </td>
            </tr>
        `).join('');
    } catch (error) {
        console.error('Failed to load all courses:', error);
    }
}

async function loadAllClasses() {
    try {
        const response = await fetch(`${API_BASE_URL}/admin/classes/all/get`, {
            headers: { 'Authorization': `Bearer ${authToken}` }
        });

        if (response.ok) {
            const classes = await response.json();
            const tbody = document.getElementById('classesManagementTableBody');
            
            if (classes.length === 0) {
                tbody.innerHTML = '<tr><td colspan="6" style="text-align: center;">No classes found</td></tr>';
                return;
            }

            tbody.innerHTML = classes.map(cls => `
                <tr>
                    <td>${cls.name}</td>
                    <td>${cls.type}</td>
                    <td>${cls.day}</td>
                    <td>${cls.startTime} - ${cls.endTime}</td>
                    <td>${cls.room}</td>
                    <td>
                        <button class="btn btn-primary btn-small" onclick="showEditClassModal(
                        ${cls.classId || cls.id},
                        '${cls.startTime}',
                        '${cls.endTime}',
                        '${cls.room}',
                        '${cls.day}',
                        ${cls.facultyMemberId}
                        )">Edit</button>
                        <button class="btn btn-danger btn-small" onclick="deleteClass(${cls.classId || cls.id})">Delete</button>
                    </td>
                </tr>
            `).join('');
        }
    } catch (error) {
        console.error('Failed to load classes:', error);
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
            loadDashboardStats();
        }
    } catch (error) {
        alert('Failed to delete user');
    }
}

async function deleteCourse(courseId) {
    if (!confirm('Are you sure you want to delete this course?')) return;

    try {
        const response = await fetch(`${API_BASE_URL}/admin/course/${courseId}/delete`, {
            method: 'DELETE',
            headers: { 'Authorization': `Bearer ${authToken}` }
        });

        if (response.ok) {
            alert('Course deleted successfully');
            loadAllCourses();
            loadDashboardStats();
        }
    } catch (error) {
        alert('Failed to delete course');
    }
}

async function deleteClass(classId) {
    if (!confirm('Are you sure you want to delete this class?')) return;

    try {
        const response = await fetch(`${API_BASE_URL}/admin/class/${classId}/delete`, {
            method: 'DELETE',
            headers: { 'Authorization': `Bearer ${authToken}` }
        });

        if (response.ok) {
            alert('Class deleted successfully');
            loadAllClasses();
        }
    } catch (error) {
        alert('Failed to delete class');
    }
}

function showAddUserModal() {
    document.getElementById('addUserModal').classList.add('active');
}

function showAddCourseModal() {
    document.getElementById('addCourseModal').classList.add('active');
}

async function showAddClassModal() {
    document.getElementById('addClassModal').classList.add('active');
    await loadCoursesAndFacultyForClass();
}

function toggleNewUserStudentFields() {
    const role = document.getElementById('newUserRole').value;
    document.getElementById('newUserStudentFields').classList.toggle('hidden', role !== 'STUDENT');
    document.getElementById('newUserFacultyFields').classList.toggle('hidden', role !== 'FACULTYMEMBER');
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
        isAdmin: document.getElementById('newUserAdmin').checked
    };

    if (role === 'STUDENT') {
        userData.profile = {
            studentCode: parseInt(document.getElementById('newStudentCode').value),
            currentLevel: document.getElementById('newCurrentLevel').value,
            department: document.getElementById('newStudentDepartment').value
        };
    } else if (role === 'FACULTYMEMBER') {
        userData.profile = {
            jobTitle: document.getElementById('newFacultyJobTitle').value,
            department: document.getElementById('newFacultyDepartment').value,
            scientificDegree: document.getElementById('newFacultyDegree').value,
            bio: document.getElementById('newFacultyBio').value
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

    if (!authToken) {
        showModalAlert('addCourseAlert', 'You are not logged in', 'error');
        return;
    }

    const nameEl = document.getElementById('newCourseName');
    const codeEl = document.getElementById('newCourseCode');
    const creditEl = document.getElementById('newCourseCreditHours');
    const descEl = document.getElementById('newCourseDescription');

    if (!nameEl || !codeEl || !creditEl || !descEl) {
        console.error("Add course inputs missing");
        return;
    }

    const courseData = {
        name: nameEl.value.trim(),
        code: codeEl.value.trim(),
        creditHours: Number(creditEl.value),
        description: descEl.value.trim()
    };

    console.log("Sending course:", courseData);

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
            const err = await response.text();
            console.error(err);
            showModalAlert('addCourseAlert', err || 'Failed to add course', 'error');
        }
    } catch (error) {
        console.error(error);
        showModalAlert('addCourseAlert', 'Network error', 'error');
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

async function showEditProfileModal() {

    const role = currentUser?.role?.toString().toUpperCase().trim();
    if (role?.includes('STUDENT')) return;

    document.getElementById('editProfileModal').classList.add('active');
    
    try {
        const response = await fetch(`${API_BASE_URL}/user/profile/get`, {
            headers: { 'Authorization': `Bearer ${authToken}` }
        });

        if (response.ok) {
            const profile = await response.json();

            if (currentUser.role === 'FACULTYMEMBER') {
                document.getElementById('editFacultyProfileFields').classList.remove('hidden');
                document.getElementById('editJobTitle').value = profile.jobTitle || '';
                document.getElementById('editScientificDegree').value = profile.scientificDegree || '';
                document.getElementById('editFacultyDepartment').value = profile.department || 'GENERAL';
                document.getElementById('editBio').value = profile.bio || '';
            }
        }
    } catch (error) {
        console.error('Failed to load profile for editing:', error);
    }
}

async function handleEditProfile(e) {
    e.preventDefault();
    
    try {
        if (currentUser.role === 'FACULTYMEMBER') {
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

async function showEditStudentModal(userId, studentId) {
    document.getElementById('editStudentModal').classList.add('active');
    document.getElementById('editStudentUserId').value = userId;
    
    try {
        const response = await fetch(`${API_BASE_URL}/admin/students/${studentId}/get`, {
            headers: { 'Authorization': `Bearer ${authToken}` }
        });

        if (response.ok) {
            const student = await response.json();
            document.getElementById('editStudentCodeModal').value = student.studentCode;
            document.getElementById('editCurrentLevelModal').value = student.currentLevel;
            document.getElementById('editStudentDepartmentModal').value = student.department;
        }
    } catch (error) {
        console.error('Failed to load student data:', error);
    }
}

async function handleEditStudent(e) {
    e.preventDefault();
    const userId = document.getElementById('editStudentUserId').value;
    
    const profileData = {
        role: 'STUDENT',
        updateStudentProfile: {
            studentCode: parseInt(document.getElementById('editStudentCodeModal').value),
            currentLevel: document.getElementById('editCurrentLevelModal').value,
            department: document.getElementById('editStudentDepartmentModal').value
        }
    };

    try {
        const response = await fetch(`${API_BASE_URL}/admin/user/${userId}/profile/update`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${authToken}`
            },
            body: JSON.stringify(profileData)
        });

        if (response.ok) {
            showModalAlert('editStudentAlert', 'Student updated successfully!', 'success');
            setTimeout(() => {
                closeModal('editStudentModal');
                loadStudents();
            }, 1500);
        } else {
            showModalAlert('editStudentAlert', 'Failed to update student', 'error');
        }
    } catch (error) {
        showModalAlert('editStudentAlert', 'Failed to update student', 'error');
    }
}

async function showEditFacultyModal(userId, facultyId) {
    document.getElementById('editFacultyModal').classList.add('active');
    document.getElementById('editFacultyUserId').value = userId;
    
    try {
        const response = await fetch(`${API_BASE_URL}/faculty_member/profile/${facultyId}/get`, {
            headers: { 'Authorization': `Bearer ${authToken}` }
        });

        if (response.ok) {
            const faculty = await response.json();
            document.getElementById('editFacultyJobTitleModal').value = faculty.jobTitle || '';
            document.getElementById('editFacultyDegreeModal').value = faculty.scientificDegree || '';
            document.getElementById('editFacultyDepartmentModal').value = faculty.department || 'GENERAL';
            document.getElementById('editFacultyBioModal').value = faculty.bio || '';
        }
    } catch (error) {
        console.error('Failed to load faculty data:', error);
    }
}

async function handleEditFaculty(e) {
    e.preventDefault();
    const userId = document.getElementById('editFacultyUserId').value;
    
    const profileData = {
        role: 'FACULTYMEMBER',
        updateFacultyProfile: {
            jobTitle: document.getElementById('editFacultyJobTitleModal').value,
            department: document.getElementById('editFacultyDepartmentModal').value,
            scientificDegree: document.getElementById('editFacultyDegreeModal').value,
            bio: document.getElementById('editFacultyBioModal').value
        }
    };

    try {
        const response = await fetch(`${API_BASE_URL}/admin/user/${userId}/profile/update`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${authToken}`
            },
            body: JSON.stringify(profileData)
        });

        if (response.ok) {
            showModalAlert('editFacultyAlert', 'Faculty member updated successfully!', 'success');
            setTimeout(() => {
                closeModal('editFacultyModal');
                loadFaculty();
            }, 1500);
        } else {
            showModalAlert('editFacultyAlert', 'Failed to update faculty member', 'error');
        }
    } catch (error) {
        showModalAlert('editFacultyAlert', 'Failed to update faculty member', 'error');
    }
}

function showEditCourseModal(courseId, name, code, creditHours, description) {
    document.getElementById('editCourseModal').classList.add('active');
    document.getElementById('editCourseId').value = courseId;
    document.getElementById('editCourseName').value = name;
    document.getElementById('editCourseCode').value = code;
    document.getElementById('editCourseCreditHours').value = creditHours;
    document.getElementById('editCourseDescription').value = description;
}

async function handleEditCourse(e) {
    e.preventDefault();
    const courseId = document.getElementById('editCourseId').value;
    
    const courseData = {
        name: document.getElementById('editCourseName').value,
        code: document.getElementById('editCourseCode').value,
        creditHours: document.getElementById('editCourseCreditHours').value,
        description: document.getElementById('editCourseDescription').value
    };

    try {
        const response = await fetch(`${API_BASE_URL}/admin/course/${courseId}/update`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${authToken}`
            },
            body: JSON.stringify(courseData)
        });

        if (response.ok) {
            showModalAlert('editCourseAlert', 'Course updated successfully!', 'success');
            setTimeout(() => {
                closeModal('editCourseModal');
                loadAllCourses();
            }, 1500);
        } else {
            showModalAlert('editCourseAlert', 'Failed to update course', 'error');
        }
    } catch (error) {
        showModalAlert('editCourseAlert', 'Failed to update course', 'error');
    }
}

async function showEditClassModal(
    classId,
    startTime,
    endTime,
    room,
    day,
    facultyMemberId
) {
    document.getElementById('editClassModal').classList.add('active');

    await loadCoursesAndFacultyForClass();

    document.getElementById('editClassId').value = classId;
    document.getElementById('editClassStartTime').value = startTime;
    document.getElementById('editClassEndTime').value = endTime;
    document.getElementById('editClassRoom').value = room;
    document.getElementById('editClassDay').value = day;

    document.getElementById('editClassFaculty').value = facultyMemberId;
}

async function handleEditClass(e) {
    e.preventDefault();
    const classId = document.getElementById('editClassId').value;
    
    const classData = {
        startTime: document.getElementById('editClassStartTime').value,
        endTime: document.getElementById('editClassEndTime').value,
        room: document.getElementById('editClassRoom').value,
        day: document.getElementById('editClassDay').value,
        facultyMemberId: parseInt(document.getElementById('editClassFaculty').value)
    };

    try {
        const response = await fetch(`${API_BASE_URL}/admin/class/${classId}/update`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${authToken}`
            },
            body: JSON.stringify(classData)
        });

        if (response.ok) {
            showModalAlert('editClassAlert', 'Class updated successfully!', 'success');
            setTimeout(() => {
                closeModal('editClassModal');
                loadAllClasses();
            }, 1500);
        } else {
            showModalAlert('editClassAlert', 'Failed to update class', 'error');
        }
    } catch (error) {
        showModalAlert('editClassAlert', 'Failed to update class', 'error');
    }
}

async function showEnrollStudentModal(studentId) {
    document.getElementById('enrollStudentModal').classList.add('active');
    document.getElementById('enrollStudentId').value = studentId;
    
    try {
        const [coursesRes, classesRes] = await Promise.all([
            fetch(`${API_BASE_URL}/student/courses/get`, {
                headers: { 'Authorization': `Bearer ${authToken}` }
            }),
            fetch(`${API_BASE_URL}/admin/classes/all/get`, {
                headers: { 'Authorization': `Bearer ${authToken}` }
            })
        ]);
        
        if (classesRes.ok) {
            const classes = await classesRes.json();
            const classSelect = document.getElementById('enrollClassId');
            classSelect.innerHTML = '<option value="">Select Class</option>' +
                classes.map(c => `<option value="${c.classId || c.id}">${c.name} - ${c.day} ${c.startTime}</option>`).join('');
        }
    } catch (error) {
        console.error('Failed to load enrollment data:', error);
    }
}

async function handleEnrollStudent(e) {
    e.preventDefault();
    const studentId = document.getElementById('enrollStudentId').value;
    const classId = document.getElementById('enrollClassId').value;

    try {
        const response = await fetch(`${API_BASE_URL}/admin/Student/addClass/${studentId}/${classId}`, {
            method: 'POST',
            headers: { 'Authorization': `Bearer ${authToken}` }
        });

        if (response.ok) {
            showModalAlert('enrollStudentAlert', 'Student enrolled successfully!', 'success');
            setTimeout(() => {
                closeModal('enrollStudentModal');
            }, 1500);
        } else {
            showModalAlert('enrollStudentAlert', 'Failed to enroll student', 'error');
        }
    } catch (error) {
        showModalAlert('enrollStudentAlert', 'Failed to enroll student', 'error');
    }
}

async function loadCoursesAndFacultyForClass() {
    try {
        const [coursesRes, facultyRes] = await Promise.all([
            fetch(`${API_BASE_URL}/admin/courses/all/get`, {
                headers: { 'Authorization': `Bearer ${authToken}` }
            }),
            fetch(`${API_BASE_URL}/admin/faculty_members/all/get`, {
                headers: { 'Authorization': `Bearer ${authToken}` }
            })
        ]);

        if (coursesRes.ok) {
            const courses = await coursesRes.json();
            const courseSelect = document.getElementById('newClassCourse');
            courseSelect.innerHTML =
                '<option value="">Select Course</option>' +
                courses.map(c =>
                    `<option value="${c.id}">${c.name} (${c.code})</option>`
                ).join('');
        }

        if (facultyRes.ok) {
            const faculty = await facultyRes.json();

            const newFacultySelect = document.getElementById('newClassFaculty');
            const editFacultySelect = document.getElementById('editClassFaculty');

            if (newFacultySelect) {
                newFacultySelect.innerHTML =
                    '<option value="">Select Faculty</option>' +
                    faculty.map(f =>
                        `<option value="${f.facultyMemberId}">${f.name}</option>`
                    ).join('');
            }

            if (editFacultySelect) {
                editFacultySelect.innerHTML =
                    '<option value="">Select Faculty</option>' +
                    faculty.map(f =>
                        `<option value="${f.facultyMemberId}">${f.name}</option>`
                    ).join('');
            }
        }
    } catch (error) {
        console.error('Failed to load courses/faculty:', error);
    }
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
        if (!currentUser) return;

        showDashboard();
    });
    }


}

function updateProfileUIByRole() {
    const editBtn = document.getElementById('editProfileBtn');

    if (!editBtn || !currentUser) return;

    if (currentUser.role === 'STUDENT') {
        editBtn.style.display = 'none';
    } else {
        editBtn.style.display = 'inline-block';
    }
}


function showAlert(elementId, message, type) {
    const alertDiv = document.getElementById(elementId);
    alertDiv.innerHTML = `<div class="alert alert-${type === 'error' ? 'error' : 'success'}">${message}</div>`;
    setTimeout(() => alertDiv.innerHTML = '', 5000);
}

window.onclick = function(event) {
    if (event.target.classList.contains('modal')) {
        event.target.classList.remove('active');
    }
}