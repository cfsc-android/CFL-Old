package org.xutils.app;

import android.app.Activity;

import java.util.Stack;

public class LynActivityManager {

    private volatile static LynActivityManager instance = null;
    private static Stack<Activity> activityStack;
    private LynActivityManager () {}

    public static LynActivityManager getInstance() {
        if (instance == null) {
            synchronized (LynActivityManager.class) {
                if (instance == null) {
                    instance = new LynActivityManager();
                }
            }
        }
        return instance;

    }
    /**
     * @param activity
     * @方法说明:销毁指定的Activity
     * @方法名称:popActivity
     * @返回 void
     */
    public void popActivity(Activity activity) {
        if (activity != null) {
            // 在从自定义集合中取出当前Activity时，也进行了Activity的关闭操作
            activity.finish();
            activityStack.remove(activity);
            activity = null;
        }
    }

    /**
     * /**
     * 结束指定类名的Activity
     */
    public void finishActivity(Class<?> cls) {
        for (int i = 0; i < activityStack.size(); i++) {
            if (activityStack.get(i).getClass().equals(cls)) {
                popActivity(activityStack.get(i));
            }
        }
    }

    /**
     * @return
     * @方法说明:获得当前栈顶Activity
     * @方法名称:currentActivity
     * @返回 Activity
     */
    public Activity currentActivity() {
        Activity activity = null;
        if (null != activityStack) {
            if (!activityStack.empty()) {
                activity = activityStack.lastElement();
            }
        }
        return activity;
    }

    /**
     * @param activity
     * @方法说明:将当前Activity推入栈中
     * @方法名称:pushActivity
     * @返回�void
     */
    public void pushActivity(Activity activity) {
        if (activityStack == null) {
            activityStack = new Stack<>();
        }
        activityStack.add(activity);
    }

    /**
     * @param cls
     * @方法说明:退出栈中所有Activity,到指定的activity截止
     * @方法名称:popAllActivityExceptOne
     * @返回�void
     */
    public void popAllActivityExceptOne(Class cls) {
        while (true) {
            Activity activity = currentActivity();
            if (activity == null) {
                break;
            }
            if (activity.getClass().equals(cls)) {
                break;
            }
            popActivity(activity);
        }
    }

    /**
     * @方法说明:退出栈中所有的activity
     * @方法名称:removeAllActivity
     * @返回 void
     */
    public void removeAllActivity() {
        for (int i = activityStack.size() - 1; i >= 0; i--) {
            Activity activity = activityStack.get(i);
            popActivity(activity);
        }
    }

    /**
     * @方法说明:退出除首页界面栈中所有的activity
     * @方法名称:removeAllActivity
     * @返回 void
     */
    public void removeAllThisActivity(Activity homepageActivity) {
        if(homepageActivity == null)return;
        for (int i = activityStack.size() - 1; i >= 0; i--) {
            Activity activity = activityStack.get(i);
            if (homepageActivity != activity) {
                popActivity(activity);
            }
        }
    }

    /**
     * @param cls
     * @return
     * @方法说明:获取指定的activity
     * @方法名称:getActivityByClass
     * @返回 Activity
     */
    public Activity getActivityByClass(Class cls) {
        if (activityStack == null) {
            return null;
        }
        for (int i = activityStack.size() - 1; i >= 0; i--) {
            Activity activity = activityStack.get(i);
            String name1 = activity.getClass().getName();
            String name2 = cls.getName();
            if (name1.equals(name2)) {
                return activity;
            }
        }
        return null;
    }

    /**
     * @param cls
     * @return
     * @方法说明:判断当前的activity是当前的运行 * @方法名称:isCurrentActivity
     * @返回�boolean
     */
    public boolean isCurrentActivity(Class cls) {
        String name1 = currentActivity().getClass().getName();
        String name2 = cls.getName();
        return name1.endsWith(name2);
    }
}
