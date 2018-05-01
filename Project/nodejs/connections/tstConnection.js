var elasticsearch=require('elasticsearch');
var client = new elasticsearch.Client( { 
	hosts: [
		'http://localhost:9300/' ]
	});
module.exports = client;