package com.xiandao.android.entity;

import java.io.Serializable;

/**
 * Created by zengx on 2019/5/13.
 * Describe:
 */
public class PersonalInfomation implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * ownerid : 4532
     * name : 测试账户14
     * nickname : 居家
     * mobile : 18044440001
     * backupphone :
     * mailaddress : 湖南长沙
     * zipcode :
     * isjuridicalperson : false
     * companytype :
     * businessitems :
     * idcardno : 430121199007100065
     * idcardtype : 0
     * face : upload/ownerFace/4532_face.png
     * gender : 0
     * commonAddress :
     * projectId : 1
     * receiptAddress :
     * cardNo : 000014
     * vehicleCode :
     * faceDisUrl :
     * faceDisTime :
     * birthday : 1990-07-10
     * checkindate : 2019-04-09
     * livemode : 3
     */

    private int ownerid;
    private String name;
    private String nickname;
    private String mobile;
    private String backupphone;
    private String mailaddress;
    private String zipcode;
    private boolean isjuridicalperson;
    private String companytype;
    private String businessitems;
    private String idcardno;
    private int idcardtype;
    private String face;
    private int gender;
    private String commonAddress;
    private int projectId;
    private String receiptAddress;
    private String cardNo;
    private String vehicleCode;
    private String faceDisUrl;
    private String faceDisTime;
    private String birthday;
    private String checkindate;
    private String livemode;

    public int getOwnerid() {
        return ownerid;
    }

    public void setOwnerid(int ownerid) {
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

    public boolean isIsjuridicalperson() {
        return isjuridicalperson;
    }

    public void setIsjuridicalperson(boolean isjuridicalperson) {
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

    public int getIdcardtype() {
        return idcardtype;
    }

    public void setIdcardtype(int idcardtype) {
        this.idcardtype = idcardtype;
    }

    public String getFace() {
        return face;
    }

    public void setFace(String face) {
        this.face = face;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getCommonAddress() {
        return commonAddress;
    }

    public void setCommonAddress(String commonAddress) {
        this.commonAddress = commonAddress;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public String getReceiptAddress() {
        return receiptAddress;
    }

    public void setReceiptAddress(String receiptAddress) {
        this.receiptAddress = receiptAddress;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getVehicleCode() {
        return vehicleCode;
    }

    public void setVehicleCode(String vehicleCode) {
        this.vehicleCode = vehicleCode;
    }

    public String getFaceDisUrl() {
        return faceDisUrl;
    }

    public void setFaceDisUrl(String faceDisUrl) {
        this.faceDisUrl = faceDisUrl;
    }

    public String getFaceDisTime() {
        return faceDisTime;
    }

    public void setFaceDisTime(String faceDisTime) {
        this.faceDisTime = faceDisTime;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getCheckindate() {
        return checkindate;
    }

    public void setCheckindate(String checkindate) {
        this.checkindate = checkindate;
    }

    public String getLivemode() {
        return livemode;
    }

    public void setLivemode(String livemode) {
        this.livemode = livemode;
    }

    @Override
    public String toString() {
        return "PersonalInfomation{" +
                "ownerid=" + ownerid +
                ", name='" + name + '\'' +
                ", nickname='" + nickname + '\'' +
                ", mobile='" + mobile + '\'' +
                ", backupphone='" + backupphone + '\'' +
                ", mailaddress='" + mailaddress + '\'' +
                ", zipcode='" + zipcode + '\'' +
                ", isjuridicalperson=" + isjuridicalperson +
                ", companytype='" + companytype + '\'' +
                ", businessitems='" + businessitems + '\'' +
                ", idcardno='" + idcardno + '\'' +
                ", idcardtype=" + idcardtype +
                ", face='" + face + '\'' +
                ", gender=" + gender +
                ", commonAddress='" + commonAddress + '\'' +
                ", projectId=" + projectId +
                ", receiptAddress='" + receiptAddress + '\'' +
                ", cardNo='" + cardNo + '\'' +
                ", vehicleCode='" + vehicleCode + '\'' +
                ", faceDisUrl='" + faceDisUrl + '\'' +
                ", faceDisTime='" + faceDisTime + '\'' +
                ", birthday='" + birthday + '\'' +
                ", checkindate='" + checkindate + '\'' +
                ", livemode='" + livemode + '\'' +
                '}';
    }
}
