package com.example.xianicai.minivideo;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.meicam.sdk.NvsLiveWindow;
import com.meicam.sdk.NvsRational;
import com.meicam.sdk.NvsStreamingContext;

import java.io.File;

public class MainActivity extends AppCompatActivity implements NvsStreamingContext.CaptureDeviceCallback {

    private NvsStreamingContext mStreamingContext;
    private int i = 0;
    private File mCaptureDir;
    private TextView mImageSwitch;
    private ImageView mImageCamera;
    private NvsRational mNvsRational;
    private ImageView mImageLight;
    private ImageView mImageBeauty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //        初始化NvsStreamingContext类（流媒体上下文）。
        mStreamingContext = NvsStreamingContext.init(this, null);
        setContentView(R.layout.activity_main);

//        设置回调
        initView();

    }

    private void initView() {
        NvsLiveWindow nvsLiveWindow = (NvsLiveWindow) findViewById(R.id.liveWindow);
        mImageLight = (ImageView) findViewById(R.id.image_fill_light);
        mImageCamera = (ImageView) findViewById(R.id.image_vide);
        mImageBeauty = (ImageView) findViewById(R.id.image_beauty);
        mImageSwitch = (TextView) findViewById(R.id.image_camera_switch);
        mImageBeauty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mStreamingContext.getBeautyVideoFxName().length() > 0) {
                    mStreamingContext.removeAllCaptureVideoFx();
                } else {
                    mStreamingContext.appendBeautyCaptureVideoFx();
                }
            }
        });
        mImageLight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mStreamingContext.toggleFlash(!mStreamingContext.isFlashOn());
            }
        });
        mImageCamera.setOnClickListener(new View.OnClickListener() {

            private int mType = 0;

            @Override
            public void onClick(View v) {
                if (mStreamingContext.isCaptureDeviceBackFacing(mType)) {
                    mStreamingContext.startCapturePreview(1, NvsStreamingContext.VIDEO_CAPTURE_RESOLUTION_GRADE_HIGH, 0, mNvsRational);
                    mType = 1;
                    setCameraStant();
                } else {
                    mStreamingContext.startCapturePreview(0, NvsStreamingContext.VIDEO_CAPTURE_RESOLUTION_GRADE_HIGH, 0, mNvsRational);
                    mType = 0;
                    setCameraStant();
                }

            }
        });
        mImageSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //获取录取状态
                setCameraStant();
            }
        });
//        设置回调
        mStreamingContext.setCaptureDeviceCallback(this);
//       预览状态
        mStreamingContext.connectCapturePreviewWithLiveWindow(nvsLiveWindow);
//        采集摄像头是否大于1
        if (mStreamingContext.getCaptureDeviceCount() > 1)
//        判断采集设备是否是后置设备
            mStreamingContext.isCaptureDeviceBackFacing(0);
//        开始采集预览
        mNvsRational = new NvsRational(1, 1);
        mStreamingContext.startCapturePreview(0, NvsStreamingContext.VIDEO_CAPTURE_RESOLUTION_GRADE_HIGH, 0, mNvsRational);


    }

    //获取录取状态
    private void setCameraStant() {
        if (mStreamingContext.getStreamingEngineState() == mStreamingContext.STREAMING_ENGINE_STATE_CAPTURERECORDING) {
            mImageSwitch.setText("继续");
            mStreamingContext.stopRecording();
        } else {
            mImageSwitch.setText("暂停");
            startAction();
        }
    }

    private void startAction() {

        if (mCaptureDir == null) {
            mCaptureDir = new File(Environment.getExternalStorageDirectory(), "MiniVideo" + File.separator + "Record");
        }
        if (!mCaptureDir.exists() && !mCaptureDir.mkdirs()) {
            return;
        }
        i++;
        File file = new File(mCaptureDir, i + "capture.mp4");
        if (file.exists())
            file.delete();

        mStreamingContext.startRecording(file.getAbsolutePath());
    }

    @Override
    public void onCaptureDeviceCapsReady(int i) {

    }

    @Override
    public void onCaptureDevicePreviewResolutionReady(int i) {

    }

    @Override
    public void onCaptureDevicePreviewStarted(int i) {

    }

    @Override
    public void onCaptureDeviceError(int i, int i1) {

    }

    @Override
    public void onCaptureDeviceStopped(int i) {

    }

    @Override
    public void onCaptureDeviceAutoFocusComplete(int i, boolean b) {

    }

    @Override
    public void onCaptureRecordingFinished(int i) {

    }

    @Override
    public void onCaptureRecordingError(int i) {

    }
}

