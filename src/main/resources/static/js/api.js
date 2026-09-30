const API_BASE = window.location.origin;

function getToken() { return localStorage.getItem('token'); }
function getRole() { return localStorage.getItem('role'); }
function getUsername() { return localStorage.getItem('username'); }

function requireAuth() {
    if (!getToken()) window.location.href = 'login.html';
}

function requireRole(role) {
    requireAuth();
    if (getRole() !== role) {
        alert(`Access denied. This page is for ${role} accounts only.`);
        window.location.href = 'index.html';
    }
}

function logout() {
    localStorage.clear();
    window.location.href = 'login.html';
}

async function apiCall(endpoint, options = {}) {
    const headers = { 'Content-Type': 'application/json', ...(options.headers || {}) };
    if (getToken()) headers['Authorization'] = `Bearer ${getToken()}`;

    const res = await fetch(`${API_BASE}${endpoint}`, { ...options, headers });

    if (res.status === 401) {
        logout();
        throw new Error('Session expired. Please login again.');
    }

    if (!res.ok) {
        const err = await res.json().catch(() => ({ message: 'Something went wrong' }));
        const msg = err.message || err.error ||
            (err.errors ? Object.values(err.errors).join(', ') : 'Request failed');
        throw new Error(msg);
    }

    if (res.status === 204) return null;
    return res.json();
}

function showToast(message, type = 'success') {
    let toast = document.getElementById('toast');
    if (!toast) {
        toast = document.createElement('div');
        toast.id = 'toast';
        toast.className = 'toast';
        document.body.appendChild(toast);
    }
    toast.textContent = message;
    toast.className = `toast ${type} show`;
    setTimeout(() => toast.classList.remove('show'), 3000);
}

function renderNavbar(activePage) {
    const role = getRole();
    const isAdmin = role === 'ADMIN';
    return `
    <nav class="navbar">
        <div class="brand" style="cursor:pointer" onclick="window.location.href='index.html'">📚 Library</div>
        <div class="nav-links">
            <a class="nav-link-btn" href="index.html">📖 Catalog</a>
            ${isAdmin ? `<a class="nav-link-btn" href="add-book.html">➕ Add Book</a>` : ''}
            ${!isAdmin ? `<a class="nav-link-btn" href="my-borrows.html">📋 My Borrows</a>` : ''}
            ${isAdmin ? `<a class="nav-link-btn" href="manage-members.html">👥 Members</a>` : ''}
        </div>
        <div class="user-info">
            <span class="badge ${!isAdmin ? 'member' : ''}">${role}</span>
            <span style="font-weight:600; font-size:14px;">${getUsername()}</span>
            <button class="btn-logout" onclick="logout()">Logout</button>
        </div>
    </nav>`;
}