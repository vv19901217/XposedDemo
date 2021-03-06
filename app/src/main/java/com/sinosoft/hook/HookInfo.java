package com.sinosoft.hook;

/**
 * @ProjectName: XposedDemo
 * @Package: com.sinosoft.hook
 * @ClassName: HookInfo
 * @Description: hook对象的包名方法之类的
 * @Author: wiky
 * @CreateDate: 2020/12/11 5:12 PM
 * @UpdateUser: 更新者
 * @UpdateDate: 2020/12/11 5:12 PM
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class HookInfo {

    //hook对象的包名
    public static final String hookPackage="com.tencent.wework";
    //hook对象的类名
    public static final String hookClassName_Contact="ContactService";
    //对象的所在包
    public static final String hookClassNameBypackage_Contact=".foundation.logic.";
    //对象指定的包名+类名
    public static final String hookClassPath_Contact=hookPackage+hookClassNameBypackage_Contact+hookClassName_Contact;
    //对象的方法名：
    public static final String hookMethod_GetCacheOneContactGroupInfoUsers_Contact="a";

}
