package com.efida.sports.dmp.dao.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.efida.sports.dmp.dao.entity.OpenScoreRankEntity;

public interface OpenScoreRankEntityMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table open_score_rank
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(Long id);

    /**
     * 删除自动排序的记录。 只针对自动排序记录有效。
     * 
     * @param id
     * @return
     */
    int deleteAutoRankByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table open_score_rank
     *
     * @mbggenerated
     */
    int insert(OpenScoreRankEntity record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table open_score_rank
     *
     * @mbggenerated
     */
    int insertSelective(OpenScoreRankEntity record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table open_score_rank
     *
     * @mbggenerated
     */
    OpenScoreRankEntity selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table open_score_rank
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(OpenScoreRankEntity record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table open_score_rank
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(OpenScoreRankEntity record);

    /**
     * 
     * 
     * @param unitCode
     * @param idempotentId
     * @return
     */
    OpenScoreRankEntity getScoreRankInfoByIdempotent(@Param(value = "unitCode") String unitCode,
                                                     @Param(value = "idempotentId") String idempotentId);

    /**
     * unitCode
     * matchCode
     * eventCode
     * playerCode
     * scoreName
     * start
     * limit
     * @title
     * @methodName
     * @author wangyi
     * @description
     * @param map
     * @return
     */
    List<OpenScoreRankEntity> selectByProp(Map<String, Object> map);

    /**
     * 获取排名信息
     * @title
     * @methodName
     * @author wangyi
     * @description
     * @param idempotentId
     * @return
     */
    OpenScoreRankEntity getScoreRankByIdempId(@Param(value = "idempotentId") String idempotentId);

    /**
     * 
     * 
     * @param map
     * @return
     */
    List<OpenScoreRankEntity> selectUnSyncRecord(Map<String, Object> map);

    /**
     * 根据条件模糊查询报名信息
     * match
     * playerPhone
     * playerName
     * startTime
     * endTime
     * @title
     * @methodName
     * @author wangyi
     * @description
     * @param unitCode
     * @param queryParams
     * @return
     */
    List<OpenScoreRankEntity> selectScoreRankByLikeParams(Map<String, Object> queryParams);

    /**
     * 查询赛事编号
     * 
     * @param queryParams
     * @return
     */
    List<String> queryHasScoreMatch(Map<String, Object> queryParams);

    /**
     * 根据赛事编号和电话号码查询成绩排名数据
     * 
     * @param queryParams
     * @return
     */
    List<OpenScoreRankEntity> getAllRankByMatchAndPhone(Map<String, Object> queryParams);

    /**
     * 根据map查询
     * 
     * @param queryParams
     * @return
     */
    List<OpenScoreRankEntity> querySoreByMap(Map<String, Object> queryParams);

    /**
     * 根据排名编号获取排名数据
     * 
     * @param rankCode
     * @return
     */
    OpenScoreRankEntity getScoreRankByRankCode(@Param(value = "rankCode") String rankCode);

    /**
     * 查询赛事排名根据条件
     * @title
     * @methodName
     * @author wangyi
     * @description
     * @param queryParams
     * @return
     */
    List<OpenScoreRankEntity> queryCompetitionScoreRank(Map<String, Object> queryParams);

    /**
     * 查询官方排名数据，通过比赛编号
     * 
     * @param queryParams
     * @return
     */
    List<OpenScoreRankEntity> querySelfRankByMap(Map<String, Object> queryParams);

    /**
     * 
     * 
     * @param cometitionCode
     * @return
     */
    int deleteByCompetitionCode(String cometitionCode);

    /**
     * 查询比赛时间
     * 
     * @param queryParams
     * @return
     */
    //    List<Date> queryDateSoreByMap(Map<String, Object> queryParams);

    /**
     * 根据比赛编码查询有成绩的赛场信息
     *
     * @param competitionCode 比赛编号
     * @return
     */
    List<OpenScoreRankEntity> selectCompetitionInfoByScore(@Param(value = "competitionCode") String competitionCode);

}