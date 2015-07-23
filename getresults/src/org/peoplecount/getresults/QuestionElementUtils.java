package org.peoplecount.getresults;

import java.util.ArrayList;

import org.w3c.dom.Element;

public class QuestionElementUtils extends XmlElementUtils {

	protected ArrayList<Question> processQuestionList(Element listContainer) {
		//printElt("processQuestionList, li=", li);
		ArrayList<Question> questions = new ArrayList<Question>();
		while (listContainer != null) {
			Question q = new Question();
			q.fill(listContainer);
			questions.add(q);
			listContainer = getNextElement(listContainer, "li");
		}

		// Number them
		for (int i = 0;  i < questions.size();  i++)
			questions.get(i).setNum(i+1);
		return questions;
	}

}
