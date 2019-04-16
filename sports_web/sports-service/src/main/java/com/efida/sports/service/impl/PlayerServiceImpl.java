package com.efida.sports.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.efida.sports.entity.Player;
import com.efida.sports.entity.PlayerEx;
import com.efida.sports.exception.BusinessException;
import com.efida.sports.mapper.PlayerMapper;
import com.efida.sports.service.IApplyPlayerService;
import com.efida.sports.service.IPlayerService;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zoutao
 * @since 2018-05-18
 */
@Service
public class PlayerServiceImpl extends ServiceImpl<PlayerMapper, Player> implements IPlayerService {

    @Autowired
    private PlayerMapper        playerMapper;

    @Autowired
    private IApplyPlayerService applyPlayerService;

    @Override
    public Player getPlayerByPlayerCode(String playerCode) {
        Wrapper<Player> wrapper = new EntityWrapper<Player>();
        wrapper.eq("player_code", playerCode);
        Player player = selectOne(wrapper);
        return player;
    }

    @Override
    public void batchUpdatePlayerCreater(String oldRegiterCode, String newRegisterCode) {

        playerMapper.batchUpdatePlayerCreater(oldRegiterCode, newRegisterCode);
    }

    @Override
    public List<Player> selectPlayerByApplyInfoCode(String applyCode) {
        List<Player> selectList = new ArrayList<Player>();
        List<String> playerCodes = applyPlayerService.selectPlayerCodeByApplyCode(applyCode);
        Wrapper<Player> wrapper = new EntityWrapper<Player>();
        if (playerCodes == null || playerCodes.size() < 1) {
            return selectList;
        }
        wrapper.in("player_code", playerCodes);
        selectList = selectList(wrapper);
        return selectList;
    }

    @Override
    public void deletPlayerByApplyInfoCode(String applyCode) {

        playerMapper.deletPlayerByApplyInfoCode(applyCode);

    }

    @Override
    public Player getPersonalPlayerByApplyCode(String applyCode) {
        List<Player> selectList = selectPlayerByApplyInfoCode(applyCode);
        return selectList != null && selectList.size() > 0 ? selectList.iterator().next() : null;
    }

    @Override
    @Transactional
    public void deletTeamMembers(String playerCode) {
        if (StringUtils.isBlank(playerCode)) {
            throw new BusinessException("运动员属性不能为空");
        }
        Player player = getPlayerByPlayerCode(playerCode);
        if (player == null) {
            throw new BusinessException("运动员不存在");
        }
        //删除运动员
        deleteById(player.getId());
        //删除关联表
        applyPlayerService.deleteByPlayerCode(playerCode);

    }

    @Override
    public void checkPlayer(Player player) {
        if (player == null) {
            throw new BusinessException("运动员不能为空");
        }
        String playerName = player.getPlayerName();
        if (StringUtils.isNotBlank(playerName) && playerName.length() > 24) {
            throw new BusinessException("运动员名称长度不能超过24位");
        }
        String playerPhone = player.getPlayerPhone();
        if (StringUtils.isEmpty(playerPhone)) {
            throw new BusinessException("运动员名电话号码不能为空");
        }
        String email = player.getEmail();
        if (StringUtils.isNotBlank(email) && email.length() > 24) {
            throw new BusinessException("邮箱长度不能超过24位");
        }
        String playerNationality = player.getPlayerNationality();
        if (StringUtils.isNotBlank(playerNationality) && playerNationality.length() > 12) {
            throw new BusinessException("国籍长度不能超过12位");
        }
        String playerAddress = player.getPlayerAddress();
        if (StringUtils.isNotBlank(playerAddress) && playerAddress.length() > 64) {
            throw new BusinessException("地址长度不能超过64位");
        }
        String playerCerNo = player.getPlayerCerNo();
        if (StringUtils.isNotBlank(playerCerNo) && playerCerNo.length() > 32) {
            throw new BusinessException("证件号码长度不能超过32位");
        }
        String bloodType = player.getPlayerBloodType();
        if (StringUtils.isNotBlank(bloodType) && bloodType.length() > 12) {
            throw new BusinessException("血型长度不能超过12位");
        }
        String playerNation = player.getPlayerNation();
        if (StringUtils.isNotBlank(playerNation) && playerNation.length() > 24) {
            throw new BusinessException("民族长度不能超过24位");
        }
        String clothSize = player.getPlayerClothSize();
        if (StringUtils.isNotBlank(clothSize) && clothSize.length() > 12) {
            throw new BusinessException("衣服尺码长度不能超过12位");
        }
        String playerEmergencyName = player.getPlayerEmergencyName();
        if (StringUtils.isNotBlank(playerEmergencyName) && playerEmergencyName.length() > 24) {
            throw new BusinessException("紧急联系人长度不能超过24位");
        }
        String playerEmergencyPhone = player.getPlayerEmergencyPhone();
        if (StringUtils.isNotBlank(playerEmergencyPhone) && playerEmergencyPhone.length() > 24) {
            throw new BusinessException("紧急联系电话长度不能超过24位");
        }
        String attOne = player.getAttOne();
        if (StringUtils.isNotBlank(attOne) && attOne.length() > 128) {
            throw new BusinessException("附件正面长度不能超过128位");
        }
        String attTwo = player.getAttTwo();
        if (StringUtils.isNotBlank(attTwo) && attTwo.length() > 128) {
            throw new BusinessException("附件反面长度不能超过128位");
        }
        String attUrl = player.getAttUrl();
        if (StringUtils.isNotBlank(attUrl) && attUrl.length() > 128) {
            throw new BusinessException("附件地址长度不能超过128位");
        }
        String imgUrl = player.getImgUrl();
        if (StringUtils.isNotBlank(imgUrl) && imgUrl.length() > 128) {
            throw new BusinessException("头像地址长度不能超过128位");
        }
        String playerWorkUnit = player.getPlayerWorkUnit();
        if (StringUtils.isNotBlank(playerWorkUnit) && playerWorkUnit.length() > 32) {
            throw new BusinessException("工作单位长度不能超过32位");
        }
        String assettoId = player.getAssettoId();
        if (StringUtils.isNotBlank(assettoId) && assettoId.length() > 32) {
            throw new BusinessException("神力科莎游戏昵称长度不能超过32位");
        }
    }

    @Override
    public List<PlayerEx> selectPlayersByApplyInfoCodes(List<String> applyCodes) {

        return this.playerMapper.selectPlayersByApplyInfoCodes(applyCodes);

    }
}
