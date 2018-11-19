'use strict';


var mongoose = require('mongoose'),
  user = mongoose.model('users'),
  ticket = mongoose.model('tickets'),
  order = mongoose.model('orders'),
  voucher = mongoose.model('vouchers');

var rn = require('random-number');

var options = {
 integer: true
}



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
  var resp = [];
  var new_ticket = new ticket(req.body);
  new_ticket.save(function(err, ticket) {
    if (err)
      resp[0] = err;
    else
      resp[0] = [ticket];
      if(rn(options) == 0){
        var new_voucher = new voucher({product:'coffee',userid: req.body.customer});
      }else{
        var new_voucher = new voucher({product:'popcorn',userid: req.body.customer});
      }
      new_voucher.save(function(err, voucher){
        if(err)
          resp[1] = err;
        else
          resp[1] = [voucher];
        res.json(resp);
      });
  });
};

exports.create_more_tickets = function(req, res) {
  var array = [];
  var array_voucher = [];
  for(var i = 0; i < req.params.ticketId ; i++){
    array[i] = req.body;
    if(rn(options) == 0){
    array_voucher[i] = {product:"coffee",userid: req.body.customer};}
    else{
      array_voucher[i] = {product:"popcorn",userid: req.body.customer};
    }
  }
  var resp = [];
  ticket.create(array ,function(err, ticket) {
    if (err)
      resp[0] = err;
    else
      resp[0] = ticket;
      
    voucher.create(array_voucher ,function(err, voucher) {
      if (err)
        resp[1] = err;
      else
        resp[1] = voucher;
        
      res.json(resp);
    });
  });
};


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

exports.validate_ticket = function(req, res) {
  ticket.findById(req.params.ticketId, function(err, ticket) {
  console.log("req.body.userId" + req.body.customer);
    if (err)
      res.send(err);
    else if(ticket != null && ticket.validated)
      res.send("Alredy Validated");
    else if(ticket != null && req.body.customer != ticket.customer)
      res.send("Not the same user");
    else
      update_ticket(req, res);
  });
  

};

function update_ticket(req, res) {
    ticket.findOneAndUpdate({_id: req.params.ticketId}, {$set:{validated:true}}, {new: true}, function(err, ticket) {
      if (err)
        res.send(err);
      else if(ticket == null)
        res.send("Not Valid Ticket");
      else
        res.json(ticket);
    });
}




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
  }, function(err, order) {
    if (err)
      res.send(err);
    res.json({ message: 'order successfully deleted' });
  });
};

exports.list_all_vouchers = function(req, res) {
  voucher.find({}, function(err, voucher) {
    if (err)
      res.send(err);
    res.json(voucher);
  });
};




exports.create_a_voucher = function(req, res) {
  var new_voucher = new voucher(req.body);
  new_voucher.save(function(err, voucher) {
    if (err)
      res.send(err);
    res.json(voucher);
  });
};


exports.read_a_voucher = function(req, res) {
  voucher.findById(req.params.voucherId, function(err, voucher) {
    if (err)
      res.send(err);
    res.json(voucher);
  });
};

exports.read_a_voucher_user = function(req, res) {
  voucher.find({userid: req.params.voucherUser}, function(err, voucher) {
    if (err)
      res.send(err);
    res.json(voucher);
  });
};


exports.update_a_voucher = function(req, res) {
  voucher.findOneAndUpdate({_id: req.params.voucherId}, req.body, {new: true}, function(err, voucher) {
    if (err)
      res.send(err);
    res.json(voucher);
  });
};


exports.delete_a_voucher = function(req, res) {


  voucher.remove({
    _id: req.params.voucherId
  }, function(err, voucher) {
    if (err)
      res.send(err);
    res.json({ message: 'voucher successfully deleted' });
  });
};




