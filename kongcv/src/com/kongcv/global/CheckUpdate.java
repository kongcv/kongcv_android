package com.kongcv.global;

import java.io.Serializable;
import java.util.List;

public class CheckUpdate implements Serializable {
	private static final long serialVersionUID = 40059892L;
	private List<Result> result;

	public void setResult(List<Result> result) {
		this.result = result;
	}

	public List<Result> getResult() {
		return result;
	}

	public class Result {

		private int must;
		private int version_num;
		private String app_type;
		private String version;
		private Apk apk;
		private String objectId;
		private String createdAt;
		private String updatedAt;

		public void setMust(int must) {
			this.must = must;
		}

		public int getMust() {
			return must;
		}
		public void setVersion_Num(int version_num) {
			this.version_num = version_num;
		}
		public int getVersion_Num() {
			return version_num;
		}
		
		public void setAppType(String appType) {
			this.app_type = appType;
		}

		public String getAppType() {
			return app_type;
		}

		public void setVersion(String version) {
			this.version = version;
		}

		public String getVersion() {
			return version;
		}

		public void setApk(Apk apk) {
			this.apk = apk;
		}

		public Apk getApk() {
			return apk;
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

		public class Apk {

			private String __type;
			private String id;
			private String name;
			private String url;

			public void set_type(String _type) {
				this.__type = _type;
			}

			public String get_type() {
				return __type;
			}

			public void setId(String id) {
				this.id = id;
			}

			public String getId() {
				return id;
			}

			public void setName(String name) {
				this.name = name;
			}

			public String getName() {
				return name;
			}

			public void setUrl(String url) {
				this.url = url;
			}

			public String getUrl() {
				return url;
			}

		}

	}
}
