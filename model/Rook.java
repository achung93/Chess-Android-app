package model;

public class Rook extends Piece {

	public String getName() { return "r"; }

	public boolean validateMove(Square endingSquare) {

		return (endingSquare.x == getLocation().x || endingSquare.y == getLocation().y) && this.pieceBlocking(endingSquare);
	}

}
