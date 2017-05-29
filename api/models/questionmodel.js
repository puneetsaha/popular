var mongoose = require("mongoose"),
 ObjectId = mongoose.Schema.ObjectId;

var AnswerSchema = new mongoose.Schema({
  answer : String,
  numberOfTimeChoosen : {type:Number, default:0}
});

var Answer = mongoose.model('Answer', AnswerSchema);

var QuestionSchema = new mongoose.Schema({
  question : String,
  createdBy : { type: ObjectId, ref: 'Question' },
  answers : [AnswerSchema],
  created : {type:Date, default: Date.now},
  updated : {type:Date, default: Date.now},
  tag : [{type:String}]
},
{
  toObject: { virtuals: true },
  toJSON: { virtuals: true }
});

QuestionSchema.virtual('numberOfTimeAnswered').get(function () {
  total = 0;
  for (i = 0; i < this.answers.length; i++) {
    a = this.answers[i];
    total = total + a.numberOfTimeChoosen;
  }
  return total;
});

QuestionSchema.methods = {
  /** Increament the answer id given has parameters
  * @param answerid to increment the answer
  **/
  answer : function (answerid, callback) {
    answerFound = false;
    for (i=0; i < this.answers.length; i++) {
      // We find the answer
      a = this.answers[i];
      if (a._id == answerid) {
          a.numberOfTimeChoosen = a.numberOfTimeChoosen + 1;
          answerFound = true;
          console.log("answerFound !")
      }

    }
    if (answerFound) {
      this.updated = new Date(); 
      this.save();
      callback(false, this);
    } else {
      console.log("answerNotFound ! for " + this._id + " " + answerid);
      callback(true, this);
    }
  }

}


var Question = mongoose.model('Question', QuestionSchema);

module.exports = {
  Answer : Answer,
  Question : Question,
}
