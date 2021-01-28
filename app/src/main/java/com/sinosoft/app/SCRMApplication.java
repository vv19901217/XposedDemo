package com.sinosoft.app;

import android.app.Application;

import com.sinosoft.utils.ExceptionHandler;

/**
 * @ProjectName: XposedDemo
 * @Package: com.sinosoft.app
 * @ClassName: SCRMApplication
 * @Description: 程序入口
 * @Author: wiky
 * @CreateDate: 2020/12/1 6:03 PM
 * @UpdateUser: 更新者
 * @UpdateDate: 2020/12/1 6:03 PM
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class SCRMApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        initLog();

    }
    /**
     * 错误日志-app
     */
    private void initLog() {
        ExceptionHandler.getInstance().initConfig(this);

    }

}
