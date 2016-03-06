package com.gyxian9.hx_wechat;

/**
 * Created by gyxian9 on 2016/3/6.
 */
public class Constant {

    //App
    public static final String PACKAGENAME = "com.gyxian9.hx_wechat";
    //Splash
    public static final String RUNCOUNT = "runCount";
    public static final String LOGINSTATE = "LoginState";
    public static final String USERID = "userId";
    public static final String PASSWD = "passwd";

    //NetClient
    public static final String NET_ERROR = "网络错误，请稍后再试！";

    //Login
    public static final String TITLE = "title";
    public static final String URL = "url";
    public static final String NAME = "name";
    public static final String USERINFO = "userInfo";

    //BaseJsonRes
    // JSON status
    public static final String Info = "info";
    public static final String Value = "data";
    public static final String Result = "status";

    // 主机地址
    // public static String IP = "http://wechatjuns.sinaapp.com/";
    // String MAIN_ENGINE = "http://10.16.16.79/wechat/index.php/mobile/";
    public static final String MAIN_ENGINE = "http://wechatjuns.sinaapp.com/index.php/";

    // 发送验证码 codeType 1注册 2修改密码
    public static final String SendCodeURL = "";
    // 用户注册
    public static final String RegistURL = MAIN_ENGINE + "user/regigter";
    // 用户登录
    public static final String Login_URL = MAIN_ENGINE + "user/login";
    // 更新用户信息
    public static final String UpdateInfoURL = MAIN_ENGINE + "user/update_userinfo";
    // 获取用户信息
    public static final String getUserInfoURL = MAIN_ENGINE + "user/get_user_list";
    // 检查版本
    public static final String getVersionURL = MAIN_ENGINE + "020";
    // 更新用户信息
    public static final String updateUserInfoURL = MAIN_ENGINE + "004";
    // 我的群组
    public static final String getGroupListURL = MAIN_ENGINE + "group/get_group_list";
    // 反馈
    public static final String FeedbackURL = MAIN_ENGINE + "019";
    // 获取群组信息
    public static final String getGroupInfoURL = MAIN_ENGINE + "027";

    // 查询所有好友033
    public static final String getContactFriendURL = MAIN_ENGINE + "user/get_contact_list";
    // 举报018
    public static final String JuBaoURL = MAIN_ENGINE + "018";
    // 加入群组
    public static final String addGroupURL = MAIN_ENGINE + "031";
    // 退出群组
    public static final String exitGroupURL = MAIN_ENGINE + "029";
    // 新建群组
    public static final String newGroupURL = MAIN_ENGINE + "group/add_group";
}
