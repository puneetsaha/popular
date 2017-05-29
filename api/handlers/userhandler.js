var restify = require('restify');
var User = require("../models/usermodel").User;
var passwordHash = require('password-hash');

/** Function use to handle login **/
exports.signup = function(req, res, next) {
  username = req.params.username;
  password = req.params.password;
  gender = req.params.gender;
  ethnicity = req.params.ethnicity;

  alreadyCreated = false;

  if (username == undefined || username.length < 5) {
    return next(new restify.InvalidArgumentError("The user name given should be at least 5 lettors long"));
  }

  if (password == undefined || password.length < 4) {
    return next(new restify.InvalidArgumentError("The password given should be at least 4 letters long"));
  }

  if (gender != undefined && gender.length < 1)  {
    return next(new restify.InvalidArgumentError("Gender should be provided"));
  } else {
    if (gender != "male" && gender != "female") {
      return next(new restify.InvalidArgumentError("Gender should be male or female"));
    }
  }

 
  // Check the user name is already existing
  User.count({username:username}).exec(function (err, count) {
    if (count > 0 ) {
      alreadyCreated = true;
      return next(new restify.InvalidArgumentError("The username given already exists"));
    } else {
      var user = new User ({
        username:username,
        password: passwordHash.generate(password),
        gender:gender,
        ethnicity:ethnicity
      });
      user.save(function (err, user) {
        if (err) return console.error(err);
        res.status(201);
        res.send({id:user.id});
      });
    }
    return next();
  });
}


exports.login = function(req, res, next) {
  username = req.params.username;
  password = req.params.password;
  if (username == undefined || username.length < 5) {
    return next(new restify.InvalidArgumentError("The user name given should be at least 5 lettors long"));
  }
  if (password == undefined || password.length < 4) {
    return next(new restify.InvalidArgumentError("The password given should be at least 4 letters long"));
  }
  User.findOne({ username:username},
    function (err, user){
      if (err) {
        return next(new restify.InvalidArgumentError("The password given should be at least 4 letters long"));
      }
      if (user != undefined) {
        if (passwordHash.verify(password, user.password)) {
          res.send({id:user.id});
          return next();
        } else {
          return next(new restify.InvalidArgumentError("Invalid password"));
        }
      } else {
        return next(new restify.InvalidArgumentError("Invalid username"));
      }
    });
}


exports.userid = function(req, res, next) {
  userid = req.params.userid;
  console.log("User id sent " + userid);
  User.findOne( { _id: userid }, function (err, users){
      if (err) {
        return next(new restify.InvalidArgumentError("error while retrieving the id " + err ));
      }
      if (users != undefined) {
          res.send(users);
          next();
      }
    });
}
