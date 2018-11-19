package org.feup.cmov.customerapp.model;

public class Vouchers {

    String id;
    String prduct;
    String createdDate;

    public Vouchers(String id, String prduct, String createdDate) {
        this.id = id;
        this.prduct = prduct;
        this.createdDate = createdDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPrduct() {
        return prduct;
    }

    public void setPrduct(String prduct) {
        this.prduct = prduct;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }
}
