package com.efida.sports.dmp.dao.mapper;

import com.efida.sports.dmp.dao.entity.ScoreCertPicEntity;

public interface ScoreCertPicEntityMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table score_cert_pic
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table score_cert_pic
     *
     * @mbggenerated
     */
    int insert(ScoreCertPicEntity record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table score_cert_pic
     *
     * @mbggenerated
     */
    int insertSelective(ScoreCertPicEntity record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table score_cert_pic
     *
     * @mbggenerated
     */
    ScoreCertPicEntity selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table score_cert_pic
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(ScoreCertPicEntity record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table score_cert_pic
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(ScoreCertPicEntity record);
}