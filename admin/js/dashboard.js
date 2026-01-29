// Check authentication
document.addEventListener('DOMContentLoaded', () => {
    const token = localStorage.getItem('token');
    const userStr = localStorage.getItem('user');

    // Uncomment to enforce login
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
        window.location.href = 'index.html';
        return;
    }

    // Restore last view or default
    const lastView = localStorage.getItem('currentView') || 'dashboard';
    loadView(lastView);
});

function logout() {
    localStorage.removeItem('token');
    localStorage.removeItem('user');
    localStorage.removeItem('currentView'); // Clear view state
    window.location.href = 'index.html';
}

function loadView(viewName, menuItem) {
    // Save state
    localStorage.setItem('currentView', viewName);

    // Find menu item if not provided (state restore case)
    if (!menuItem) {
        // Try to find the link with the matching viewName in its onclick
        const links = document.querySelectorAll('.menu-item');
        for (let link of links) {
            if (link.getAttribute('onclick') && link.getAttribute('onclick').includes(`'${viewName}'`)) {
                menuItem = link;
                break;
            }
        }
    }

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
            pageTitle.innerText = 'Dashboard'; // Reset title
            renderDashboard(contentDiv);
            break;
        case 'products':
            pageTitle.innerText = 'Quản lý sản phẩm';
            renderProducts(contentDiv);
            break;
        case 'customers':
            pageTitle.innerText = 'Quản lý khách hàng';
            renderCustomers(contentDiv);
            break;
        case 'orders':
            // TEMPORARY: Empty state for Orders to avoid 404 text
            pageTitle.innerText = 'Quản lý đơn hàng';
            contentDiv.innerHTML = '<div class="p-4"><h3>Danh sách đơn hàng</h3><p>Tính năng đang phát triển...</p></div>';
            break;
        case 'settings':
            pageTitle.innerText = 'Cài đặt hệ thống';
            contentDiv.innerHTML = '<div class="table-container p-4"><h3>Cấu hình hệ thống (Chỉ Admin thấy)</h3><p>Server Port: 5000</p><p>Version: 1.0.0</p></div>';
            break;
        default:
            // Fallback for unimplemented views
            pageTitle.innerText = 'Chức năng chưa có';
            contentDiv.innerHTML = '<div class="p-4"><p>Tính năng này chưa được triển khai.</p></div>';
    }
}

// Logic for Dashboard View
async function renderDashboard(container) {
    const tpl = document.getElementById('tpl-dashboard');
    container.innerHTML = '';
    container.appendChild(tpl.content.cloneNode(true));

    try {
        const [products, customers, orders] = await Promise.all([
            fetch(`${CONFIG.API_URL}/products?limit=1000`).then(r => r.json()),
            fetch(`${CONFIG.API_URL}/auth/users`).then(r => r.json()),
            fetch(`${CONFIG.API_URL}/orders`).then(r => r.json())
        ]);

        // Products Stats
        const totalProducts = products.length || 0;
        const outOfStock = products.filter(p => p.countInStock === 0).length;
        const onSale = products.filter(p => p.oldPrice && p.oldPrice > p.price).length;

        // Users Stats
        const totalUsers = customers.length || 0;

        // Orders Stats
        const totalOrders = orders.length || 0;
        const revenue = orders.reduce((acc, order) => acc + (order.totalPrice || 0), 0);

        // Update UI
        document.getElementById('dash-total-products').innerText = totalProducts;
        document.getElementById('dash-out-stock').innerText = outOfStock;
        document.getElementById('dash-promo').innerText = onSale;

        document.getElementById('dash-total-users').innerText = totalUsers;
        document.getElementById('dash-total-orders').innerText = totalOrders;
        document.getElementById('dash-revenue').innerText = formatCurrency(revenue);

    } catch (e) {
        console.error("Error loading stats", e);
        // Simple fallback or user alert if needed
    }
}

// Logic for Products View
let allProducts = []; // Store locally for filtering

