package com.moon.ihaquiz;

import java.util.ArrayList;
import java.util.Random;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

public class MainActivity extends Activity {

	private TextView mQuestionTextView;
	private Button mButtonPrevious;
	private Button mButtonNext;
	private Button mButtonHighlightCorrectAnswer;
	private Button[] mButtonChoiceArray = new Button[4];
	
	private static final String SaveQuestionPosition = "index1";
	private static final String SavePreviousQuestionNumbers = "index2";
	private final int mQuestionAmount = 25;
	private Question[] mQuestion = new Question[mQuestionAmount];
	private int mCurrentQuestionIndex;
	private int buttonNumber;
	private int SeriesOrRandomQuestionOrder = 1;
	
	private ArrayList<Integer> mPrevQuestionNumbers = new ArrayList<Integer>();
	
	@TargetApi(11)
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		//SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
		
		//SlidingMenu
		SlidingMenu menu = new SlidingMenu(this);
		menu.setMode(SlidingMenu.LEFT);
		menu.setFadeEnabled(true);
		menu.setShadowDrawable(R.drawable.shadow);
        menu.setFadeDegree(0.80f);
        menu.setBehindOffset(50);
        menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
        menu.setMenu(R.layout.expand_listview_main);
        
        //TEST START - If these two lines are enabled, the ExpandableListView will show up as a new activity on top of my MainActivity
        Intent intent = new Intent(this, SlideMainMenu.class);
		startActivity(intent); 
        //TEST END
		
        setQuestionData();
		
