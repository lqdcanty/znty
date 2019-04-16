package com.efida.sports.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.enums.IdType;

/**
 * <p>
 * 
 * </p>
 *
 * @author zoutao
 * @since 2018-05-18
 */

public class Player implements Serializable {

    private static final long   serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long                id;
    /**
     * 运动员唯一标识
     */
    private String              playerCode;
    /**
     * 运动员电话号码
     */
    private String              playerPhone;
    /**
     * 运动员名称
     */
    private String              playerName;
    /**
     * 性别
     */
    private String              sex;
    /**
     * 用户唯一编号
     */
    private String              registerCode;
    /**
     * 邮箱
     */
    private String              email;
    /**
     * 身高
     */
    private Integer             playerHeight;
    /**
     * 体重
     */
    private Double              playerWeight;
    /**
     * 生日
     */
    private Date                playerBirth;
    /**
     * 国籍
     */
    private String              playerNationality;
    /**
     * 地址
     */
    private String              playerAddress;
    /**
     * 证件类型
     */
    private String              playerCerType;
    /**
     * 证件号码
     */
    private String              playerCerNo;
    /**
     * 血型
     */
    private String              playerBloodType;
    /**
     * 民族
     */
    private String              playerNation;
    /**
     * 衣服尺码
     */
    private String              playerClothSize;
    /**
     * 工作单位
     */
    private String              playerWorkUnit;
    /**
     * 紧急联系人
     */
    private String              playerEmergencyName;
    /**
     * 紧急联系人电话
     */
    private String              playerEmergencyPhone;

    /**
     * 附件地址
     */
    private String              attUrl;
    /**
     * 附件1
     */
    private String              attOne;
    /**
     * 附件2
     */
    private String              attTwo;

    /**
     * 头像地址
     */
    private String              imgUrl;
    /**
     * 创建时间
     */
    private Date                gmtCreate;
    /**
     * 修改时间
     */
    private Date                gmtModified;

    /**
     * 不知道是啥 
     */
    private String              assettoId;
    /**
     * 扩展属性
     */
    private String              extPro;
    @TableField(exist = false)
    private Map<String, Object> extProMap;

    @TableField(exist = false)
    private String              verifyCode;

    @TableField(exist = false)
    private String              orderCode;

    @TableField(exist = false)
    private String              playerBirthStr;

    @TableField(exist = false)
    private String              sexStr;

    public String getPlayerWorkUnit() {
        return playerWorkUnit;
    }

