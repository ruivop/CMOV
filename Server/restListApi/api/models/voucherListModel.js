'use strict';
var mongoose = require('mongoose');
var Schema = mongoose.Schema;


var VoucherSchema = new Schema({
    Created_date: {
    type: Date,
    default: Date.now
  },
  product:{ 
  	type: String
  },
  userid:{
    type: String 
  }
});



module.exports = mongoose.model('vouchers', VoucherSchema);
