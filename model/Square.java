package model;

public class Square {

	public int x;
	public int y;

	public Piece piece;

	public Square(int rank, int file) {
		this.y = rank;
		this.x = file;
	}

	public boolean equals(Object o) {

		if (!(o instanceof Square)) return false;

		return this.x == ((Square) o).x && this.y == ((Square) o).y;

	}

}
