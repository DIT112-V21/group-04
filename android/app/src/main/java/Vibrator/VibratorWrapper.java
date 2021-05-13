//Code adapted from: https://stackoverflow.com/questions/29862965/detect-the-device-is-vibrating
/*This wrapper class was added due to its implementation of isVibrating().
as there isn't a way to see if the phone is vibrating in the emulator. */
package Vibrator;

import android.content.Context;
import android.media.AudioAttributes;
import android.os.Build;
import android.os.Vibrator;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class VibratorWrapper {

    public static final String TAG = VibratorWrapper.class.getSimpleName();

    private Context mContext;
    private Vibrator mVibrator;
    private boolean mIsVibrating = false;
    private ScheduledThreadPoolExecutor mExecutor;

    private Runnable mVibrationEndRunnable = new Runnable() {
        @Override
        public void run() {
            setVibrating(false);
        }
    };

    public VibratorWrapper(Context context) {
        this.mContext = context;
        mVibrator = (Vibrator) mContext.getSystemService(Context.VIBRATOR_SERVICE);
        mExecutor = new ScheduledThreadPoolExecutor(1);
    }

    public boolean hasVibrator() {
        return mVibrator.hasVibrator();
    }

    public void vibrate(long milliseconds) {
        if (this.hasVibrator()) {
            setVibrating(true);
            mVibrator.vibrate(milliseconds);
            notifyOnVibrationEnd(milliseconds);
            Log.i(TAG, "Is vibrating? " + this.isVibrating());
        } else {
            Log.i(TAG, "device doesn't support vibrations");
        }

    }


    public boolean isVibrating() {
        return mIsVibrating;
    }

    private void setVibrating(boolean isVibrating) {
        mIsVibrating = isVibrating;
    }

    private void notifyOnVibrationEnd(long milliseconds) {
        try {
            mExecutor.schedule(mVibrationEndRunnable, milliseconds, TimeUnit.MILLISECONDS);
        } catch (RejectedExecutionException e) {
            Log.e(TAG, e.getMessage());
        }
    }

}