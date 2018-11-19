package org.feup.cmov.customerapp.model;

public class LastTransactions {
    public static enum TransctionType { Ticket, Order}

    private TransctionType type; //0
    private String content;
    private String date;

    public LastTransactions(TransctionType type, String content, String date) {
        this.type = type;
        this.content = content;
        this.date = date;
    }

    public TransctionType getType() {
        return type;
    }

    public void setType(TransctionType type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
