package com.kongcv.global;

import java.io.Serializable;
import java.util.List;

public class CarBean implements Serializable {
	private static final long serialVersionUID = 1195622473389553092L;

	/**
	 * iso : 2015-10-17T00:00:00.000Z __type : Date
	 */

	private HireEndEntity hire_end;
	/**
	 * __type : GeoPoint longitude : 116.3213 latitude : 39.89497
	 */

	private LocationEntity location;
	/**
	 * hire_time : ["0"] hire_end :
	 * {"iso":"2015-10-17T00:00:00.000Z","__type":"Date"} location :
	 * {"__type":"GeoPoint","longitude":116.3213,"latitude":39.89497} normal :
	 * false tail_num : 2 interval_light_month : 0 city : beijing
	 * interval_night_day : 0 gate_card : 121212122 all_time_day : 0
	 * interval_night_month : 0 createdAt : 2015-10-19T11:02:18.207Z objectId :
	 * 5624cdba00b07c4da7231928 hire_method :
	 * [{"objectId":"56373dd160b294bc66269806"}] struct : 1 no_hire : ["二","六"]
	 * park_height : 0 hire_price : ["121"] all_time_month : 0 park_hide : 1
	 * hire_start : {"iso":"2015-10-17T00:00:00.000Z","__type":"Date"}
	 * park_description : 1212321321 updatedAt : 2016-01-09T07:13:59.045Z
	 * interval_light_day : 25 address : 东四十条&asass park_area : 0 long_time :
	 * false park_space : 1 user : {"objectId":"5621f512ddb2dd000ac16189"}
	 */

	private boolean normal;
	private String tail_num;
	private int interval_light_month;
	private String city;
	private int interval_night_day;
	private String gate_card;
	private int all_time_day;
	private int interval_night_month;
	private String createdAt;
	private String objectId;
	private String hire_method;
	private int struct;
	private int park_height;
	private int all_time_month;
	private int park_hide;
	/**
	 * iso : 2015-10-17T00:00:00.000Z __type : Date
	 */

	private HireStartEntity hire_start;
	private String park_description;
	private String updatedAt;
	private int interval_light_day;
	private String address;
	private int park_area;
	private boolean long_time;
	private int park_space;
	private String user;
	private List<String> hire_time;
	private List<String> no_hire;
	private List<String> hire_price;
	private int park_struct; 
	

	public int getPark_struct() {
		return park_struct;
	}

	public void setPark_struct(int park_struct) {
		this.park_struct = park_struct;
	}

	public void setHire_end(HireEndEntity hire_end) {
		this.hire_end = hire_end;
	}

	public void setLocation(LocationEntity location) {
		this.location = location;
	}

	public void setNormal(boolean normal) {
		this.normal = normal;
	}

	public void setTail_num(String tail_num) {
		this.tail_num = tail_num;
	}

	public void setInterval_light_month(int interval_light_month) {
		this.interval_light_month = interval_light_month;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public void setInterval_night_day(int interval_night_day) {
		this.interval_night_day = interval_night_day;
	}

	public void setGate_card(String gate_card) {
		this.gate_card = gate_card;
	}

	public void setAll_time_day(int all_time_day) {
		this.all_time_day = all_time_day;
	}

	public void setInterval_night_month(int interval_night_month) {
		this.interval_night_month = interval_night_month;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}

	public void setHire_method(String hire_method) {
		this.hire_method = hire_method;
	}

	public void setStruct(int struct) {
		this.struct = struct;
	}

	public void setPark_height(int park_height) {
		this.park_height = park_height;
	}

	public void setAll_time_month(int all_time_month) {
		this.all_time_month = all_time_month;
	}

	public void setHire_start(HireStartEntity hire_start) {
		this.hire_start = hire_start;
	}

	public void setPark_description(String park_description) {
		this.park_description = park_description;
	}

	public void setUpdatedAt(String updatedAt) {
		this.updatedAt = updatedAt;
	}

	public void setInterval_light_day(int interval_light_day) {
		this.interval_light_day = interval_light_day;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public void setPark_area(int park_area) {
		this.park_area = park_area;
	}

	public void setLong_time(boolean long_time) {
		this.long_time = long_time;
	}

	public void setPark_space(int park_space) {
		this.park_space = park_space;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public void setHire_time(List<String> hire_time) {
		this.hire_time = hire_time;
	}

	public void setNo_hire(List<String> no_hire) {
		this.no_hire = no_hire;
	}

	public void setHire_price(List<String> hire_price) {
		this.hire_price = hire_price;
	}

	public HireEndEntity getHire_end() {
		return hire_end;
	}

	public LocationEntity getLocation() {
		return location;
	}

	public boolean isNormal() {
		return normal;
	}

	public String getTail_num() {
		return tail_num;
	}

	public int getInterval_light_month() {
		return interval_light_month;
	}

	public String getCity() {
		return city;
	}

	public int getInterval_night_day() {
		return interval_night_day;
	}

	public String getGate_card() {
		return gate_card;
	}

	public int getAll_time_day() {
		return all_time_day;
	}

	public int getInterval_night_month() {
		return interval_night_month;
	}

	public String getCreatedAt() {
		return createdAt;
	}

	public String getObjectId() {
		return objectId;
	}

	public String getHire_method() {
		return hire_method;
	}

	public int getStruct() {
		return struct;
	}

	public int getPark_height() {
		return park_height;
	}

	public int getAll_time_month() {
		return all_time_month;
	}

	public int getPark_hide() {
		return park_hide;
	}

	public void setPark_hide(int park_hide) {
		this.park_hide = park_hide;
	}

	public HireStartEntity getHire_start() {
		return hire_start;
	}

	public String getPark_description() {
		return park_description;
	}

	public String getUpdatedAt() {
		return updatedAt;
	}

	public int getInterval_light_day() {
		return interval_light_day;
	}

	public String getAddress() {
		return address;
	}

	public int getPark_area() {
		return park_area;
	}

	public boolean isLong_time() {
		return long_time;
	}

	public int getPark_space() {
		return park_space;
	}

	public String getUser() {
		return user;
	}

	public List<String> getHire_time() {
		return hire_time;
	}

	public List<String> getNo_hire() {
		return no_hire;
	}

	public List<String> getHire_price() {
		return hire_price;
	}

	public static class HireEndEntity implements Serializable {
		private String iso;
		private String __type;

		public void setIso(String iso) {
			this.iso = iso;
		}

		public void set__type(String __type) {
			this.__type = __type;
		}

		public String getIso() {
			return iso;
		}

		public String get__type() {
			return __type;
		}
	}

	public static class LocationEntity {
		private String __type;
		private double longitude;
		private double latitude;

		public void set__type(String __type) {
			this.__type = __type;
		}

		public void setLongitude(double longitude) {
			this.longitude = longitude;
		}

		public void setLatitude(double latitude) {
			this.latitude = latitude;
		}

		public String get__type() {
			return __type;
		}

		public double getLongitude() {
			return longitude;
		}

		public double getLatitude() {
			return latitude;
		}
	}

	public static class HireStartEntity implements Serializable {
		private String iso;
		private String __type;

		public void setIso(String iso) {
			this.iso = iso;
		}

		public void set__type(String __type) {
			this.__type = __type;
		}

		public String getIso() {
			return iso;
		}

		public String get__type() {
			return __type;
		}
	}
}
