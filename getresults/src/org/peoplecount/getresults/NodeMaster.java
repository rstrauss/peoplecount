package org.peoplecount.getresults;

import org.w3c.dom.Node;

/**
 * 
 * @author Benjy Strauss
 *
 */

public class NodeMaster {
	
	protected enum NodeType { USELESS, DATA, DUPLICATE }
	
	protected Node meta;
	protected NodeType nodeClass;
	protected String nodeString;
	protected boolean isPercent;
	protected boolean ol;
	protected byte olLength;
	
	public NodeMaster(Node input) {
		isPercent = true;
		meta = input;
		if(meta.getFirstChild() == null) {
			nodeClass = NodeType.USELESS;
		} else {
			nodeClass = NodeType.DATA;
		}
		
		processString();
		
		if(input.getNodeName() == "ol") {
			ol = true;
			setOlLength();
			nodeClass = NodeType.DATA;
		} else {
			ol = false;
		}
	}
	

	public Node getNode() { return meta; }	
	public boolean isUseless() { if(nodeClass == NodeType.USELESS) { return true; } else { return false; } }
	public boolean isData() { if(nodeClass == NodeType.DATA) { return true; } else { return false; } }
	public boolean isDuplicate() { if(nodeClass == NodeType.DUPLICATE) { return true; } else { return false; } }
	public boolean isPercent() { return isPercent; }
	public boolean ol() { return ol; }
	public byte olLength() { return olLength; }
	
	public String toString() {
		return nodeString;
	}

	private void processString() {
		if(meta.getNodeName() == "ol") {
			nodeString = "****OL****";
			return;
		}
		
		String processMe = meta.getTextContent();
		nodeString = processMe.replace('\n',' ');
		
		if(nodeString.length() != 0) {
			if(nodeString.charAt(0) == 32) {
				nodeClass = NodeType.DUPLICATE;
			}
		} else {
			nodeClass = NodeType.USELESS;
			isPercent = false;
		}
		
		if(nodeClass == NodeType.DATA) {
			for(int strIndex = 0; strIndex < nodeString.length(); strIndex++) {
				if(nodeString.charAt(strIndex) < 48 || nodeString.charAt(strIndex) > 57) {
					if(nodeString.charAt(strIndex) != 37) {
						isPercent = false;
						break;
					}
				}
			}
		}
	}
	
	private void setOlLength() {
		olLength = 1;
		String stageOne =  meta.getTextContent().replace('\n','|');
		
		int barCount = 0;
		
		for(int strPos = 0; strPos < stageOne.length(); strPos++ ) {
			if(stageOne.charAt(strPos) == '|') {
				barCount++;
			} else {
				barCount = 0;
			}
			
			if(barCount == 10) {
				olLength++;
			}
		}
	}
}
