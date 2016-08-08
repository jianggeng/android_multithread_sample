package com.jgeng.multithread;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

/**
 * Created by jgeng on 8/5/16.
 */

public class TaskWithHandler implements Task {
  private static final String TAG = "TaskWithHandler";

  Handler mWorkHandler;
  Handler mUIHandler;
  Worker.OnDataChangedListener mUIListener;
  Worker mWorker;
  public TaskWithHandler(Worker.OnDataChangedListener listener) {
    super();
    mUIListener = listener;
    mUIHandler = new Handler(Looper.getMainLooper());
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
    mWorkHandler.post(new Runnable() {
      @Override
      public void run() {
        mWorker.start();
      }
    });
  }

  @Override
  public void pause() {
    mWorkHandler.post(new Runnable() {
      @Override
      public void run() {
        mWorker.pause();
      }
    });
  }

  @Override
  public void stop() {
    mWorkHandler.post(new Runnable() {
      @Override
      public void run() {
        mWorker.stop();
      }
    });
  }

  @Override
  public void quit() {
    mWorkHandler.post(new Runnable() {
      @Override
      public void run() {
        Looper.myLooper().quit();
      }
    });
  }

  protected void onDataChanged(final int count) {
    mUIHandler.post(new Runnable() {
      @Override
      public void run() {
        mUIListener.onDataChanged(count);
      }
    });
  }
}
