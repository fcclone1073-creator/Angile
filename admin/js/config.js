const CONFIG = {
    API_URL: 'http://localhost:5000/api', // Adjust if your backend port differs
    DEBUG: true
};

const log = (msg, data = '') => {
    if (CONFIG.DEBUG) console.log(`[Admin] ${msg}`, data);
};
