'use strict';
var mongoose = require('mongoose');
var Schema = mongoose.Schema;


var OrderSchema = new Schema({
    Created_date: {
    type: Date,
    default: Date.now
  },
  product:{ 
  	type: String
  },
   number:{
   	type: String
   },
  price: {
  	type: Number
  },
  userid: {
  	type: String
  }
});



module.exports = mongoose.model('orders', OrderSchema);
