'use strict';
var mongoose = require('mongoose');
var Schema = mongoose.Schema;


var TicketSchema = new Schema({
Created_date: {
    type: Date,
    default: Date.now
  },
  customer: {
    type: Buffer
  },
  edate: {
    type: String
  }
});

module.exports = mongoose.model('tickets',TicketSchema);