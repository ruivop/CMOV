'use strict';
var mongoose = require('mongoose');
var Schema = mongoose.Schema;


var UserSchema = new Schema({
    Created_date: {
    type: Date,
    default: Date.now
  },
  userData: {
    type: Buffer
  },
  publicKey: {
    type: String
  }
});

module.exports = mongoose.model('users', UserSchema);