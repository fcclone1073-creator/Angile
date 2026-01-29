const mongoose = require('mongoose');
const Product = require('./models/Product');
const Category = require('./models/Category');
const User = require('./models/User');
const bcrypt = require('bcryptjs');

const seedData = async () => {
    try {
        await mongoose.connect('mongodb://localhost:27017/kttstore');
        console.log('Connected to DB');

        await Category.deleteMany({});
        await Product.deleteMany({});
        await User.deleteMany({});
        console.log('Cleared old data');


        console.log('Categories created');

        const products = [];

        console.log('Products list cleared (no samples)');

        // Create Users
        const salt = await bcrypt.genSalt(10);
        const hashedPassword = await bcrypt.hash('123456', salt);

        const adminUser = new User({
            name: 'Admin User',
            email: 'admin@kttstore.com',
            password: hashedPassword,
            phone: '0987654321',
            gender: 'Nam',
            role: 'admin',
            status: 'active'
        });

        await adminUser.save();


    } catch (err) {
        console.error('Error:', err);
    } finally {
        await mongoose.disconnect();
    }
};

seedData();
