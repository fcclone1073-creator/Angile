const mongoose = require('mongoose');
const Product = require('./models/Product');

const checkProducts = async () => {
    try {
        await mongoose.connect('mongodb://localhost:27017/kttstore');
        console.log('Connected to DB');

        const count = await Product.countDocuments();
        console.log('Product count:', count);

        if (count === 0) {
            console.log('Database is empty. You need to run the seed.');
        } else {
            const products = await Product.find().limit(2);
            console.log('Sample products:', products);
        }

    } catch (err) {
        console.error('Error:', err);
    } finally {
        await mongoose.disconnect();
    }
};

checkProducts();
