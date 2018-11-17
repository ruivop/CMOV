'use strict';


var mongoose = require('mongoose'),
  user = mongoose.model('users'),
  ticket = mongoose.model('tickets'),
  order = mongoose.model('orders');

exports.list_all_users = function(req, res) {
  user.find({}, function(err, user) {
    if (err)
      res.send(err);
    res.json(user);
  });
};




exports.create_a_user = function(req, res) {
  var new_user = new user(req.body);
  new_user.save(function(err, user) {
    if (err)
      res.send(err);
    res.json(user);
  });
};


exports.read_a_user = function(req, res) {
  user.findById(req.params.userId, function(err, user) {
    if (err)
      res.send(err);
    res.json(user);
  });
};


exports.update_a_user = function(req, res) {
  user.findOneAndUpdate({_id: req.params.userId}, req.body, {new: true}, function(err, user) {
    if (err)
      res.send(err);
    res.json(user);
  });
};


exports.delete_a_user = function(req, res) {


  user.remove({
    _id: req.params.userId
  }, function(err, user) {
    if (err)
      res.send(err);
    res.json({ message: 'user successfully deleted' });
  });
};

exports.list_all_tickets = function(req, res) {
  ticket.find({}, function(err, ticket) {
    if (err)
      res.send(err);
    res.json(ticket);
  });
};




exports.create_a_ticket = function(req, res) {
  var new_ticket = new ticket(req.body);
  new_ticket.save(function(err, ticket) {
    if (err)
      res.send(err);
    res.json(ticket);
  });
};

exports.create_more_tickets = function(req, res) {
  for(var i = 0; i < req.params.ticketId ; i++){
  var new_ticket = new ticket(req.body);
  new_ticket.save(function(err, ticket) {
    if (err)
      res.send(err);
    res.json(ticket);
  });
}};


exports.read_a_ticket = function(req, res) {
  ticket.findById(req.params.ticketId, function(err, ticket) {
    if (err)
      res.send(err);
    res.json(ticket);
  });
};


exports.update_a_ticket = function(req, res) {
  ticket.findOneAndUpdate({_id: req.params.ticketId}, req.body, {new: true}, function(err, ticket) {
    if (err)
      res.send(err);
    res.json(ticket);
  });
};


exports.delete_a_ticket = function(req, res) {


  ticket.remove({
    _id: req.params.ticketId
  }, function(err, ticket) {
    if (err)
      res.send(err);
    res.json({ message: 'ticket successfully deleted' });
  });
};

exports.list_all_orders = function(req, res) {
  order.find({}, function(err, order) {
    if (err)
      res.send(err);
    res.json(order);
  });
};


exports.create_a_order = function(req, res) {
  var new_order = new order(req.body);
  new_order.save(function(err, order) {
    if (err)
      res.send(err);
    res.json(order);
  });
};

exports.read_a_order = function(req, res) {
  order.findById(req.params.orderId, function(err, order) {
    if (err)
      res.send(err);
    res.json(order);
  });
};

exports.update_a_order = function(req, res) {
  order.findOneAndUpdate({_id: req.params.orderId}, req.body, {new: true}, function(err, order) {
    if (err)
      res.send(err);
    res.json(order);
  });
};

exports.delete_a_order = function(req, res) {
  order.remove({
    _id: req.params.orderId
  }, function(err, user) {
    if (err)
      res.send(err);
    res.json({ message: 'order successfully deleted' });
  });
};






