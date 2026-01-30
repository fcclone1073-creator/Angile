// Promotion Management Functions

let allPromotions = [];

async function renderPromotions(container) {
    const tpl = document.getElementById('tpl-promotions');
    container.innerHTML = '';
    container.appendChild(tpl.content.cloneNode(true));

    try {
        const response = await fetch(`${CONFIG.API_URL}/promotions`);
        allPromotions = await response.json();

        calculatePromotionStats(allPromotions);
        renderPromotionCards(allPromotions);

        document.getElementById('searchPromotion')?.addEventListener('input', filterPromotions);
        document.getElementById('filterPromotionStatus')?.addEventListener('change', filterPromotions);
        document.getElementById('filterPromotionType')?.addEventListener('change', filterPromotions);

    } catch (err) {
        console.error(err);
        const container = document.getElementById('promoGrid');
        if (container) container.innerHTML = `<p class="text-center text-red-500">Lỗi tải dữ liệu</p>`;
    }
}

function calculatePromotionStats(promotions) {
    const now = new Date();
    const total = promotions.length;
    const active = promotions.filter(p => {
        const start = p.startDate ? new Date(p.startDate) : null;
        const end = p.endDate ? new Date(p.endDate) : null;
        return p.status === 'active' && (!start || start <= now) && (!end || now <= end);
    }).length;
    const upcoming = promotions.filter(p => p.startDate && new Date(p.startDate) > now).length;
    const ended = promotions.filter(p => (p.endDate && new Date(p.endDate) < now) || p.status === 'expired').length;

    const totalEl = document.getElementById('promo-total');
    const activeEl = document.getElementById('promo-active');
    const upcomingEl = document.getElementById('promo-upcoming');
    const expiredEl = document.getElementById('promo-expired');

    if (totalEl) totalEl.innerText = total;
    if (activeEl) activeEl.innerText = active;
    if (upcomingEl) upcomingEl.innerText = upcoming;
    if (expiredEl) expiredEl.innerText = ended;
}

function renderPromotionCards(promotions) {
    const container = document.getElementById('promoGrid');

    if (!container) {
        console.error('promoGrid container not found!');
        return;
    }

    if (promotions.length === 0) {
        container.innerHTML = '<p class="text-center">Chưa có khuyến mãi nào</p>';
        return;
    }

    console.log('Rendering', promotions.length, 'promotions');

    container.innerHTML = promotions.map(promo => {
        const statusBadge = getPromotionStatusBadge(promo);
        const discountText = promo.discountText || '0%';
        const productCount = promo.productCount || 0;
        const categoryCount = promo.categoryCount || 0;
        const typeBadge = promo.type === 'flash_sale' ? 'Flash Sale' : (promo.type === 'holiday' ? 'Holiday' : 'Khuy ến mãi');

        return `
            <div class="promo-card" style="position: relative; padding: 1.5rem; background: white; border-radius: 12px; box-shadow: 0 2px 8px rgba(0,0,0,0.1); margin-bottom: 1rem;">
                ${promo.type === 'flash_sale' ? `<div class="promo-badge" style="position: absolute; top: -8px; right: -8px; background: #ef4444; color: white; padding: 6px 14px; border-radius: 8px; font-size: 0.75rem; font-weight: 700; transform: rotate(15deg); box-shadow: 0 2px 4px rgba(0,0,0,0.2);">
                    <i class="fas fa-bolt"></i> ${typeBadge}
                </div>` : ''}
                
                <div style="display: flex; align-items: center; gap: 0.75rem; margin-bottom: 0.75rem;">
                    <i class="fas fa-bullhorn" style="font-size: 1.5rem; color: #3b82f6;"></i>
                    <div style="flex: 1;">
                        <h3 style="font-weight: 700; font-size: 1.125rem; margin: 0;">${promo.title}</h3>
                        <p style="color: #666; font-size: 0.875rem; margin: 0.25rem 0 0 0;">${promo.description || ''}</p>
                    </div>
                    ${statusBadge}
                </div>

                <div style="display: flex; align-items: center; gap: 2rem; margin: 1rem 0; padding: 1rem; background: linear-gradient(135deg, #f9fafb 0%, #f3f4f6 100%); border-radius: 8px;">
                    <div style="text-align: center;">
                        <div style="font-size: 2.5rem; font-weight: 700; color: #10b981;">${discountText}</div>
                        <div style="font-size: 0.75rem; color: #666;">Giảm giá</div>
                    </div>
                    <div style="text-align: center; flex: 1;">
                        <div style="font-size: 1.5rem; font-weight: 600; color: #3b82f6;">
                            <i class="fas fa-box"></i> ${productCount}
                        </div>
                        <div style="font-size: 0.75rem; color: #666;">sản phẩm</div>
                    </div>
                    <div style="text-align: center; flex: 1;">
                        <div style="font-size: 1.5rem; font-weight: 600; color: #8b5cf6;">
                            <i class="fas fa-tags"></i> ${categoryCount}
                        </div>
                        <div style="font-size: 0.75rem; color: #666;">danh mục</div>
                    </div>
                </div>

                <div style="display: flex; justify-content: space-between; align-items: center; margin-top: 1rem; padding-top: 1rem; border-top: 1px solid #e5e7eb;">
                    <div style="font-size: 0.875rem; color: #666;">
                        <i class="fas fa-calendar"></i> 
                        ${promo.startDate ? new Date(promo.startDate).toLocaleDateString('vi-VN') : '?'} - 
                        ${promo.endDate ? new Date(promo.endDate).toLocaleDateString('vi-VN') : '?'}
                    </div>
                    <div style="display: flex; gap: 0.5rem;">
                        <button class="action-btn" onclick="editPromotion('${promo._id}')" title="Sửa" style="padding: 0.5rem 1rem; background: #3b82f6; color: white; border-radius: 6px; border: none; cursor: pointer;">
                            <i class="fas fa-edit"></i>
                        </button>
                        <button class="action-btn" onclick="deletePromotion('${promo._id}')" title="Xóa" style="padding: 0.5rem 1rem; background: #ef4444; color: white; border-radius: 6px; border: none; cursor: pointer;">
                            <i class="fas fa-trash-alt"></i>
                        </button>
                    </div>
                </div>
            </div>
        `;
    }).join('');
}

