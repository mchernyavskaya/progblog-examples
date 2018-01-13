var express = require('express');
var router = express.Router();

router.get('/:username*?', function (req, res, next) {
    res.setHeader('Content-Type', 'application/json');
    res.send(JSON.stringify({hello: req.params["username"] || "stranger"}));
});


module.exports = router;
