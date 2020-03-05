package com.xiandao.android.entity;

import java.io.Serializable;

/**
 * Created by 谭勇 on 2017/12/4.
 */
public class EditPersonResultData implements Serializable {
    private static final long serialVersionUID = 1L;

    private String ownerid;
    private String name;
    private String nickname;
    private String mobile;
    private String backupphone;
    private String mailaddress;
    private String zipcode;
    private String isjuridicalperson;
    private String companytype;
    private String businessitems;
    private String idcardno;
    private String idcardtype;
    private String face;
    private boolean gender;
    private String commonAddress;
    private String projectId;


    public String getOwnerid() {
        return ownerid;
    }

    public void setOwnerid(String ownerid) {
        this.ownerid = ownerid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getBackupphone() {
        return backupphone;
    }

    public void setBackupphone(String backupphone) {
        this.backupphone = backupphone;
    }

    public String getMailaddress() {
        return mailaddress;
    }

    public void setMailaddress(String mailaddress) {
        this.mailaddress = mailaddress;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getIsjuridicalperson() {
        return isjuridicalperson;
    }

    public void setIsjuridicalperson(String isjuridicalperson) {
        this.isjuridicalperson = isjuridicalperson;
    }

    public String getCompanytype() {
        return companytype;
    }

    public void setCompanytype(String companytype) {
        this.companytype = companytype;
    }

    public String getBusinessitems() {
        return businessitems;
    }

    public void setBusinessitems(String businessitems) {
        this.businessitems = businessitems;
    }

    public String getIdcardno() {
        return idcardno;
    }

    public void setIdcardno(String idcardno) {
        this.idcardno = idcardno;
    }

    public String getIdcardtype() {
        return idcardtype;
    }

    public void setIdcardtype(String idcardtype) {
        this.idcardtype = idcardtype;
    }

    public String getFace() {
        return face;
    }

    public void setFace(String face) {
        this.face = face;
    }

    public boolean isGender() {
        return gender;
    }

    public void setGender(boolean gender) {
        this.gender = gender;
    }

    public String getCommonAddress() {
        return commonAddress;
    }

    public void setCommonAddress(String commonAddress) {
        this.commonAddress = commonAddress;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }
}
