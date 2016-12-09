package controller;

import java.util.ArrayList;

import model.*;
import model.Move.MoveType;


public class ChessBoard {

	private Square[][] board;
	private Player black;
	private Player white;
	private Player turn;
	private boolean blackInCheck;
	private boolean whiteInCheck;
	private boolean blackWins;
	private boolean whiteWins;
	private ArrayList<Move> moves;

	public ChessBoard() {

		int NUMBER_OF_RANKS = 8;
		int NUMBER_OF_FILES = 8;

		board = new Square[NUMBER_OF_RANKS][NUMBER_OF_FILES];
		black = new Player(Color.BLACK);
		white = new Player(Color.WHITE);
		black.setOpponent(white);
		white.setOpponent(black);
		blackInCheck = false;
		whiteInCheck = false;
		turn = white;
		moves = new ArrayList<>();

		for (int i = 0; i < 8 ; i++) {
			for (int j = 0; j < 8; j++) {
				board[i][j] = new Square(i, j);
			}
		}

		standardSetup(black);
		standardSetup(white);
	}

	private void standardSetup(Player player) {

		int row = player.getColor() == Color.WHITE ? 7: 0;

		Piece[] pieces = new Piece[]
				{ new Rook(), new Knight(), new Bishop(), new Queen(), new King(), new Bishop(), new Knight(), new Rook()};


		player.setKing((King)pieces[4]);

		for (int i = 0; i < 8; i++) {
			pieces[i].setLocation(board[row][i]);
			pieces[i].setBoard(board);
			board[row][i].piece = pieces[i];
			board[row][i].piece.setPlayer(player);
			player.addPiece(pieces[i]);

		}

		row = player.getColor() == Color.WHITE ? 6 : 1;

		for (int i = 0; i < 8; i++) {
			Piece pawn = new Pawn();
			pawn.setLocation(board[row][i]);
			pawn.setBoard(board);
			board[row][i].piece = pawn;
			board[row][i].piece.setPlayer(player);
			player.addPiece(pawn);
		}
	}

	public Square[][] getBoard() { return board; }

	public Player getCurrentPlayer() { return turn; }

	public boolean move(int startingPos, int endingPos) {

		blackInCheck = false;
		whiteInCheck = false;

		if (startingPos > 63 || startingPos < 0 || endingPos > 63 || endingPos < 0) return false;

		int file = startingPos % 8;
		int rank = startingPos / 8;

		Square startingSquare = board[rank][file];

		file = endingPos % 8;
		rank = endingPos / 8;


		Square endingSquare = board[rank][file];

		Piece movingPiece = startingSquare.piece;

		Piece takenPiece = endingSquare.piece;

		Piece capture = null;

		MoveType moveType = MoveType.NORMAL;

		if (movingPiece == null) return false;

		if (movingPiece.getPlayer().getColor() != getCurrentPlayer().getColor()) return false;


		if (!movingPiece.canMoveTo(endingSquare)) return false;

		if (takenPiece != null) {

			takenPiece.setLocation(null);
			capture = takenPiece;

		}

		Move move = new Move(movingPiece, capture, startingSquare, endingSquare);

		if (enPassant(movingPiece, endingSquare)) moveType = MoveType.ENPASSANT;

		if (castle(movingPiece, endingSquare)) {

			moveType = MoveType.CASTLE;
			if (movingPiece.getPlayer().getColor() == Color.WHITE) {

				if (endingSquare.x == 6) {


					Piece rook = board[7][7].piece;
					rook.setLocation(board[7][5]);
					rook.incrementMoves();
					board[7][5].piece = rook;
					board[7][7].piece = null;
				}
				else if (endingSquare.x == 2) {

					Piece rook = getBoard()[7][0].piece;
					rook.setLocation(getBoard()[7][3]);
					rook.incrementMoves();
					board[7][3].piece = rook;
					board[7][0].piece = null;

				}
			}
			else {

				if (endingSquare.x == 6) {

					Piece rook = board[0][7].piece;
					rook.setLocation(board[0][5]);
					rook.incrementMoves();
					board[0][5].piece = rook;
					board[0][7].piece = null;

				}
				else if (endingSquare.x == 2) {

					Piece rook = board[0][0].piece;
					rook.setLocation(board[0][3]);
					rook.incrementMoves();
					board[0][3].piece = rook;
					board[0][0].piece =null;
				}
			}
		}

		startingSquare.piece = null;
		movingPiece.setLocation(endingSquare);
		endingSquare.piece = movingPiece;
		movingPiece.incrementMoves();

		if (((turn == white && endingSquare.y == 0) || (turn == black && endingSquare.y == 7)) && movingPiece instanceof Pawn) {

			Piece promotedPiece = new Queen();
			promotedPiece.setLocation(endingSquare);
			promotedPiece.setNumberOfMoves(movingPiece.numberOfMoves());
			promotedPiece.setPlayer(turn);
			promotedPiece.setBoard(board);
			movingPiece = promotedPiece;
			endingSquare.piece = movingPiece;
		}

		Square oppKing = turn.getOpponent().getKing().getLocation();

		if (turn.getOpponent().getKing().kingIsInCheck(oppKing)) {

			if (turn.getColor() == Color.WHITE) {
				blackInCheck = true;
				if (black.getKing().checkmate(black.getKing().getLocation())) {
					whiteWins = true;
				}
			} else {
				whiteInCheck = true;
				if (white.getKing().checkmate(white.getKing().getLocation())) {
					blackWins = true;
				}
			}
		}

		move.setType(moveType);
		move.setStartingPosition(startingPos);
		move.setEndingPosition(endingPos);
		moves.add(move);
		turn = (turn == white) ? black : white;

		return true;
	}

