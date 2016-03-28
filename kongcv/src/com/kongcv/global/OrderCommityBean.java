package com.kongcv.global;

import java.util.List;

import android.graphics.Bitmap;

public class OrderCommityBean {
	/**
	 * money : 100 coupon : 0 hire_end :
	 * {"__type":"Date","iso":"2015-09-30T15:00:00.000Z"} handsel_state : 0
	 * trade_state : 1 balance : 0 handsel : 0 pay_tool : alipy action : 0
	 * hire_start : {"__type":"Date","iso":"2015-09-30T15:00:00.000Z"}
	 * extra_flag : 1 price : 10 user :
	 * {"__type":"Pointer","className":"_User","objectId"
	 * :"5629973860b28da5c9b6ca16"} pay_state : 2 hire_method :
	 * {"picture":{"__type":"File","id":"564db1a100b0ee7f59dcc12d","name":
	 * "Rectangle 214 Copy 5.png"
	 * ,"url":"http://ac-ATcs8k4n.clouddn.com/58711ad1c423e06a.png"
	 * },"park_type":
	 * ["5620a6dc60b27457e84bb21d","5620a6d060b27457e84bb0fe"],"method"
	 * :"全时/天","hire_type":0,"objectId":"561f4cb460b22ed7ca73ab6c","createdAt":
	 * "2015-10-15T06:50:28.044Z","updatedAt":"2015-11-19T11:25:21.753Z"}
	 * park_community :
	 * {"address":"聚龙花园8a&88888","hire_end":{"__type":"Date","iso"
	 * :"2015-10-17T10:00:00.000Z"
	 * },"long_time":false,"park_height":0,"park_hide"
	 * :0,"city":"beijing","normal"
	 * :true,"hire_price":["10","10","10","10","10","10"
	 * ],"gate_card":"xxxxx","hire_start"
	 * :{"__type":"Date","iso":"2015-10-17T00:00:00.000Z"
	 * },"hire_method":[{"__type"
	 * :"Pointer","className":"kongcv_hire_method","objectId"
	 * :"561f4cb460b22ed7ca73ab6c"
	 * },{"__type":"Pointer","className":"kongcv_hire_method"
	 * ,"objectId":"56373dd160b294bc66269806"
	 * },{"__type":"Pointer","className":"kongcv_hire_method"
	 * ,"objectId":"56346a4f60b2260e5d7c7800"
	 * },{"__type":"Pointer","className":"kongcv_hire_method"
	 * ,"objectId":"561f4ca660b2ce30d1f3b6e2"
	 * },{"__type":"Pointer","className":"kongcv_hire_method"
	 * ,"objectId":"561f4cde60b27457e8330b57"
	 * },{"__type":"Pointer","className":"kongcv_hire_method"
	 * ,"objectId":"561f4cd400b08664c528889f"
	 * }],"park_space":1,"hire_time":["0","0"
	 * ,"9:00 - 20:00","1:00 - 8:00","9:00 - 20:00"
	 * ,"22:00 - 8:00"],"location":{"__type"
	 * :"GeoPoint","latitude":39.930686,"longitude"
	 * :116.440887},"struct":0,"user"
	 * :{"__type":"Pointer","className":"_User","objectId"
	 * :"5621f512ddb2dd000ac16189"
	 * },"objectId":"5624964860b27db44f08fd83","createdAt"
	 * :"2015-10-19T07:05:44.748Z","updatedAt":"2015-12-31T03:44:58.593Z"}
	 * objectId : 56723baaddb2084a55637cc5 createdAt : 2015-12-17T04:35:54.409Z
	 * updatedAt : 2015-12-29T09:17:17.696Z
	 */

	private List<ResultEntity> result;

	public void setResult(List<ResultEntity> result) {
		this.result = result;
	}

	public List<ResultEntity> getResult() {
		return result;
	}

	public static class ResultEntity {
		private int money;
		private int coupon;
		/**
		 * __type : Date iso : 2015-09-30T15:00:00.000Z
		 */
		private HireEndEntity hire_end;
		private int handsel_state;
		private int trade_state;
		private int balance;
		private int handsel;
		private String pay_tool;
		private int action;
		/**
		 * __type : Date iso : 2015-09-30T15:00:00.000Z
		 */
		private HireStartEntity hire_start;
		private String extra_flag;
	//	private int price;
		private double price;
		/**
		 * __type : Pointer className : _User objectId :
		 * 5629973860b28da5c9b6ca16
		 */
		private UserEntity users;
		private int pay_state;
		private String hire_method;
		private String park_community;
		private String park_curb;
		private UserList user;
		private String method;

		public String getPark_curb() {
			return park_curb;
		}

		public void setPark_curb(String park_curb) {
			this.park_curb = park_curb;
		}

