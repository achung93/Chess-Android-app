package deleonchung.chess;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.ListIterator;

import controller.ChessBoard;
import model.Move;
import model.gamesPlayed;
import view.SquareAdapter;

public class Replay extends Activity {

	private ChessBoard chessBoard;
	private static boolean RUN_ONCE = false;
	private SquareAdapter adapter;
	private GridView chessboard;
	private ListIterator<Move> listIterator;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.replay);
		
		if (!RUN_ONCE) {

			RUN_ONCE = true;
			this.chessBoard = new ChessBoard();
			ArrayList<Move> moves = gamesPlayed.gamesPlayed.get(gamesPlayed.index);
			listIterator = moves.listIterator();
			adapter = new SquareAdapter(this, chessBoard.getBoard());

		}

		final GridView chessBoardGridView = (GridView)findViewById(R.id.chessboard);

		initNextButton();
		initPreviousButton();
		chessBoardGridView.setAdapter(adapter);

		this.chessboard = chessBoardGridView;
	}

	private void initNextButton() {

		Button nextButton = (Button) findViewById(R.id.nextButton);
		nextButton.setOnClickListener(new OnClickListener() {

			@Override 
			public void onClick(View argo) {

				if (!listIterator.hasNext()) return;
				
				Move move = listIterator.next();
				chessBoard.move(move.getStartingPosition(), move.getEndingPosition());
				adapter.notifyDataSetChanged();
				chessboard.setAdapter(adapter);
			}
		});
	}
	
	private void initPreviousButton() {

		Button previousButton = (Button) findViewById(R.id.previousButton);
		previousButton.setOnClickListener(new OnClickListener() {

			@Override 
			public void onClick(View argo) {

				if (!listIterator.hasPrevious()) return;
				listIterator.previous();
				chessBoard.undo();
				adapter.notifyDataSetChanged();
				chessboard.setAdapter(adapter);
			}
		});
	}

	@Override
	public void onBackPressed() {
		startActivity(new Intent(Replay.this, MainMenu.class));
		RUN_ONCE = false;
		finish();
	}

}
