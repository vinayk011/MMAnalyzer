package com.hug.mma.model;

public class Dialog {
    private String title;
    private String desc;
    private String btnStr;

    public Dialog(String title, String desc, String btnStr) {
        this.title = title;
        this.desc = desc;
        this.btnStr = btnStr;
    }

    public Dialog() {

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getBtnStr() {
        return btnStr;
    }

    public void setBtnStr(String btnStr) {
        this.btnStr = btnStr;
    }
}
