package com.moon.ihaquiz;

public class Question
{
	private String mQuestion;
	private QuestionChoice[] mChoice = new QuestionChoice[4];
	private int mCorrectAnswerNumber;
	
	public Question()
	{
		for(int i = 0; i < mChoice.length; i++)
		{
			mChoice[i] = new QuestionChoice();
		}
		
		//TEMP x4
		mChoice[0].setChoiceText("Choice 1");
		mChoice[1].setChoiceText("Choice 2");
		mChoice[2].setChoiceText("Choice 3");
		mChoice[3].setChoiceText("Choice 4");
	}
	
	public void setQuestionText(String QText)
	{
		mQuestion = QText;
	}
	
	public String getQuestionText()
	{
		return mQuestion;
	}
	
	public void setChoiceData(int numberOfChoice, String choiceText, boolean trueOrFalse)
	{
		mChoice[numberOfChoice].setChoiceText(choiceText);
		mChoice[numberOfChoice].setChoiceCorrectOrFalse(trueOrFalse);
	}
	
	public String getChoiceText(int numberOfChoice)
	{
		return mChoice[numberOfChoice].getChoiceText();
	}
	
	public boolean isAnswerCorrect(int numberOfChoice)
	{
		return mChoice[numberOfChoice].isAnswerCorrect();
	}
	
	public int getCorrectChoiceNumber()
	{
		for(int i = 0; i < mChoice.length; i++)
		{
			if(mChoice[i].isAnswerCorrect() == true)
			{
				mCorrectAnswerNumber = i;
			}
		}
		return mCorrectAnswerNumber;
	}
}