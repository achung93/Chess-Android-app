package model;

public class Knight extends Piece {

	public String getName() {
		return "n";
	}

	public boolean validateMove(Square endingSquare) {
		return endingSquare.x == getLocation().x + 1 && endingSquare.y == getLocation().y + 2 ||
				endingSquare.x == getLocation().x + 1 && endingSquare.y == getLocation().y - 2 ||
				endingSquare.x == getLocation().x + 2 && endingSquare.y == getLocation().y + 1 ||
				endingSquare.x == getLocation().x + 2 && endingSquare.y == getLocation().y - 1 ||
				endingSquare.x == getLocation().x - 1 && endingSquare.y == getLocation().y + 2 ||
				endingSquare.x == getLocation().x - 1 && endingSquare.y == getLocation().y - 2 ||
				endingSquare.x == getLocation().x - 2 && endingSquare.y == getLocation().y + 1 ||
				endingSquare.x == getLocation().x - 2 && endingSquare.y == getLocation().y - 1;

	}
}
