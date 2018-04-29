'use strict';
var client = require('../connections/tstConnection');
exports.list_all_indices = function(req, res) { 
	client.cat.indices({
		v: true
	}, function(err, resp, status) {
		if(err) 
			res.send(err);
		res.json(resp); 
	});
};

exports.read_a_record = function(req, res) { 
	client.search({
		index: req.params.indexID,
		q: '_id:' + req.params.recordID 
	}, function(err, record) {
		if (err) 
			res.send(err);
		res.json(record);
	}); 
};