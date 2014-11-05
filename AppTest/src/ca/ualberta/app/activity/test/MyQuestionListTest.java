package ca.ualberta.app.activity.test;

import android.graphics.Bitmap;
import ca.ualberta.app.models.Author;
import ca.ualberta.app.models.AuthorMapManager;
import ca.ualberta.app.models.Question;
import junit.framework.TestCase;

public class MyQuestionListTest extends TestCase {
	public void testAddToMyQuestionList(){
		String userName = "TestUserName";
		Author author = new Author(userName);
		AuthorMapManager authorMapManager = new AuthorMapManager();
		authorMapManager.addAuthor(author);
		
		String questionString = "A Question";
		String titleString = "title";
		Bitmap image = null;
		Question question = new Question(questionString, userName, titleString,
				image);
		
		author.addAQuestion(question.getID());
		authorMapManager.updateAuthor(author);
		Author result = authorMapManager.getAuthor(userName);
		
		assertTrue(result.getAuthorQuestionId().size() == 1);
		
		authorMapManager.deleteAuthor(userName);
	}
}