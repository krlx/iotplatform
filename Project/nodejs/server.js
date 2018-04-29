var express = require('express'), 
	app = express(),
	port = process.env.PORT || 3000;
	
var routes = require('./routes/tstRoutes'); //importing route 
routes(app); //register the route

app.listen(port);
console.log('Test API server listening on: ' + port);

