package com.sinosoft.hook;

import java.lang.reflect.Proxy;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

import static de.robv.android.xposed.XposedHelpers.findClass;
import static de.robv.android.xposed.XposedHelpers.newInstance;

/**
 * @ProjectName: XposedDemo
 * @Package: com.sinosoft.hook
 * @ClassName: MainHook
 * @Description: 入口
 * @Author: wiky
 * @CreateDate: 2020/12/11 5:06 PM
 * @UpdateUser: 更新者
 * @UpdateDate: 2020/12/11 5:06 PM
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class MainHook implements IXposedHookLoadPackage {


    /**
     * @param lpparam
     * @throws Throwable
     */
    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {

        XposedBridge.log("当前启动的程序是" + lpparam.packageName);
        if (lpparam.packageName.equals(HookInfo.hookPackage) && lpparam.processName.equals(HookInfo.hookPackage)) {
            XposedBridge.log("进入的包和进程..." + lpparam.packageName + "...进程..." + lpparam.processName);

            //第一个参数，指定的包名+类名，第二个参数classloader，第三个参数方法名，第四个参数....第n-1个参数：方法传参数的类型.class，接口
            XposedHelpers.findAndHookMethod(HookInfo.hookClassPath_Contact,
                    lpparam.classLoader,
                    HookInfo.hookMethod_GetCacheOneContactGroupInfoUsers_Contact,
                    long.class,
                    new XC_MethodHook() {
                        /**
                         * hook之前打印参数信息及修改参数
                         * @param param
                         * @throws Throwable
                         */
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                            super.beforeHookedMethod(param);
                            //修改之前
                            XposedBridge.log("hookMethod_GetCacheOneContactGroupInfoUsers_Contact 修改之前的参数l" + param.args[0]);
                        }

                        /**
                         * hook之后
                         * 打印返回值信息和修改返回值
                         * @param param
                         * @throws Throwable
                         */
                        @Override
                        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                            super.afterHookedMethod(param);
                            XposedBridge.log("hookMethod_GetCacheOneContactGroupInfoUsers_Contact 返回值：" + param.getResult());

                        }
                    });
        }
    }
}