    public void setPlayerWorkUnit(String playerWorkUnit) {
        this.playerWorkUnit = playerWorkUnit;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPlayerCode() {
        return playerCode;
    }

    public void setPlayerCode(String playerCode) {
        this.playerCode = playerCode;
    }

    public String getPlayerPhone() {
        return playerPhone;
    }

    public void setPlayerPhone(String playerPhone) {
        this.playerPhone = playerPhone;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getRegisterCode() {
        return registerCode;
    }

    public void setRegisterCode(String registerCode) {
        this.registerCode = registerCode;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSexStr() {
        return sexStr;
    }

    public void setSexStr(String sexStr) {
        this.sexStr = sexStr;
    }

    /**
     * Getter method for property <tt>playerHeight</tt>.
     * 
     * @return property value of playerHeight
     */
    public Integer getPlayerHeight() {
        return playerHeight;
    }

    /**
     * Setter method for property <tt>playerHeight</tt>.
     * 
     * @param playerHeight value to be assigned to property playerHeight
     */
    public void setPlayerHeight(Integer playerHeight) {
        this.playerHeight = playerHeight;
    }

    /**
     * Getter method for property <tt>playerWeight</tt>.
     * 
     * @return property value of playerWeight
     */
    public Double getPlayerWeight() {
        return playerWeight;
    }

    /**
     * Setter method for property <tt>playerWeight</tt>.
     * 
     * @param playerWeight value to be assigned to property playerWeight
     */
    public void setPlayerWeight(Double playerWeight) {
        this.playerWeight = playerWeight;
    }

    public Date getPlayerBirth() {
        return playerBirth;
    }

    public void setPlayerBirth(Date playerBirth) {
        this.playerBirth = playerBirth;
    }

    public String getPlayerNationality() {
        return playerNationality;
    }

    public void setPlayerNationality(String playerNationality) {
        this.playerNationality = playerNationality;
    }

    public String getPlayerAddress() {
        return playerAddress;
    }

    public void setPlayerAddress(String playerAddress) {
        this.playerAddress = playerAddress;
    }

    public String getPlayerCerType() {
        return playerCerType;
    }

    public void setPlayerCerType(String playerCerType) {
        this.playerCerType = playerCerType;
    }

    public String getPlayerCerNo() {
        return playerCerNo;
    }

    public void setPlayerCerNo(String playerCerNo) {
        this.playerCerNo = playerCerNo;
    }

    public String getPlayerBloodType() {
        return playerBloodType;
    }

    public void setPlayerBloodType(String playerBloodType) {
        this.playerBloodType = playerBloodType;
    }

    public String getPlayerNation() {
        return playerNation;
    }

    public void setPlayerNation(String playerNation) {
        this.playerNation = playerNation;
    }

    public String getPlayerClothSize() {
        return playerClothSize;
    }

    public void setPlayerClothSize(String playerClothSize) {
        this.playerClothSize = playerClothSize;
    }

    public String getPlayerEmergencyName() {
        return playerEmergencyName;
    }

    public void setPlayerEmergencyName(String playerEmergencyName) {
        this.playerEmergencyName = playerEmergencyName;
    }

    public String getPlayerEmergencyPhone() {
        return playerEmergencyPhone;
    }

    public void setPlayerEmergencyPhone(String playerEmergencyPhone) {
        this.playerEmergencyPhone = playerEmergencyPhone;
    }

    public String getAttUrl() {
        return attUrl;
    }

    public void setAttUrl(String attUrl) {
        this.attUrl = attUrl;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public Date getGmtModified() {
        return gmtModified;
    }

    public void setGmtModified(Date gmtModified) {
        this.gmtModified = gmtModified;
    }

    public String getVerifyCode() {
        return verifyCode;
    }

    public void setVerifyCode(String verifyCode) {
        this.verifyCode = verifyCode;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public String getPlayerBirthStr() {
        return playerBirthStr;
    }

    public void setPlayerBirthStr(String playerBirthStr) {
        this.playerBirthStr = playerBirthStr;
    }

    public String getAttOne() {
        return attOne;
    }

    public void setAttOne(String attOne) {
        this.attOne = attOne;
    }

    public String getAttTwo() {
        return attTwo;
    }

    public void setAttTwo(String attTwo) {
        this.attTwo = attTwo;
    }

    public String getAssettoId() {
        return assettoId;
    }

    public void setAssettoId(String assettoId) {
        this.assettoId = assettoId;
    }

    public String getExtPro() {
        return extPro;
    }

    public void setExtPro(String extPro) {
        this.extPro = extPro;
    }

    public Map<String, Object> getExtProMap() {
        return extProMap;
    }

    public void setExtProMap(Map<String, Object> extProMap) {
        this.extProMap = extProMap;
    }

    @Override
    public String toString() {
        return "Player{" + ", id=" + id + ", playerCode=" + playerCode + ", playerPhone="
               + playerPhone + ", playerName=" + playerName + ", sex=" + sex + ", registerCode="
               + registerCode + ", email=" + email + ", playerHeight=" + playerHeight
               + ", playerWeight=" + playerWeight + ", playerBirth=" + playerBirth
               + ", playerNationality=" + playerNationality + ", playerAddress=" + playerAddress
               + ", playerCerType=" + playerCerType + ", playerCerNo=" + playerCerNo
               + ", playerBloodType=" + playerBloodType + ", playerNation=" + playerNation
               + ", playerClothSize=" + playerClothSize + ", playerEmergencyName="
               + playerEmergencyName + ", playerEmergencyPhone=" + playerEmergencyPhone
               + ", attUrl=" + attUrl + ", imgUrl=" + imgUrl + ", gmtCreate=" + gmtCreate
               + ", gmtModified=" + gmtModified + "}";
    }
}
