const mongoose = require('mongoose');

const CategorySchema = new mongoose.Schema({
    name: {
        type: String,
        required: true
    },
    image: {
        type: String, // URL to image
        required: true
    }
});

module.exports = mongoose.model('Category', CategorySchema);
