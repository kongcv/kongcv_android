package com.kongcv.global;


import java.util.HashMap;

import okhttp3.Headers;
import okhttp3.MediaType;

/**
 * 全局信息
 * 
 * @author kcw
 * 
 */
public class Information {

	/**
	 * 服务端URL地址
	 */
	public static final String SERVER_URL = "https://api.leancloud.cn/1.1/functions/";
	
	/**
	 * 闪屏页时间
	 */
	public static final int time = 8000;
	/**
	 * 填写从短信SDK应用后台注册得到的APPKEY APPSECRET
	 */
	public static final String APP_ID = "ATcs8k4nK1f2VFd69QtNHcuN";
	public static final String APP_KEY = "bs5tH7T0alfJyepntY5Npy37";
	
	public static Headers getHeaders(){
		HashMap<String,String> arg0=new HashMap<String, String>();
		arg0.put("X-LC-Id", APP_ID);
		arg0.put("X-LC-Key", APP_KEY);
		Headers headers=Headers.of(arg0);
		return headers;
	}
	public static MediaType MEDIA_TYPE_MARKDOWN= MediaType.parse("application/json;charset=utf-8");
	/**
	 * 得到短信验证码
	 */
	public static final String KONGCV_GET_SMSCODE = SERVER_URL+ "kongcv_get_smscode";
	/**
	 * 手机号和短信验证码一键注册
	 */
	public static final String KONGCV_SIGNUP = SERVER_URL + "kongcv_signup";
	/**
	 * 得到轮播图
	 */
	public static final String KONGCV_GET_ADVERTISE = SERVER_URL+ "kongcv_get_advertise";
	/**
	 * 获取停车类型 即道路停还是社区停车
	 */
	public static final String KONGCV_GET_PARK_TYPE = SERVER_URL+ "kongcv_get_park_type";
	/**
	 * 得到停车类型的出租方法 即点击出现社 出现 按钮
	 */
	public static final String KONGCV_GET_HIRE_METHOD = SERVER_URL+ "kongcv_get_hire_method";
	/**
	 * 插入停车位数据 即是否有门禁卡 等详细信息
	 */
	public static final String KONGCV_INSERT_PARKDATA = SERVER_URL+ "kongcv_insert_parkdata";
	/**
	 * 车位搜索
	 */
	public static final String KONGCV_LOCATION_SEARCH = SERVER_URL+ "kongcv_location_search";
	/**
	 * 获取车位详细信息
	 */
	public static final String KONGCV_GET_PARK_INFO = SERVER_URL+ "kongcv_get_park_info";
	/**
	 * 插入交易数据
	 */
	public static final String KONGCV_INSERT_TRADEDATA = SERVER_URL+ "kongcv_insert_tradedata";
	public static final String KONGCV_INSERT_TRADE_BILLDATA = SERVER_URL+ "kongcv_insert_trade_billdata";
	/**
	 * 插入评论数据
	 */
	public static final String KONGCV_INSERT_COMMENT = SERVER_URL+ "kongcv_insert_comment";

	/**
	 * 得到评论数据
	 */
	public static final String KONGCV_GET_COMMENT = SERVER_URL+ "kongcv_get_comment";
	/**
	 * 插入提现数据
	 */
	public static final String KONGCV_INSERT_WITHDRAW_DEPOSIT = SERVER_URL+ "kongcv_insert_withdraw_deposit";

	public static String TAG = "kongcv...";
	/**
	 * 发送短信消息
	 */
	public static final String KONGCV_PUSH_SMSINFO = SERVER_URL+ "kongcv_push_smsinfo";
	/**
	 * 发送JPsh推送通知
	 */
	public static final String KONGCV_JPUSH_MESSAGE_P2P = SERVER_URL+ "kongcv_jpush_message_p2p";
	/**
	 * 验证白名单
	 */
	public static final String KONGCV_QUERY_WHITE_LIST= SERVER_URL+ "kongcv_query_white_list";
    /**
     * 得到银行信息
     */
    public static final String KONGCV_GET_BANK=SERVER_URL+"kongcv_get_bank";
     /**
      * 得到停车位列表
      */
    public static final String KONGCV_GET_PARK_LIST=SERVER_URL+"kongcv_get_park_list";
    /**
     * 插入反馈信息
     */
    public static final String KONGCV_INSERT_FEEDBACK=SERVER_URL+"kongcv_insert_feedback";
    /**
     * 验证账户短信验证码
     */
    public static final String KONGCV_VERIFY_MOBILE=SERVER_URL+"kongcv_verify_mobile";
    /**
     * 插入或更新钱包信息
     */
    public static final String KONGCV_PUT_PURSE=SERVER_URL+"kongcv_put_purse";
    /**
     * 得到钱包数据
     */
    public static final String KONGCV_GET_PURSE=SERVER_URL+"kongcv_get_purse";
    /**
     * 得到提现数据
     */
    public static final String  KONGCV_GET_WITHDRAW_DEPOSIT=SERVER_URL+"kongcv_get_withdraw_deposit";
    /**
     * 得到通知列表
     */
    public static final String KONGCV_GET_PUSHMESSAGE_LIST=SERVER_URL+"kongcv_get_pushmessage_list";
    
    /**
     * 上传头像
     */
    public static final String KONGCV_UPLOAD_IMAGE=SERVER_URL+"kongcv_upload_image";
    /**
     * 更新用户信息
     */
    public static final String KONGCV_PUT_USERINFO=SERVER_URL+"kongcv_put_userinfo";
    /**
     * 支付测试接口
     */
//    public static final String PINGPP_PAY="http://www.kongcv.com/pingpp_pay";
//    public static final String PINGPP_PAY="http://kongcv_test.avosapps.com/pingpp_pay";
    public static final String PINGPP_PAY="https://kongcv_test.avosapps.com/pingpp_pay";
    
    
    /**
     * 得到用户信息
     */
    public static final String KONGCV_GET_USERINFO=SERVER_URL+"kongcv_get_userinfo";
    
    /**
     * 得到交易单详细信息
     */
    public static final String  KONGCV_GET_TRADE_LIST=SERVER_URL+"kongcv_get_trade_list";
    /**
	 * 验证钱包密码
	 */
    public static final String KONGCV_VERIFY_PURSE_PASSWD=SERVER_URL+"kongcv_verify_purse_passwd";
    /**
     * 改变推送消息状态
     */
    public static final String KONGCV_CHANGE_PUSHMESSAGE_STATE=SERVER_URL+"kongcv_change_pushmessage_state";
    /**
     * 插入通知接受数据
     */
    public static final String KONGCV_INSERT_ACCEPT=SERVER_URL+"kongcv_insert_accept";
    /**
     * 得到用户租用月列表
     */
    public static final String KONGCV_GET_TRADE_DATE_LIST=SERVER_URL+"kongcv_get_trade_date_list";
    /**
     * 得公司信息(关于我们）
     */
    public static final String KONGCV_GET_COMPANY_INFO=SERVER_URL+"kongcv_get_company_info";
	/**
	 *设置停车位隐藏or显示 
	 */
    public static final String KONGCV_PUT_PARK_HIDE=SERVER_URL+"kongcv_put_park_hide";
    
    /**
     * 用户版本更新
     */
    public static final String KONGCV_GET_ANDROID_VERSION=SERVER_URL+"kongcv_get_android_version";
}
    
    
    
    
    
    
    
    
    
    
    
    






















