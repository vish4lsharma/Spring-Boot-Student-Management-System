const API_URL = 'http://localhost:8080/api/students';

// DOM Elements
const studentsTable = document.getElementById('studentList');
const loadingState = document.getElementById('loadingState');
const emptyState = document.getElementById('emptyState');
const modal = document.getElementById('studentModal');
const studentForm = document.getElementById('studentForm');
const modalTitle = document.getElementById('modalTitle');
const searchInput = document.getElementById('searchInput');
const toast = document.getElementById('toast');

// Buttons
const addBtn = document.getElementById('addStudentBtn');
const closeBtn = document.getElementById('closeModalBtn');
const cancelBtn = document.getElementById('cancelBtn');

let allStudents = [];

// Initialize
document.addEventListener('DOMContentLoaded', fetchStudents);

// Fetch Students
async function fetchStudents() {
    showLoading(true);
    try {
        const response = await fetch(API_URL);
        const data = await response.json();

        if (data.success) {
            allStudents = data.data;
            renderStudents(allStudents);
        }
    } catch (error) {
        showToast('Failed to connect to the server', 'error');
    } finally {
        showLoading(false);
    }
}

// Render Table
function renderStudents(students) {
    studentsTable.innerHTML = '';

    if (students.length === 0) {
        emptyState.style.display = 'block';
        studentsTable.parentElement.style.display = 'none';
        return;
    }

    emptyState.style.display = 'none';
    studentsTable.parentElement.style.display = 'table';

    students.forEach(student => {
        const tr = document.createElement('tr');
        tr.innerHTML = `
            <td>
                <div class="student-name">${student.firstName} ${student.lastName}</div>
            </td>
            <td>${student.email}</td>
            <td><span class="badge">${student.department}</span></td>
            <td>${student.enrollmentDate}</td>
            <td>
                <button class="action-btn edit" onclick="editStudent(${student.id})" title="Edit">
                    <svg width="18" height="18" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15.232 5.232l3.536 3.536m-2.036-5.036a2.5 2.5 0 113.536 3.536L6.5 21.036H3v-3.572L16.732 3.732z"/>
                    </svg>
                </button>
                <button class="action-btn delete" onclick="deleteStudent(${student.id})" title="Delete">
                    <svg width="18" height="18" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16"/>
                    </svg>
                </button>
            </td>
        `;
        studentsTable.appendChild(tr);
    });
}

// Form Submit (Create / Update)
studentForm.addEventListener('submit', async (e) => {
    e.preventDefault();

    const id = document.getElementById('studentId').value;
    const studentData = {
        firstName: document.getElementById('firstName').value,
        lastName: document.getElementById('lastName').value,
        email: document.getElementById('email').value,
        department: document.getElementById('department').value,
        enrollmentDate: document.getElementById('enrollmentDate').value
    };

    const method = id ? 'PUT' : 'POST';
    const url = id ? `${API_URL}/${id}` : API_URL;

    try {
        const response = await fetch(url, {
            method,
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(studentData)
        });

        const data = await response.json();

        if (response.ok && data.success) {
            showToast(data.message);
            closeModal();
            fetchStudents();
        } else {
            showToast(data.message || 'Validation Failed', 'error');
        }
    } catch (error) {
        showToast('Error saving student', 'error');
    }
});

// Edit Student
window.editStudent = (id) => {
    const student = allStudents.find(s => s.id === id);
    if (!student) return;

    modalTitle.textContent = 'Edit Student';
    document.getElementById('studentId').value = student.id;
    document.getElementById('firstName').value = student.firstName;
    document.getElementById('lastName').value = student.lastName;
    document.getElementById('email').value = student.email;
    document.getElementById('department').value = student.department;
    document.getElementById('enrollmentDate').value = student.enrollmentDate;

    openModal();
};

// Delete Student
window.deleteStudent = async (id) => {
    if (!confirm('Are you sure you want to delete this student?')) return;

    try {
        const response = await fetch(`${API_URL}/${id}`, { method: 'DELETE' });
        const data = await response.json();

        if (response.ok && data.success) {
            showToast(data.message);
            fetchStudents();
        } else {
            showToast('Failed to delete student', 'error');
        }
    } catch (error) {
        showToast('Error deleting student', 'error');
    }
};

// Search Filter
searchInput.addEventListener('input', (e) => {
    const term = e.target.value.toLowerCase();
    const filtered = allStudents.filter(s =>
        s.firstName.toLowerCase().includes(term) ||
        s.lastName.toLowerCase().includes(term) ||
        s.email.toLowerCase().includes(term) ||
        s.department.toLowerCase().includes(term)
    );
    renderStudents(filtered);
});

// Modal UI Handlers
function openModal() {
    modal.classList.add('active');
}

function closeModal() {
    modal.classList.remove('active');
    studentForm.reset();
    document.getElementById('studentId').value = '';
    modalTitle.textContent = 'New Student';
}

addBtn.addEventListener('click', openModal);
closeBtn.addEventListener('click', closeModal);
cancelBtn.addEventListener('click', closeModal);
modal.addEventListener('click', (e) => {
    if (e.target === modal) closeModal();
});

// Utilities
function showLoading(show) {
    loadingState.style.display = show ? 'block' : 'none';
    if (show) {
        studentsTable.parentElement.style.display = 'none';
        emptyState.style.display = 'none';
    }
}

function showToast(message, type = 'success') {
    toast.textContent = message;
    toast.className = `toast show ${type}`;

    setTimeout(() => {
        toast.className = 'toast';
    }, 3000);
}
