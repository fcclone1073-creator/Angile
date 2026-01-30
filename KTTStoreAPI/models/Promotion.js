const mongoose = require('mongoose');

const PromotionSchema = new mongoose.Schema({
    title: { type: String, required: true },
    description: { type: String },
    discountText: { type: String, required: true }, // ví dụ '30%' hoặc '100K'
    type: {
        type: String,
        enum: ['general', 'flash_sale', 'holiday', 'other'],
        default: 'general'
    },
    status: {
        type: String,
        enum: ['active', 'inactive', 'expired'],
        default: 'inactive'
    },
    productCount: { type: Number, default: 0 },
    categoryCount: { type: Number, default: 0 },
    startDate: { type: Date },
    endDate: { type: Date },
    createdAt: { type: Date, default: Date.now }
});

module.exports = mongoose.model('Promotion', PromotionSchema);

