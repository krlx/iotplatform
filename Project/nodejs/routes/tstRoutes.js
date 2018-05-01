'use strict';
module.exports = function(app) {
	var controller = require('../controllers/tstController');
	app.route('/indices') 
		.get(controller.list_all_indices);
	app.route('/indices/:indexID/:recordID')
		.get(controller.read_a_record);
	app.route('/indices/:indexname')
		.get(controller.get_all_index_records);
	};