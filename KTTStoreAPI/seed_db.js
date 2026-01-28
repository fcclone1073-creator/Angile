const mongoose = require('mongoose');
const Product = require('./models/Product');
const Category = require('./models/Category');

const seedData = async () => {
    try {
        await mongoose.connect('mongodb://localhost:27017/kttstore');
        console.log('Connected to DB');

        await Category.deleteMany({});
        await Product.deleteMany({});
        console.log('Cleared old data');

        const cat1 = new Category({ name: 'Chân váy chữ A', image: 'https://img.freepik.com/free-photo/young-woman-beautiful-dress-hat-posing_1303-17517.jpg' });
        const cat2 = new Category({ name: 'Jumpsuit', image: 'https://img.freepik.com/free-photo/fashionable-pale-brunette-long-green-dress-black-jacket-sunglasses-standing-street-during-daytime-against-wall-light-city-building_197531-24468.jpg' });
        const cat3 = new Category({ name: 'Quần Jean', image: 'https://img.freepik.com/free-photo/fashion-portrait-young-businesswoman-handsome-model-dark-suit-standing-street_1157-48352.jpg' });
        const cat4 = new Category({ name: 'Áo sơ mi', image: 'https://img.freepik.com/free-photo/handsome-confident-hipster-model-posing-street_158538-19349.jpg' });

        await cat1.save();
        await cat2.save();
        await cat3.save();
        await cat4.save();
        console.log('Categories created');

        const products = [
            {
                name: 'Áo khoác len kẻ',
                price: 875000,
                oldPrice: 1000000,
                image: 'https://img.freepik.com/premium-photo/woman-wearing-striped-cardigan-jeans_1040166-54.jpg',
                categoryId: cat1._id,
                rating: 4.5,
                isFeatured: true,
                countInStock: 10
            },
            {
                name: 'Áo sơ mi vải thô dáng slim fit',
                price: 350000,
                image: 'https://img.freepik.com/free-photo/solid-navy-blue-shirt-men-s-fashion-apparel-studio-shoot_53876-102146.jpg',
                categoryId: cat4._id,
                rating: 4.5,
                isFeatured: true,
                countInStock: 0 // Out of stock
            },
            {
                name: 'Quần ống suông Sophia Elegance',
                price: 693000,
                oldPrice: 850000,
                image: 'https://img.freepik.com/free-photo/fashion-woman-pants_1303-4552.jpg',
                categoryId: cat2._id,
                rating: 4.5,
                isFeatured: true,
                countInStock: 5
            },
            {
                name: 'Áo khoác len ôm cơ bản',
                price: 903000,
                image: 'https://img.freepik.com/free-photo/portrait-young-woman-with-red-sweater_144627-21014.jpg',
                categoryId: cat1._id,
                rating: 4.5,
                isFeatured: true,
                countInStock: 20
            },
            {
                name: 'Váy đỏ dạo phố',
                price: 1200000,
                image: 'https://img.freepik.com/free-photo/full-shot-woman-posing-stairs_23-2149867512.jpg',
                categoryId: cat1._id,
                rating: 5.0,
                isFeatured: false,
                countInStock: 3
            }
        ];

        await Product.insertMany(products);
        console.log('Products created');

    } catch (err) {
        console.error('Error:', err);
    } finally {
        await mongoose.disconnect();
    }
};

seedData();