async function renderProducts(container) {
    const tpl = document.getElementById('tpl-products');
    container.innerHTML = '';
    container.appendChild(tpl.content.cloneNode(true));

    const tbody = document.getElementById('productTableBody');
    const categorySelect = document.getElementById('filterCategory');

    // Stats Elements
    const statTotal = document.getElementById('stat-total-products');
    const statMale = document.getElementById('stat-male-products');
    const statFemale = document.getElementById('stat-female-products');

    tbody.innerHTML = '<tr><td colspan="7" class="text-center">Đang tải dữ liệu...</td></tr>';

    try {
        // Fetch Data
        const [productsRes, categoriesRes] = await Promise.all([
            fetch(`${CONFIG.API_URL}/products?limit=100`),
            fetch(`${CONFIG.API_URL}/products/categories`)
        ]);

        allProducts = await productsRes.json(); // Store global
        const categories = await categoriesRes.json();

        // 1. Populate Category Filter & Modal Selection
        const catOptions = categories.map(c => `<option value="${c._id}">${c.name}</option>`).join('');
        categorySelect.innerHTML = '<option value="">Tất cả danh mục</option>' + catOptions;

        // Populate Modal Category Select as well (cached for valid HTML element finding later or now)
        window.categoryOptions = catOptions; // Hacky share

        // 2. Calculate Stats
        statTotal.innerText = allProducts.length;
        statMale.innerText = allProducts.filter(p => p.gender === 'Nam').length;
        statFemale.innerText = allProducts.filter(p => p.gender === 'Nữ').length;

        // 3. Render Table
        renderProductTable(allProducts, categories);

        // 4. Setup Filter Listeners
        document.getElementById('searchProduct').addEventListener('input', (e) => filterProducts(e.target.value, 'name'));
        document.getElementById('filterGender').addEventListener('change', (e) => filterProducts(e.target.value, 'gender'));
        document.getElementById('filterCategory').addEventListener('change', (e) => filterProducts(e.target.value, 'category'));
        document.getElementById('filterStatus').addEventListener('change', (e) => filterProducts(e.target.value, 'status'));

        // Setup Modal Form
        document.getElementById('productForm').addEventListener('submit', saveProduct);

    } catch (e) {
        console.error(e);
        tbody.innerHTML = `<tr><td colspan="7" class="text-center text-red-500">Lỗi: ${e.message}</td></tr>`;
    }
}

function renderProductTable(products, categories) {
    const tbody = document.getElementById('productTableBody');
    if (products.length === 0) {
        tbody.innerHTML = '<tr><td colspan="7" class="text-center">Không tìm thấy sản phẩm</td></tr>';
        return;
    }

    const catMap = {};
    categories.forEach(c => catMap[c._id] = c.name);

    tbody.innerHTML = products.map(p => `
        <tr>
            <td>
                <div class="flex items-center">
                    <img src="${p.image}" style="width: 40px; height: 40px; border-radius: 4px; object-fit: cover; margin-right: 10px;">
                    <div>
                        <div style="font-weight: 500;">${p.name}</div>
                        <div style="font-size: 0.8rem; color: #777;">ID: ${p._id.substring(p._id.length - 6)}</div>
                    </div>
                </div>
            </td>
            <td style="font-weight: 600; color: #e53935;">${formatCurrency(p.price)}</td>
            <td><span style="background: #e0f2fe; color: #0369a1; padding: 2px 8px; border-radius: 4px; font-size: 0.8rem;">${catMap[p.categoryId] || 'Khác'}</span></td>
            <td><span style="font-size: 0.85rem;" class="${p.gender === 'Nữ' ? 'text-pink-500' : 'text-blue-600'}">${p.gender || 'Unisex'}</span></td>
            <td style="color: #666;">${new Date(p.createdAt).toLocaleDateString()}</td>
            <td>
                <span class="status-badge ${p.status === 'active' ? 'status-active' : 'status-inactive'}">
                    ${p.status === 'active' ? 'Hoạt động' : 'Đã ẩn'}
                </span>
            </td>
            <td>
                <button class="action-btn" title="Xem" onclick="event.stopPropagation()"><i class="fas fa-eye" style="color: #6366f1;"></i></button>
                <button class="action-btn" title="Sửa" onclick="editProduct('${p._id}')"><i class="fas fa-edit" style="color: #f59e0b;"></i></button>
                <button class="action-btn" title="Xóa" onclick="deleteProduct('${p._id}')"><i class="fas fa-trash-alt" style="color: #ef4444;"></i></button>
            </td>
        </tr>
    `).join('');
}

