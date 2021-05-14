package com.bin.smart.za.ui.StudentActivity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bin.smart.za.R;
import com.bin.smart.za.ui.DoctorActivity.AddLectureDoctorActivity;
import com.bin.smart.za.ui.DoctorActivity.AddLectureForSubjectDoctorDetails;
import com.bin.smart.za.ui.PdfShowActivity;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Log;
import com.google.android.exoplayer2.util.Util;

public class VideoAndTextDetails extends AppCompatActivity {

    private TextView txt;
    private SimpleExoPlayer player;
    private PlayerView playerView;
    private ImageView btn_fullScreen;
    private Toolbar toolbar_video;
    private ProgressBar progressBar;
    private boolean flag = false;
    private String TAG = ".VideoTextDetails";
    private Button btn_show_pdfActivity;
    private SharedPreferences sharedPreferences;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_and_text_details);
        txt = findViewById(R.id.txt_show);
        playerView = (PlayerView) findViewById(R.id.exoplayer_id);
        btn_fullScreen = findViewById(R.id.btn_fullScreen);
        //inflate Tool
//
        progressBar = findViewById(R.id.progressBar);
        Intent intent = getIntent();
        String tV_story_details = intent.getStringExtra("text_url");
        txt.setText(tV_story_details);
        Uri uri = Uri.parse(intent.getStringExtra("url"));
       final Uri uri_pdf = Uri.parse(intent.getStringExtra("uri_pdf"));

       saveData("uri_pdf",uri_pdf.toString());


        btn_show_pdfActivity = findViewById(R.id.btn_show_pdf);

        btn_show_pdfActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent i = new Intent(VideoAndTextDetails.this, PdfShowActivity.class);
               startActivity(i);

            }
        });



        player = ExoPlayerFactory.newSimpleInstance(this);

//        playerView.requestFocus();
        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter.Builder(VideoAndTextDetails.this).build();
        TrackSelector trackSelector = new DefaultTrackSelector(new AdaptiveTrackSelection.Factory(bandwidthMeter));
        DefaultHttpDataSourceFactory dataSourceFactory = new DefaultHttpDataSourceFactory("video");
        ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
        MediaSource mediaSource = new ExtractorMediaSource(uri,dataSourceFactory,extractorsFactory,new Handler()
                ,null);


//        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(this,
//                Util.getUserAgent(this, "Exoplayer"));
// This is the MediaSource representing the media to be played.
//        MediaSource videoSource =
//                new ProgressiveMediaSource.Factory(dataSourceFactory)
//                        .createMediaSource(uri);
// Prepare the player with the source.
        playerView.setPlayer(player);
        player.prepare(mediaSource);
        player.setPlayWhenReady(true);

        player.addListener(new Player.EventListener() {
            @Override
            public void onTimelineChanged(Timeline timeline, @Nullable Object manifest, int reason)
            {

            }
            @Override
            public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections)
            {

            }
            @Override
            public void onLoadingChanged(boolean isLoading)
            {

            }
            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState)
            {
                if (playbackState == Player.STATE_BUFFERING)
                {
                    progressBar.setVisibility(View.VISIBLE);
                }
                else if(playbackState == Player.STATE_READY)
                {
                    progressBar.setVisibility(View.GONE);

                }

            }

            @Override
            public void onPlayerError(ExoPlaybackException error)
            {
              switch (error.type)
              {
                  case ExoPlaybackException.TYPE_SOURCE:
                      Log.d(TAG,"mohamed type Source" + error.getSourceException().getMessage());
                      break;
                  case ExoPlaybackException.TYPE_RENDERER:
                      Log.d(TAG,"mohamed TYPE_RENDERER" + error.getRendererException().getMessage());
                      break;
                  case ExoPlaybackException.TYPE_UNEXPECTED:
                      Log.d(TAG,"mohamed TYPE_UNEXPECTED" + error.getUnexpectedException().getMessage());
                      break;
              }

            }

            @Override
            public void onPositionDiscontinuity(int reason) {

            }
        });
        btn_fullScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (flag)
                {
                    btn_fullScreen.setImageDrawable(getResources()
                    .getDrawable(R.drawable.ic_fullscreen));
                    //set portrait orintaion
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    flag= false;
                }
                else
                {
                    //when flag is false set exit fullscreen
                    btn_fullScreen.setImageDrawable(getResources()
                    .getDrawable(R.drawable.icfullscreen_exit));
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                    flag = true;

                }
            }
        });


    }


    @Override
    protected void onPause() {
        super.onPause();
        player.stop();
        player.release();
    }

    @Override
    protected void onStop() {
        super.onStop();
        player.stop();
        player.release();
    }
    public void saveData(String key , String value)
    {
        sharedPreferences = VideoAndTextDetails.this.getSharedPreferences(
                "Garage", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key , value);
        editor.commit();
    }


}