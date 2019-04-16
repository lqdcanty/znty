package com.efida.sports.pc.web.vo;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.efida.sports.entity.Player;
import com.efida.sports.enums.SexEnum;
import com.efida.sports.util.DateUtil;

/**
 * 运动员转换VO
 * 
 * @author zengbo
 * @version $Id: PlayerVo.java, v 0.1 2018年7月19日 下午5:50:06 zengbo Exp $
 */
public class PlayerVo {

    /**
     * 主键
     */
    private Long                      id;
    /**
     * 运动员唯一标识
     */
    private String                    playerCode;
    /**
     * 运动员电话号码
     */
    private String                    playerPhone;
    /**
     * 运动员名称
     */
    private String                    playerName;
    /**
     * 性别
     */
    private String                    sex;
    /**
     * 用户唯一编号
     */
    private String                    registerCode;
    /**
     * 邮箱
     */
    private String                    email;
    /**
     * 身高
     */
    private Integer                   playerHeight;
    /**
     * 体重
     */
    private Double                    playerWeight;
    /**
     * 生日
     */
    private Date                      playerBirth;
    /**
     * 国籍
     */
    private String                    playerNationality;
    /**
     * 地址
     */
    private String                    playerAddress;
    /**
     * 证件类型
     */
    private String                    playerCerType;
    /**
     * 证件号码
     */
    private String                    playerCerNo;
    /**
     * 血型
     */
    private String                    playerBloodType;
    /**
     * 民族
     */
    private String                    playerNation;
    /**
     * 衣服尺码
     */
    private String                    playerClothSize;
    /**
     * 工作单位
     */
    private String                    playerWorkUnit;
    /**
     * 紧急联系人
     */
    private String                    playerEmergencyName;
    /**
     * 紧急联系人电话
     */
    private String                    playerEmergencyPhone;

    /**
     * 附件地址
     */
    private String                    attUrl;
    /**
     * 附件1
     */
    private String                    attOne;
    /**
     * 附件2
     */
    private String                    attTwo;

    /**
     * 头像地址
     */
    private String                    imgUrl;
    /**
     * 创建时间
     */
    private Date                      gmtCreate;
    /**
     * 修改时间
     */
    private Date                      gmtModified;

    /**
     * 不知道是啥 
     */
    private String                    assettoId;
    /**
     * 扩展属性
     */
    private String                    extPro;

    private String                    verifyCode;

    private String                    orderCode;

    private String                    playerBirthStr;

    private String                    sexStr;

    private List<Map<String, Object>> array;

    public static List<PlayerVo> getPlayerVoList(List<Player> list) {
        List<PlayerVo> vos = new ArrayList<PlayerVo>();
        for (Player player : list) {
            PlayerVo vo = getPlayerVo(player);
            if (vo != null) {
                vos.add(vo);
            }
        }
        return vos;
    }

    public static PlayerVo getPlayerVo(Player player) {
        PlayerVo vo = new PlayerVo();
        if (player == null) {
            return vo;
        }
        vo.setId(player.getId());
        vo.setPlayerCode(player.getPlayerCode());
        vo.setPlayerPhone(player.getPlayerPhone());
        vo.setPlayerName(player.getPlayerName());
        vo.setSex(player.getSex());
        vo.setRegisterCode(player.getRegisterCode());
        vo.setEmail(player.getEmail());
        vo.setPlayerHeight(player.getPlayerHeight());
        vo.setPlayerWeight(player.getPlayerWeight());
        vo.setPlayerBirth(player.getPlayerBirth());
        vo.setPlayerNationality(player.getPlayerNationality());
        vo.setPlayerAddress(player.getPlayerAddress());
        vo.setPlayerCerType(player.getPlayerCerType());
        if (StringUtils.isNotBlank(player.getPlayerCerType())) {
            vo.setPlayerCerNo(player.getPlayerCerNo());
        }
        vo.setPlayerBloodType(player.getPlayerBloodType());
        vo.setPlayerNation(player.getPlayerNation());
        vo.setPlayerClothSize(player.getPlayerClothSize());
        vo.setPlayerWorkUnit(player.getPlayerWorkUnit());
        vo.setPlayerEmergencyName(player.getPlayerEmergencyName());
        vo.setPlayerEmergencyPhone(player.getPlayerEmergencyPhone());
        vo.setAttUrl(player.getAttUrl());
        vo.setAttOne(player.getAttOne());
        vo.setAttTwo(player.getAttTwo());
        vo.setImgUrl(player.getImgUrl());
        vo.setGmtCreate(player.getGmtCreate());
        vo.setGmtModified(player.getGmtModified());
        vo.setAssettoId(player.getAssettoId());
        vo.setExtPro(player.getExtPro());
        vo.setVerifyCode(player.getVerifyCode());
        vo.setOrderCode(player.getOrderCode());
        if (player.getPlayerBirth() != null) {
            vo.setPlayerBirthStr(DateUtil.formatWeb(player.getPlayerBirth()));
        }
        if (StringUtils.isNotBlank(player.getSex())) {
            vo.setSexStr(SexEnum.getDescByCode(player.getSex()));
        }

        if (StringUtils.isNotBlank(player.getExtPro())) {
            List<Map<String, Object>> arrays = new ArrayList<Map<String, Object>>();
            JSONObject parseObject = JSON.parseObject(player.getExtPro());
            Set<String> sets = parseObject.keySet();
            //            Set<Entry<String, Object>> entrySet = parseObject.entrySet();
            Iterator<String> iterator = sets.iterator();
            while (iterator.hasNext()) {
                Map<String, Object> map = new HashMap<String, Object>();
                String key = iterator.next().toString();
                String value = parseObject.getString(key);
                map.put("key", key);
                map.put("value", value);
                arrays.add(map);
            }
            vo.setArray(arrays);
        }
        return vo;
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

    public String getSexStr() {
        return sexStr;
    }

    public void setSexStr(String sexStr) {
        this.sexStr = sexStr;
    }

    public List<Map<String, Object>> getArray() {
        return array;
    }

    public void setArray(List<Map<String, Object>> array) {
        this.array = array;
    }

}
