package org.peoplecount.getresults;

import org.w3c.dom.Node;

/**
 * 
 * @author Benjy Strauss
 *
 */

public class NodeMaster {
	public static final byte USELESS_NODE = -1;
	public static final byte DATA_NODE = 0;
	public static final byte DUPLICATE_NODE = 1;
	
	protected Node meta;
	protected byte nodeClass;
	protected String nodeString;
	protected boolean isPercent;
	
	public NodeMaster(Node input) {
		isPercent = true;
		meta = input;
		if(meta.getFirstChild() == null) {
			nodeClass = USELESS_NODE;
		} else {
			nodeClass = DATA_NODE;
		}
		
		processString();
	}
	
	public Node getNode() { return meta; }	
	public int getNodeClass() { return nodeClass; }
	public boolean isPercent() { return isPercent; }
	
	public String toString() {
		return nodeString;
	}
	
	// TODO: maybe call this processTextContent() instead
	// TODO: please put space after "if", so "if (", not "if(" - more standard
	private void processString() {
		String processMe = meta.getTextContent();
		nodeString = processMe.replace('\n',' ');
		
		if(nodeString.length() != 0) {
			if(nodeString.charAt(0) == 32) {
				nodeClass = DUPLICATE_NODE;
			}
		} else {
			nodeClass = USELESS_NODE;
			isPercent = false;
		}
		
		if(nodeClass == DATA_NODE) {
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
}
