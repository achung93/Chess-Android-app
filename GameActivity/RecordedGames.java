package deleonchung.chess;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import model.gamesPlayed;

public class RecordedGames extends AppCompatActivity implements OnItemClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.recorded_games);
		
		final ListView v = (ListView)findViewById(R.id.listView);
		
		 ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
				 android.R.layout.simple_list_item_1, android.R.id.text1,
				 gamesPlayed.gameNames.toArray(new String[gamesPlayed.gameNames.size()]));

		assert v != null;
		v.setAdapter(adapter);
		v.setOnItemClickListener(this);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		
		gamesPlayed.index = position;
		startActivity(new Intent(RecordedGames.this, Replay.class));
		
	}
	
}
