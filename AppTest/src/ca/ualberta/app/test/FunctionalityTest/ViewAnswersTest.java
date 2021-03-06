package ca.ualberta.app.test.FunctionalityTest;

import java.util.ArrayList;
import ca.ualberta.app.ESmanager.QuestionListManager;
import ca.ualberta.app.controller.QuestionListController;
import ca.ualberta.app.models.Answer;
import ca.ualberta.app.models.Question;
import junit.framework.TestCase;

public class ViewAnswersTest extends TestCase {
	public void testViewAnswersOffline() {
		// Create a Question and an Answer objects
		String questionString = "A Question";
		String answerString = "A Answer";
		String titleString = "title";
		String imageByteArray = null;
		long userId = 123456789;
		Question question = new Question(questionString, userId, titleString,
				imageByteArray);
		Answer answer = new Answer(answerString, userId, imageByteArray);

		// add the answer to the Question, and add the Quesiton to the
		// QuesitonList
		QuestionListController inputsListController = new QuestionListController();
		question.addAnswer(answer);
		inputsListController.addQuestion(question);

		// get the AnswerList from the List
		ArrayList<Answer> answerList = inputsListController.getAnswers(0);
		Answer result = answerList.get(0);

		// check the result
		assertTrue("User cannot view answers", answerList.size() == 1);
		assertEquals(answer.getContent(), result.getContent());
	}

	public void testViewAnswersOffline_extremeCase() {
		// Create a Question and an Answer Objects
		String questionString = "A Question";
		String answerString = "A Answer";
		String titleString = "title";
		String imageByteArray = null;
		long userId = 123456789;
		Question question = new Question(questionString, userId, titleString,
				imageByteArray);
		Answer answer = new Answer(answerString, userId, imageByteArray);

		// add the Answer to the Question, and add Question to the List
		QuestionListController inputsListController = new QuestionListController();
		question.addAnswer(answer);
		inputsListController.addQuestion(question);

		// get the Question position and answer position of their List
		int position_q = inputsListController.getQuestionPosition(question);
		int position_a = inputsListController.getAnswerPosition(answer,
				position_q);

		// get the answer using the position
		ArrayList<Answer> answerList = inputsListController
				.getAnswers(position_q);

		// check the result
		Answer result = answerList.get(position_a);
		assertTrue("User cannot view answers", answerList.size() == 1);
		assertEquals(answer.getContent(), result.getContent());
	}

	public void testViewAnswerOnline() {
		// create a Question and Answer object
		String questionString = "A Question";
		String answerString = "A Answer";
		String titleString = "title";
		String imageByteArray = null;
		long userId = 123456789;
		Question question = new Question(questionString, userId, titleString,
				imageByteArray);
		Answer answer = new Answer(answerString, userId, imageByteArray);

		// add the answer to the Question, and add the Question to the server
		QuestionListManager questionListManager = new QuestionListManager();
		question.addAnswer(answer);
		questionListManager.addQuestion(question);

		// get the Question from the server
		Question targetQuestion = questionListManager.getQuestion(question
				.getID());

		// get the answerList
		ArrayList<Answer> answerList = targetQuestion.getAnswers();
		Answer result = answerList.get(0);

		// check the result
		assertTrue("User cannot view answers", answerList.size() == 1);
		assertEquals(answer.getContent(), result.getContent());

		questionListManager.deleteQuestion(question.getID());
	}
}
