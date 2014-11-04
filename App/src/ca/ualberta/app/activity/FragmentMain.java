package ca.ualberta.app.activity;

import ca.ualberta.app.activity.R;
import ca.ualberta.app.adapter.QuestionListAdapter;
import ca.ualberta.app.controller.QuestionListController;
import ca.ualberta.app.models.Question;
import ca.ualberta.app.models.QuestionListManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

//The fragment part is from this website: http://www.programering.com/a/MjNzIDMwATI.html 2014-Oct-20

public class FragmentMain extends Fragment {
	private QuestionListAdapter adapter = null;
	private QuestionListController questionListController = null;
	private TextView titleBar = null;
	private ListView questionListView = null;
	private Spinner sortOptionSpinner;
	private QuestionListManager questionListManager;
	private Context mcontext;
	private ArrayAdapter<?> spin_adapter;
	private static long categoryID;
	private String sortString = null;

	// Thread to update adapter after an operation
	private Runnable doUpdateGUIList = new Runnable() {
		public void run() {
			adapter.notifyDataSetChanged();
			spin_adapter.notifyDataSetChanged();
		}
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mcontext = getActivity().getApplicationContext();
		return inflater.inflate(R.layout.fragment_main, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		titleBar = (TextView) getView().findViewById(R.id.titleTv);
		titleBar.setText("Main");
		questionListView = (ListView) getView().findViewById(
				R.id.question_listView);
		sortOptionSpinner = (Spinner) getView().findViewById(R.id.sort_spinner);
	}

	@Override
	public void onStart() {
		super.onStart();
		questionListManager = new QuestionListManager();
		questionListController = new QuestionListController();
		adapter = new QuestionListAdapter(mcontext, R.layout.single_question,
				questionListController.getQuestionArrayList());
		spin_adapter = ArrayAdapter.createFromResource(mcontext,
				R.array.list_type,
				android.R.layout.simple_spinner_dropdown_item);

		questionListView.setAdapter(adapter);
		sortOptionSpinner.setAdapter(spin_adapter);
		sortOptionSpinner
				.setOnItemSelectedListener(new change_category_click());
		// Show details when click on a question
		questionListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int pos,
					long id) {
				long questionID = questionListController.getQuestion(pos)
						.getID();

				Intent intent = new Intent(mcontext,
						QuestionDetailActivity.class);
				intent.putExtra(QuestionDetailActivity.QUESTION_ID, questionID);

				startActivity(intent);

			}

		});

		// Delete question on long click
		questionListView
				.setOnItemLongClickListener(new OnItemLongClickListener() {

					@Override
					public boolean onItemLongClick(AdapterView<?> parent,
							View view, int position, long id) {
						Question question = questionListController
								.getQuestion(position);
						Toast.makeText(
								mcontext,
								"Deleting the Question: " + question.getTitle(),
								Toast.LENGTH_LONG).show();

						Thread thread = new DeleteThread(question.getID());
						thread.start();

						return true;
					}
				});
		// updateList();
	}

	private class change_category_click implements OnItemSelectedListener {
		public void onItemSelected(AdapterView<?> parent, View view,
				int position, long id) {
			categoryID = position;

			// sort by Date
			if (categoryID == 0) {
				sortString = "date";
				updateList();
			}

			// sort by Picture
			if (categoryID == 1) {
				sortString = "picture";
				updateList();
			}

			// sort by Score
			if (categoryID == 2) {
				sortString = "score";
				updateList();
			}
			// sort by Question upvote
			if (categoryID == 3) {
				sortString = "q_upvote";
				updateList();
			}

			// sort by Answer upvote
			if (categoryID == 4) {
				sortString = "a_upvote";
				updateList();
			}
		}

		public void onNothingSelected(AdapterView<?> parent) {
			sortOptionSpinner.setSelection(3);
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		updateList();
	}

	@Override
	public void onPause() {
		super.onPause();
		// updateList();
	}

	private void updateList() {
		questionListController.clear();
		Thread thread = new SearchThread("");
		thread.start();
	}

	class SearchThread extends Thread {
		// TODO: Implement search thread
		private String search;

		public SearchThread(String s) {
			search = s;

		}

		public void run() {
			questionListController.clear();
			questionListController.addAll(questionListManager.searchQuestions(
					search, null, sortString));

			getActivity().runOnUiThread(doUpdateGUIList);
		}
	}

	class DeleteThread extends Thread {
		private long questionID;

		public DeleteThread(long questionID) {
			this.questionID = questionID;
		}

		@Override
		public void run() {
			questionListManager.deleteQuestion(questionID);

			// Remove movie from local list
			for (int i = 0; i < questionListController.size(); i++) {
				Question q = questionListController.getQuestion(i);

				if (q.getID() == questionID) {
					questionListController.removeQuestion(i);
					break;
				}
			}

			getActivity().runOnUiThread(doUpdateGUIList);
		}
	}

}
