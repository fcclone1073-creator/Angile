// Notification Management Functions

let allNotifications = [];

async function renderNotifications(container) {
    const tpl = document.getElementById('tpl-notifications');
    container.innerHTML = '';
    container.appendChild(tpl.content.cloneNode(true));

    try {
        const response = await fetch(`${CONFIG.API_URL}/notifications`);
        allNotifications = await response.json();

        calculateNotificationStats(allNotifications);
        renderNotificationTable(allNotifications);

        // Setup filters
        document.getElementById('searchNotif')?.addEventListener('input', filterNotifications);
        document.getElementById('filterNotifType')?.addEventListener('change', filterNotifications);

    } catch (err) {
        console.error(err);
        const tbody = document.getElementById('notifTableBody');
        if (tbody) tbody.innerHTML = `<tr><td colspan="7" class="text-center text-red-500">Lỗi tải dữ liệu</td></tr>`;
    }
}

function calculateNotificationStats(notifications) {
    const total = notifications.length;
    const active = notifications.filter(n => n.status === 'sent').length;
    const pending = notifications.filter(n => n.status === 'pending').length;
    const expired = notifications.filter(n => n.status === 'expired').length;

    document.getElementById('notif-total').innerText = total;
    document.getElementById('notif-active').innerText = active;
    document.getElementById('notif-pending').innerText = pending;
    document.getElementById('notif-expired').innerText = expired;
}

function renderNotificationTable(notifications) {
    const tbody = document.getElementById('notifTableBody');

    if (notifications.length === 0) {
        tbody.innerHTML = '<tr><td colspan="7" class="text-center">Chưa có thông báo nào</td></tr>';
        return;
    }

    tbody.innerHTML = notifications.map(notif => {
        const typeBadge = getNotifTypeBadge(notif.type);
        const statusBadge = getNotifStatusBadge(notif.status);
        const createdDate = notif.createdAt ? new Date(notif.createdAt).toLocaleDateString('vi-VN') : '-';
        const expireDate = notif.expireDate ? new Date(notif.expireDate).toLocaleDateString('vi-VN') : 'Không giới hạn';

        return `
            <tr>
                <td style="font-weight: 600;">${notif.title}</td>
                <td>${typeBadge}</td>
                <td>${statusBadge}</td>
                <td style="text-align: center; color: #666;">${notif.readCount || 0}</td>
                <td style="color: #666; font-size: 0.875rem;">${createdDate}</td>
                <td style="color: #666; font-size: 0.875rem;">${expireDate}</td>
                <td>
                    <button class="action-btn" title="Kích hoạt/Vô hiệu hóa" onclick="toggleNotifStatus('${notif._id}')">
                        <i class="fas ${notif.status === 'sent' ? 'fa-toggle-on' : 'fa-toggle-off'}" 
                           style="color: ${notif.status === 'sent' ? '#10b981' : '#6b7280'};"></i>
                    </button>
                    <button class="action-btn" title="Sửa" onclick="editNotification('${notif._id}')">
                        <i class="fas fa-edit" style="color: #3b82f6;"></i>
                    </button>
                    <button class="action-btn" title="Xóa" onclick="deleteNotification('${notif._id}')">
                        <i class="fas fa-trash-alt" style="color: #ef4444;"></i>
                    </button>
                </td>
            </tr>
        `;
    }).join('');
}

function filterNotifications() {
    const searchVal = document.getElementById('searchNotif')?.value.toLowerCase() || '';
    const typeFilter = document.getElementById('filterNotifType')?.value || '';

    let filtered = allNotifications.filter(notif => {
        const matchesSearch = !searchVal ||
            notif.title.toLowerCase().includes(searchVal) ||
            (notif.content && notif.content.toLowerCase().includes(searchVal));
        const matchesType = !typeFilter || notif.type === typeFilter;

        return matchesSearch && matchesType;
    });

    renderNotificationTable(filtered);
}

window.editNotification = function (id) {
    const notif = allNotifications.find(n => n._id === id);
    if (!notif) return;

    openAddNotifModal();
    document.getElementById('notificationModalTitle').innerText = 'Cập nhật thông báo';
    document.getElementById('notificationId').value = notif._id;
    document.getElementById('notifTitle').value = notif.title;
    document.getElementById('notifType').value = notif.type;
    document.getElementById('notifContent').value = notif.content || '';
    document.getElementById('notifStatus').value = notif.status;
}

window.toggleNotifStatus = async function (id) {
    const notif = allNotifications.find(n => n._id === id);
    if (!notif) return;

    const newStatus = notif.status === 'sent' ? 'expired' : 'sent';

    try {
        const res = await fetch(`${CONFIG.API_URL}/notifications/${id}`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ ...notif, status: newStatus })
        });

        if (res.ok) {
            const contentDiv = document.getElementById('content');
            renderNotifications(contentDiv);
        } else {
            alert('Lỗi khi cập nhật trạng thái');
        }
    } catch (err) {
        console.error(err);
    }
}

window.deleteNotification = async function (id) {
    if (!confirm('Bạn có chắc muốn xóa thông báo này?')) return;

    try {
        const res = await fetch(`${CONFIG.API_URL}/notifications/${id}`, {
            method: 'DELETE'
        });

        if (res.ok) {
            alert('Đã xóa thông báo');
            const contentDiv = document.getElementById('content');
            renderNotifications(contentDiv);
        } else {
            alert('Lỗi khi xóa');
        }
    } catch (err) {
        console.error(err);
    }
}

function getNotifTypeBadge(type) {
    const typeMap = {
        'welcome': { text: 'Chào mừng', class: 'badge-delivered' },
        'promotion': { text: 'Khuyến mãi', class: 'badge-processing' },
        'policy': { text: 'Chính sách', class: 'badge-shipped' },
        'maintenance': { text: 'Bảo trì', class: 'badge-cancelled' },
        'general': { text: 'Chung', class: 'badge-pending' },
        'system': { text: 'Hệ thống', class: 'badge-pending' }
    };

    const badge = typeMap[type] || { text: type, class: 'badge-pending' };
    return `<span class="status-badge ${badge.class}">${badge.text}</span>`;
}

function getNotifStatusBadge(status) {
    const statusMap = {
        'sent': { text: 'Đã gửi', class: 'badge-delivered' },
        'expired': { text: 'Hết hạn', class: 'badge-cancelled' },
        'pending': { text: 'Chờ gửi', class: 'badge-pending' }
    };

    const badge = statusMap[status] || { text: status, class: 'badge-pending' };
    return `<span class="status-badge ${badge.class}">${badge.text}</span>`;
}
