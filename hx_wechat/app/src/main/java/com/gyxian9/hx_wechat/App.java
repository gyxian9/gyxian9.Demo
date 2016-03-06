package com.gyxian9.hx_wechat;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import com.easemob.chat.EMChat;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMChatOptions;
import com.easemob.chat.EMMessage;
import com.easemob.chat.OnMessageNotifyListener;
import com.easemob.chat.OnNotificationClickListener;

import java.io.File;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by gyxian9 on 2016/3/6.
 */
public class App extends Application {

    /**
     * 暂不用百度分享捕获全局异常  FrontiaApplication
     * 包是pushservice-4.4
     *   1.获取context和app
     *   2.初始化环信，设置环信接受功能
     *   3.创建存储activity的集合，添加和删除功能
     *   4.清理缓存功能(1.获取缓存地址，2.如果没有1就设置地址)
     */

    //获取context，初始化context
    private Context _context;
    private static App instance;

    public Context getAppContext() {
        return _context;
    }

    public synchronized static App getInstance() {
        if (null == instance){
            instance = new App();
        }
        return instance;
    }

    //初始化环信，设置环信接受功能
    @Override
    public void onCreate() {
        super.onCreate();

        _context = getAppContext();

        initEMChat();
        EMChat.getInstance().init(this);
        EMChat.getInstance().setDebugMode(true);
        EMChat.getInstance().setAutoLogin(true);
        EMChatManager.getInstance().getChatOptions().setUseRoster(true);
    }

    private void initEMChat() {
        int pid = android.os.Process.myPid();
        String processAppName = getAppName(pid);

        if (processAppName == null ||!processAppName.equalsIgnoreCase(Constant.PACKAGENAME)) {
//             则此application::onCreate 是被service 调用的，直接返回
            return;
        }
        //用户可以用来配置SDK的各种参数，选项 比如，发送消息加密，受到消息是否播放提示音
        EMChatOptions options = EMChatManager.getInstance().getChatOptions();
        // 获取到EMChatOptions对象
        // 设置自定义的文字提示
        options.setNotifyText(new OnMessageNotifyListener() {
            //有新消息通知
            @Override
            public String onNewMessageNotify(EMMessage emMessage) {
                return "你的好友发来了一条消息";
            }
            //最后一条消息通知
            @Override
            public String onLatestMessageNotify(EMMessage emMessage,
                                                int fromUsersNum, int messageNum) {
                return fromUsersNum + "个好友，发来了" + messageNum + "条消息";
            }

            @Override
            public String onSetNotificationTitle(EMMessage emMessage) {
                return null;
            }

            @Override
            public int onSetSmallIcon(EMMessage emMessage) {
                return 0;
            }
        });

        //当用于点击Notification后跳转到mainAty便可
        options.setOnNotificationClickListener(new OnNotificationClickListener() {
            @Override
            public Intent onNotificationClick(EMMessage emMessage) {
//                Intent intent = new Intent(_context, MainActivity.class);
//                EMMessage.ChatType chatType = emMessage.getChatType();
//                if (chatType == EMMessage.ChatType.Chat) { // 单聊信息
//                    intent.putExtra("userId", emMessage.getFrom());
//                    intent.putExtra("chatType", ChatActivity.CHATTYPE_SINGLE);
//                } else { // 群聊信息
//                    // message.getTo()为群聊id
//                    intent.putExtra("groupId", emMessage.getTo());
//                    intent.putExtra("chatType", ChatActivity.CHATTYPE_GROUP);
//                }
//                return intent;
                return null;
            }
        });
    }

    //官方建议
    private String getAppName(int pID) {
        String processName = null;
        ActivityManager am = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
        List l = am.getRunningAppProcesses();
        Iterator i = l.iterator();
        PackageManager pm = this.getPackageManager();
        while (i.hasNext()) {
            ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (i.next());
            try {
                if (info.pid == pID) {
                    CharSequence c = pm.getApplicationLabel(pm.getApplicationInfo(info.processName, PackageManager.GET_META_DATA));
                    processName = info.processName;
                    return processName;
                }
            } catch (Exception e) {
            }
        }
        return processName;
    }

    //创建存储activity的集合，添加和删除功能
    private List<Activity> activities = new LinkedList<>();

    public void addActivity(Activity activity){
        activities.add(activity);
    }

    public void exit(){
        try{
            for (Activity activity : activities){
                if (null != activity)
                    activity.finish();
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            System.exit(0);
        }
    }

    //清理缓存功能

    /**
     * 获取缓存地址
     * @return
     */
    public static String get_CacheDir(){
        //SD卡是否挂载
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED))
            return Environment.getExternalStorageDirectory().toString()
                    + "/Health/Cache";
        else
            return "/System/com.gyxian9.Walk/Walk/Cache";
    }

    /**
     * 获取下载地址
     * @return
     */
    public static String getDownLoadDir() {
        //SD卡是否挂载
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED))
            return Environment.getExternalStorageDirectory().toString()
                    + "/Walk/Download";
        else {
            return "/System/com.gyxian9.Walk/Walk/Download";
        }
    }

    /**
     * 删除文件夹
     * @param filePath
     * @param deleteThisPath
     */
    public static void deleteCacheDirFile(String filePath,boolean deleteThisPath){
        if (!TextUtils.isEmpty(filePath)){
            File file = new File(filePath);
            if (file.isDirectory()){//如果是目录
                File files[] = file.listFiles();
                for (int i = 0; i < files.length; i++) {
                    //再判断子级是否是文件
                    deleteCacheDirFile(files[i].getAbsolutePath(), true);
                }
            }
            //确认删除
            if (deleteThisPath){
                //是文件，删除
                if (!file.isDirectory()){
                    file.delete();
                }else{
                    //空文件夹。删除
                    if (file.listFiles().length == 0){
                        file.delete();
                    }
                }
            }
        }
    }
}
