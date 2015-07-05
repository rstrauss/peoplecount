package org.peoplecount.getresults;

import java.util.ArrayList;

import org.w3c.dom.NodeList;

public class GetProfileData {
	private static int FILTER_OFFSET = 3;

	GetProfileData() {}

	ArrayList<Question> get(NodeList nodes) {
		ArrayList<NodeMaster> rawData = new ArrayList<NodeMaster>();

		addDataNodes(nodes, rawData);
		filterList(rawData);
		
		//The first nodes are garbage
		for (int index = 0; index < FILTER_OFFSET+2; index++)
			rawData.remove(0);

		ArrayList<Question> data = new ArrayList<Question>();
		data = setupData(rawData);
		return data;
	}

	//should be finished
	private static void addDataNodes(NodeList nodes, ArrayList<NodeMaster> rawData) {
		for (int index = 0; index < nodes.getLength(); index++) {
			String protoFilter = nodes.item(index).getNodeName();
			
			if (protoFilter != "div" && protoFilter != "ol") {
				continue;
			}
				
			NodeMaster meta = new NodeMaster(nodes.item(index));
			if (meta.isData()) {
				rawData.add(meta);
			}
		}
	}
	
	/**
	 * 
	 * (%) 
	 * %(#)
	 * #XX(#)%
	 * >> Ignore "ol"s
	 * This should work
	 */
	private static void filterList(ArrayList<NodeMaster> rawData) {
		// Keep only an ol node, a percent node,
		//   and the 2 after a percent, 
		//   and the third after a percent if the next is also a percent.
		for (int index = FILTER_OFFSET; index < rawData.size()-1; index++) {

			NodeMaster x = rawData.get(index);
			if (x.isOlElement())
				continue;

			if (x.isPercent())
				continue;

			if (rawData.get(index-1).isPercent())
				continue;

			if (rawData.get(index-2).isPercent())
				continue;

			if (!rawData.get(index-3).isPercent() && rawData.get(index+1).isPercent())
				continue;

			rawData.remove(index);
			index--;
		}
	}

	enum AutoState {
		QUESTION, // = 0,
		ANSWER, // = 1,
		PERCENT, // 2;
		END_OF_INPUT // = 2;
	};

	/**
	 * 
	 */
	ArrayList<Question> setupData(ArrayList<NodeMaster> rawData) {
		Question q = null;
		QuestionAnswer qa;
		int questionNum = 1;
		int subQuestionNum = 1;
		int percent = 0;
		boolean jump = false;
		byte olLength = 0;
		AutoState automataState = AutoState.QUESTION;
		
		ArrayList<Question> data = new ArrayList<Question>(25);

		for (int index = 0; index < rawData.size(); index++) {
			//System.out.println("Evaluating: " + rawData.get(index));

			if (rawData.get(index).isOlElement()) {
				olLength = rawData.get(index).olLength();
				automataState = AutoState.QUESTION;
				jump = true;
				continue;
			} 

			switch (automataState) {

			case QUESTION: {

				//qp("olLen: " + olLength);

				if (olLength > 0 && jump) {
					data.add(q);
				} else if (olLength > 0) {
					data.get(data.size()-1).addSubQuestion(q);
					olLength--;
				} else {
					if (q != null) {
						data.add(q);
					}
				}

				if (olLength > 0) {
					q = new Question(questionNum-1, rawData.get(index).toString());
					q.setSubqNum(subQuestionNum);
					subQuestionNum++;
				} else {
					q = new Question(questionNum, rawData.get(index).toString());
					questionNum++;
					subQuestionNum = 1;
				}

				jump = false;
				automataState = AutoState.PERCENT;
				break;
			}

			case ANSWER: {
				qa = new QuestionAnswer(rawData.get(index).toString(), percent, 0);
				q.addAnswer(qa);

				if (index == rawData.size()-1) {
					data.add(q);
					break;
				}

				NodeMaster nextNode = rawData.get(index + 1);
				if (nextNode.isPercent() && !nextNode.isOlElement()) {
					automataState = AutoState.PERCENT;
				} else {
					automataState = AutoState.QUESTION;
				}

				break;
			}

			case PERCENT: {
				String s = rawData.get(index).toString();
				s = s.substring(0, s.length()-1);
				percent = Integer.parseInt(s);

				automataState = AutoState.ANSWER;
				break;
			}

			case END_OF_INPUT:
				// do nothing. This case satisfies Java syntax
			}
		} // end of case
		
		return data;
	}
	
}
