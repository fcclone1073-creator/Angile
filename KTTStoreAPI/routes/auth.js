const express = require('express');
const router = express.Router();
const User = require('../models/User');
const bcrypt = require('bcryptjs');

// @route   GET /api/auth/health
// @desc    Health check endpoint
// @access  Public
router.get('/health', (req, res) => {
    res.json({
        status: 'OK',
        message: 'KTT Store API is running',
        timestamp: new Date().toISOString()
    });
});

// @route   POST /api/auth/register
// @desc    Register new user
// @access  Public
router.post('/register', async (req, res) => {
    const { name, email, password, phone, gender, role } = req.body;

    try {
        let user = await User.findOne({ email });
        if (user) {
            return res.status(400).json({ msg: 'Email đã tồn tại' });
        }

        user = new User({
            name,
            email,
            password,
            phone,
            gender,
            role: role || 'user'
        });

        // Hash password
        const salt = await bcrypt.genSalt(10);
        user.password = await bcrypt.hash(password, salt);

        await user.save();

        res.json({ msg: 'Đăng ký thành công', user });
    } catch (err) {
        console.error(err.message);
        res.status(500).send('Server Error');
    }
});

// @route   POST /api/auth/login
// @desc    Authenticate user
// @access  Public
router.post('/login', async (req, res) => {
    const { email, password } = req.body;

    try {
        let user = await User.findOne({ email });
        if (!user) {
            return res.status(400).json({ msg: 'Thông tin đăng nhập không đúng' });
        }

        // Check password
        const isMatch = await bcrypt.compare(password, user.password);
        if (!isMatch) {
            return res.status(400).json({ msg: 'Thông tin đăng nhập không đúng' });
        }

        // Return user info
        res.json({ msg: 'Đăng nhập thành công', user });

    } catch (err) {
        console.error(err.message);
        res.status(500).send('Server Error');
    }
});

// @route   POST /api/auth/forgot-password
// @desc    Mock forgot password
// @access  Public
router.post('/forgot-password', async (req, res) => {
    const { email } = req.body;
    try {
        const user = await User.findOne({ email });
        if (!user) {
            return res.status(400).json({ msg: 'Email không tồn tại trong hệ thống' });
        }
        // Mock OTP sending
        res.json({ msg: 'Mã OTP đã được gửi đến email của bạn' });
    } catch (err) {
        console.error(err.message);
        res.status(500).send('Server Error');
    }
});

// @route   POST /api/auth/verify-otp
// @desc    Mock verify OTP
router.post('/verify-otp', (req, res) => {
    // For now accept any OTP
    const { email, otp } = req.body;
    if (otp === "123456") {
        res.json({ msg: 'OTP Verified' });
    } else {
        res.status(400).json({ msg: 'Mã OTP không chính xác' });
    }
});
// @desc    Mock reset password
router.post('/reset-password', async (req, res) => {
    const { email, newPassword } = req.body;
    try {
        let user = await User.findOne({ email });
        if (!user) {
            return res.status(400).json({ msg: 'User not found' });
        }
        const salt = await bcrypt.genSalt(10);
        user.password = await bcrypt.hash(newPassword, salt);
        await user.save();
        res.json({ msg: 'Đặt lại mật khẩu thành công' });
    } catch (err) {
        console.error(err.message);
        res.status(500).send('Server Error');
    }
});


// @route   GET /api/auth/users
// @desc    Get all users
// @access  Public (for now)
// @route   GET /api/auth/users
// @desc    Get all users
// @access  Public (for now)
router.get('/users', async (req, res) => {
    try {
        const users = await User.find().select('-password');
        res.json(users);
    } catch (err) {
        console.error(err.message);
        res.status(500).send('Server Error');
    }
});

// @route   POST /api/auth/users
// @desc    Create a user (Admin)
router.post('/users', async (req, res) => {
    const { name, email, password, phone, gender, role, status } = req.body;
    try {
        let user = await User.findOne({ email });
        if (user) return res.status(400).json({ msg: 'Email đã tồn tại' });

        user = new User({ name, email, password, phone, gender, role, status });

        const salt = await bcrypt.genSalt(10);
        user.password = await bcrypt.hash(password, salt);

        await user.save();
        res.json(user);
    } catch (err) {
        res.status(500).send('Server Error');
    }
});

// @route   PUT /api/auth/users/:id
// @desc    Update a user
router.put('/users/:id', async (req, res) => {
    const { name, email, phone, gender, role, status, password } = req.body;
    try {
        const updateFields = { name, email, phone, gender, role, status };
        if (password) {
            const salt = await bcrypt.genSalt(10);
            updateFields.password = await bcrypt.hash(password, salt);
        }

        const user = await User.findByIdAndUpdate(req.params.id, updateFields, { new: true }).select('-password');
        res.json(user);
    } catch (err) {
        res.status(500).send('Server Error');
    }
});

// @route   DELETE /api/auth/users/:id
// @desc    Delete a user
router.delete('/users/:id', async (req, res) => {
    try {
        await User.findByIdAndDelete(req.params.id);
        res.json({ msg: 'User removed' });
    } catch (err) {
        res.status(500).send('Server Error');
    }
});

module.exports = router;
