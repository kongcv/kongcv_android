package com.kongcv.global;

import java.util.List;

/**
 * Created by kcw on 2015/12/28.
 */
public class MethodBean {


    /**
     * method : 全时/天
     * hire_type : 0
     * picture : {"__type":"File","id":"564db1a100b0ee7f59dcc12d","name":"Rectangle 214 Copy 5.png","url":"http://ac-ATcs8k4n.clouddn.com/58711ad1c423e06a.png"}
     * park_type : ["5620a6dc60b27457e84bb21d","5620a6d060b27457e84bb0fe"]
     * objectId : 561f4cb460b22ed7ca73ab6c
     * createdAt : 2015-10-15T06:50:28.044Z
     * updatedAt : 2015-11-19T11:25:21.753Z
     */

    private String method;
    private int hire_type;
    /**
     * __type : File
     * id : 564db1a100b0ee7f59dcc12d
     * name : Rectangle 214 Copy 5.png
     * url : http://ac-ATcs8k4n.clouddn.com/58711ad1c423e06a.png
     */

    private PictureEntity picture;
    private String objectId;
    private String createdAt;
    private String updatedAt;
    private List<String> park_type;

    public void setMethod(String method) {
        this.method = method;
    }

    public void setHire_type(int hire_type) {
        this.hire_type = hire_type;
    }

    public void setPicture(PictureEntity picture) {
        this.picture = picture;
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

    public void setPark_type(List<String> park_type) {
        this.park_type = park_type;
    }

    public String getMethod() {
        return method;
    }

    public int getHire_type() {
        return hire_type;
    }

    public PictureEntity getPicture() {
        return picture;
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

    public List<String> getPark_type() {
        return park_type;
    }

    public static class PictureEntity {
        private String __type;
        private String id;
        private String name;
        private String url;

        public void set__type(String __type) {
            this.__type = __type;
        }

        public void setId(String id) {
            this.id = id;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String get__type() {
            return __type;
        }

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getUrl() {
            return url;
        }
    }
}
