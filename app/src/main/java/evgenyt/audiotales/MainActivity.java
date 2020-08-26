package evgenyt.audiotales;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SeekBar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private MediaPlayer mediaPlayer;
    private SeekBar seekBar;
    private Map<String, Integer> talesMap = new HashMap();
    private Button playPauseButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Init tales list
        talesMap.put("Красная шапочка", R.raw.red_hat);
        talesMap.put("Заюшкина избушка", R.raw.izbushka);
        List<String> tailsList = new ArrayList<>();
        for (String taleName : talesMap.keySet())
            tailsList.add(taleName);
        final ListAdapter arrayAdapter = new ArrayAdapter<>(this, R.layout.list_item,
                tailsList);
        ListView talesListView = findViewById(R.id.tales_list);
        talesListView.setAdapter(arrayAdapter);
        talesListView.setOnItemClickListener((parent, view, position, id) -> {
            String taleName = String.valueOf(arrayAdapter.getItem(position));
            Integer taleId = talesMap.get(taleName);
            if (taleId != null) {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                    mediaPlayer.release();
                }
                mediaPlayer = MediaPlayer.create(this, taleId);
                startPlay();
            }
        });
        // Init player with controls
        mediaPlayer = MediaPlayer.create(this, R.raw.red_hat);
        seekBar = findViewById(R.id.player_seek);
        playPauseButton = findViewById(R.id.play_pause_button);
        Handler handler = new Handler();
        MainActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(mediaPlayer != null){
                    int mCurrentPosition = mediaPlayer.getCurrentPosition() / 1000;
                    seekBar.setProgress(mCurrentPosition);
                }
                handler.postDelayed(this, 1000);
            }
        });
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (mediaPlayer != null && fromUser) {
                    mediaPlayer.seekTo(progress * 1000);
                }
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }

    public void startPlay() {
        seekBar.setMax(mediaPlayer.getDuration() / 1000);
        mediaPlayer.start();
        playPauseButton.setText("Пауза");
    }

    // Play/pause button click handler
    public void playOrPause(View view) {
        Button button = (Button) view;
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            button.setText("Слушать сказку");
        } else {
            startPlay();
        }
    }

}