package model;

public class Pawn extends Piece {

	public String getName() { return "p"; }

	public boolean validateMove(Square endingSquare) {

		int yPos;
		if (getPlayer().getColor() == Color.WHITE) {
			yPos = 1;
		} else {
			yPos = -1;
		}

		if (getLocation().x == endingSquare.x) {

			return (endingSquare.piece == null
					&& getLocation().y == endingSquare.y + yPos)
					||
					(numberOfMoves() == 0
							&& endingSquare.piece == null
							&& getLocation().y == endingSquare.y + (2*yPos));

		}

		int sum= Math.abs(getLocation().x - endingSquare.x);
		int sum2 = Math.abs(getLocation().y - endingSquare.y);

		if ((sum == 1) && (sum2 == 1))
			return true;

		if (Math.abs(getLocation().x - endingSquare.x) == 1 && getLocation().y == endingSquare.y || Math.abs(getLocation().x - endingSquare.x) == 2 && getLocation().y == endingSquare.y) {

			if (endingSquare.piece == null) return true;


			Piece piece = getBoard()[getLocation().y][endingSquare.x].piece;

			if (piece != null && piece instanceof Pawn && piece.numberOfMoves() == 1) {


				return piece.getPlayer().getColor() != getPlayer().getColor() && ((getPlayer().getColor() == Color.WHITE && endingSquare.y == 2 || getPlayer().getColor() == Color.BLACK && endingSquare.y == 5));

			}
		}

		return false;
	}

}
