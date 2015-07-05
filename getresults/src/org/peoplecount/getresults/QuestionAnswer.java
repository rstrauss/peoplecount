package org.peoplecount.getresults;

/**
 * 
 * @author Benjy Strauss
 *
 */

public class QuestionAnswer {
	protected String answer;
	protected int percentAnswered;
	protected int numAnswered;
	
	public QuestionAnswer(String text, int percent, int num) {
		answer = text;
		percentAnswered = percent;
		numAnswered = num;
	}
	
	public String getText() {
		return answer;
	}
	
	public long getTotalAnswers() {
		return percentAnswered;
	}
	
	public String toString() {
		String retVal = "Chosen by ";
		if (numAnswered > 0) 
			return numAnswered + " : " + answer;

		if (percentAnswered < 10)
			retVal += "0";

		retVal += percentAnswered + "%: " + answer;
		return retVal;
	}
}
