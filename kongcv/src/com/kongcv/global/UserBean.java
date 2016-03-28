package com.kongcv.global;

public class UserBean {

	private RoleEntity role;
	private CouponEntity coupon;
	private String device_type;
	private String username;
	private String device_token;
	private String license_plate;//加入车牌号
	private int hide;
	private boolean emailVerified;
	private String mobilePhoneNumber;
	private ImageEntity image;
	private String version;
	private Object authData;
	private boolean mobilePhoneVerified;
	private String objectId;
	private String createdAt;
	private String updatedAt;
	
	public String getLicense_plate() {
		return license_plate;
	}
	public void setLicense_plate(String license_plate) {
		this.license_plate = license_plate;
	}

	public void setRole(RoleEntity role) {
		this.role = role;
	}

	public void setCoupon(CouponEntity coupon) {
		this.coupon = coupon;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setHide(int hide) {
		this.hide = hide;
	}

	public void setEmailVerified(boolean emailVerified) {
		this.emailVerified = emailVerified;
	}

	public void setMobilePhoneNumber(String mobilePhoneNumber) {
		this.mobilePhoneNumber = mobilePhoneNumber;
	}

	public void setAuthData(Object authData) {
		this.authData = authData;
	}

	public void setMobilePhoneVerified(boolean mobilePhoneVerified) {
		this.mobilePhoneVerified = mobilePhoneVerified;
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

	public RoleEntity getRole() {
		return role;
	}

	public CouponEntity getCoupon() {
		return coupon;
	}

	public String getUsername() {
		return username;
	}

	public int getHide() {
		return hide;
	}

	public boolean isEmailVerified() {
		return emailVerified;
	}

	public String getMobilePhoneNumber() {
		return mobilePhoneNumber;
	}

	public Object getAuthData() {
		return authData;
	}

	public boolean isMobilePhoneVerified() {
		return mobilePhoneVerified;
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
	/**
	 * @return the device_type
	 */
	public String getDevice_type() {
		return device_type;
	}

	/**
	 * @param device_type the device_type to set
	 */
	public void setDevice_type(String device_type) {
		this.device_type = device_type;
	}

	/**
	 * @return the device_token
	 */
	public String getDevice_token() {
		return device_token;
	}

	/**
	 * @param device_token the device_token to set
	 */
	public void setDevice_token(String device_token) {
		this.device_token = device_token;
	}

	/**
	 * @return the image
	 */
	public ImageEntity getImage() {
		return image;
	}

	/**
	 * @param image the image to set
	 */
	public void setImage(ImageEntity image) {
		this.image = image;
	}

	/**
	 * @return the version
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * @param version the version to set
	 */
	public void setVersion(String version) {
		this.version = version;
	}




	public static class RoleEntity {
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

	public static class CouponEntity {
		private String __type;
		private String className;

		public void set__type(String __type) {
			this.__type = __type;
		}

		public void setClassName(String className) {
			this.className = className;
		}

		public String get__type() {
			return __type;
		}

		public String getClassName() {
			return className;
		}
	}
	public static class ImageEntity {
		private String __type;
		private String id;
		private String name;
		private String url;
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
		 * @return the id
		 */
		public String getId() {
			return id;
		}
		/**
		 * @param id the id to set
		 */
		public void setId(String id) {
			this.id = id;
		}
		/**
		 * @return the name
		 */
		public String getName() {
			return name;
		}
		/**
		 * @param name the name to set
		 */
		public void setName(String name) {
			this.name = name;
		}
		/**
		 * @return the url
		 */
		public String getUrl() {
			return url;
		}
		/**
		 * @param url the url to set
		 */
		public void setUrl(String url) {
			this.url = url;
		}
	}
	@Override
	public String toString() {
		return "UserBean [role=" + role + ", coupon=" + coupon
				+ ", device_type=" + device_type + ", username=" + username
				+ ", device_token=" + device_token + ", license_plate="
				+ license_plate + ", hide=" + hide + ", emailVerified="
				+ emailVerified + ", mobilePhoneNumber=" + mobilePhoneNumber
				+ ", image=" + image + ", version=" + version + ", authData="
				+ authData + ", mobilePhoneVerified=" + mobilePhoneVerified
				+ ", objectId=" + objectId + ", createdAt=" + createdAt
				+ ", updatedAt=" + updatedAt + "]";
	}
}
	
