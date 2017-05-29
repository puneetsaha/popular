var mongoose = require("mongoose"),
  ObjectId = mongoose.Schema.ObjectId;
  mongoose.Promise = global.Promise;




var UserSchema = new mongoose.Schema(
  {
    username:String,
    password:String,
    gender:String,
    ethnicity : String,
    birthday: {type:Date},
    created: {type:Date, default: Date.now},
    lastLogin : Date,
    hasAnswered : [ {answer : { type: ObjectId, ref: 'Answer' },
      question : { type: ObjectId, ref: 'Question' }}]
  },
  {
    toObject: { virtuals: true },
    toJSON: { virtuals: true }
  }
);




UserSchema.statics.checkId = function (id, cb) {
  this.findById(id, function (err, user) {
    if (err) {
      console.log(err);
      cb(err,undefined);
    }
    else if (user.username.length > 0) {
       cb(err,user);
    }
  });
}

UserSchema.methods.hasAnsweredQuestion = function (questionId) {
  return this.hasAnswered.some( function(element) {
    return element.question == questionId;
  });
}

UserSchema.virtual('total_answered').get(function () {
  return this.hasAnswered.length;
});




var User = mongoose.model('User', UserSchema);
module.exports = {
  User : User
}
