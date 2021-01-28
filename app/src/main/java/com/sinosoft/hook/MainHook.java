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
                            //修改一下参数
//                            param.args[0]=1;
                            //修改之后打印
//                            XposedBridge.log("hookMethod_GetCacheOneContactGroupInfoUsers_Contact 修改之后的参数l"+param.args[0]);
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
//                            param.setResult(null);
//                            XposedBridge.log("hookMethod_GetCacheOneContactGroupInfoUsers_Contact 修改后的返回值："+param.getResult());

                        }
                    });


            // TODO: 2020/12/11 测一下消息

            //package com.tencent.wework.foundation.model
            Class<?> Conversation = findClass("com.tencent.wework.foundation.model.Conversation", lpparam.classLoader);
            Class<?> Message = findClass("com.tencent.wework.foundation.model.Message", lpparam.classLoader);
            Class<?> User = findClass("com.tencent.wework.foundation.model.User", lpparam.classLoader);
            Class<?> ISendMessageCallback = findClass("com.tencent.wework.foundation.callback.ISendMessageCallback", lpparam.classLoader);

            XposedHelpers.findAndHookMethod(HookInfo.hookClassPath_ConversationService,
                    lpparam.classLoader,
                    HookInfo.hookMethod_SendMessage_ConversationService,
                    Conversation, Message,"com.tencent.wework.foundation.callback.ISendMessageCallback",
                    new XC_MethodHook() {
                        /**
                         * hook之前打印参数信息及修改参数
                         * @param param
                         * @throws Throwable
                         */
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                            super.beforeHookedMethod(param);
                            Object conversation =param.args[0];//Conversation
                            Object Message =param.args[1];//Conversation
//                           long localid= (long) XposedHelpers.callMethod(conversation,"getLocalId");
                           Object[]  mMembers= (Object[]) XposedHelpers.getObjectField(conversation,"mMembers");
                           Object msg= XposedHelpers.callMethod(Message,"getInfo");
//                            XposedBridge.log("hook之前打印getLocalId:"+(localid==0?("localid:"+localid):"localid为null"));
//                            XposedBridge.log("hook之前打印name:"+(name==null?("name:"+name):"name为null"));
                            XposedBridge.log("hook之前打印msg:"+msg.toString());
                            XposedBridge.log("hook之前打印con:"+conversation.toString());
                            for (int i = 0; i < mMembers.length; i++) {
                                XposedBridge.log("hook之前mMembers:"+i+mMembers[i].toString());
                            }
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
                            Object conversation =param.args[0];//Conversation
                            Object Message =param.args[1];//Conversation
//                            long localid= XposedHelpers.getLongField(conversation,"getLocalId");
                            Object con=  XposedHelpers.callMethod(conversation,"getInfo");
//                            String name= (String) XposedHelpers.getObjectField(conversation,"name");
                            Object msg= XposedHelpers.callMethod(Message,"getInfo");

//                            XposedBridge.log("hook之前打印getLocalId:"+(localid!=0?("localid:"+localid):"localid为null"));
//                            XposedBridge.log("hook之后打印getLocalId:"+(localid==0?("localid:"+localid):"localid为null"));
//                            XposedBridge.log("hook之后打印name:"+(name==null?("name:"+name):"name为null"));
                            XposedBridge.log("hook之后打印con:"+con.toString());
                            XposedBridge.log("hook之后打印msg:"+msg.toString());
                            Object[]  mMembers= (Object[]) XposedHelpers.getObjectField(conversation,"mMembers");
                            for (int i = 0; i < mMembers.length; i++) {
                                XposedBridge.log("hook之后mMembers:"+i+mMembers[i].toString());
                            }
                        }
                    });

            XposedHelpers.findAndHookMethod(HookInfo.hookClassPath_Conversation,
                    lpparam.classLoader,
                    HookInfo.hookMethod_GetUserList_Conversation,
                    long[].class,
                    new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                            super.beforeHookedMethod(param);
                            Object user =param.args[0];//
                            long[] jArr= (long[]) user;
//                            for (int i = 0; i < jArr.length; i++) {
//
//                            }
                            XposedBridge.log("hook之前打印 long[]:"+jArr.length+"---"+jArr.toString());

                        }

                        @Override
                        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                            super.afterHookedMethod(param);
                            Object user =param.args[0];//
                            long[] jArr= (long[]) user;
//                            for (int i = 0; i < jArr.length; i++) {
//
//                            }
                            XposedBridge.log("hook之后打印 long[]:"+jArr.length+"---"+jArr.toString());
                        }
                    });

//            Conversation.this.mOutObservers.Notify("onAddMessages", conversation, messageArr, Boolean.valueOf(z));
//            Message[] messageArr, boolean z
            XposedHelpers.findAndHookMethod(HookInfo.hookClassPath_Conversation,
                    lpparam.classLoader,
                    HookInfo.hookMethod_onAddMessages_Conversation,
                    Conversation,
                    Object[].class,
                    boolean.class,
                    new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                            super.beforeHookedMethod(param);
                            Object con =param.args[0];//Conversation
                            Object message =param.args[1];//Message[]
                            Object z =param.args[2];//z



                            XposedBridge.log("hookMethod_onAddMessages_Conversation hook之前打印con："+con.toString());
                            XposedBridge.log("hookMethod_onAddMessages_Conversation hook之前打印msg："+message.toString());
                            XposedBridge.log("hookMethod_onAddMessages_Conversation hook之前打印z："+z);

                        }

                        @Override
                        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                            super.afterHookedMethod(param);
                            Object con =param.args[0];//Conversation
                            Object message =param.args[1];//Message[]
                            Object z =param.args[2];//z


                            XposedBridge.log("hookMethod_onAddMessages_Conversation hook之后打印con："+con.toString());
                            XposedBridge.log("hookMethod_onAddMessages_Conversation hook之后打印msg："+message.toString());
                            XposedBridge.log("hookMethod_onAddMessages_Conversation hook之后打印z："+z);
                        }
                    });


        }


    }
}