	public boolean undo() {

		if (moves.size() == 0) return false;

		Move lastMove = moves.remove(moves.size() - 1);

		Square startingSquare = lastMove.getStartingSquare();

		Square endingSquare = lastMove.getEndingSquare();

		Piece piece = lastMove.getPiece();

		Piece capture = lastMove.getCapture();

		MoveType type = lastMove.getType();

		if (type == MoveType.NORMAL) {

			piece.setLocation(startingSquare);

			if (capture != null)  {
				capture.setLocation(endingSquare);
			}
			piece.decrementMoves();

			board[startingSquare.y][startingSquare.x].piece = piece;
			board[endingSquare.y][endingSquare.x].piece = capture;

		} else if (type == MoveType.ENPASSANT) {

			piece.setLocation(startingSquare);

			Square capLoc = capture.getLocation();

			board[capLoc.y][capLoc.x].piece = capture;
			board[startingSquare.y][startingSquare.x].piece = piece;
			board[endingSquare.y][endingSquare.x].piece = null;

			piece.decrementMoves();

		} else if (type == MoveType.CASTLE) {

			piece.setLocation(startingSquare);

			if (piece.getPlayer().getColor() == Color.WHITE) {

				if (endingSquare.x == 6) {

					board[startingSquare.y][startingSquare.x].piece = piece;
					board[endingSquare.y][endingSquare.x].piece = null;
					board[7][7].piece = board[7][5].piece;
					board[7][5].piece.decrementMoves();
					board[7][5].piece = null;
				} else {

					board[startingSquare.y][startingSquare.x].piece = piece;
					board[endingSquare.y][endingSquare.x].piece = null;
					board[7][0].piece = board[7][3].piece;
					board[7][3].piece.decrementMoves();
					board[7][3].piece = null;
				}
			}
			else {

				if (endingSquare.x == 6) {
					board[startingSquare.y][startingSquare.x].piece = piece;
					board[endingSquare.y][endingSquare.x].piece = null;
					board[0][7].piece = board[0][5].piece;
					board[0][5].piece.decrementMoves();
					board[0][5].piece = null;
				} else {
					board[startingSquare.y][startingSquare.x].piece = piece;
					board[endingSquare.y][endingSquare.x].piece = null;
					board[0][0].piece = board[0][3].piece;
					board[0][3].piece.decrementMoves();
					board[0][3].piece = null;
				}
			}
			piece.decrementMoves();

		}

		turn = (turn == white) ? black : white;
		return true;

	}

	private boolean enPassant(Piece movingPiece, Square endingSquare) {

		if (movingPiece instanceof Pawn && endingSquare.piece == null) {

			int yPos = movingPiece.getPlayer().getColor() == Color.WHITE ? 1 : -1;


			if (Math.abs(movingPiece.getLocation().x - endingSquare.x) == 1 && movingPiece.getLocation().y == endingSquare.y + yPos) {

				board[movingPiece.getLocation().y][endingSquare.x].piece = null;

				return true;
			}
		}

		return false;
	}


	private boolean castle(Piece movingPiece, Square endingSquare) {

		int xPos = Math.abs( endingSquare.x - movingPiece.getLocation().x);

		return movingPiece instanceof King && xPos == 2;

	}

	public boolean blackKingInCheck() { return blackInCheck; }

	public boolean whiteKingInCheck() { return whiteInCheck; }

	public boolean whiteWin() { return whiteWins; }

	public boolean blackWin() { return blackWins; }

	public ArrayList<Move> getMoves() { return moves; }

}

