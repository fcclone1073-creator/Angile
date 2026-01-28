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
    const { name, email, password, phone, gender } = req.body;

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
            gender
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


module.exports = router;