        //ActionBar (only works on 3.0+) - ActionBarSherlock could replace this and work across all Android versions if needed/wanted
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			ActionBar actionBar = getActionBar();
			actionBar.setSubtitle("Engineering College of Aarhus - Moon");
		}
		
		mButtonChoiceArray[0] = (Button)findViewById(R.id.choice_1_id);
		mButtonChoiceArray[0].setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				CorrectOrFalseToast(mQuestion[mCurrentQuestionIndex].isAnswerCorrect(0));
			}
		});
		
		mButtonChoiceArray[1] = (Button)findViewById(R.id.choice_2_id);
		mButtonChoiceArray[1].setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				CorrectOrFalseToast(mQuestion[mCurrentQuestionIndex].isAnswerCorrect(1));
			}
		});
		
		mButtonChoiceArray[2] = (Button)findViewById(R.id.choice_3_id);
		mButtonChoiceArray[2].setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				CorrectOrFalseToast(mQuestion[mCurrentQuestionIndex].isAnswerCorrect(2));
			}
		});
		
		mButtonChoiceArray[3] = (Button)findViewById(R.id.choice_4_id);
		mButtonChoiceArray[3].setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				CorrectOrFalseToast(mQuestion[mCurrentQuestionIndex].isAnswerCorrect(3));
			}
		});
		
		mButtonNext = (Button)findViewById(R.id.next_button_id);
		mButtonNext.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(SeriesOrRandomQuestionOrder == 0)
				{
					//Option 1 - Questions in series
					mCurrentQuestionIndex = (mCurrentQuestionIndex + 1) % mQuestionAmount;
				}
				else if(SeriesOrRandomQuestionOrder == 1)
				{
					//Option 2 - Questions in random order
					mPrevQuestionNumbers.add(mCurrentQuestionIndex);
					Random r = new Random();
					mCurrentQuestionIndex = r.nextInt(mQuestionAmount-0) + 0;
				}
				
				updateQuestion();
				setOriginalBG();
			}
		});
		
		mButtonPrevious = (Button)findViewById(R.id.previous_button_id);
		mButtonPrevious.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) 
			{
				if(SeriesOrRandomQuestionOrder == 0)
				{
					//Option 1 - Questions in series
					mCurrentQuestionIndex = (mCurrentQuestionIndex - 1);
					//Enables the "Previous" button to go from the first question to the last question
					if(mCurrentQuestionIndex == -1)
					{
						mCurrentQuestionIndex = (mQuestionAmount-1);
					}
				}
				else if(SeriesOrRandomQuestionOrder == 1)
				{
					//Option 2 - Questions in random order
					if(mPrevQuestionNumbers.isEmpty())
					{
						mCurrentQuestionIndex = 0;
					}
					else
					{
						mCurrentQuestionIndex = mPrevQuestionNumbers.remove(mPrevQuestionNumbers.size()-1);
					}
				}

				updateQuestion();
				setOriginalBG();
			}
		});
		
		mButtonHighlightCorrectAnswer = (Button)findViewById(R.id.show_answer_button_id);
		mButtonHighlightCorrectAnswer.setOnClickListener(new View.OnClickListener() {
			@TargetApi(16)
			@Override
			public void onClick(View v) {
				buttonNumber = mQuestion[mCurrentQuestionIndex].getCorrectChoiceNumber();
				mButtonChoiceArray[buttonNumber].getBackground().setColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
				if(buttonNumber != 0)
					mButtonChoiceArray[0].getBackground().setColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);
				if(buttonNumber != 1)
					mButtonChoiceArray[1].getBackground().setColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);
				if(buttonNumber != 2)
					mButtonChoiceArray[2].getBackground().setColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);
				if(buttonNumber != 3)
					mButtonChoiceArray[3].getBackground().setColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);
				
				Handler delayBGHandler = new Handler();
				delayBGHandler.postDelayed(new Runnable() {
					public void run()
					{
						setOriginalBG();
					}
				}, 2500); 
			}
		});
		
		if (savedInstanceState != null) {
			mCurrentQuestionIndex = savedInstanceState.getInt(SaveQuestionPosition, 0);
			mPrevQuestionNumbers = savedInstanceState.getIntegerArrayList(SavePreviousQuestionNumbers);
		}
		
		//Makes a question appear at first when opening the application
		updateQuestion();
	}

	private void setQuestionData()
	{
		//TEMP
		for(int i = 0; i < mQuestionAmount; i++)
		{
			mQuestion[i] = new Question();
			mQuestion[i].setQuestionText(String.valueOf(i));
		}
		mQuestion[0].setChoiceData(1, "SoftWare Test", true);
		
		/*int i = 0;
		mQuestion[i] = new Question();
		mQuestion[i].setQuestionText("What does SWT stand for?");
		mQuestion[i].setChoiceData(0, "SoftWare Test", true);
		mQuestion[i].setChoiceData(1, "Semi Working Tractor", false);
		mQuestion[i].setChoiceData(2, "Senile Walking Tortoise", false);
		mQuestion[i].setChoiceData(3, "Should i Walk There", false);
		
		i++;
		mQuestion[i] = new Question();
		mQuestion[i].setQuestionText("What does SWD stand for?");
		mQuestion[i].setChoiceData(0, "SoftWare Design", true);
		mQuestion[i].setChoiceData(1, "Semi Working Dealer", false);
		mQuestion[i].setChoiceData(2, "Senile Walking Dentist", false);
		mQuestion[i].setChoiceData(3, "Seeing Weird Dog", false);*/
	}
	
	private void updateQuestion()
	{
		mQuestionTextView = (TextView)findViewById(R.id.question_text_view_id);
		mQuestionTextView.setText(mQuestion[mCurrentQuestionIndex].getQuestionText());
		mButtonChoiceArray[0].setText(mQuestion[mCurrentQuestionIndex].getChoiceText(0));
		mButtonChoiceArray[1].setText(mQuestion[mCurrentQuestionIndex].getChoiceText(1));
		mButtonChoiceArray[2].setText(mQuestion[mCurrentQuestionIndex].getChoiceText(2));
		mButtonChoiceArray[3].setText(mQuestion[mCurrentQuestionIndex].getChoiceText(3));
	}
	
	private void setOriginalBG()
	{
		for(int i = 0; i < mButtonChoiceArray.length; i++)
		{
			mButtonChoiceArray[i].getBackground().clearColorFilter();
		}
	}
	
	private void CorrectOrFalseToast(boolean answer)
	{
		int messageResId = 0;
		if(answer == true)
		{
			messageResId = R.string.correct_toast;
		}
		else
		{
			messageResId = R.string.incorrect_toast;
		}
		Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show();
	}
	
	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
		super.onSaveInstanceState(savedInstanceState);
		savedInstanceState.putInt(SaveQuestionPosition, mCurrentQuestionIndex);
		savedInstanceState.putIntegerArrayList(SavePreviousQuestionNumbers, mPrevQuestionNumbers);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		//Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId())
		{
			case R.id.actionbar_settings_id:
				Intent intent = new Intent(this, ActionBarSettings.class);
				startActivity(intent); 
				return true;
			
			case R.id.actionbar_about_id:
				
				return true;
				
			default:
				return super.onOptionsItemSelected(item);
		}
	}
}