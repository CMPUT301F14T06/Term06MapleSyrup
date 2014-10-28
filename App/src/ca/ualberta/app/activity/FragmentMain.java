package ca.ualberta.app.activity;

import ca.ualberta.app.activity.R;
import ca.ualberta.app.adapter.QuestionListAdapter;
import ca.ualberta.app.models.InputsListModel;
import ca.ualberta.app.models.Question;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

//The fragment part is from this website: http://www.programering.com/a/MjNzIDMwATI.html 2014-Oct-20

public class FragmentMain extends Fragment {
	private QuestionListAdapter adapter=null;
	private InputsListModel currentQuestionList = null;
	private Question newQuestion = null;
	private Bitmap testImage = null;
	private ListView questionListView=null;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		currentQuestionList = new InputsListModel();
		return inflater.inflate(R.layout.fragment_main, container, false);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		currentQuestionList = new InputsListModel();
		newQuestion =new Question("Question1","Name1","Title1",testImage,false);
		currentQuestionList.addQuestion(newQuestion);
		newQuestion =new Question("Question2","Name2","Title2",testImage,false);
		currentQuestionList.addQuestion(newQuestion);
		newQuestion =new Question("Question3","Name3","Title3",testImage,false);
		currentQuestionList.addQuestion(newQuestion);

		questionListView = (ListView) getView().findViewById(R.id.question_listView);
		adapter=new QuestionListAdapter(getActivity(),R.layout.single_question,currentQuestionList.getArrayList());
		questionListView.setAdapter(adapter);
		
	}
}
