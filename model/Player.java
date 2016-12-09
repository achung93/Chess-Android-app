package model;

import java.util.ArrayList;

public class Player {

	private ArrayList<Piece> pieces;

	private Player opponent;

	private King king;

	private Color color;

	public Player(Color color) {
		this.color = color;
		pieces = new ArrayList<>(16);
	}

	public Color getColor() { return color; }

	public Player getOpponent() { return opponent; }

	public void addPiece(Piece piece) { pieces.add(piece); }

	public ArrayList<Piece> getPieces() { return pieces; }

	public void setOpponent(Player opponent) { this.opponent = opponent; }

	public void setKing(King king) { this.king = king; }

	public King getKing() { return king; }

}