function filterProducts(val, type) {
    // Basic Client-side filtering for speed
    // In real app, call API with query params
    let filtered = allProducts;
    const catVal = document.getElementById('filterCategory').value;
    const genderVal = document.getElementById('filterGender').value;
    const searchVal = document.getElementById('searchProduct').value.toLowerCase();

    if (catVal) filtered = filtered.filter(p => p.categoryId === catVal);
    if (genderVal) filtered = filtered.filter(p => p.gender === genderVal);

    // Status Filter
    const statusVal = document.getElementById('filterStatus').value;
    if (statusVal) filtered = filtered.filter(p => p.status === statusVal);

    if (searchVal) filtered = filtered.filter(p => p.name.toLowerCase().includes(searchVal));

    // Need categories map again, fetch or store global. 
    // Quick fix: pass empty categories to render (names will be missing), OR make categories global too.
    // Let's refetch categories from Select options for display mapping
    const categories = Array.from(document.getElementById('filterCategory').options).slice(1).map(opt => ({ _id: opt.value, name: opt.text }));
    renderProductTable(filtered, categories);
}


// --- CRUD Functions ---

window.openAddProductModal = function () {
    document.getElementById('productForm').reset();
    document.getElementById('productId').value = '';
    document.getElementById('modalTitle').innerText = 'Thêm sản phẩm mới';

    // Fill categories in modal
    const prodCategory = document.getElementById('prodCategory');
    prodCategory.innerHTML = window.categoryOptions || '';

    document.getElementById('productModal').classList.remove('hidden');
}

window.closeModal = function () {
    document.getElementById('productModal').classList.add('hidden');
}

window.saveProduct = async function (e) {
    e.preventDefault();

    const id = document.getElementById('productId').value;
    const data = {
        name: document.getElementById('prodName').value,
        price: Number(document.getElementById('prodPrice').value),
        oldPrice: Number(document.getElementById('prodOldPrice').value || 0),
        categoryId: document.getElementById('prodCategory').value,
        gender: document.getElementById('prodGender').value,
        countInStock: Number(document.getElementById('prodStock').value),
        image: document.getElementById('prodImage').value,
        status: document.getElementById('prodStatus').value
    };

    const method = id ? 'PUT' : 'POST';
    const url = id ? `${CONFIG.API_URL}/products/${id}` : `${CONFIG.API_URL}/products`;

    try {
        const res = await fetch(url, {
            method: method,
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(data)
        });

        if (res.ok) {
            alert('Lưu sản phẩm thành công!');
            closeModal();
            // Refresh
            const contentDiv = document.getElementById('content');
            renderProducts(contentDiv);
        } else {
            alert('Lỗi khi lưu sản phẩm');
        }
    } catch (err) {
        console.error(err);
        alert('Lỗi kết nối');
    }
}

window.editProduct = function (id) {
    const p = allProducts.find(x => x._id === id);
    if (!p) return;

    openAddProductModal();
    document.getElementById('modalTitle').innerText = 'Cập nhật sản phẩm';
    document.getElementById('productId').value = p._id;
    document.getElementById('prodName').value = p.name;
    document.getElementById('prodPrice').value = p.price;
    document.getElementById('prodOldPrice').value = p.oldPrice || '';
    document.getElementById('prodCategory').value = p.categoryId;
    document.getElementById('prodGender').value = p.gender || 'Unisex';
    document.getElementById('prodStock').value = p.countInStock;
    document.getElementById('prodImage').value = p.image;
    document.getElementById('prodStatus').value = p.status || 'active';
}

window.deleteProduct = async function (id) {
    if (!confirm('Bạn có chắc chắn muốn xóa sản phẩm này không?')) return;

    try {
        const res = await fetch(`${CONFIG.API_URL}/products/${id}`, {
            method: 'DELETE'
        });

        if (res.ok) {
            alert('Đã xóa sản phẩm');
            const contentDiv = document.getElementById('content');
            renderProducts(contentDiv);
        } else {
            alert('Lỗi khi xóa');
        }
    } catch (err) {
        console.error(err);
    }
}


// Logic for Customers View
let allCustomers = [];

