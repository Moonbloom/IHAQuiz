package com.moon.ihaquiz;

public class QuestionChoice
{
	private String mChoiceText;
	private boolean mChoiceCorrect;
	
	public QuestionChoice()
	{}
		
	public void setChoiceText(String choiceText)
	{
		mChoiceText = choiceText;
	}
	
	public String getChoiceText()
	{
		return mChoiceText;
	}
	
	public void setChoiceCorrectOrFalse(boolean trueOrFalse)
	{
		mChoiceCorrect = trueOrFalse;
	}
	
	public boolean isAnswerCorrect()
	{
		return mChoiceCorrect;
	}
}