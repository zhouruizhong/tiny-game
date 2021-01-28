package com.zrz.game.model;

import com.zrz.game.annotation.Column;
import lombok.Data;

/**
 * 残联申报企业名单
 * @author zrz
 */

@Data
public class Clsbqy {

    @Column(name = "id")
    public int id;

    @Column(name = "日期")
    public String date;

    @Column(name = "序号")
    public String number;

    @Column(name = "企业名称")
    public String company;

    @Column(name = "流水号")
    public String caseId;

    @Column(name = "标记号")
    public String tagNumber;

    @Column(name = "是否打印")
    public String isPrint;

    @Column(name = "初步筛选结果")
    public String firstResult;

    @Column(name = "是否有问题")
    public String isProblem;

    @Column(name = "需重新处理")
    public String reHandle;

    @Column(name = "11-17打印材料")
    public String printMaterial;

}
