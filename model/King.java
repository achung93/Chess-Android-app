package model;

public class King extends Piece {

	public String getName() { return "k"; }

	public boolean validateMove(Square endingSquare)
	{
		int xPos = Math.abs( endingSquare.x - getLocation().x);
		int yPos= Math.abs( endingSquare.y - getLocation().y);

		if (((xPos) <=1 && (yPos) <=1)) return true;

		if (getPlayer().getColor() == Color.WHITE) {

			if (getLocation().x == 4 && getLocation().y == 7 && numberOfMoves() == 0) {

				if ((endingSquare.x == 6 && endingSquare.y == 7)) {

					Piece rook = getBoard()[endingSquare.x + 1][endingSquare.y].piece;

					return (rook instanceof Rook && rook.numberOfMoves() == 0) && pieceBlocking(endingSquare);

				}
				else if ((endingSquare.x == 2 && endingSquare.y == 7)) {

					Piece rook = getBoard()[endingSquare.x - 2][endingSquare.y].piece;

					return (rook instanceof Rook && rook.numberOfMoves() == 0) && pieceBlocking(endingSquare);

				}

			}
			return false;
		}
		else {

			if (getLocation().x == 4 && getLocation().y == 0 && numberOfMoves() == 0) {

				if ((endingSquare.x == 6 && endingSquare.y == 0)) {

					Piece rook = getBoard()[endingSquare.x + 1][endingSquare.y].piece;

					return (rook instanceof Rook && rook.numberOfMoves() == 0) && pieceBlocking(endingSquare);

				}
				else if ((endingSquare.x == 2 && endingSquare.y == 0)) {

					Piece rook = getBoard()[endingSquare.x - 2][endingSquare.y].piece;

					return (rook instanceof Rook && rook.numberOfMoves() == 0) && pieceBlocking(endingSquare);

				}


			}
			return false;
		}

	}

	public boolean kingIsInCheck(Square kingLoc) {

		for (Piece chessP: getPlayer().getOpponent().getPieces())
		{
			if (chessP.getLocation() == null) {
				return false;

			}
			if (chessP.validateMove(kingLoc) && chessP.getLocation() != null)
			{
				return true;
			}

		}

		return false;
	}

	public boolean checkmate(Square kingLoc) {

		Square forward = new Square(kingLoc.y + 1, kingLoc.x);
		Square backward = new Square(kingLoc.y - 1, kingLoc.x);
		Square rightUp = new Square(kingLoc.y + 1, kingLoc.x + 1);
		Square leftUp = new Square(kingLoc.y + 1, kingLoc.x - 1);
		Square rightDown = new Square(kingLoc.y - 1, kingLoc.x + 1);
		Square leftDown = new Square(kingLoc.y - 1, kingLoc.x - 1);

		if (forward.x >= 0 && forward.x <= 7
				&& forward.y >= 0 && forward.y <= 7) {

			if (!kingIsInCheck(forward) && getBoard()[forward.y][forward.x].piece == null) return false;

		}
		if (backward.x >= 0 && backward.y <= 7
				&& backward.y >= 0 && backward.y <= 7) {

			if (!kingIsInCheck(backward) && getBoard()[backward.y][backward.x].piece == null) return false;
		}
		if (rightUp.x >= 0 && rightUp.y <= 7
				&& rightUp.y >= 0 && rightUp.y <= 7) {

			if (!kingIsInCheck(rightUp) && getBoard()[rightUp.y][rightUp.x].piece == null) return false;
		}
		if (leftUp.x >= 0 && leftUp.y <= 7
				&& leftUp.y >= 0 && leftUp.y <= 7) {

			if (!kingIsInCheck(leftUp) && getBoard()[leftUp.y][leftUp.x].piece == null) return false;
		}
		if (rightDown.x >= 0 && rightDown.y <= 7
				&& rightDown.y >= 0 && rightDown.y <= 7) {

			if (!kingIsInCheck(rightDown) && getBoard()[rightDown.y][rightDown.x].piece == null) return false;
		}
		if (leftDown.x >= 0 && leftDown.y <= 7
				&& leftDown.y >= 0 && leftDown.y <= 7) {

			if (!kingIsInCheck(leftDown) && getBoard()[leftDown.y][leftDown.x].piece == null) return false;
		}

		return true;

	}
}
