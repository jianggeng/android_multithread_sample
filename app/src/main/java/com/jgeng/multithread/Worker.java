package com.jgeng.multithread;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;


/**
 * Created by jgeng on 8/5/16.
 */

public class Worker {
  private static final String TAG = "Worker";
  public interface OnDataChangedListener {
    void onDataChanged(int data);
  }

  OnDataChangedListener mListener;
  private enum Status {
    STOPPED,
    RUNNING,
  }

  private Handler mHandler;
  int count = 0;

  Status mStatus = Status.STOPPED;

  public Worker(OnDataChangedListener listener) {
    mListener = listener;
  }
  protected void start() {
    if (mStatus != Status.RUNNING) {
      mStatus = Status.RUNNING;
      if (mHandler == null) mHandler = new Handler();
      mHandler.postDelayed(mTimerRunnable, 500);
    }
  }

  Runnable mTimerRunnable = new Runnable() {
    @Override
    public void run() {
      if (mStatus == Status.RUNNING) {
        count++;
        mListener.onDataChanged(count);
        mHandler.postDelayed(mTimerRunnable, 500);
      }
    }
  };

  protected void stop() {
    count = 0;
    mStatus = Status.STOPPED;

  }

  protected void pause() {
    mStatus = Status.STOPPED;
  }

}
