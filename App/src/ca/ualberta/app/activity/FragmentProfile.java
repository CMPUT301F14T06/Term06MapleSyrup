/*
 * Copyright 2014 Anni Dai
 * Copyright 2014 Bicheng Yan
 * Copyright 2014 Liwen Chen
 * Copyright 2014 Liang Jingjing
 * Copyright 2014 Xiaocong Zhou
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ca.ualberta.app.activity;

import ca.ualberta.app.activity.R;
import ca.ualberta.app.models.User;
import ca.ualberta.app.network.InternetConnectionChecker;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

/**
 * This is the fragment activity for the mean question list, once the app is
 * started, or a user clicks the "Main" button on the bottom action bar
 * 
 * The fragment part is from this web site:
 * http://www.programering.com/a/MjNzIDMwATI.html
 * 
 */
public class FragmentProfile extends Fragment {
	private TextView titleBar;
	private ImageButton changePhotoButton;
	private TextView setAuthorName;
	private RadioGroup profile_menu;
	private RadioButton local_cache;
	private RadioButton fav_question;
	private RadioButton my_question;
	private RadioButton waiting_list;
	private RadioButton login;
	private RadioButton logout;
	private boolean loginStatus;
	private Context mcontext;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		return inflater.inflate(R.layout.fragment_profile, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mcontext = getActivity().getApplicationContext();
		titleBar = (TextView) getView().findViewById(R.id.titleTv);
		titleBar.setText("Profile");
		changePhotoButton = (ImageButton) getView().findViewById(
				R.id.changePhotoButton);
		setAuthorName = (TextView) getView().findViewById(R.id.setAuthorName);
		profile_menu = (RadioGroup) getView().findViewById(R.id.profile_menu);
		local_cache = (RadioButton) getView().findViewById(R.id.local_cache);
		fav_question = (RadioButton) getView().findViewById(R.id.fav_question);
		my_question = (RadioButton) getView().findViewById(R.id.my_question);
		waiting_list = (RadioButton) getView().findViewById(R.id.waiting_list);
		login = (RadioButton) getView().findViewById(R.id.login);
		logout = (RadioButton) getView().findViewById(R.id.logout);
		checkLoginStatus();

		login.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), LoginActivity.class);
				startActivity(intent);

			}
		});

		logout.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				User.loginStatus = false;
				User.author = null;
				getActivity().recreate();
			}
		});

		my_question.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(),
						MyQuestionActivity.class);
				startActivity(intent);

			}
		});

		fav_question.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(),
						MyFavoriteActivity.class);
				startActivity(intent);

			}
		});

		local_cache.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), MyLocalActivity.class);
				startActivity(intent);
			}
		});
	}


	@Override
	public void onResume() {
		super.onResume();
		checkLoginStatus();
		if (InternetConnectionChecker.isNetworkAvailable(mcontext)) {
			titleBar.setText("Profile");
		} else {
			titleBar.setText("Profile(Not Connected)");
		}
	}


	@Override
	public void onPause() {
		super.onPause();
		checkLoginStatus();
	}

	public void checkLoginStatus() {
		loginStatus = User.loginStatus;
		if (loginStatus) {
			changePhotoButton.setVisibility(View.VISIBLE);
			setAuthorName.setVisibility(View.VISIBLE);
			login.setVisibility(View.GONE);
			waiting_list.setVisibility(View.VISIBLE);
			logout.setVisibility(View.VISIBLE);
			my_question.setVisibility(View.VISIBLE);
			setAuthorName.setText(User.author.getUsername());

		} else {
			changePhotoButton.setVisibility(View.GONE);
			setAuthorName.setVisibility(View.GONE);
			waiting_list.setVisibility(View.GONE);
			login.setVisibility(View.VISIBLE);
			logout.setVisibility(View.GONE);
			my_question.setVisibility(View.GONE);
		}
	}
}