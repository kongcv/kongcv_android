package com.kongcv.global;


import java.util.List;

public class CurbInfoBean {

	 	private ResultEntity result;

	    public void setResult(ResultEntity result) {
	        this.result = result;
	    }
	    public ResultEntity getResult() {
	        return result;
	    }
	    public static class ResultEntity {
	        private int park_num;
	        private int park_struct;
	        private String address;
	        private int park_count;
	        private int interval_night_month;
	        private int park_hide;
	        private String city;
	        private int interval_light_month;
	        private int hour_meter;
	        private int all_time_day;
	        private int interval_light_day;
	        private String park_description;
	        private int park_space;
	        /**
	         * __type : GeoPoint
	         * latitude : 39.945528
	         * longitude : 116.33648
	         */
	        private double rate;
	        private LocationEntity location;
	        /**
	         * __type : Pointer
	         * className : _User
	         * objectId : 569067be60b2c2974cc69508
	         */
	        
	        private WorkerEntity worker;
	        /**
			 * @return the rate
			 */
			public double getRate() {
				return rate;
			}

			/**
			 * @param rate the rate to set
			 */
			public void setRate(double rate) {
				this.rate = rate;
			}

			private int interval_night_day;
	        private int all_time_month;
	        private String hire_method;
	        private String user;
	        private String objectId;
	        private String createdAt;
	        private String updatedAt;
	        private List<String> hire_price;
	        private List<String> hire_time;

	        public void setPark_num(int park_num) {
	            this.park_num = park_num;
	        }

	        public void setPark_struct(int park_struct) {
	            this.park_struct = park_struct;
	        }

	        public void setAddress(String address) {
	            this.address = address;
	        }

	        public void setPark_count(int park_count) {
	            this.park_count = park_count;
	        }

	        public void setInterval_night_month(int interval_night_month) {
	            this.interval_night_month = interval_night_month;
	        }

	        public void setPark_hide(int park_hide) {
	            this.park_hide = park_hide;
	        }

	        public void setCity(String city) {
	            this.city = city;
	        }

	        public void setInterval_light_month(int interval_light_month) {
	            this.interval_light_month = interval_light_month;
	        }

	        public void setHour_meter(int hour_meter) {
	            this.hour_meter = hour_meter;
	        }

	        public void setAll_time_day(int all_time_day) {
	            this.all_time_day = all_time_day;
	        }

	        public void setInterval_light_day(int interval_light_day) {
	            this.interval_light_day = interval_light_day;
	        }

	        public void setPark_description(String park_description) {
	            this.park_description = park_description;
	        }

	        public void setPark_space(int park_space) {
	            this.park_space = park_space;
	        }

	        public void setLocation(LocationEntity location) {
	            this.location = location;
	        }

	        public void setWorker(WorkerEntity worker) {
	            this.worker = worker;
	        }

	        public void setInterval_night_day(int interval_night_day) {
	            this.interval_night_day = interval_night_day;
	        }

	        public void setAll_time_month(int all_time_month) {
	            this.all_time_month = all_time_month;
	        }

	        public void setHire_method(String hire_method) {
	            this.hire_method = hire_method;
	        }

	        public void setUser(String user) {
	            this.user = user;
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

	        public void setHire_price(List<String> hire_price) {
	            this.hire_price = hire_price;
	        }

	        public void setHire_time(List<String> hire_time) {
	            this.hire_time = hire_time;
	        }

	        public int getPark_num() {
	            return park_num;
	        }

	        public int getPark_struct() {
	            return park_struct;
	        }

	        public String getAddress() {
	            return address;
	        }

	        public int getPark_count() {
	            return park_count;
	        }

	        public int getInterval_night_month() {
	            return interval_night_month;
	        }

	        public int getPark_hide() {
	            return park_hide;
	        }

	        public String getCity() {
	            return city;
	        }

	        public int getInterval_light_month() {
	            return interval_light_month;
	        }

	        public int getHour_meter() {
	            return hour_meter;
	        }

	        public int getAll_time_day() {
	            return all_time_day;
	        }

	        public int getInterval_light_day() {
	            return interval_light_day;
	        }

	        public String getPark_description() {
	            return park_description;
	        }

	        public int getPark_space() {
	            return park_space;
	        }

	        public LocationEntity getLocation() {
	            return location;
	        }

	        public WorkerEntity getWorker() {
	            return worker;
	        }

	        public int getInterval_night_day() {
	            return interval_night_day;
	        }

	        public int getAll_time_month() {
	            return all_time_month;
	        }

	        public String getHire_method() {
	            return hire_method;
	        }

	        public String getUser() {
	            return user;
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

	        public List<String> getHire_price() {
	            return hire_price;
	        }

	        public List<String> getHire_time() {
	            return hire_time;
	        }

	        public static class LocationEntity {
	            private String __type;
	            private double latitude;
	            private double longitude;

	            public void set__type(String __type) {
	                this.__type = __type;
	            }

	            public void setLatitude(double latitude) {
	                this.latitude = latitude;
	            }

	            public void setLongitude(double longitude) {
	                this.longitude = longitude;
	            }

	            public String get__type() {
	                return __type;
	            }

	            public double getLatitude() {
	                return latitude;
	            }

	            public double getLongitude() {
	                return longitude;
	            }

				/* (non-Javadoc)
				 * @see java.lang.Object#toString()
				 */
				@Override
				public String toString() {
					return "LocationEntity [__type=" + __type + ", latitude="
							+ latitude + ", longitude=" + longitude + "]";
				}
	            
	        }

	        public static class WorkerEntity {
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