async function renderCustomers(container) {
    const tpl = document.getElementById('tpl-customers');
    container.innerHTML = '';
    container.appendChild(tpl.content.cloneNode(true));

    const tbody = document.getElementById('customerTableBody');
    tbody.innerHTML = '<tr><td colspan="6" class="text-center">Đang tải...</td></tr>';

    try {
        const response = await fetch(`${CONFIG.API_URL}/auth/users`);
        allCustomers = await response.json();

        // Calculate Stats
        const total = allCustomers.length;
        const active = allCustomers.filter(c => c.status === 'active').length;
        const disabled = allCustomers.filter(c => c.status === 'disabled').length;

        document.getElementById('cust-total').innerText = total;
        document.getElementById('cust-active').innerText = active;
        document.getElementById('cust-disabled').innerText = disabled;

        // Update Filter Count
        const filterOption = document.getElementById('cust-filter-all');
        if (filterOption) filterOption.innerText = `Tất cả trạng thái (${total})`;

        renderCustomerTable(allCustomers);



        // Setup Filter Listeners
        const inputs = ['searchCustomer', 'filterCustStatus', 'filterCustGender', 'sortCustField', 'sortCustOrder'];
        inputs.forEach(id => {
            const el = document.getElementById(id);
            if (el) el.addEventListener(id.includes('search') ? 'input' : 'change', filterCustomers);
        });

    } catch (e) {
        console.error(e);
        tbody.innerHTML = `<tr><td colspan="6" class="text-center text-red-500">Lỗi kết nối server: ${e.message}</td></tr>`;
    }
}

function filterCustomers() {
    let filtered = [...allCustomers];

    // 1. Search
    const searchVal = document.getElementById('searchCustomer').value.toLowerCase();
    if (searchVal) {
        filtered = filtered.filter(c =>
            (c.name && c.name.toLowerCase().includes(searchVal)) ||
            (c.email && c.email.toLowerCase().includes(searchVal)) ||
            (c.phone && c.phone.includes(searchVal))
        );
    }

    // 2. Status
    const statusVal = document.getElementById('filterCustStatus').value;
    if (statusVal) {
        filtered = filtered.filter(c => c.status === statusVal);
    }

    // 3. Gender
    // Note: DB values might be 'Nam'/'Nữ' (Capitalized) or 'nam'/'nu' (lowercase). 
    // Check seed data: 'Nam', 'Nữ'. HTML values: 'Nam', 'Nữ'.
    const genderVal = document.getElementById('filterCustGender').value;
    if (genderVal) {
        filtered = filtered.filter(c => c.gender === genderVal);
    }

    // 4. Sort
    const sortField = document.getElementById('sortCustField').value; // 'id' or 'name'
    const sortOrder = document.getElementById('sortCustOrder').value; // 'asc' or 'desc'

    filtered.sort((a, b) => {
        let valA, valB;
        if (sortField === 'id') {
            valA = a._id;
            valB = b._id;
        } else {
            valA = (a.name || '').toLowerCase();
            valB = (b.name || '').toLowerCase();
        }

        if (valA < valB) return sortOrder === 'asc' ? -1 : 1;
        if (valA > valB) return sortOrder === 'asc' ? 1 : -1;
        return 0;
    });

    renderCustomerTable(filtered);
}

// Setup Modal Form
document.getElementById('userForm').addEventListener('submit', saveUser);




function renderCustomerTable(customers) {
    const tbody = document.getElementById('customerTableBody');
    if (customers.length === 0) {
        tbody.innerHTML = '<tr><td colspan="6" class="text-center">Chưa có khách hàng nào</td></tr>';
        return;
    }

    tbody.innerHTML = customers.map(c => `
        <tr>
            <td>
                <div class="flex items-center">
                   <div class="icon-box ${c.gender === 'Nữ' ? 'bg-pink-100 text-pink-500' : 'bg-blue-100 text-blue-600'}" style="width: 32px; height: 32px; margin-right: 10px; font-size: 0.8rem; border-radius: 50%;">
                        <i class="fas fa-user"></i>
                   </div>
                   <div>
                       <div style="font-weight: 500;">${c.name}</div>
                       <div style="font-size: 0.8rem; color: #777;">${c.role === 'admin' ? '<span class="text-red-500">Admin</span>' : 'Khách hàng'}</div>
                   </div>
                </div>
            </td>
            <td><span class="status-badge ${c.gender === 'Nữ' ? 'bg-pink-100 text-pink-500' : 'bg-blue-100 text-blue-600'}">${c.gender || 'Nam'}</span></td>
            <td style="color: #555;">${c.email}</td>
            <td style="color: #555;">${c.phone}</td>
            <td>
                <span class="status-badge ${c.status === 'active' ? 'status-active' : 'status-inactive'}">
                    ${c.status === 'active' ? 'Đang hoạt động' : 'Vô hiệu hóa'}
                </span>
            </td>
            <td>
                 <button class="action-btn" title="Đổi trạng thái" onclick="toggleCustomerStatus('${c._id}', '${c.status}')">
                    <i class="fas fa-power-off ${c.status === 'active' ? 'text-green-600' : 'text-red-500'}"></i>
                </button>
                <button class="action-btn" title="Sửa" onclick="editCustomer('${c._id}')">
                    <i class="fas fa-edit" style="color: #3b82f6;"></i>
                </button>
                 <button class="action-btn" title="Xóa" onclick="deleteUser('${c._id}')">
                    <i class="fas fa-trash-alt" style="color: #ef4444;"></i>
                </button>
            </td>
        </tr>
    `).join('');
}

