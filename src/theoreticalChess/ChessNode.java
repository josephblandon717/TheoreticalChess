package theoreticalChess;
import java.util.*;

import javax.swing.ImageIcon;

// Class that represents a node in the chess decision tree.
public class ChessNode {
	
	// The move the a chess node holds. 
	String move;
	
	// The list of children of a node. 
	HashMap<String, ChessNode> children;
	
	// The variation that leads to this node
	String variation;
	
	// Constructor to create a chess node. 
	public ChessNode(String move, HashMap<String, ChessNode> children) {
		this.move = move;
		this.children = children;
	}
	
	// Constructor to create a chess node with a variation
	public ChessNode(String move, HashMap<String, ChessNode> children, String variation) {
		this.move = move;
		this.children = children;
		this.variation = variation;
	}
	
	// Method to return the move of a node
	public String getMove(){
		return move;
	}
	
	// Method to add a child to the list of children. 
	public void addChild(ChessNode child){
		children.put(child.getMove(), child);
	}
	
	// Method that returns a child via the index
	public ChessNode getChild(int index) {
		return children.get(index);
	}
	
	// Method that returns a child via the move. 
	public ChessNode getChild(String move) {
		for(ChessNode node : children.values()) {
			if (node.getMove().equalsIgnoreCase(move))
				return node;
		}
		return null;
	}
	
	
	// Function to compare two children, false if the moves are not the same, true if the moves are the same
	public boolean equals(ChessNode node) {
		if(this.getMove().equals(node.getMove()))
			return true;
		else
			return false;
	}
	
	
}
