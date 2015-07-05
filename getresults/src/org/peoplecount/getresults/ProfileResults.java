package org.peoplecount.getresults;

import java.util.ArrayList;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ProfileResults extends XmlElementUtils {
	String issueArea;
	int numUsersWhoAnswered;

	ProfileResults() {}
	
	ArrayList<Question> fill(Element mainElt) {
		printElt("Question.fill: mainElt", mainElt);
		Element h1 = getH1(mainElt);
		printElt("Question.fill: h1", h1);
		Element div = getDivWithIssueAndNumUsers(h1);
		printElt("Question.fill: div", div);
		Element divListParent = getNextElementOfClass(div, "div", "survey list_style_two");
		printElt("Question.fill: divListParent", divListParent);
		Element li = getFirstListElement(divListParent);

		ArrayList<Question> list = processQuestionList(li);
		for (int i = 0;  i < list.size();  i++)
			list.get(i).setNum(i+1);
		return list;
	}
	
	Element getH1(Element mainElt) {
		NodeList h1s = mainElt.getElementsByTagName("h1");
		Node h1Node = h1s.item(0);
		return (Element)h1Node;
	}
	
	Element getDivWithIssueAndNumUsers(Element h1) {
		Node h1contents = h1.getFirstChild();
		issueArea = h1contents.getTextContent();
		Node nextDiv = getNextElement(h1, "div");
		String text = nextDiv.getTextContent().trim();
		numUsersWhoAnswered = 0;
		for (int i = 0;  i < text.length();  i++) {
			char c = text.charAt(i);
			if (c < '0' || '9' < c)
				break;
			numUsersWhoAnswered = (10 * numUsersWhoAnswered) + (c - '0');
		}
		return (Element)nextDiv;
	}

	private Element getFirstListElement(Element container) {
		// assert: container is the <div class="survey list_style_two"
		Node ol = getFirstElement(container);
		Node li = getFirstElement(ol);
		return (Element)li;
	}
	
}
