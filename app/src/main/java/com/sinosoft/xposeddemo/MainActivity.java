package com.sinosoft.xposeddemo;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.fulongbin.decoder.Silk;
import com.sinosoft.logger.Logger;
import com.sinosoft.utils.SystemManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.sinosoft.myokhttp.MyOkHttp;
import cn.sinosoft.myokhttp.response.JsonResponseHandler;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.btn_silk_to_mp3)
    Button silkToMp3;
    @BindView(R.id.btn_mp3_to_silk)
    Button mp3ToSilk;
    @BindView(R.id.btn_silk_to_wav)
    Button silkToWav;
    @BindView(R.id.btn_wav_to_silk)
    Button wavToSilk;
    @BindView(R.id.btn_clear_cache)
    Button clearCache;
    @BindView(R.id.spinner)
    Spinner spinner;
    @BindView(R.id.show)
    TextView show;

    String httpurl = "http://mock-api.com/bKkAVmgB.mock/testNULLNULLNULL";

    int defaultRate = 24000; //assets
    String path_mp3 = "/storage/emulated/0/$MuMu共享文件夹/pkq.mp3";
    String path_silk = "/storage/emulated/0/$MuMu共享文件夹/hello.silk";
    String path_wav = "/storage/emulated/0/$MuMu共享文件夹/tiktok.wav";

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            String s = (String) msg.obj;
            show.setText(s);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
//        checkRoot();
        initView();
//        testHttp();

    }

    /**
     * 测试网络
     */
    private void testHttp() {
        MyOkHttp.get().get(this, httpurl, null, new JsonResponseHandler() {
            @Override
            public void onSuccess(int statusCode, String response) {
                parse(response);
            }

            @Override
            public void onFailure(int statusCode, String error_msg) {
                Logger.e(statusCode + "----" + error_msg);
            }
        });
    }

    private void checkRoot() {
        //     申请root
        String apkRoot = "chmod 777 " + this.getPackageCodePath();
        SystemManager.RootCommand(apkRoot);
    }

    private void initView() {
        spinner.setSelection(3);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int rate = Integer.parseInt(parent.getItemAtPosition(position).toString());
                defaultRate = rate;
                Logger.e("采样率" + rate);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    /**
     * 解析
     *
     * @param response
     */
    private void parse(String response) {
        try {
            Logger.e("网落请求返回response：" + response);
            JSONObject jsonObject = new JSONObject(response);
            String s = jsonObject.optString("version");
            Message.obtain(handler, 0, s).sendToTarget();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @OnClick({R.id.btn_silk_to_mp3, R.id.btn_mp3_to_silk, R.id.btn_silk_to_wav, R.id.btn_wav_to_silk, R.id.btn_clear_cache})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_silk_to_mp3:
                Logger.e("silk2mp3转换" );

                String dst2_sm = this.getCacheDir() + "/silkTpMp3.mp3";
                //该步骤很耗时请勿在主线程执行
                new Thread(() -> {
                    try {
                        boolean result = Silk.convertSilkToMp3(path_silk, dst2_sm, defaultRate);
                        Logger.e((result ? "silk2mp3转换成功" : "silk2mp3转换失败") + dst2_sm);
                    } catch (Exception e) {
                        Logger.e("silk2mp3转换失败----error：：：" + e.getMessage());

                    }
                }).start();
                break;
            case R.id.btn_mp3_to_silk:
                String dst2_ms = this.getCacheDir() + "/mp3Tpsilk.silk";
                new Thread(() -> {
                    try {
                        //该步骤很耗时请勿在主线程执行
//                    defaultRate=getMp3SampleRate(path_mp3);
                        boolean result2 = Silk.convertMp3ToSilk(path_mp3, dst2_ms, defaultRate);
                        Logger.e((result2 ? "mp32silk转换成功" : "mp32silk转换失败") + dst2_ms + "----" + defaultRate);

                    } catch (Exception e) {
                        Logger.e("mp32silk转换失败----error：：：" + e.getMessage());

                    }
                }).start();

                break;
            case R.id.btn_silk_to_wav:
                try {
                    String dst2_sw = this.getCacheDir() + "/silkToWav.wav";
                    //该步骤很耗时请勿在主线程执行 todo 执行崩掉
//                    boolean result2 = Silk.convertSilkToWav(path_wav, dst2_sw,defaultRate);
//                    Logger.e((result2 ? "silkToWav 转换成功" : "silkToWav 转换失败") + dst2_sw);
                } catch (Exception e) {
                    Logger.e("silkToWav 转换失败----error：：：" + e.getMessage());

                }
                break;
            case R.id.btn_wav_to_silk:
                String dst2_ws = this.getCacheDir() + "/wavToSilk.silk";
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            //该步骤很耗时请勿在主线程执行
                            boolean result2 = Silk.convertMp3ToSilk(path_wav, dst2_ws, defaultRate);
                            Logger.e((result2 ? "wavToSilk 转换成功" : "wavToSilk 转换失败") + dst2_ws);

                        } catch (Exception e) {
                            Logger.e("wavToSilk 转换失败----error：：：" + e.getMessage());
                        }
                    }
                }).start();

                break;
            case R.id.btn_clear_cache:
                File[] files = getCacheDir().listFiles();
                for (File file : files) {
                    file.delete();
                }
                break;
        }
    }

    /**
     * 测试一下pcm文件？
     *
     * @param pcmUrl
     */
    public void playPcm(String pcmUrl) {
        try {
//            InputStream is = new FileInputStream(new File(this.getCacheDir() + "/wavToSilk.pcm"));
            InputStream is = new FileInputStream(new File(pcmUrl));
            ByteArrayOutputStream bos = new ByteArrayOutputStream(10240);
            for (int b; (b = is.read()) != -1; ) {
                bos.write(b);
            }
            byte[] audioData = bos.toByteArray();
            AudioTrack audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, 16000, AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT, audioData.length, AudioTrack.MODE_STREAM);
            audioTrack.write(audioData, 0, audioData.length);
            audioTrack.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取mp3文件的采样率
     *
     * @param filefullname 文件完整路径
     * @return 采样率
     */
    public int getMp3SampleRate(String filefullname) {
        MediaExtractor mExtractor = new MediaExtractor();
        try {
            mExtractor.setDataSource(filefullname);
        } catch (IOException e) {
            e.printStackTrace();
        }
        MediaFormat format = mExtractor.getTrackFormat(0);
        //获取当前音频的采样率
        mExtractor.selectTrack(0);
        int mSampleRate = format.getInteger(MediaFormat.KEY_SAMPLE_RATE);
        return mSampleRate;
    }

}