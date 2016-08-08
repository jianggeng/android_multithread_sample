package com.jgeng.multithread;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
  private static final String TAG = "MainActivity";
  private TaskWithMessage mMessageThread;
  private TaskWithHandler mHandlerThread;
  private TaskWithAsyncTask mAsyncTask;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    initMessageThread();
    initHandlerThread();
    initAsyncTask();
  }

  protected void onDestroy() {
    super.onDestroy();
    mMessageThread.quit();
    mHandlerThread.quit();
    mAsyncTask.quit();
  }

  private void initMessageThread() {
    mMessageThread = new TaskWithMessage(mMessageHandler);

    Button btnStart = (Button) findViewById(R.id.btn_start);
    btnStart.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        mMessageThread.start();
      }
    });


    Button btnPause= (Button) findViewById(R.id.btn_pause);
    btnPause.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        mMessageThread.pause();
      }
    });

    Button btnStop = (Button) findViewById(R.id.btn_stop);
    btnStop.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        mMessageThread.stop();
      }
    });
  }

  private Handler mMessageHandler = new Handler() {
    @Override
    public void handleMessage(Message msg) {
      Log.e(TAG, "handleMessage " + msg.what);
      switch (msg.what) {
        case TaskWithMessage.MSG_UPDATE_UI: {
          TextView tv = (TextView) findViewById(R.id.tv_msg);
          tv.setText("msg: " + msg.arg1);
        }
        break;
        default:
          Log.e(TAG, "unknown message " + msg.what);
          break;
      }
    }
  };

  private void initHandlerThread() {
    mHandlerThread = new TaskWithHandler(new Worker.OnDataChangedListener() {
      @Override
      public void onDataChanged(int data) {
        TextView tv = (TextView) findViewById(R.id.tv_result2);
        tv.setText("result: " + data);
      }
    });

    Button btnStart = (Button) findViewById(R.id.btn_start_2);
    btnStart.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        mHandlerThread.start();
      }
    });


    Button btnPause= (Button) findViewById(R.id.btn_pause_2);
    btnPause.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        mHandlerThread.pause();
      }
    });

    Button btnStop = (Button) findViewById(R.id.btn_stop_2);
    btnStop.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        mHandlerThread.stop();
      }
    });
  }

  private void initAsyncTask() {
    mAsyncTask = new TaskWithAsyncTask(new Worker.OnDataChangedListener() {
      @Override
      public void onDataChanged(int data) {
        TextView tv = (TextView) findViewById(R.id.tv_result3);
        tv.setText("async task: " + data);
      }
    });

    Button btnStart = (Button) findViewById(R.id.btn_start_3);
    btnStart.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Log.e(TAG, "TaskWithAsyncTask start");
        mAsyncTask.start();
      }
    });


    Button btnPause= (Button) findViewById(R.id.btn_pause_3);
    btnPause.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        mAsyncTask.pause();
      }
    });

    Button btnStop = (Button) findViewById(R.id.btn_stop_3);
    btnStop.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        mAsyncTask.stop();
      }
    });
  }
}
