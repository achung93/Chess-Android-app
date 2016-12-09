package model;

public class Move {

	public enum MoveType {
		NORMAL, CASTLE, ENPASSANT
	}

	private Piece piece;
	private Piece capture;
	private Square startingSquare;
	private Square endingSquare;
	private MoveType type;
	private int startingPos, endingPos;

	public Move(Piece piece, Piece capture, Square startingSquare, Square endingSquare) {
		this.piece = piece;
		this.capture = capture;
		this.startingSquare = startingSquare;
		this.endingSquare = endingSquare;
	}

	public MoveType getType() {
		return type;
	}
	
	public void setType(MoveType type) {
		this.type = type;
	}
	
	public Square getEndingSquare() {
		return endingSquare;
	}
	
	public Square getStartingSquare() {
		return startingSquare;
	}
	
	public Piece getCapture() {
		return capture;
	}
	
	public Piece getPiece() {
		return piece;
	}
	
	public void setStartingPosition(int startingPos) {
		this.startingPos = startingPos;
	}
	
	public int getStartingPosition() {
		return startingPos;
	}
	
	public void setEndingPosition(int endingPos) {
		this.endingPos = endingPos;
	}
	
	public int getEndingPosition() {
		return endingPos;
	}

}

