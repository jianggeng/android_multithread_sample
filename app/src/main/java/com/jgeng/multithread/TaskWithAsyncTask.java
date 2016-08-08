package com.jgeng.multithread;

import android.os.AsyncTask;
import android.util.Log;

import java.util.concurrent.atomic.AtomicInteger;


/**
 * Created by jgeng on 8/5/16.
 */

public class TaskWithAsyncTask implements Task {
  private static final String TAG = "TaskWithAsyncTask";
  AtomicInteger count = new AtomicInteger(0);
  MyAsyncTask asyncTask;
  Worker.OnDataChangedListener mUIListener;
  public TaskWithAsyncTask(Worker.OnDataChangedListener listener) {
    mUIListener = listener;
  }
  @Override
  public void start() {
    Log.e(TAG, "start "+ asyncTask );
    if (asyncTask == null) {
      asyncTask = new MyAsyncTask();
      asyncTask.execute();
    }
  }

  @Override
  public void pause() {
    if (asyncTask != null) {
      Log.e(TAG, "pause ");
      asyncTask.cancel(true);
      asyncTask = null;
    }

  }

  @Override
  public void stop() {
    if (asyncTask != null) {
      Log.e(TAG, "stop ");
      asyncTask.cancel(true);
      asyncTask = null;
      count.set(0);
    }
  }

  @Override
  public void quit() {
    if (asyncTask != null) {
      Log.e(TAG, "quit");
      asyncTask.cancel(true);
      asyncTask = null;
      count.set(0);
    }
  }

  private class MyAsyncTask extends AsyncTask<Void, Integer, Integer> {
    @Override
    protected Integer doInBackground(Void... params) {
      Log.e(TAG, "doInBackground" );
      while(!isCancelled()) {
        synchronized(this) {
          count.incrementAndGet();
          publishProgress(count.get());
          try {
            Thread.sleep(500);
          } catch (Exception e) {

          }
        }
      }
      Log.e(TAG, "doInBackground end" );
      return count.get();
    }

  protected void onProgressUpdate(Integer... progress) {
    Log.e(TAG, "onProgressUpdate" + progress[0]);
    mUIListener.onDataChanged(progress[0]);
  }

  protected void onPostExecute(Integer result) {
    Log.e(TAG, "onPostExecute" + result);
    mUIListener.onDataChanged(result);
  }
}
}
