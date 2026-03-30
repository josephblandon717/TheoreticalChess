package theoreticalChess;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.datatransfer.StringSelection;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List; 
import java.util.Stack;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import javax.swing.*;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;



// The main menu for Theoretical Chess
public class ChessTree extends JFrame implements ActionListener{
	
	// Saves the nodes of the current traversal.
	static Stack<ChessNode> nodePath = new Stack<ChessNode>();
	
	// Saves the moves of the current traversal.
	static ArrayList<String> movePath = new ArrayList<String>();
	
	// Saves all possible variations.
	static HashMap<String, String> variations = new HashMap<String, String>();
	
	// Stores the root of the chess tree. 
	static ChessNode root = new ChessNode("Welcome to Theoretical Chess, choose  a move!", new HashMap<String, ChessNode>());
	
	// Stores the current node of the chess tree.
	static ChessNode currentNode = root;
	
	// Stores the button panel of all the options.
	static JPanel buttonPanel = new JPanel();
	
	// Stores a dynamic image of a chess board.
	static chessBoardImage chessBoardImage = new chessBoardImage();
	
	// Stores a JLabel that shows the user the current move, path, and variation if there is one.  
	static JLabel currentMoveLabel = new JLabel();
	
	//
	static File varTxt = null;
	
	// Reads in the chess tree from a file
	public static void readInChessTree() throws IOException {
		Path path = Paths.get("variations.txt");
		if (!Files.exists(path))
			Files.createFile(path);
		varTxt = new File(path.toString());
		BufferedReader reader = new BufferedReader(new FileReader(varTxt));
		List<String> lines = reader.lines().toList();
		currentMoveLabel.setText(currentNode.getMove());
		for(String line : lines) {
			currentNode = root;
			String variation = line.split(":")[0].trim();
			String movesBranch = line.split(":")[1].trim();
			String[] moves = movesBranch.split("\\s+");

			variations.put(movesBranch, variation);
			for (String move : moves) {
				if(moves[moves.length - 1].equals(move))
					currentNode.addChild(new ChessNode(move, new HashMap<String, ChessNode>(), variation));
				else if(currentNode.getChild(move) == null)
					currentNode.addChild(new ChessNode(move, new HashMap<String, ChessNode>()));	
				currentNode = currentNode.getChild(move);
					}
				}
		reader.close();
	}
	
	// Gets current variation.
	public String getVariation() {
		if(currentNode.variation != null)
			return currentNode.variation;
		else
			return "No variation";
	}
	