// --- Customer CRUD ---

window.openAddCustomerModal = function () {
    document.getElementById('userForm').reset();
    document.getElementById('userId').value = '';
    document.getElementById('userModalTitle').innerText = 'Thêm khách hàng mới';
    document.getElementById('userModal').classList.remove('hidden');
}

window.closeUserModal = function () {
    document.getElementById('userModal').classList.add('hidden');
}

window.saveUser = async function (e) {
    e.preventDefault();
    const id = document.getElementById('userId').value;
    const data = {
        name: document.getElementById('userNameInput').value,
        email: document.getElementById('userEmail').value,
        phone: document.getElementById('userPhone').value,
        gender: document.getElementById('userGender').value,
        status: document.getElementById('userStatus').value,
        role: document.getElementById('userRole').value
    };

    // Only send password if entered (for edit) or required (for new) -> logic simplified
    const pass = document.getElementById('userPassword').value;
    if (pass) data.password = pass;

    const method = id ? 'PUT' : 'POST';
    const url = id ? `${CONFIG.API_URL}/auth/users/${id}` : `${CONFIG.API_URL}/auth/users`;

    try {
        const res = await fetch(url, {
            method: method,
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(data)
        });

        const resData = await res.json();

        if (res.ok) {
            alert('Lưu khách hàng thành công!');
            closeUserModal();
            const contentDiv = document.getElementById('content');
            renderCustomers(contentDiv);
        } else {
            alert(resData.msg || 'Lỗi khi lưu khách hàng');
        }
    } catch (err) {
        console.error(err);
        alert('Lỗi kết nối server');
    }
}

window.editCustomer = function (id) {
    const c = allCustomers.find(x => x._id === id);
    if (!c) return;

    openAddCustomerModal();
    document.getElementById('userModalTitle').innerText = 'Cập nhật khách hàng';
    document.getElementById('userId').value = c._id;
    document.getElementById('userNameInput').value = c.name;
    document.getElementById('userEmail').value = c.email;
    document.getElementById('userPhone').value = c.phone;
    document.getElementById('userGender').value = c.gender || 'Nam';
    document.getElementById('userStatus').value = c.status || 'active';
    document.getElementById('userRole').value = c.role || 'user';
    document.getElementById('userPassword').value = ''; // Don't show hash
}

window.toggleCustomerStatus = async function (id, currentStatus) {
    const newStatus = currentStatus === 'active' ? 'disabled' : 'active';
    if (!confirm(`Bạn có chắc muốn đổi trạng thái thành ${newStatus === 'active' ? 'Hoạt động' : 'Vô hiệu hóa'}?`)) return;

    try {
        const res = await fetch(`${CONFIG.API_URL}/auth/users/${id}`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ status: newStatus })
        });
        if (res.ok) {
            const contentDiv = document.getElementById('content');
            renderCustomers(contentDiv);
        } else {
            alert('Lỗi cập nhật trạng thái');
        }
    } catch (err) {
        console.error(err);
    }
}

window.deleteUser = async function (id) {
    if (!confirm('Bạn có chắc chắn muốn xóa người dùng này?')) return;
    try {
        const res = await fetch(`${CONFIG.API_URL}/auth/users/${id}`, {
            method: 'DELETE'
        });
        if (res.ok) {
            alert('Đã xóa người dùng');
            const contentDiv = document.getElementById('content');
            renderCustomers(contentDiv);
        } else {
            alert('Lỗi khi xóa');
        }
    } catch (err) {
        console.error(err);
    }
}
