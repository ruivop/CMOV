'use strict';
var mongoose = require('mongoose');
var Schema = mongoose.Schema;


var OrderSchema = new Schema({
    Created_date: {
    type: Date,
    default: Date.now
  },
  product: [{ body: String, number: Number }],
  price: {
  	type: Number
  }
});



module.exports = mongoose.model('orders', OrderSchema);
