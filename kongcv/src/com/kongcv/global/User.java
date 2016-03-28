
package com.kongcv.global;

public class User {
    private Coupon coupon;
    private String username;
    private int hide;
    private ImageUser image;
    private boolean emailverified;
    private String mobilePhoneNumber;
    private boolean mobilePhoneVerified;
    private String objectId;
    private String createdAt;
    private String updatedAt;
    public void setCoupon(Coupon coupon) {
         this.coupon = coupon;
     }
     public Coupon getCoupon() {
         return coupon;
     }

    public void setUsername(String username) {
         this.username = username;
     }
     public String getUsername() {
         return username;
     }

    public void setHide(int hide) {
         this.hide = hide;
     }
     public int getHide() {
         return hide;
     }

    public void setEmailverified(boolean emailverified) {
         this.emailverified = emailverified;
     }
     public boolean getEmailverified() {
         return emailverified;
     }

    public void setMobilephonenumber(String mobilephonenumber) {
         this.mobilePhoneNumber = mobilephonenumber;
     }
     public String getMobilephonenumber() {
         return mobilePhoneNumber;
     }

    public void setMobilephoneverified(boolean mobilephoneverified) {
         this.mobilePhoneVerified = mobilephoneverified;
     }
     public boolean getMobilephoneverified() {
         return mobilePhoneVerified;
     }

    public void setObjectid(String objectid) {
         this.objectId = objectid;
     }
     public String getObjectid() {
         return objectId;
     }

    public void setCreatedat(String createdat) {
         this.createdAt = createdat;
     }
     public String getCreatedat() {
         return createdAt;
     }

    public void setUpdatedat(String updatedat) {
         this.updatedAt = updatedat;
     }
     public String getUpdatedat() {
         return updatedAt;
     }
     
	public ImageUser getImage() {
		return image;
	}
	public void setImage(ImageUser image) {
		this.image = image;
	}

	public class ImageUser {
    		private String url;
    		private String id;
    		private String __type;
    		private String name;

    		public String getUrl() {
    			return this.url;
    		}

    		public void setUrl(String url) {
    			this.url = url;
    		}

    		public String getId() {
    			return this.id;
    		}

    		public void setId(String id) {
    			this.id = id;
    		}

    		public String get__type() {
    			return this.__type;
    		}

    		public void set__type(String __type) {
    			this.__type = __type;
    		}

    		public String getName() {
    			return this.name;
    		}

    		public void setName(String name) {
    			this.name = name;
    		}


    		public String toString() {
    			return "Image [url = " + url + ", id = " + id + ", __type = " + __type + ", name = " + name + "]";
    		}
    	}

}