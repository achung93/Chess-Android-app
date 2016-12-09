package model;

public class Queen extends Piece {

	public String getName() { return "q"; }

	public boolean validateMove(Square endingSquare) {

		if (endingSquare.x - getLocation().x != 0 && !(endingSquare.x == getLocation().x || endingSquare.y == getLocation().y)) {
			if ((endingSquare.y - getLocation().y) / (endingSquare.x - getLocation().x) == 1 ||
					(endingSquare.y - getLocation().y) / (endingSquare.x - getLocation().x) == -1)
				return this.pieceBlocking(endingSquare);
		} else if (endingSquare.x == getLocation().x ||
				endingSquare.y == getLocation().y) {
				return this.pieceBlocking(endingSquare);
		}
		return false;
	}

}
