'use strict';
module.exports = function(app) {
  var restList = require('../controllers/restListController');

  // restList Routes
  app.route('/users')
    .get(restList.list_all_users)
    .post(restList.create_a_user);


  app.route('/users/:userId')
    .get(restList.read_a_user)
    .put(restList.update_a_user)
    .delete(restList.delete_a_user);

  app.route('/tickets')
  .get(restList.list_all_tickets)
  .post(restList.create_a_ticket);

  app.route('/tickets/:ticketId')
    .get(restList.read_a_ticket)
    .post(restList.create_more_tickets)
    .put(restList.update_a_ticket)
    .delete(restList.delete_a_ticket);

    app.route('/orders')
    .get(restList.list_all_orders)
    .post(restList.create_a_order);

   app.route('/orders/:orderId')
    .get(restList.read_a_order)
    .put(restList.update_a_order)
    .delete(restList.delete_a_order);
};
