const mongoose = require('mongoose');

const UserSchema = new mongoose.Schema({
    name: {
        type: String,
        required: true
    },
    email: {
        type: String,
        required: true,
        unique: true
    },
    password: {
        type: String,
        required: true
    },
    phone: {
        type: String,
        required: true
    },
    gender: {
        type: String, // 'Nam' or 'Nu'
        default: 'Nam'
    },
    role: {
        type: String,
        enum: ['user', 'staff', 'admin', 'manager'],
        default: 'user'
    },
    status: {
        type: String,
        enum: ['active', 'disabled'],
        default: 'active'
    },
    createdAt: {
        type: Date,
        default: Date.now
    }
});

module.exports = mongoose.model('User', UserSchema);
