package com.sinosoft.utils;


import com.sinosoft.logger.Logger;

import java.io.DataOutputStream;

//申请root，不用了
public class SystemManager  {



    /**

     * 应用程序运行命令获取Root权限，设备必须已破解(获得ROOT权限)

     * @param command 命令：String apkRoot="chmod 777 "+getPackageCodePath();  RootCommand(apkRoot);

     * @return 应用程序是/否获取Root权限

     */

    public static boolean RootCommand(String command) {

        Process process = null;

        DataOutputStream os = null;

        try {

            process = Runtime.getRuntime().exec("su");

            os = new DataOutputStream(process.getOutputStream());

            os.writeBytes(command + "\n");

            os.writeBytes("exit\n");

            os.flush();

            process.waitFor();

        } catch (Exception e) {

            Logger.e("ROOT REE" + e.getMessage());

            return false;

        } finally {

            try {

                if (os != null) {

                    os.close();

                }

                process.destroy();

            } catch (Exception e) {

            }

        }

        Logger.e("Root SUC ");

        return true;

    }
}