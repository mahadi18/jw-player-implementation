package com.jwplayer.opensourcedemo;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.KeyEvent;

import com.google.android.gms.cast.framework.CastContext;
import com.longtailvideo.jwplayer.JWPlayerView;
import com.longtailvideo.jwplayer.configuration.PlayerConfig;
import com.longtailvideo.jwplayer.events.FullscreenEvent;
import com.longtailvideo.jwplayer.events.listeners.VideoPlayerEvents;
import com.longtailvideo.jwplayer.media.ads.AdBreak;
import com.longtailvideo.jwplayer.media.ads.AdSource;
import com.longtailvideo.jwplayer.media.ads.Advertising;
import com.longtailvideo.jwplayer.media.playlists.PlaylistItem;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;


public class JwPlayerActivity extends AppCompatActivity
        implements VideoPlayerEvents.OnFullscreenListener {

    private JWPlayerView mPlayerView;

    private CastContext mCastContext;

    private CallbackScreen mCallbackScreen;

    private String videoUrl = "http://103.89.68.179:1935/mediacache/_definst_/smil:path1/67d1f04e-71a4-4744-a8c9-c1c85f08f508.smil/playlist.m3u8";
    private String adsUrl = "http://103.89.68.180/storage/v_a/xml/20012ea4-aa0e-4762-a21a-9a664d09e914.xml";
    private String adsUrl2 = "https://s3.amazonaws.com/demo.jwplayer.com/advertising/assets/vast3_jw_ads.xml";
    private String[] ads = new String[]{adsUrl, adsUrl2};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jwplayerview);

        mPlayerView = findViewById(R.id.jwplayer);
        mPlayerView.addOnFullscreenListener(this);
        new KeepScreenOnHandler(mPlayerView, getWindow());

        // Event Logging
        mCallbackScreen = findViewById(R.id.callback_screen);
        mCallbackScreen.registerListeners(mPlayerView);

        // Get a reference to the CastContext
        mCastContext = CastContext.getSharedInstance(this);

        playVideoWithAd();

    }

    private void playVideoWithAd() {
        AdBreak adBreak = new AdBreak("pre", AdSource.VAST, ads);
        List<AdBreak> adSchedule = new ArrayList<AdBreak>();
        adSchedule.add(adBreak);

        PlaylistItem video = new PlaylistItem(videoUrl);
        //video.setAdSchedule(adSchedule);

        List<PlaylistItem> playlist = new ArrayList<PlaylistItem>();
        playlist.add(video);

        Advertising advertising = new Advertising(AdSource.VAST, adSchedule);

        PlayerConfig playerConfig = new PlayerConfig.Builder()
                .playlist(playlist)
                .advertising(advertising)
                .autostart(true)
                .build();

        mPlayerView.setup(playerConfig);
    }


    @Override
    protected void onStart() {
        super.onStart();
        mPlayerView.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPlayerView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPlayerView.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mPlayerView.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPlayerView.onDestroy();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        // Set fullscreen when the device is rotated to landscape
        mPlayerView.setFullscreen(newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE,
                true);
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // Exit fullscreen when the user pressed the Back button
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mPlayerView.getFullscreen()) {
                mPlayerView.setFullscreen(false, true);
                return false;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onFullscreen(FullscreenEvent fullscreenEvent) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            if (fullscreenEvent.getFullscreen()) {
                actionBar.hide();
            } else {
                actionBar.show();
            }
        }
    }
}
