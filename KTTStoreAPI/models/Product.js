const mongoose = require('mongoose');

const ProductSchema = new mongoose.Schema({
    name: {
        type: String,
        required: true
    },
    description: {
        type: String
    },
    price: {
        type: Number,
        required: true
    },
    oldPrice: {
        type: Number
    },
    image: {
        type: String, // URL to image
        required: true
    },
    rating: {
        type: Number,
        default: 0
    },
    categoryId: {
        type: mongoose.Schema.Types.ObjectId,
        ref: 'Category'
    },
    isFeatured: {
        type: Boolean,
        default: false
    },
    countInStock: {
        type: Number,
        required: true,
        default: 0
    }
});

module.exports = mongoose.model('Product', ProductSchema);
