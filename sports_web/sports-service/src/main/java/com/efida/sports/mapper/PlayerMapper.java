package com.efida.sports.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.efida.sports.entity.Player;
import com.efida.sports.entity.PlayerEx;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zoutao
 * @since 2018-05-18
 */
public interface PlayerMapper extends BaseMapper<Player> {

    void batchUpdatePlayerCreater(@Param("oldRegisterCode") String oldRegiterCode,
                                  @Param("newRegisterCode") String newRegisterCode);

    List<PlayerEx> selectPlayersByApplyInfoCodes(List<String> applyCodes);

    void deletPlayerByApplyInfoCode(String applyCode);
}
