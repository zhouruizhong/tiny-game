package com.zrz.game.model;

import com.zrz.game.annotation.Column;
import lombok.Data;

/**
 * 残联申报企业名单
 * @author zrz
 */

public class Clsbqy {

    @Column(name = "id")
    private Integer id;

    @Column(name = "日期")
    private String date;

    @Column(name = "序号")
    private String number;

    @Column(name = "企业名称")
    private String company;

    @Column(name = "流水号")
    private String caseId;

    @Column(name = "标记号")
    private String tagNumber;

    @Column(name = "是否打印")
    private String isPrint;

    @Column(name = "初步筛选结果")
    private String firstResult;

    @Column(name = "是否有问题")
    private String isProblem;

    @Column(name = "需重新处理")
    private String reHandle;

    @Column(name = "11-17打印材料")
    private String printMaterial;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getCaseId() {
        return caseId;
    }

    public void setCaseId(String caseId) {
        this.caseId = caseId;
    }

    public String getTagNumber() {
        return tagNumber;
    }

    public void setTagNumber(String tagNumber) {
        this.tagNumber = tagNumber;
    }

    public String getIsPrint() {
        return isPrint;
    }

    public void setIsPrint(String isPrint) {
        this.isPrint = isPrint;
    }

    public String getFirstResult() {
        return firstResult;
    }

    public void setFirstResult(String firstResult) {
        this.firstResult = firstResult;
    }

    public String getIsProblem() {
        return isProblem;
    }

    public void setIsProblem(String isProblem) {
        this.isProblem = isProblem;
    }

    public String getReHandle() {
        return reHandle;
    }

    public void setReHandle(String reHandle) {
        this.reHandle = reHandle;
    }

    public String getPrintMaterial() {
        return printMaterial;
    }

    public void setPrintMaterial(String printMaterial) {
        this.printMaterial = printMaterial;
    }
}
