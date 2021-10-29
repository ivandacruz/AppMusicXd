package com.ivan.appmusicxd;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.MediaController;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.VideoView;
import android.widget.ViewFlipper;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private MediaPlayer mediaPlayer;
    private SeekBar seekVolume;
    private AudioManager audioManager;
    private SeekBar progressBar;
    private TextView tvTime;
    private ViewFlipper vf;
    private TextView txTitle;
    ImageButton btnPlay, btnPause, btnStop;
    private VideoView videoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnAudio = findViewById(R.id.btnAudio);
        Button btnImage = findViewById(R.id.btnImage);
        Button btnVideo = findViewById(R.id.btnVideo);
        txTitle = findViewById(R.id.txTitle);
        vf = findViewById(R.id.vf);
        btnPlay = findViewById(R.id.btnPlay);
        btnPause =findViewById(R.id.btnPause);
        btnStop = findViewById(R.id.btnStop);
        progressBar = findViewById(R.id.progressBar);
        tvTime = findViewById(R.id.tvTime);

        initSeekBar();
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        int volMax = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int volNow = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        seekVolume.setMax(volMax);
        seekVolume.setProgress(volNow);
        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.music);
        videoView = findViewById(R.id.videoView);
        videoView.setMediaController(new MediaController(this));

        btnAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPlay();
                txTitle.setText("Audio");
                vf.setDisplayedChild(1);
            }
        });
        btnImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPlay();
                txTitle.setText("Image");
                vf.setDisplayedChild(2);
            }
        });
        btnVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPlay();
                txTitle.setText("Video");
                vf.setDisplayedChild(3);
                videoView.setVideoPath("android.resource://" + getPackageName() + "/" + R.raw.video);
            }
        });

        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playSong();
            }
        });
        btnPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pauseSong();
            }
        });
        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopSong();
            }
        });
        seekVolume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, i, AudioManager.FLAG_SHOW_UI);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        progressBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if(b){
                    mediaPlayer.seekTo(i);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }

    private void initSeekBar(){
        seekVolume = findViewById(R.id.seekVolume);
    }
    private Handler handler = new Handler();
    private Runnable update;
    public void  play(){
        progressBar.setMax(mediaPlayer.getDuration());
        update = new Runnable() {
            @Override
            public void run(){
                SimpleDateFormat sdf = new SimpleDateFormat("mm:ss");
                Date d = new Date(mediaPlayer.getCurrentPosition());
                tvTime.setText(sdf.format(d));
                progressBar.setProgress(mediaPlayer.getCurrentPosition());
                handler.postDelayed( this, 1000);
            }
        };
        handler.postDelayed(update,100);
    }
    public void playSong(){
        if(mediaPlayer != null) {
            mediaPlayer.start();
            play();
        }
        else{
            mediaPlayer = MediaPlayer.create(getApplicationContext(),R.raw.music);
            play();
        }
    }
    public void pauseSong(){
        if(mediaPlayer.isPlaying()){
            mediaPlayer.pause();
        }
    }
    public void stopSong(){
        mediaPlayer.stop();
        mediaPlayer = MediaPlayer.create(getApplicationContext(),R.raw.music);
    }
    public void checkPlay(){
        if (mediaPlayer.isPlaying()) {
            stopSong();
        }
        if(videoView.isPlaying()){
            videoView.stopPlayback();
        }
    }
    @Override
    public void onBackPressed() {
        checkPlay();
        vf.setDisplayedChild(0);
    }
}