	// Adds a variation to variations.txt
	public void addVariation(String variation, String moves) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(varTxt));
		BufferedWriter writer = new BufferedWriter(new FileWriter("variations.txt", true));
		List<String> lines = reader.lines().toList();
		ArrayList<String> variationsList = new ArrayList<String>();
		ArrayList<String> movesList = new ArrayList<String>();
		for(String line : lines) {
			variationsList.add(line.split(":")[0].trim());
			movesList.add(line.split(":")[1].trim());
		}
		if(!variationsList.contains(variation) && !movesList.contains(moves)) {
			writer.write(variation + " : " + moves);
			writer.newLine();
		}
		writer.close();
		reader.close();
	}
	
	// Removes a variation from variations.txt
	public boolean removeVariation(String variation) throws IOException{
		boolean removed = false;
		BufferedReader reader = new BufferedReader(new FileReader(varTxt));
		List<String> lines = new ArrayList<String>(reader.lines().toList());
		for(int i = 0; i < lines.size();i++) {
			if((lines.get(i).split(":")[0].trim()).compareTo(variation) == 0) {
				lines.remove(lines.get(i));
				removed = true;
			}
		}
		reader.close();
		Files.delete(varTxt.toPath());
		varTxt = new File("variations.txt");
		BufferedWriter writer = new BufferedWriter(new FileWriter("variations.txt", true));
		for(String line : lines) {
			writer.write(line);
			writer.newLine();
		}
		writer.close();
		return removed;
	}

	// Prints the current path.
	public String getPath() {
		String path = new String();
		path = String.join("->", movePath);
		return path;
	}

	
	// Function that updates the current selection of options based on the current node. 
		public void updateButtons() {
			buttonPanel.removeAll();
			Dimension buttonSize = new Dimension(50, 50);
			if(currentNode.equals(root)) 
				currentMoveLabel.setText(currentNode.getMove());
			else {
				ChessTree.movePath.add(currentNode.getMove());
				chessBoardImage.updateBoard(currentNode.getMove());
				currentMoveLabel.setText(currentNode.getMove() + " : " + getVariation() + ". Path : " + getPath());
				ChessTree.nodePath.push(currentNode);
				}
			for(ChessNode child : currentNode.children.values()) {
				JButton button = new JButton(child.getMove());
				button.setSize(buttonSize);
				buttonPanel.add(button);
				
				// Adds an action listener for each child that when pressed, updates the buttons and changes the current node. 
				button.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						currentNode = child;
						updateButtons();
					}});
			}
			
			// buttonPanel settings
			buttonPanel.setLayout(new GridLayout(7, 4));
			buttonPanel.setPreferredSize(new Dimension(200, 300));
			
			// Recalculates all added buttons
			ChessTree.buttonPanel.revalidate();
			
			// Repaints all added buttons
			ChessTree.buttonPanel.repaint();
		
		}
		
	// Function that goes back in the path to the previous node.
		public void returnBack() {
			if(nodePath.isEmpty()) {
				ChessTree.buttonPanel.removeAll();
				ChessTree.currentNode = root;
				ChessTree.movePath.clear();
				this.updateButtons();
			}
			else if(nodePath.size() != 1) {
				ChessTree.buttonPanel.removeAll();
				ChessTree.nodePath.pop();
				ChessTree.currentNode = nodePath.pop();
				ChessTree.movePath.removeLast();
				ChessTree.movePath.removeLast();
				chessBoardImage.updateBoard("undo");
				chessBoardImage.updateBoard("undo");
				this.updateButtons();
			}
			else {
				ChessTree.buttonPanel.removeAll();
				ChessTree.nodePath.pop();
				ChessTree.currentNode = root;
				ChessTree.movePath.clear();
				chessBoardImage.updateBoard("undo");
				this.updateButtons();
			}
		}
	
	
	ChessTree() throws IOException{
		try {
			// Changes image of the program
			BufferedImage programImage = ImageIO.read(getClass().getResourceAsStream("/chessIcons/zanePrime.png"));
			setIconImage(new ImageIcon(programImage).getImage());
			
			// Program initialization
			ChessTree.readInChessTree();
			currentNode = root;
			chessBoardImage.buildBoard();
			JPanel chessBoardImageFrame = new JPanel();
			chessBoardImage.setPreferredSize(new Dimension(600, 600));
			chessBoardImageFrame.add(chessBoardImage);
			JPanel frame = new JPanel();
			JPanel bottomButtons = new JPanel();
			
			// Settings for currentMoveLabel
			currentMoveLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
			currentMoveLabel.setHorizontalAlignment(JTextField.CENTER);
		
			
			// Window settings
			this.setTitle("TheoreticalChess");
			this.setSize(800, 700);
			this.setVisible(true);
			this.setResizable(false);
			this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		
			// Creates the back button and its function
			JButton backButton = new JButton("Undo Move");
			backButton.setSize(new Dimension(100, 100));
			backButton.addActionListener(e -> { returnBack(); });
			
			// Creates the copy to clipboard button and its function
			JButton copyToClipboard = new JButton("Copy FEN String");
			copyToClipboard.setSize(new Dimension(100, 100));;
			copyToClipboard.addActionListener(e -> {
				StringSelection stringSelection = new StringSelection(chessBoardImage.getFen());
				Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
				clipboard.setContents(stringSelection, null);
			});
			
			// Creates a button to add a variation
			JButton addVariation = new JButton("Add a Variation");
			addVariation.setSize(new Dimension(100, 100));
			addVariation.addActionListener( e -> {
				String input = JOptionPane.showInputDialog("Enter a variation to add:");
				while(true) {
					if(input != null && input.isBlank())
						input = JOptionPane.showInputDialog("Invalid Input. Enter a variation to add:");
					else if(input != null) { 
						try {
							String variation = input.trim();
							input = JOptionPane.showInputDialog(null, "Please enter the path for the previously specified variation.");
							while(true) {
								if (input != null && input.isBlank())
									input = JOptionPane.showInputDialog("Invalid Input. Enter a path to add:");
								else if(input != null) {
									this.addVariation(variation, input.trim());
									JOptionPane.showMessageDialog(null, variation + " was added.");
									ChessTree.readInChessTree();
									nodePath.clear();
									movePath.clear();
									currentNode = root;
									chessBoardImage.resetBoard();
									this.updateButtons();
									input = null;
								}
								else 
									break;
									
							}		
						} catch (IOException e1) {}
					}
					else
						break;
				}
			});
			
			// Creates a button to remove a variation
			JButton removeVariation = new JButton("Remove a Variation");
			removeVariation.setSize(new Dimension(100, 100));
			removeVariation.addActionListener(e -> {
				String input = JOptionPane.showInputDialog("Enter a variation to remove:");
				while(true) {
					if(input != null && input.isBlank())
						input = JOptionPane.showInputDialog("Invalid Input. Enter a variation to remove:");
					else if(input != null) { 
						try {
							boolean removed = this.removeVariation(input.trim());
							if(removed) {
								JOptionPane.showMessageDialog(null, input.trim() + " was removed, restart application for this change to take effect.");
								ChessTree.readInChessTree();
								currentNode = root;
								chessBoardImage.resetBoard();
								nodePath.clear();
								movePath.clear();
								this.updateButtons();
								input = null;
							}
							else {
								JOptionPane.showMessageDialog(null, input.trim() + " was not in the file.");
								input = null;
							}
								
						} catch (IOException e1) {}
					}
					else
						break;
				}
			});
			
			// Adds buttons/labels to the bottom buttons JPanel
			bottomButtons.add(backButton);
			bottomButtons.add(copyToClipboard);
			bottomButtons.add(addVariation);
			bottomButtons.add(removeVariation);
			
			// Adds components to the frame JPanel
			frame.add(chessBoardImageFrame, BorderLayout.SOUTH);
			
			// Adds components to this instance of ChessTree
			this.add(currentMoveLabel, BorderLayout.NORTH);
			this.add(frame);
			this.add(buttonPanel, BorderLayout.WEST);
			this.add(bottomButtons, BorderLayout.SOUTH);

			updateButtons();
			
			}
		catch (IOException e) {
			System.out.println("Cannot find file.");
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
	}
	
	

	
}
