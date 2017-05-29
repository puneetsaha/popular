var userhandler = require('./handlers/userhandler.js');
var questionhandler = require('./handlers/questionhandler.js');


module.exports = function(server){
  server.post({path: "/signup", version: '0.0.1'}, userhandler.signup);
  server.post({path: "/login", version: '0.0.1'}, userhandler.login);
  server.post({path: "/user", version: '0.0.1'}, userhandler.userid);



  server.post({path: "question/answer", version: '0.0.1'}, questionhandler.answer);
  server.post({path: "question/answer_batch", version: '0.0.1'}, questionhandler.answerBatch);
  server.post({path: "question/", version: '0.0.1'}, questionhandler.createquestion);
  server.get({path: "question/", version: '0.0.1'}, questionhandler.question);
  server.get({path: "question/search/{tag}", version: '0.0.1'}, questionhandler.question);

}
