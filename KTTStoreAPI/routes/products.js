const express = require('express');
const router = express.Router();
const Product = require('../models/Product');
const Category = require('../models/Category');

// @route   GET /api/products
// @desc    Get all products
router.get('/', async (req, res) => {
    try {
        const products = await Product.find();
        res.json(products);
    } catch (err) {
        res.status(500).send('Server Error');
    }
});

// @route   GET /api/products/featured
// @desc    Get featured products
router.get('/featured', async (req, res) => {
    try {
        const products = await Product.find({ isFeatured: true });
        res.json(products);
    } catch (err) {
        res.status(500).send('Server Error');
    }
});

// @route   GET /api/categories
// @desc    Get all categories
router.get('/categories', async (req, res) => {
    try {
        const categories = await Category.find();
        res.json(categories);
    } catch (err) {
        res.status(500).send('Server Error');
    }
});

// @route   POST /api/products/seed
// @desc    Seed sample data
router.post('/seed', async (req, res) => {
    try {
        await Category.deleteMany({});
        await Product.deleteMany({});

        const cat1 = new Category({ name: 'Chân váy chữ A', image: 'https://img.freepik.com/free-photo/young-woman-beautiful-dress-hat-posing_1303-17517.jpg' });
        const cat2 = new Category({ name: 'Jumpsuit', image: 'https://img.freepik.com/free-photo/fashionable-pale-brunette-long-green-dress-black-jacket-sunglasses-standing-street-during-daytime-against-wall-light-city-building_197531-24468.jpg' });
        const cat3 = new Category({ name: 'Quần Jean', image: 'https://img.freepik.com/free-photo/fashion-portrait-young-businesswoman-handsome-model-dark-suit-standing-street_1157-48352.jpg' });
        const cat4 = new Category({ name: 'Áo sơ mi', image: 'https://img.freepik.com/free-photo/handsome-confident-hipster-model-posing-street_158538-19349.jpg' });

        await cat1.save();
        await cat2.save();
        await cat3.save();
        await cat4.save();

        const products = [
            {
                name: 'Áo khoác len kẻ',
                price: 875000,
                image: 'https://img.freepik.com/premium-photo/woman-wearing-striped-cardigan-jeans_1040166-54.jpg',
                categoryId: cat1._id,
                rating: 4.5,
                isFeatured: true
            },
            {
                name: 'Áo sơ mi vải thô dáng slim fit',
                price: 350000,
                image: 'https://img.freepik.com/free-photo/solid-navy-blue-shirt-men-s-fashion-apparel-studio-shoot_53876-102146.jpg',
                categoryId: cat4._id,
                rating: 4.5,
                isFeatured: true
            },
            {
                name: 'Quần ống suông Sophia Elegance',
                price: 693000,
                image: 'https://img.freepik.com/free-photo/fashion-woman-pants_1303-4552.jpg',
                categoryId: cat2._id,
                rating: 4.5,
                isFeatured: true
            },
            {
                name: 'Áo khoác len ôm cơ bản',
                price: 903000,
                image: 'https://img.freepik.com/free-photo/portrait-young-woman-with-red-sweater_144627-21014.jpg',
                categoryId: cat1._id,
                rating: 4.5,
                isFeatured: true
            },
            {
                name: 'Váy đỏ dạo phố',
                price: 1200000,
                image: 'https://img.freepik.com/free-photo/full-shot-woman-posing-stairs_23-2149867512.jpg',
                categoryId: cat1._id,
                rating: 5.0,
                isFeatured: false
            }
        ];

        await Product.insertMany(products);

        res.json({ msg: 'Data seeded' });
    } catch (err) {
        console.error(err);
        res.status(500).send('Server Error');
    }
});

module.exports = router;
