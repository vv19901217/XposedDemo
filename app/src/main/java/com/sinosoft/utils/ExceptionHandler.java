package com.sinosoft.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 全局捕捉异常并写入本地文件
 * Created by wiky on 2020/12/3.
 */
public class ExceptionHandler implements Thread.UncaughtExceptionHandler {
    private static ExceptionHandler handler = new ExceptionHandler();
    private Thread.UncaughtExceptionHandler defaultHandler;
    private File saveSpacePath ;
    private File localErrorSave;
    private Context context;
    private StringBuilder sb = new StringBuilder();
    private ExceptionHandler(){}
    public static ExceptionHandler getInstance(){
        return handler;
    }

    public void initConfig(Context context) {
        this.context = context;
        saveSpacePath = context.getExternalFilesDir("error_log");
        if (saveSpacePath == null) {
            saveSpacePath = context.getFilesDir();
        }
        localErrorSave = new File(saveSpacePath,"error_app.txt");
        if (!saveSpacePath.exists()){
            saveSpacePath.mkdirs();
        }
        if (!localErrorSave.exists()){
            try {
                localErrorSave.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        defaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override
    public void uncaughtException(final Thread t, Throwable e) {
        writeErrorToLocal(t, e);
        upLoadException(t);
    }


    /***
     * 上传异常信息到服务器
     * @param t1
     */
    private void upLoadException(Thread t1) {
        //暂时仅放本地


    }

    private void writeErrorToLocal(Thread t, Throwable e) {
        try {
            BufferedWriter fos = new BufferedWriter(new FileWriter(localErrorSave,true));
            PackageManager packageManager = context.getPackageManager();
            String line = "\n----------------------------------------------------------------------------------------\n";
            sb.append(line);
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), PackageManager.GET_ACTIVITIES);
            sb.append(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(System.currentTimeMillis()))+"<---->"+
                    "包名::"+packageInfo.packageName+"<---->版本名::"+packageInfo.versionName+"<---->版本号::"+packageInfo.versionCode+"\n");
            sb.append( "手机制造商::"+ Build.MANUFACTURER+"\n");
            sb.append( "手机型号::"+ Build.MODEL+"\n");
            sb.append( "CPU架构::"+ Build.CPU_ABI+"\n");
            sb.append(e.toString()+"\n");
            StackTraceElement[] trace = e.getStackTrace();
            for (StackTraceElement traceElement : trace)
                sb.append("\n\tat " + traceElement);
            sb.append("\n");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                Throwable[] suppressed = e.getSuppressed();
                for (Throwable se : suppressed)
                    sb.append("\tat " + se.getMessage());
            }
            fos.write(sb.toString());
            fos.close();


        } catch (IOException e1) {
            e1.printStackTrace();
            defaultHandler.uncaughtException(t,e1);
        } catch (PackageManager.NameNotFoundException e1) {
            e1.printStackTrace();
            defaultHandler.uncaughtException(t,e1);
        }
    }
}
