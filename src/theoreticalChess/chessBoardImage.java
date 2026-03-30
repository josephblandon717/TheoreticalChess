package theoreticalChess;

import java.awt.Color;
import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.github.bhlangonijr.chesslib.*;

public class chessBoardImage extends JPanel{

	// Stores each square of the chessBoardImage
	static JLabel[][] squares = new JLabel[8][8];

	// Stores a virtual memory of the board.
	static Board chessBoard = new Board();

	// Constructor to create a chessBoardImage
	public chessBoardImage() {
		this.setLayout(new GridLayout(8, 8));
	}

	// Function that builds the image of the chess board
	public void buildBoard() {
		// If squares is empty, will build the checkered grid
		if (squares.length != 0) {
			for (int i = 0; i < 8; i++) {
				for (int j = 0; j < 8; j++) {
					JLabel square = new JLabel();
					square.setOpaque(true);
					if ((i + j) % 2 == 0) {
						int red = 235;
						int green = 236;
						int blue = 208;
						square.setBackground(new Color(red, green, blue));
					} else {
						int red = 115;
						int green = 149;
						int blue = 82;
						square.setBackground(new Color(red, green, blue));
					}
					squares[i][j] = square;
					this.add(square);
				}
			}
		}

		Board initialState = new Board();

		// Will put the pieces on the board according to the initial state.
		for (int i = 0; i < 64; i++) {
			Square square = Square.squareAt(i);
			Piece piece = initialState.getPiece(square);
			int row = Math.abs((i / 8) - 7);
			int col = i % 8;
			if (piece != Piece.NONE)
				squares[row][col].setIcon(getPieceIcon(piece));
			else
				squares[row][col].setIcon(null);
		}
	}

	// Function that updates the image of the board
	public void updateBoard(String move) {
		Board previous = chessBoard.clone();
		if (move.compareTo("undo") == 0)
			chessBoard.undoMove();
		else
			chessBoard.doMove(move);
		ArrayList<Square> changedSquares = new ArrayList<Square>();

		// Checks all squares and saves the changed squares in the array
		for (int i = 0; i < 64; i++) {
			Square square = Square.squareAt(i);
			Piece piece = chessBoard.getPiece(square);
			Piece previousPiece = previous.getPiece(square);
			if (piece != previousPiece)
				changedSquares.add(square);
		}

		// Updates the images for each of the changed squares.
		for (Square square : changedSquares) {
			int i = square.ordinal();
			int row = Math.abs((i / 8) - 7);
			int col = i % 8;
			Piece piece = chessBoard.getPiece(square);
			if (piece != Piece.NONE)
				squares[row][col].setIcon(getPieceIcon(piece));
			else
				squares[row][col].setIcon(null);
		}
	}
	
	// Function that resets the board to the initial state
	public void resetBoard() {
		chessBoard = new Board();
		// Will put the pieces on the board according to the initial state.
		for (int i = 0; i < 64; i++) {
			Square square = Square.squareAt(i);
			Piece piece = chessBoard.getPiece(square);
			int row = Math.abs((i / 8) - 7);
			int col = i % 8;
			if (piece != Piece.NONE)
				squares[row][col].setIcon(getPieceIcon(piece));
			else
				squares[row][col].setIcon(null);
		}
	}

	// Function that gets an icon of a chess piece
	public ImageIcon getPieceIcon(Piece piece) {
		if (piece.getPieceSide() == Side.WHITE && piece.getPieceType() == PieceType.PAWN)
			return new ImageIcon(ChessTree.class.getResource("/chessIcons/whitepawn.png"));
		else if (piece.getPieceSide() == Side.WHITE && piece.getPieceType() == PieceType.KNIGHT)
			return new ImageIcon(ChessTree.class.getResource("/chessIcons/whiteknight.png"));
		else if (piece.getPieceSide() == Side.WHITE && piece.getPieceType() == PieceType.BISHOP)
			return new ImageIcon(ChessTree.class.getResource("/chessIcons/whitebishop.png"));
		else if (piece.getPieceSide() == Side.WHITE && piece.getPieceType() == PieceType.ROOK)
			return new ImageIcon(ChessTree.class.getResource("/chessIcons/whiterook.png"));
		else if (piece.getPieceSide() == Side.WHITE && piece.getPieceType() == PieceType.QUEEN)
			return new ImageIcon(ChessTree.class.getResource("/chessIcons/whitequeen.png"));
		else if (piece.getPieceSide() == Side.WHITE && piece.getPieceType() == PieceType.KING)
			return new ImageIcon(ChessTree.class.getResource("/chessIcons/whiteking.png"));
		else if (piece.getPieceSide() == Side.BLACK && piece.getPieceType() == PieceType.PAWN)
			return new ImageIcon(ChessTree.class.getResource("/chessIcons/blackpawn.png"));
		else if (piece.getPieceSide() == Side.BLACK && piece.getPieceType() == PieceType.KNIGHT)
			return new ImageIcon(ChessTree.class.getResource("/chessIcons/blackknight.png"));
		else if (piece.getPieceSide() == Side.BLACK && piece.getPieceType() == PieceType.BISHOP)
			return new ImageIcon(ChessTree.class.getResource("/chessIcons/blackbishop.png"));
		else if (piece.getPieceSide() == Side.BLACK && piece.getPieceType() == PieceType.ROOK)
			return new ImageIcon(ChessTree.class.getResource("/chessIcons/blackrook.png"));
		else if (piece.getPieceSide() == Side.BLACK && piece.getPieceType() == PieceType.QUEEN)
			return new ImageIcon(ChessTree.class.getResource("/chessIcons/blackqueen.png"));
		else if (piece.getPieceSide() == Side.BLACK && piece.getPieceType() == PieceType.KING)
			return new ImageIcon(ChessTree.class.getResource("/chessIcons/blackking.png"));
		else
			return null;
	}
	
	// Function that returns the fen string from current board state
	public String getFen() {
		return chessBoard.getFen();
	}
}
