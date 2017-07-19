package com.example.mango.focustime.processutil;

import android.content.Context;

import com.example.mango.focustime.service.DetectionService;

/**
 * Created by wenmingvs on 2016/1/14.
 */
public class BackgroundUtil {

    /**
     * 自动根据参数选择判断前后台的方法
     *
     * @param context     上下文参数
     * @param packageName 需要检查是否位于栈顶的App的包名
     * @return
     */
    public static boolean isForeground(Context context, int methodID, String packageName) {
        return isForegroundPkgViaDectectService(packageName);
    }

    /**
     * 使用 Android AccessibilityService 探测窗口变化，跟据系统回传的参数获取 前台对象 的包名与类名
     *
     * @param packageName 需要检查是否位于栈顶的App的包名
     */
    public static boolean isForegroundPkgViaDectectService(String packageName) {
        return packageName.equals(DetectionService.foregroundPackageName);
    }

}
