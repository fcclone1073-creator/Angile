// Check authentication
document.addEventListener('DOMContentLoaded', () => {
    const token = localStorage.getItem('token');
    const userStr = localStorage.getItem('user');

    // Uncomment to enforce login (disabled for dev ease if desired)
    // if (!token) {
    //     window.location.href = 'index.html';
    //     return;
    // }

    if (userStr) {
        const user = JSON.parse(userStr);
        document.getElementById('userName').innerText = user.name || 'Admin';

        // Authorization Check
        if (user.role !== 'admin' && user.role !== 'manager') {
            alert('Bạn không có quyền truy cập vào trang này (Role: ' + user.role + ')');
            window.location.href = 'index.html';
            return;
        }

        // Role based UI
        if (user.role === 'admin') {
            document.querySelectorAll('.role-admin').forEach(el => el.classList.remove('hidden'));
        }
    } else {
        // No user found, redirect
        window.location.href = 'index.html';
        return;
    }

    // Default load
    loadView('dashboard');
});

function logout() {
    localStorage.removeItem('token');
    localStorage.removeItem('user');
    window.location.href = 'index.html';
}

function loadView(viewName, menuItem) {
    // Update Menu Active State
    if (menuItem) {
        document.querySelectorAll('.menu-item').forEach(el => el.classList.remove('active'));
        menuItem.classList.add('active');
    }

    const contentDiv = document.getElementById('content');
    const pageTitle = document.getElementById('pageTitle');

    // Simple Router
    switch (viewName) {
        case 'dashboard':
            // pageTitle.innerText = 'Tổng quan'; // Template handles this
            renderDashboard(contentDiv);
            break;
        case 'products':
            pageTitle.innerText = 'Quản lý sản phẩm';
            renderProducts(contentDiv);
            break;
        case 'customers':
            pageTitle.innerText = 'Quản lý khách hàng';
            contentDiv.innerHTML = '<div class="table-container p-4">Tính năng đang phát triển...</div>';
            break;
        case 'settings':
            pageTitle.innerText = 'Cài đặt hệ thống';
            contentDiv.innerHTML = '<div class="table-container p-4"><h3>Cấu hình hệ thống (Chỉ Admin thấy)</h3><p>Server Port: 5000</p><p>Version: 1.0.0</p></div>';
            break;
        default:
            contentDiv.innerHTML = '<p>Page not found</p>';
    }
}

// Logic for Dashboard View
async function renderDashboard(container) {
    const tpl = document.getElementById('tpl-dashboard');
    container.innerHTML = '';
    container.appendChild(tpl.content.cloneNode(true));

    // Fetch Stats (Mocking/Real)
    // In real app, create specific endpoints for stats
    // Here we can just count items from list endpoints for demo
    try {
        const [products, customers] = await Promise.all([
            fetch(`${CONFIG.API_URL}/products?limit=1000`).then(r => r.json()),
            // fetch(`${CONFIG.API_URL}/users`).then(r => r.json()) // Assuming a users endpoint exists or we mock it
            Promise.resolve([])
        ]);

        document.getElementById('totalProducts').innerText = products.length || 0;
        document.getElementById('totalUsers').innerText = 16; // Hardcoded from UI screenshot for now
        document.getElementById('totalOrders').innerText = 194; // Hardcoded
    } catch (e) {
        console.error("Error loading stats", e);
    }
}

// Logic for Products View
async function renderProducts(container) {
    const tpl = document.getElementById('tpl-products');
    container.innerHTML = '';
    container.appendChild(tpl.content.cloneNode(true));

    const tbody = document.getElementById('productTableBody');
    tbody.innerHTML = '<tr><td colspan="5" class="text-center">Đang tải...</td></tr>';

    try {
        // Fetch products
        const res = await fetch(`${CONFIG.API_URL}/products?limit=50&sort=newest`); // Get more products
        const products = await res.json();

        if (products.length === 0) {
            tbody.innerHTML = '<tr><td colspan="5" class="text-center">Chưa có sản phẩm nào</td></tr>';
            return;
        }

        tbody.innerHTML = products.map(p => `
            <tr>
                <td>
                    <div class="flex items-center">
                        <img src="${p.image || 'https://via.placeholder.com/40'}" style="width: 40px; height: 40px; border-radius: 4px; object-fit: cover; margin-right: 10px;">
                        <div>
                            <div style="font-weight: 500;">${p.name}</div>
                            <div style="font-size: 0.8rem; color: #777;">ID: ${p._id.substr(-6)}</div>
                        </div>
                    </div>
                </td>
                <td style="font-weight: 600; color: var(--primary-color);">${formatCurrency(p.price)}</td>
                <td><span style="background: #e0f2fe; color: #0369a1; padding: 2px 8px; border-radius: 4px; font-size: 0.8rem;">${p.categoryName || 'Thời trang'}</span></td>
                <td>
                    <span class="status-badge ${p.countInStock > 0 ? 'status-active' : 'status-inactive'}">
                        ${p.countInStock > 0 ? 'Hoạt động' : 'Hết hàng'}
                    </span>
                </td>
                <td>
                    <button class="action-btn" title="Xem"><i class="fas fa-eye" style="color: #6366f1;"></i></button>
                    <button class="action-btn" title="Sửa"><i class="fas fa-edit" style="color: #f59e0b;"></i></button>
                    <button class="action-btn" title="Xóa"><i class="fas fa-trash-alt" style="color: #ef4444;"></i></button>
                </td>
            </tr>
        `).join('');

    } catch (e) {
        console.error(e);
        tbody.innerHTML = '<tr><td colspan="5" class="text-center text-red-500">Lỗi tải dữ liệu</td></tr>';
    }
}

function openAddProductModal() {
    alert("Tính năng thêm sản phẩm sẽ được cập nhật trong bước tiếp theo!");
}
