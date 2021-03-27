package com.arkavyapar.Model.ReponseModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

class USERDETAIL{
    @SerializedName("MESSAGE")
    @Expose
    public String mESSAGE;
    @SerializedName("TRANSACTIONFLAG")
    @Expose
    public String tRANSACTIONFLAG;
    @SerializedName("USERNAME")
    @Expose
    public String uSERNAME;
    @SerializedName("PHONENO")
    @Expose
    public String pHONENO;
    @SerializedName("EMAIL")
    @Expose
    public String eMAIL;
    @SerializedName("ADDRESS1")
    @Expose
    public String aDDRESS1;
    @SerializedName("ADDRESS2")
    @Expose
    public String aDDRESS2;
    @SerializedName("PINCODE")
    @Expose
    public String pINCODE;
    @SerializedName("CITY")
    @Expose
    public String cITY;
    @SerializedName("STATE")
    @Expose
    public String sTATE;
    @SerializedName("USERLAT")
    @Expose
    public String uSERLAT;
    @SerializedName("USERLANG")
    @Expose
    public String uSERLANG;
    @SerializedName("PROFILEPICPATH")
    @Expose
    public String pROFILEPICPATH;
    @SerializedName("IDPICPATH")
    @Expose
    public String iDPICPATH;
    @SerializedName("PROFILEVERIFIED")
    @Expose
    public String pROFILEVERIFIED;

    public USERDETAIL() {
    }

    public String getmESSAGE() {
        return mESSAGE;
    }

    public void setmESSAGE(String mESSAGE) {
        this.mESSAGE = mESSAGE;
    }

    public String gettRANSACTIONFLAG() {
        return tRANSACTIONFLAG;
    }

    public void settRANSACTIONFLAG(String tRANSACTIONFLAG) {
        this.tRANSACTIONFLAG = tRANSACTIONFLAG;
    }

    public String getuSERNAME() {
        return uSERNAME;
    }

    public void setuSERNAME(String uSERNAME) {
        this.uSERNAME = uSERNAME;
    }

    public String getpHONENO() {
        return pHONENO;
    }

    public void setpHONENO(String pHONENO) {
        this.pHONENO = pHONENO;
    }

    public String geteMAIL() {
        return eMAIL;
    }

    public void seteMAIL(String eMAIL) {
        this.eMAIL = eMAIL;
    }

    public String getaDDRESS1() {
        return aDDRESS1;
    }

    public void setaDDRESS1(String aDDRESS1) {
        this.aDDRESS1 = aDDRESS1;
    }

    public String getaDDRESS2() {
        return aDDRESS2;
    }

    public void setaDDRESS2(String aDDRESS2) {
        this.aDDRESS2 = aDDRESS2;
    }

    public String getpINCODE() {
        return pINCODE;
    }

    public void setpINCODE(String pINCODE) {
        this.pINCODE = pINCODE;
    }

    public String getcITY() {
        return cITY;
    }

    public void setcITY(String cITY) {
        this.cITY = cITY;
    }

    public String getsTATE() {
        return sTATE;
    }

    public void setsTATE(String sTATE) {
        this.sTATE = sTATE;
    }

    public String getuSERLAT() {
        return uSERLAT;
    }

    public void setuSERLAT(String uSERLAT) {
        this.uSERLAT = uSERLAT;
    }

    public String getuSERLANG() {
        return uSERLANG;
    }

    public void setuSERLANG(String uSERLANG) {
        this.uSERLANG = uSERLANG;
    }

    public String getpROFILEPICPATH() {
        return pROFILEPICPATH;
    }

    public void setpROFILEPICPATH(String pROFILEPICPATH) {
        this.pROFILEPICPATH = pROFILEPICPATH;
    }

    public String getiDPICPATH() {
        return iDPICPATH;
    }

    public void setiDPICPATH(String iDPICPATH) {
        this.iDPICPATH = iDPICPATH;
    }

    public String getpROFILEVERIFIED() {
        return pROFILEVERIFIED;
    }

    public void setpROFILEVERIFIED(String pROFILEVERIFIED) {
        this.pROFILEVERIFIED = pROFILEVERIFIED;
    }
}

public class RequestAllProductModel{
    public int success;
    @SerializedName("USERDETAILS")
    @Expose
    public List<USERDETAIL> uSERDETAILS;
    public String msg;

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

    public List<USERDETAIL> getuSERDETAILS() {
        return uSERDETAILS;
    }

    public void setuSERDETAILS(List<USERDETAIL> uSERDETAILS) {
        this.uSERDETAILS = uSERDETAILS;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