function filterPromotions() {
    const searchVal = document.getElementById('searchPromotion')?.value.toLowerCase() || '';
    const statusFilter = document.getElementById('filterPromotionStatus')?.value || '';
    const typeFilter = document.getElementById('filterPromotionType')?.value || '';

    let filtered = allPromotions.filter(promo => {
        const matchesSearch = !searchVal || promo.title.toLowerCase().includes(searchVal);
        const matchesStatus = !statusFilter || promo.status === statusFilter;
        const matchesType = !typeFilter || promo.type === typeFilter;

        return matchesSearch && matchesStatus && matchesType;
    });

    renderPromotionCards(filtered);
}

window.openAddPromotionModal = function () {
    document.getElementById('promotionForm').reset();
    document.getElementById('promotionId').value = '';
    document.getElementById('promotionModalTitle').innerText = 'Thêm khuyến mãi mới';
    document.getElementById('promotionModal').classList.remove('hidden');
}

window.closePromotionModal = function () {
    document.getElementById('promotionModal').classList.add('hidden');
}

window.savePromotion = async function (e) {
    e.preventDefault();

    const id = document.getElementById('promotionId').value;
    const data = {
        title: document.getElementById('promoTitle').value,
        description: document.getElementById('promoDesc').value,
        discountText: document.getElementById('promoDiscount').value,
        type: document.getElementById('promoType').value,
        status: document.getElementById('promoStatus').value,
        productCount: parseInt(document.getElementById('promoProductCount').value) || 0,
        categoryCount: parseInt(document.getElementById('promoCategoryCount').value) || 0,
        startDate: document.getElementById('promoStartDate').value || null,
        endDate: document.getElementById('promoEndDate').value || null
    };

    const method = id ? 'PUT' : 'POST';
    const url = id
        ? `${CONFIG.API_URL}/promotions/${id}`
        : `${CONFIG.API_URL}/promotions`;

    try {
        const res = await fetch(url, {
            method: method,
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(data)
        });

        if (res.ok) {
            alert('Lưu khuyến mãi thành công!');
            closePromotionModal();
            const contentDiv = document.getElementById('content');
            renderPromotions(contentDiv);
        } else {
            alert('Lỗi khi lưu khuyến mãi');
        }
    } catch (err) {
        console.error(err);
        alert('Lỗi kết nối');
    }
}

window.editPromotion = function (id) {
    const promo = allPromotions.find(p => p._id === id);
    if (!promo) return;

    openAddPromotionModal();
    document.getElementById('promotionModalTitle').innerText = 'Cập nhật khuyến mãi';
    document.getElementById('promotionId').value = promo._id;
    document.getElementById('promoTitle').value = promo.title;
    document.getElementById('promoDesc').value = promo.description || '';
    document.getElementById('promoDiscount').value = promo.discountText;
    document.getElementById('promoType').value = promo.type;
    document.getElementById('promoStatus').value = promo.status;
    document.getElementById('promoProductCount').value = promo.productCount || 0;
    document.getElementById('promoCategoryCount').value = promo.categoryCount || 0;

    if (promo.startDate) {
        document.getElementById('promoStartDate').value = new Date(promo.startDate).toISOString().split('T')[0];
    }
    if (promo.endDate) {
        document.getElementById('promoEndDate').value = new Date(promo.endDate).toISOString().split('T')[0];
    }
}

window.deletePromotion = async function (id) {
    if (!confirm('Bạn có chắc muốn xóa khuyến mãi này?')) return;

    try {
        const res = await fetch(`${CONFIG.API_URL}/promotions/${id}`, {
            method: 'DELETE'
        });

        if (res.ok) {
            alert('Đã xóa khuyến mãi');
            const contentDiv = document.getElementById('content');
            renderPromotions(contentDiv);
        } else {
            alert('Lỗi khi xóa');
        }
    } catch (err) {
        console.error(err);
    }
}

function getPromotionStatusBadge(promo) {
    const now = new Date();
    const start = promo.startDate ? new Date(promo.startDate) : null;
    const end = promo.endDate ? new Date(promo.endDate) : null;

    let badge;

    if (start && now < start) {
        badge = { text: 'Sắp diễn ra', class: 'badge-pending' };
    } else if ((end && now > end) || promo.status === 'expired') {
        badge = { text: 'Đã kết thúc', class: 'badge-cancelled' };
    } else {
        badge = { text: 'Đang hoạt động', class: 'badge-delivered' };
    }

    return `<span class="status-badge ${badge.class}">${badge.text}</span>`;
}
