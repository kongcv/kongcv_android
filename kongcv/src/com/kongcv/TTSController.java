package com.kongcv;


import android.content.Context;
import android.os.Bundle;

import com.amap.api.navi.AMapNaviListener;
import com.amap.api.navi.model.AMapNaviInfo;
import com.amap.api.navi.model.AMapNaviLocation;
import com.amap.api.navi.model.NaviInfo;
import com.iflytek.cloud.speech.SpeechConstant;
import com.iflytek.cloud.speech.SpeechError;
import com.iflytek.cloud.speech.SpeechListener;
import com.iflytek.cloud.speech.SpeechSynthesizer;
import com.iflytek.cloud.speech.SpeechUser;
import com.iflytek.cloud.speech.SynthesizerListener;

public class TTSController implements SynthesizerListener, AMapNaviListener {

    public static TTSController ttsManager;
    boolean isfinish = true;
    private Context mContext;
    private SpeechSynthesizer mSpeechSynthesizer;
    private SpeechListener listener = new SpeechListener() {

        @Override
        public void onData(byte[] arg0) {
        }

        @Override
        public void onCompleted(SpeechError error) {
            if (error != null) {

            }
        }

        @Override
        public void onEvent(int arg0, Bundle arg1) {
        }
    };

    TTSController(Context context) {
        mContext = context;
    }

    public static TTSController getInstance(Context context) {
        if (ttsManager == null) {
            ttsManager = new TTSController(context);
        }
        return ttsManager;
    }

    public void init() {
        SpeechUser.getUser().login(mContext, null, null,
                "appid=" + mContext.getString(R.string.app_id), listener);
        mSpeechSynthesizer = SpeechSynthesizer.createSynthesizer(mContext);
        initSpeechSynthesizer();
    }

    public void playText(String playText) {
        if (!isfinish) {
            return;
        }
        if (null == mSpeechSynthesizer) {
            mSpeechSynthesizer = SpeechSynthesizer.createSynthesizer(mContext);
            initSpeechSynthesizer();
        }
        mSpeechSynthesizer.startSpeaking(playText, this);

    }

    public void stopSpeaking() {
        if (mSpeechSynthesizer != null)
            mSpeechSynthesizer.stopSpeaking();
    }

    public void startSpeaking() {
        isfinish = true;
    }

    private void initSpeechSynthesizer() {
        mSpeechSynthesizer.setParameter(SpeechConstant.VOICE_NAME,
                mContext.getString(R.string.preference_default_tts_role));
        // 设置语速
        mSpeechSynthesizer.setParameter(SpeechConstant.SPEED,
                "" + mContext.getString(R.string.preference_key_tts_speed));
        // 设置音量
        mSpeechSynthesizer.setParameter(SpeechConstant.VOLUME,
                "" + mContext.getString(R.string.preference_key_tts_volume));
        // 设置语调
        mSpeechSynthesizer.setParameter(SpeechConstant.PITCH,
                "" + mContext.getString(R.string.preference_key_tts_pitch));

    }

    @Override
    public void onBufferProgress(int arg0, int arg1, int arg2, String arg3) {

    }

    @Override
    public void onCompleted(SpeechError arg0) {
        isfinish = true;
    }

    @Override
    public void onSpeakBegin() {
        isfinish = false;

    }

    @Override
    public void onSpeakPaused() {

    }

    @Override
    public void onSpeakProgress(int arg0, int arg1, int arg2) {

    }

    @Override
    public void onSpeakResumed() {

    }

    public void destroy() {
        if (mSpeechSynthesizer != null) {
            mSpeechSynthesizer.stopSpeaking();
        }
    }

    @Override
    public void onArriveDestination() {
        this.playText("到达目的地");
    }

    @Override
    public void onArrivedWayPoint(int arg0) {
    }

    @Override
    public void onCalculateRouteFailure(int arg0) {
        this.playText("路径计算失败，请检查网络或输入参数");
    }

    @Override
    public void onCalculateRouteSuccess() {
        String calculateResult = "路径计算就绪";

        this.playText(calculateResult);
    }

    @Override
    public void onEndEmulatorNavi() {
        this.playText("导航结束");

    }

    @Override
    public void onGetNavigationText(int arg0, String arg1) {
        // TODO Auto-generated method stub
        this.playText(arg1);
    }

    @Override
    public void onInitNaviFailure() {
        // TODO Auto-generated method stub

    }

    @Override
    public void onInitNaviSuccess() {
        // TODO Auto-generated method stub

    }

    @Override
    public void onLocationChange(AMapNaviLocation arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onReCalculateRouteForTrafficJam() {
        // TODO Auto-generated method stub
        this.playText("前方路线拥堵，路线重新规划");
    }

    @Override
    public void onReCalculateRouteForYaw() {

        this.playText("您已偏航");
    }

    @Override
    public void onStartNavi(int arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onTrafficStatusUpdate() {
        // TODO Auto-generated method stub

    }

    @Override
    public void onGpsOpenStatus(boolean arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onNaviInfoUpdated(AMapNaviInfo arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onNaviInfoUpdate(NaviInfo arg0) {

        // TODO Auto-generated method stub

    }
}
