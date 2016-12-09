package deleonchung.chess;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import java.util.ArrayList;

import model.gamesPlayed;

public class MainMenu extends AppCompatActivity {
	
	public static boolean RUN_ONCE = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_menu);
		initPlayButton();
		initReplayButton();
		if (!RUN_ONCE) {
			RUN_ONCE = true;
			gamesPlayed.gamesPlayed = new ArrayList<>();
			gamesPlayed.gameNames = new ArrayList<>();
		}
	}

	private void initPlayButton() {

		Button playButton = (Button) findViewById(R.id.playButton);
		assert playButton != null;
		playButton.setOnClickListener(new OnClickListener() {

			@Override 
			public void onClick(View argo) {
								
				startActivity(new Intent(MainMenu.this, PlayChess.class));
			}
		});
	}
	
	private void initReplayButton() {

		Button replayButton = (Button) findViewById(R.id.replayButton);
		assert replayButton != null;
		replayButton.setOnClickListener(new OnClickListener() {

			@Override 
			public void onClick(View argo) {
				
				startActivity(new Intent(MainMenu.this, RecordedGames.class));
			}
		});
	}
}

