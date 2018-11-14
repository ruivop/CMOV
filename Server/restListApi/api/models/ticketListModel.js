'use strict';
var mongoose = require('mongoose');
var Schema = mongoose.Schema;


var TicketSchema = new Schema({
Created_date: {
    type: Date,
    default: Date.now
  },
  performance: {
  	type: String
  },
  customer: {
    type: String
  },
  edate: {
    type: String
  }
});

module.exports = mongoose.model('tickets',TicketSchema);