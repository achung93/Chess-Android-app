package deleonchung.chess;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import controller.ChessBoard;
import model.Color;
import model.Square;
import model.gamesPlayed;
import view.SquareAdapter;

public class PlayChess extends AppCompatActivity implements OnItemClickListener {

	private ChessBoard chessBoard;
	private boolean record;
	private static boolean RUN_ONCE = false;
	private String gameName;
	private TextView turnView;
	private GridView board;
	private View[] selectedSquares;
	private int[] squarePositions;
	private SquareAdapter adapter;
	private boolean drawPressed;
	private boolean drawPressedThisTurn;
	private boolean undoPressed;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.play_chess);

		if (!RUN_ONCE) {

			RUN_ONCE = true;
			recordGame();
			this.chessBoard = new ChessBoard();
			selectedSquares = new View[2];
			squarePositions = new int[2];
			adapter = new SquareAdapter(this, chessBoard.getBoard());
			turnView = (TextView)findViewById(R.id.turnView);

		}

		initResignButton();
		initDrawButton();
		initUndoButton();

		final GridView chessBoardGridView = (GridView)findViewById(R.id.chessboard);

		assert chessBoardGridView != null;
		chessBoardGridView.setAdapter(adapter);

		chessBoardGridView.setOnItemClickListener(this);

		this.board = chessBoardGridView;


	}

	private void recordGame() {

		DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch (which){
				case DialogInterface.BUTTON_POSITIVE:
					record = true;

					AlertDialog.Builder alert = new AlertDialog.Builder(PlayChess.this);
					alert.setTitle("Record Game");
					alert.setMessage("Enter a name for this game:");

					final EditText input = new EditText(PlayChess.this);
					alert.setView(input);
					alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int whichButton) {
							gameName = input.getText().toString();
						}
					});

					alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int whichButton) {
							record = false;
						}
					});

					alert.show();
					break;
				case DialogInterface.BUTTON_NEGATIVE:
					record = false;
					break;
				}
			}
		};

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("Do you want to record this game? Recorded games can be viewed from the main menu.").setPositiveButton("Yes", dialogClickListener)
		.setNegativeButton("No", dialogClickListener).show();

	}

	@Override
	public void onBackPressed() {
		startActivity(new Intent(PlayChess.this, MainMenu.class));
		RUN_ONCE = false;
		finish();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {

		if (selectedSquares[0] == null) {

			Square selectedSquare = chessBoard.getBoard()[position / 8][position % 8];

			if (selectedSquare.piece == null) return;

			if (selectedSquare.piece.getPlayer().getColor() != chessBoard.getCurrentPlayer().getColor()) return;

			selectedSquares[0] = view;
			squarePositions[0] = position;

			view.setBackgroundColor(android.graphics.Color.GREEN);

		}

		else {

			selectedSquares[1] = view;
			squarePositions[1] = position;

			if (chessBoard.move(squarePositions[0], squarePositions[1])) {

				adapter.notifyDataSetChanged();
				board.setAdapter(adapter);
				changeTurnText();

				String toastMessage;
				Toast toast;
				if (chessBoard.whiteWin() || chessBoard.blackWin()) {

					if (record) {
						gamesPlayed.gamesPlayed.add(chessBoard.getMoves());
						gamesPlayed.gameNames.add(gameName);
					}

					final String winner = chessBoard.whiteWin() ? "White" : "Black";

					DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							switch (which){
							case DialogInterface.BUTTON_POSITIVE:
								Intent intent = getIntent();
								finish();
								RUN_ONCE = false;
								startActivity(intent);
								break;

							case DialogInterface.BUTTON_NEGATIVE:
								startActivity(new Intent(PlayChess.this, MainMenu.class));
								RUN_ONCE = false;
								finish();
								break;
							}
						}
					};

					AlertDialog.Builder builder = new AlertDialog.Builder(this);
					builder.setMessage(winner + " wins! Do you want to play another game?").setPositiveButton("Yes", dialogClickListener)
					.setNegativeButton("No", dialogClickListener).show();

				}
				else if (chessBoard.blackKingInCheck()) {

					toastMessage = "Black King in check.";
					toast = Toast.makeText(this, toastMessage, Toast.LENGTH_LONG);
					toast.show();
				}
				else if (chessBoard.whiteKingInCheck()) {

					toastMessage = "White King in check.";
					toast = Toast.makeText(this, toastMessage, Toast.LENGTH_LONG);
					toast.show();
				}


			} else {
				Toast toast = Toast.makeText(this, "Illegal Move", Toast.LENGTH_SHORT);
				toast.show();
			}

			selectedSquares[0].setBackgroundColor(updateColor(squarePositions[0]));
			selectedSquares[0] = null;
			selectedSquares[1] = null;
			undoPressed = false;

		}

		checkDraw();

	}

	private void checkDraw() {

		if (drawPressed && !drawPressedThisTurn) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("Draw");
			builder.setMessage("Do you want to accept the draw?");

			builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {

					DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							switch (which){
							case DialogInterface.BUTTON_POSITIVE:
								Intent intent = getIntent();
								finish();
								RUN_ONCE = false;
								startActivity(intent);
								break;

							case DialogInterface.BUTTON_NEGATIVE:
								dialog.dismiss();
								startActivity(new Intent(PlayChess.this, MainMenu.class));
								RUN_ONCE = false;
								finish();
							}
						}
					};

					AlertDialog.Builder builder = new AlertDialog.Builder(PlayChess.this);
					builder.setMessage("Draw. Do you want to play another game?").setPositiveButton("Yes", dialogClickListener)
					.setNegativeButton("No", dialogClickListener).show();
				}
			});

			builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					drawPressed = false;
				}
			});
			AlertDialog alert = builder.create();
			alert.show();
		}
	}

	private void changeTurnText() {

		if (turnView.getText().toString().compareTo(getResources().getString(R.string.white_turn)) == 0) {

			turnView.setText(getResources().getString(R.string.black_turn));
		}
		else {

			turnView.setText(getResources().getString(R.string.white_turn));
		}

		drawPressedThisTurn = false;
	}

	private int updateColor(int position) {

		int col = position / 8 % 2;
		if (col == 0) {
			if (position % 2 == 0)
				return (android.graphics.Color.rgb(169, 169, 169));
			else
				return android.graphics.Color.WHITE;
		} else {
			if (position % 2 == 0)
				return android.graphics.Color.WHITE;
			else
				return android.graphics.Color.rgb(169, 169, 169);
		}
	}

	private void initDrawButton() {

		Button drawButton = (Button) findViewById(R.id.drawButton);
		assert drawButton != null;
		drawButton.setOnClickListener(new OnClickListener() {

			@Override 
			public void onClick(View argo) {
				draw();
			}
		});
	}

	private void initResignButton() {

		Button resignButton = (Button) findViewById(R.id.resignButton);
		assert resignButton != null;
		resignButton.setOnClickListener(new OnClickListener() {

			@Override 
			public void onClick(View argo) {

				resign();
			}
		});
	}

	private void initUndoButton() {

		Button undoButton = (Button) findViewById(R.id.undoButton);
		assert undoButton != null;
		undoButton.setOnClickListener(new OnClickListener() {

			@Override 
			public void onClick(View argo) {

				if (!undoPressed) {
					undoPressed = true;
					if (chessBoard.undo()) changeTurnText();
					adapter.notifyDataSetChanged();
					board.setAdapter(adapter);
				}

			}
		});
	}

	private void resign() {

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Resign");
		builder.setMessage("Do you want to resign?");

		builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {

				final String winner = chessBoard.getCurrentPlayer().getColor() == Color.WHITE ? "Black" : "White";

				if (record) {
					gamesPlayed.gamesPlayed.add(chessBoard.getMoves());
					gamesPlayed.gameNames.add(gameName);
				}
				
				DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						switch (which){
						case DialogInterface.BUTTON_POSITIVE:
							Intent intent = getIntent();
							finish();
							RUN_ONCE = false;
							startActivity(intent);
							break;

						case DialogInterface.BUTTON_NEGATIVE:
							startActivity(new Intent(PlayChess.this, MainMenu.class));
							RUN_ONCE = false;
							finish();
							break;
						}
					}
				};

				AlertDialog.Builder builder = new AlertDialog.Builder(PlayChess.this);
				builder.setMessage(winner + " wins! Do you want to play another game?").setPositiveButton("Yes", dialogClickListener)
				.setNegativeButton("No", dialogClickListener).show();
			}
		});

		builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		AlertDialog alert = builder.create();
		alert.show();
	}


	private void draw() {

		if (!drawPressed) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("Draw");
			builder.setMessage("Do you want to request a draw?");

			builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
					drawPressed = true;
					drawPressedThisTurn = true;
				}
			});

			builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					drawPressed = false;
					drawPressedThisTurn = false;
				}
			});
			AlertDialog alert = builder.create();
			alert.show();
		}
	}
}
