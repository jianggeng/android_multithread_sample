package com.jgeng.multithread;

/**
 * Created by jgeng on 8/5/16.
 */

public interface Task {
  void start();
  void pause();
  void stop();
  void quit();
}
