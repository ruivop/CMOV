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
};
