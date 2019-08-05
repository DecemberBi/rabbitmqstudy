package com.biykcode.util;

/**
 * @author biyukun
 * @description TODO
 * @date 2019-08-05
 */
public class WorkUtil {
  public static void doWork(String task) {
    for (char ch : task.toCharArray()) {
      if (ch == '.') {
        try {
          Thread.sleep(1000);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    }
  }
}
