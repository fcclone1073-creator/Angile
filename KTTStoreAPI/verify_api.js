const axios = require('axios');

const testApi = async () => {
    try {
        const response = await axios.get('http://localhost:5000/api/products?page=1&limit=2');
        console.log('API Status:', response.status);
        console.log('Products returned:', response.data.length);
        if (response.data.length > 0) {
            console.log('First product:', response.data[0].name);
        }
    } catch (err) {
        console.error('API Error:', err.message);
    }
};

testApi();
