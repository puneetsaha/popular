var restify = require('restify');
var Question = require("../models/questionmodel").Question;
var Answer = require("../models/questionmodel").Answer;
var User = require("../models/usermodel").User;
var mongoose = require('mongoose');
var async = require('async');


exports.answerBatch = function (req, res, next ) {
  answers = req.params.answers;
  json = JSON.parse(""+answers);
  userid = json.userid;
  answersJSON = json.answers;
  arrayLength =  answersJSON.length;
  updated= new Array();
  async.each(answersJSON, function  (item, callback) {

    console.log("Answering " + item.questionid + " " + item.answerid);
    User.checkId(userid, function (err, user) {
        if (err) {
          callback(err);
        }
        if (!user.hasAnsweredQuestion(item.questionid)) {
            Question.findOne( {_id:item.questionid}, function (err, question) {
            question.answer(item.answerid, function (err, q) {
                user.hasAnswered.push( {question: item.questionid, answer:item.answerid});
                user.save();
                updated.push(q);
                if (!err) {
                  callback();
                } else {
                  callback(err);
                }
              });
            });
          } else {
            callback("user allready answered the question");
          }
    });
  },
  // 3rd param is the function to call when everything's done
  function(err){
    if (err) console.log("Something bad happened while doing the batch update :  " + err);
    // All tasks are done now
    res.send(updated);
    next();
    });

}


exports.answer = function(req, res, next) {
  userid = req.params.userid;
  answerid = req.params.answerid;
  questionid = req.params.questionid;
  console.log("question id : " + questionid);

  if (undefined == userid || userid.length < 5) {
    return next(new restify.InvalidArgumentError("The userid should be given"));
  }


  if (undefined == answerid || answerid.length < 4) {
    return next(new restify.InvalidArgumentError("The answer id should be provided"));
  }


  if (undefined == questionid || questionid.length < 4) {
    return next(new restify.InvalidArgumentError("The question id should be provided"));
  }

  Question.findOne( { _id : questionid }, function (err, question){
    if (err) console.log("{error: not able to query for question }");

    //res.send(question);
    if (undefined != question) {
    User.chekcId(userid,
      function (err, user) {
        if ( undefined != user) {
          if (user.hasAnsweredQuestion(questionid)) {
            next(new restify.InvalidArgumentError("The user has already answeered this question"));
          } else {
            answerFound = false;
            for (i=0; i < question.answers.length; i++) {
              a = question.answers[i];
              if (a.id == answerid) {
                  a.numberOfTimeChoosen = a.numberOfTimeChoosen + 1;
                  answerFound = true;
                  user.hasAnswered.push( {question: questionid, answer:answerid});
                  user.save();
              }
            }
            if (!answerFound) {
              next(new restify.InvalidArgumentError("The answer given is invalid"));
            }
            question.save();
            console.log(" Question to return" + question);
            res.send(question);
            next();
          }
        } else {
          next(new restify.InvalidArgumentError("No user existing for user id given"));
        }
      });
    } else {
      next(new restify.InvalidArgumentError("No question existing for question id given"));
    }
  });
}


exports.createquestion = function (req, res, next) {
  console.log(req.params);
  userid = req.params.userid;
  question = req.params.question;
  anwser1 = req.params.answer1;
  anwser2 = req.params.answer2;
  anwser3 = req.params.answer3;
  anwser4 = req.params.answer4;
  tags = req.params.tag;
  console.log("createquestion - step1")
  User.checkId(userid, function (err, user) {
    if (user) {
      console.log("user found -step2")
      answer1 = new Answer({
        answer : anwser1,
      });

      answer2 = new Answer({
        answer : anwser2,
      });

      answer3 = new Answer({
        answer : anwser3,
      });

      answer4 = new Answer({
        answer : anwser4,
      });

      question = new Question({
        question  : question,
        createdBy : userid
      });

      tagArray = tags.split(",");
      for (t in tagArray) {
        question.tag.push(tagArray[t]);
      }

      question.answers.push(answer1);
      question.answers.push(answer2);
      question.answers.push(answer3);
      question.answers.push(answer4);
      question.save ( function (err, question) {
        if (err) return console.error(err);
        res.send(question);
      });
      next();
    } else {
      next(new restify.InvalidArgumentError("User id given does not exists"));
    }
  });
}

exports.question = function (req, res, next) {
    pageLimit = 20;
    page = req.query.page;
    tag  = req.query.tag;
    console.log("Querying endpoints for questions page " + page + " page limit" + pageLimit);
    where = {};

    if (undefined != tag && tag.length > 0) {
      console.log(" We are building the regexp ");
      reg = new RegExp(tag, 'i');
      where = {'tag': reg};
    }

    Question.find( where )
      .skip( page * pageLimit)
      .limit(pageLimit)
      .sort ({ created : 'desc'})
      .exec(function (err, questions) {
        if (err)next(new restify.InvalidArgumentError("Something did not work while querrying question"));
         console.log("Querying endpoints for questions page " + page +
			" page limit" + pageLimit + "question return " + questions);
		res.send(questions);
        next();
      });
}

