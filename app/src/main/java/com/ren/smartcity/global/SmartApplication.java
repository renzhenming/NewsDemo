package com.ren.smartcity.global;

import android.app.Application;
import android.os.Environment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

/**
 * Created by Administrator on 2017/5/1.
 */
public class SmartApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHander());
    }

    class ExceptionHander implements Thread.UncaughtExceptionHandler {

        @Override
        public void uncaughtException(Thread t, Throwable e) {
//            String absolutePath = Environment.getExternalStorageDirectory().getAbsolutePath();
//            try {
//                PrintWriter writer = new PrintWriter(absolutePath+"/smart_error.txt");
//                e.printStackTrace(writer);
//                writer.close();
//            } catch (FileNotFoundException e1) {
//
//            }
//            android.os.Process.killProcess(android.os.Process.myPid());
            //收集错误日志, 也可以将日志自动上传到服务器
            try {
                PrintWriter writer = new PrintWriter(Environment
                        .getExternalStorageDirectory().getAbsolutePath()
                        + "/bbbbbbbbbbbbbbbbbbbb.log");
                e.printStackTrace(writer);
                writer.close();
            } catch (Exception e1) {
                e1.printStackTrace();
            }

            //强制结束当前进程, 闪退
            //System.exit(0);
            android.os.Process.killProcess(android.os.Process.myPid());
        }
    }
}
