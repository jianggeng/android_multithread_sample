package com.jgeng.multithread;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

/**
 * Created by jgeng on 8/5/16.
 */

public class TaskWithMessage implements Task {
  private static final String TAG = "TaskWithMessage";
  public static final int MSG_START = 0;
  public static final int MSG_STOP = 1;
  public static final int MSG_PAUSE = 2;
  public static final int MSG_QUIT = 3;
  public static final int MSG_UPDATE_UI = 4;

  Handler mWorkHandler;
  Handler mUIHandler;
  Worker mWorker;

  public TaskWithMessage(Handler uiHandler) {
    super();
    mUIHandler = uiHandler;

    mWorker = new Worker(new Worker.OnDataChangedListener() {
      @Override
      public void onDataChanged(int data) {
        onDataChanged(data);
      }
    });

    Thread thread = new Thread(new Runnable() {
      @Override
      public void run() {
        try {
          // preparing a looper on current thread
          // the current thread is being detected implicitly
          Looper.prepare();

          // now, the handler will automatically bind to the
          // Looper that is attached to the current thread
          // You don't need to specify the Looper explicitly
          mWorkHandler = new Handler();
          // After the following line the thread will start
          // running the message loop and will not normally
          // exit the loop unless a problem happens or you
          // quit() the looper (see below)
          Looper.loop();
        } catch (Throwable t) {
          Log.e(TAG, "halted due to an error", t);
        }
      }
    });
    thread.start();
  }

  @Override
  public void start() {
    Message msg = Message.obtain(null, MSG_START);
    mWorkHandler.sendMessage(msg);
  }

  @Override
  public void pause() {
    Message msg = Message.obtain(null, MSG_PAUSE);
    mWorkHandler.sendMessage(msg);
  }

  @Override
  public void stop() {
    Message msg = Message.obtain(null, MSG_STOP);
    mWorkHandler.sendMessage(msg);
  }

  @Override
  public void quit() {
    Message msg = Message.obtain(null, MSG_QUIT);
    mWorkHandler.sendMessage(msg);
  }

  private class MyHandler extends Handler {
    @Override
    public void handleMessage(Message msg) {
      Log.e(TAG, "handleMessage " + msg.what);
      switch (msg.what) {
        case MSG_START:
          mWorker.start();
          break;
        case MSG_STOP:
          mWorker.stop();
          break;
        case MSG_PAUSE:
          mWorker.pause();
          break;
        case MSG_QUIT:
          Looper.myLooper().quit();
          break;
        default:
          Log.e(TAG, "unknown message " + msg.what);
          break;
      }
    }
  }

  protected void onDataChanged(int count) {
    Log.e(TAG, "update UI " + count);
    Message msg = Message.obtain(null, MSG_UPDATE_UI, count,0);
    mUIHandler.sendMessage(msg);
  }
}
