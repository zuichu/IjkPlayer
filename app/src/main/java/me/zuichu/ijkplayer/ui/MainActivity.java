package me.zuichu.ijkplayer.ui;

import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TableLayout;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.zuichu.ijkplayer.R;
import me.zuichu.ijkplayer.base.BaseActivity;
import me.zuichu.ijkplayer.utils.Utils;
import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;
import tv.danmaku.ijk.media.player.widget.media.IjkVideoView;
import tv.danmaku.ijk.media.player.widget.media.Options;

public class MainActivity extends BaseActivity implements IMediaPlayer.OnPreparedListener, IMediaPlayer.OnCompletionListener, IMediaPlayer.OnInfoListener, SeekBar.OnSeekBarChangeListener {
    @Bind(R.id.ivv)
    IjkVideoView ijkVideoView;
    @Bind(R.id.hud_view)
    TableLayout mHudView;
    @Bind(R.id.fl_bg)
    FrameLayout fl_bg;
    @Bind(R.id.seekbar)
    SeekBar seekbar;
    private IjkMediaPlayer ijkMediaPlayer;
    private RelativeLayout.LayoutParams layoutParams;
    private long seek = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        IjkMediaPlayer.loadLibrariesOnce(null);
        IjkMediaPlayer.native_profileBegin("libijkplayer.so");
        layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, Utils.getWidth(this));
        fl_bg.setLayoutParams(layoutParams);
        seekbar.setOnSeekBarChangeListener(this);
        Options options = new Options();
        options.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec", 1);//是否硬解码，0软解码，1硬解码
        options.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec-auto-rotate", 1);//是否硬解自动旋转视频，0不是，1是(硬解码有效)
        options.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec-handle-resolution-change", 1);//0不是，1是(硬解码有效)
        options.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "opensles", 1);//0不是，1是
//        options.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "overlay-format", IjkMediaPlayer.SDL_FCC_RV32);
//        options.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "framedrop", 1);
//        options.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "http-detect-range-support", 0);
//        options.setOption(IjkMediaPlayer.OPT_CATEGORY_CODEC, "skip_loop_filter", 48);
//        options.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "start-on-prepared", 0);// 是否自动启动播放，0不是，1是
//        options.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "max-buffer-size", 4 * 1000);//最大的缓存大小，单位是 ms
//        options.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "an", 0);//是否禁用声音，0不禁用，1禁用
//        options.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "vn", 0);//是否禁用视频，0不禁用，1禁用
//        options.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "packet-buffering", 0);
//        options.setOption(IjkMediaPlayer.OPT_CATEGORY_CODEC, "skip_frame", 8);

//        options.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "timeout", 10 * 1000);//准备超时时间，包括创建资源、建立连接、请求码流等，单位是 ms
//        options.setOption("get-av-frame-timeout", 10 * 1000);//读取视频流超时时间，单位是 ms
//        options.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER,"cache-buffer-duration", 2 * 1000);//默认的缓存大小，单位是 ms
//        options.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER,"max-cache-buffer-duration", 4 * 1000);
//        ijkVideoView.setLogType(IjkMediaPlayer.IJK_LOG_SILENT);
        ijkVideoView.setOptions(options);
//        ijkVideoView.setFflagsOption(true);//缓冲
//        ijkVideoView.setAnalyzedurationOption("2000000", true);
//        ijkVideoView.setProbsizeOption("4096", true);
//        ijkVideoView.setHudView(mHudView);
        ijkVideoView.setVideoPath("https://yyapptest.cbg.cn:4090/upload/attachment/2016-08-16/14713104166942.mp4");
//        ijkVideoView.setCurrentAspectRatio(IRenderView.AR_MATCH_PARENT);
        //http://yyvod.cbg.cn:1935/app_1/_definst_/smil:yyapp/20161008/1475912772_5423.smil/playlist.m3u8
        ijkVideoView.start();
        ijkVideoView.setOnPreparedListener(this);
        ijkVideoView.setOnCompletionListener(this);
        ijkVideoView.setOnInfoListener(this);
    }

    private void release() {
        if (!ijkVideoView.isBackgroundPlayEnabled()) {
            ijkVideoView.stopPlayback();
            ijkVideoView.release(true);
            ijkVideoView.stopBackgroundPlay();
        } else {
            ijkVideoView.enterBackground();
        }
        IjkMediaPlayer.native_profileEnd();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        release();
    }

    @Override
    public void onPrepared(IMediaPlayer mp) {
        ijkMediaPlayer = (IjkMediaPlayer) mp;
        seekbar.setMax(100);
        Log.i("info", "视频初始化完成" + ijkMediaPlayer.getDuration());
    }

    @Override
    public void onCompletion(IMediaPlayer mp) {
        Toast.makeText(this, "播放完成", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onInfo(IMediaPlayer mp, int what, int extra) {
        Log.i("info", "视频信息：" + what + "  " + extra);
        return false;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        seek = (seekBar.getProgress() * ijkMediaPlayer.getDuration()) / 100;
        ijkMediaPlayer.seekTo(seek);
    }
}
