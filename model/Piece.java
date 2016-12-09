package model;

public abstract class Piece {

	private Square[][] board;

	private Square location;

	private Player player;

	private int moves;

	public abstract String getName();

	public abstract boolean validateMove(Square endingSquare);

	public boolean canMoveTo(Square endingSquare) {

		if (location.equals(endingSquare)) return false;

		if (endingSquare.piece != null) {

			if (player.getColor() == endingSquare.piece.getPlayer().getColor()) {

				return false;
			}
		}

		if (!validateMove(endingSquare))  return false;

		Square kingLocation;

		if (this instanceof King)
			kingLocation = endingSquare;
		else
			kingLocation = getPlayer().getKing().getLocation();

		return !getPlayer().getKing().kingIsInCheck(kingLocation);

	}

	public boolean pieceBlocking(Square endingSquare) {

		if (location.x == endingSquare.x && location.y != endingSquare.y) {

			int curr = Math.min(location.y, endingSquare.y) + 1;
			int end = Math.max(location.y, endingSquare.y) - 1;

			while (curr <= end) {

				if (board[curr++][location.x].piece != null) return false;
			}
			return true;
		}

		else if (location.x != endingSquare.x && location.y == endingSquare.y) {

			int curr = Math.min(location.x, endingSquare.x) + 1;
			int end = Math.max(location.x, endingSquare.x) - 1;

			while (curr <= end) {

				if (board[location.y][curr++].piece != null) return false;
			}
			return true;
		}

		else if (Math.abs(location.x - endingSquare.x) == Math.abs(location.y - endingSquare.y)) {

			int dx = (location.x < endingSquare.x) ? 1 : -1;
			int dy = (location.y < endingSquare.y) ? 1 : -1;
			int currX  = location.x + dx;
			int currY = location.y + dy;

			while ((dx == 1 && currX < endingSquare.x) || (dx == -1 && currX > endingSquare.x)){
				if (board[currY][currX].piece != null){

					return false;
				}
				currX += dx;
				currY += dy;
			}
			return true;
		}

		return false;
	}

	public Square getLocation() { return location; }

	public void setLocation(Square location) { this.location = location; }

	public Player getPlayer() { return player; }

	public void setPlayer(Player player) { this.player = player; }

	public Square[][] getBoard() { return board; }

	public void incrementMoves() { moves++; }

	public void decrementMoves() { moves--; }

	public int numberOfMoves() { return moves; }

	public void setNumberOfMoves(int moves) { this.moves = moves; }

	public void setBoard(Square[][] board) { this.board = board; }

	public String colorString() {

		return getPlayer().getColor() == Color.WHITE ? "w" : "b";
	}

	public String toString() {
		return colorString() + getName();
	}

}