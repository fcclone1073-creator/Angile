document.addEventListener('DOMContentLoaded', () => {
    const loginForm = document.getElementById('loginForm');

    if (loginForm) {
        loginForm.addEventListener('submit', handleLogin);
    }
});

async function handleLogin(e) {
    e.preventDefault();

    const email = document.getElementById('email').value;
    const password = document.getElementById('password').value;
    const btn = e.target.querySelector('button');

    // Simple validation
    if (!email || !password) {
        alert('Vui lòng nhập đầy đủ thông tin');
        return;
    }

    try {
        setLoading(btn, true);

        const response = await fetch(`${CONFIG.API_URL}/auth/login`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ email, password })
        });

        const data = await response.json();

        if (response.ok) {
            log('Login success', data);
            // Save token
            localStorage.setItem('token', 'mock-token-for-now'); // Using mock because API might not return token yet or we need to check response structure
            localStorage.setItem('user', JSON.stringify(data.user));

            alert('Đăng nhập thành công!');
            window.location.href = 'dashboard.html';
        } else {
            alert(data.msg || 'Đăng nhập thất bại');
        }
    } catch (error) {
        console.error('Login Error:', error);
        alert('Có lỗi xảy ra khi kết nối server. Hãy đảm bảo Server NodeJS đang chạy.');
    } finally {
        setLoading(btn, false);
    }
}

function setLoading(element, isLoading) {
    if (isLoading) {
        element.dataset.originalText = element.innerText;
        element.innerText = 'Đang xử lý...';
        element.disabled = true;
    } else {
        element.innerText = element.dataset.originalText;
        element.disabled = false;
    }
}
