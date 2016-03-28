package com.kongcv.global;

import java.util.List;

/**
 * Created by kcw on 2015/12/28.
 */
public class DetailBean {
    private ResultEntity result;
    public void setResult(ResultEntity result) {
        this.result = result;
    }
    public ResultEntity getResult() {
        return result;
    }
    public static class ResultEntity {
        private String address;
        private HireEndEntity hire_end;
        private boolean long_time;
        private int park_height;
        private int park_hide;
        private String city;
        private boolean normal;
        private String gate_card;
        private HireStartEntity hire_start;
        private int park_space;
        private LocationEntity location;
        private PropertyEntity property;
        
        private int park_struct;
        private String hire_method;
        private String user;
        
        private String objectId;
        private String createdAt;
        private String updatedAt;
        private String park_area;
        private String park_description;
        private String tail_num;
        private List<String> no_hire;
        private List<String> hire_price;
        private List<String> hire_time;

        
        /**
		 * @return the property
		 */
		public PropertyEntity getProperty() {
			return property;
		}

		/**
		 * @param property the property to set
		 */
		public void setProperty(PropertyEntity property) {
			this.property = property;
		}

		/**
		 * @return the park_struct
		 */
		public int getPark_struct() {
			return park_struct;
		}

		/**
		 * @param park_struct the park_struct to set
		 */
		public void setPark_struct(int park_struct) {
			this.park_struct = park_struct;
		}

		/**
		 * @return the tail_num
		 */
		public String getTail_num() {
			return tail_num;
		}

		/**
		 * @param tail_num the tail_num to set
		 */
		public void setTail_num(String tail_num) {
			this.tail_num = tail_num;
		}

		/**
		 * @return the park_area
		 */
		public String getPark_area() {
			return park_area;
		}

		/**
		 * @param park_area the park_area to set
		 */
		public void setPark_area(String park_area) {
			this.park_area = park_area;
		}

		/**
		 * @return the park_description
		 */
		public String getPark_description() {
			return park_description;
		}

		/**
		 * @param park_description the park_description to set
		 */
		public void setPark_description(String park_description) {
			this.park_description = park_description;
		}

		public void setAddress(String address) {
            this.address = address;
        }

        public void setHire_end(HireEndEntity hire_end) {
            this.hire_end = hire_end;
        }

        public void setLong_time(boolean long_time) {
            this.long_time = long_time;
        }

        public void setPark_height(int park_height) {
            this.park_height = park_height;
        }

        public void setPark_hide(int park_hide) {
            this.park_hide = park_hide;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public void setNormal(boolean normal) {
            this.normal = normal;
        }

        public void setGate_card(String gate_card) {
            this.gate_card = gate_card;
        }

        public void setHire_start(HireStartEntity hire_start) {
            this.hire_start = hire_start;
        }

        public void setPark_space(int park_space) {
            this.park_space = park_space;
        }

        public void setLocation(LocationEntity location) {
            this.location = location;
        }
        

        public void setStruct(int struct) {
            this.park_struct = struct;
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

        public void setNo_hire(List<String> no_hire) {
            this.no_hire = no_hire;
        }

        public void setHire_price(List<String> hire_price) {
            this.hire_price = hire_price;
        }

        public void setHire_time(List<String> hire_time) {
            this.hire_time = hire_time;
        }

        public String getAddress() {
            return address;
        }

        public HireEndEntity getHire_end() {
            return hire_end;
        }

        public boolean isLong_time() {
            return long_time;
        }

        public int getPark_height() {
            return park_height;
        }

        public int getPark_hide() {
            return park_hide;
        }

        public String getCity() {
            return city;
        }

        public boolean isNormal() {
            return normal;
        }

        public String getGate_card() {
            return gate_card;
        }

        public HireStartEntity getHire_start() {
            return hire_start;
        }

        public int getPark_space() {
            return park_space;
        }

        public LocationEntity getLocation() {
            return location;
        }

        public int getStruct() {
            return park_struct;
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

        public List<String> getNo_hire() {
            return no_hire;
        }

        public List<String> getHire_price() {
            return hire_price;
        }

        public List<String> getHire_time() {
            return hire_time;
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
        }
        public static class PropertyEntity {
            private String __type;
            private String className;
            private String objectId;
			/**
			 * @return the __type
			 */
			public String get__type() {
				return __type;
			}
			/**
			 * @param __type the __type to set
			 */
			public void set__type(String __type) {
				this.__type = __type;
			}
			/**
			 * @return the className
			 */
			public String getClassName() {
				return className;
			}
			/**
			 * @param className the className to set
			 */
			public void setClassName(String className) {
				this.className = className;
			}
			/**
			 * @return the objectId
			 */
			public String getObjectId() {
				return objectId;
			}
			/**
			 * @param objectId the objectId to set
			 */
			public void setObjectId(String objectId) {
				this.objectId = objectId;
			}

           
        }
    }
}
