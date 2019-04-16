/**
 * 
 */
package com.efida.sport.facade.model;

import java.io.Serializable;
import java.util.Date;

import com.efida.sport.facade.enums.CerTypeEnum;
import com.efida.sport.facade.enums.SexEnum;

/**
 * @author Administrator
 * 运动员信息
 */
public class PlayerModel implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * 
     * 运动员编号 ;数据交换平台传输过来
     * 
     */
    private String            playerCode;
    private Integer           playerNo;

    /**
     * 运动员电话号码
     */
    private String            playerPhone;
    /**
     * 运动员名称
     */
    private String            playerName;
    /**
     * 性别(男：m,女:f)
     * @see SexEnum
     */
    private String            sex;

    /**
     * 邮箱
     */
    private String            email;
    /**
     * 身高
     */
    private Integer           playerHeight;
    /**
     * 体重
     */
    private Double            playerWeight;
    /**
     * 生日
     */
    private Date              playerBirth;
    /**
     * 国籍
     */
    private String            playerNationality;
    /**
     * 地址
     */
    private String            playerAddress;
    /**
     * 证件类型
     * @see CerTypeEnum
     */
    private String            playerCerType;
    /**
     * 证件号码
     */
    private String            playerCerNo;
    /**
     * 血型
     */
    private String            playerBloodType;
    /**
     * 民族
     */
    private String            playerNation;
    /**
     * 衣服尺码
     */
    private String            playerClothSize;
    /**
     * 工作单位
     */
    private String            playerWorkUnit;
    /**
     * 紧急联系人
     */
    private String            playerEmergencyName;
    /**
     * 紧急联系人电话
     */
    private String            playerEmergencyPhone;

    /**
     * 附件地址
     */
    private String            attUrl;
    /**
     * 附件1
     */
    private String            attOne;
    /**
     * 附件2
     */
    private String            attTwo;

    /**
     * 头像地址
     */
    private String            imgUrl;

    private String            assettoId;

    /**
     * 扩展属性
     */
    private String            extProp;

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getPlayerHeight() {
        return playerHeight;
    }

    public void setPlayerHeight(Integer playerHeight) {
        this.playerHeight = playerHeight;
    }

    public Double getPlayerWeight() {
        return playerWeight;
    }

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

    public String getPlayerWorkUnit() {
        return playerWorkUnit;
    }

    public void setPlayerWorkUnit(String playerWorkUnit) {
        this.playerWorkUnit = playerWorkUnit;
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

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getAssettoId() {
        return assettoId;
    }

    public void setAssettoId(String assettoId) {
        this.assettoId = assettoId;
    }

    /**
     * Getter method for property <tt>playerCode</tt>.
     * 
     * @return property value of playerCode
     */
    public String getPlayerCode() {
        return playerCode;
    }

    /**
     * Setter method for property <tt>playerCode</tt>.
     * 
     * @param playerCode value to be assigned to property playerCode
     */
    public void setPlayerCode(String playerCode) {
        this.playerCode = playerCode;
    }

    /**
     * Getter method for property <tt>extProp</tt>.
     * 
     * @return property value of extProp
     */
    public String getExtProp() {
        return extProp;
    }

    /**
     * Setter method for property <tt>extProp</tt>.
     * 
     * @param extProp value to be assigned to property extProp
     */
    public void setExtProp(String extProp) {
        this.extProp = extProp;
    }

    public Integer getPlayerNo() {
        return playerNo;
    }

    public void setPlayerNo(Integer playerNo) {
        this.playerNo = playerNo;
    }

    @Override
    public String toString() {
        return "PlayerModel [playerCode=" + playerCode + ", playerNo=" + playerNo + ", playerPhone="
               + playerPhone + ", playerName=" + playerName + ", sex=" + sex + ", email=" + email
               + ", playerHeight=" + playerHeight + ", playerWeight=" + playerWeight
               + ", playerBirth=" + playerBirth + ", playerNationality=" + playerNationality
               + ", playerAddress=" + playerAddress + ", playerCerType=" + playerCerType
               + ", playerCerNo=" + playerCerNo + ", playerBloodType=" + playerBloodType
               + ", playerNation=" + playerNation + ", playerClothSize=" + playerClothSize
               + ", playerWorkUnit=" + playerWorkUnit + ", playerEmergencyName="
               + playerEmergencyName + ", playerEmergencyPhone=" + playerEmergencyPhone
               + ", attUrl=" + attUrl + ", attOne=" + attOne + ", attTwo=" + attTwo + ", imgUrl="
               + imgUrl + ", assettoId=" + assettoId + ", extProp=" + extProp + "]";
    }

}