		private String objectId;
		private String createdAt;
		private String updatedAt;

		public UserList getUser() {
			return user;
		}

		public void setUser(UserList user) {
			this.user = user;
		}

		public void setMoney(int money) {
			this.money = money;
		}

		public void setCoupon(int coupon) {
			this.coupon = coupon;
		}

		public void setHire_end(HireEndEntity hire_end) {
			this.hire_end = hire_end;
		}

		public void setHandsel_state(int handsel_state) {
			this.handsel_state = handsel_state;
		}

		public void setBalance(int balance) {
			this.balance = balance;
		}

		public void setHandsel(int handsel) {
			this.handsel = handsel;
		}

		public void setPay_tool(String pay_tool) {
			this.pay_tool = pay_tool;
		}

		public void setAction(int action) {
			this.action = action;
		}

		public void setHire_start(HireStartEntity hire_start) {
			this.hire_start = hire_start;
		}

		public void setExtra_flag(String extra_flag) {
			this.extra_flag = extra_flag;
		}

		/*public void setPrice(int price) {
			this.price = price;
		}*/
		public void setPrice(double price) {
			this.price = price;
		}

		public void setUsers(UserEntity users) {
			this.users = users;
		}

		public void setPay_state(int pay_state) {
			this.pay_state = pay_state;
		}

		public void setHire_method(String hire_method) {
			this.hire_method = hire_method;
		}

		public void setMethod(String method) {
			this.method = method;
		}

		public void setPark_community(String park_community) {
			this.park_community = park_community;
		}

		public void setObjectId(String objectId) {
			this.objectId = objectId;
		}

		public void setCreatedAt(String createdAt) {
			this.createdAt = createdAt;
		}

		public void setUpdatedAt(String updatedAt) {
			this.updatedAt = updatedAt;
		}

		public int getMoney() {
			return money;
		}

		public int getCoupon() {
			return coupon;
		}

		public HireEndEntity getHire_end() {
			return hire_end;
		}

		public int getHandsel_state() {
			return handsel_state;
		}

		public int getTrade_state() {
			return trade_state;
		}

		public void setTrade_state(int trade_state) {
			this.trade_state = trade_state;
		}

		public int getBalance() {
			return balance;
		}

		public int getHandsel() {
			return handsel;
		}

		public String getPay_tool() {
			return pay_tool;
		}

		public int getAction() {
			return action;
		}

		public HireStartEntity getHire_start() {
			return hire_start;
		}

		public String getExtra_flag() {
			return extra_flag;
		}

		/*public int getPrice() {
			return price;
		}*/
		public double getPrice() {
			return price;
		}

		public UserEntity getUsers() {
			return users;
		}

		public int getPay_state() {
			return pay_state;
		}

		public String getHire_method() {
			return hire_method;
		}

		public String getMethod() {
			return method;
		}

		public String getPark_community() {
			return park_community;
		}

		public String getObjectId() {
			return objectId;
		}

		public String getCreatedAt() {
			return createdAt;
		}

		public String getUpdatedAt() {
			return updatedAt;
		}

		public static class HireEndEntity {
			private String __type;
			private String iso;

			public void set__type(String __type) {
				this.__type = __type;
			}

			public void setIso(String iso) {
				this.iso = iso;
			}

			public String get__type() {
				return __type;
			}

			public String getIso() {
				return iso;
			}
		}

		public static class HireStartEntity {
			private String __type;
			private String iso;

			public void set__type(String __type) {
				this.__type = __type;
			}

			public void setIso(String iso) {
				this.iso = iso;
			}

			public String get__type() {
				return __type;
			}

			public String getIso() {
				return iso;
			}
		}

		public static class UserList {

			private String username;
			private String mobilePhoneNumber;
			private Bitmap bitMap;

			public String getUsername() {
				return username;
			}

			public Bitmap getBitMap() {
				return bitMap;
			}

			public void setBitMap(Bitmap bitMap) {
				this.bitMap = bitMap;
			}

			public void setUsername(String username) {
				this.username = username;
			}

			public String getMobilePhoneNumber() {
				return mobilePhoneNumber;
			}

			public void setMobilePhoneNumber(String mobilePhoneNumber) {
				this.mobilePhoneNumber = mobilePhoneNumber;
			}
		}

		public static class UserEntity {
			private String __type;
			private String className;
			private String objectId;

			public void set__type(String __type) {
				this.__type = __type;
			}

			public void setClassName(String className) {
				this.className = className;
			}

			public void setObjectId(String objectId) {
				this.objectId = objectId;
			}

			public String get__type() {
				return __type;
			}

			public String getClassName() {
				return className;
			}

			public String getObjectId() {
				return objectId;
			}
		}
	}
}
