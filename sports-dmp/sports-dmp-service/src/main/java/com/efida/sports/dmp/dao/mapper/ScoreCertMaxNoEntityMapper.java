package com.efida.sports.dmp.dao.mapper;

import com.efida.sports.dmp.dao.entity.ScoreCertMaxNoEntity;

public interface ScoreCertMaxNoEntityMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table score_cert_maxno
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table score_cert_maxno
     *
     * @mbggenerated
     */
    int insert(ScoreCertMaxNoEntity record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table score_cert_maxno
     *
     * @mbggenerated
     */
    int insertSelective(ScoreCertMaxNoEntity record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table score_cert_maxno
     *
     * @mbggenerated
     */
    ScoreCertMaxNoEntity selectByPrimaryKey(Long id);

    /**
     * 
     * 
     * @param certYear
     * @return
     */
    ScoreCertMaxNoEntity selectMaxNoForUpdate(String certYear);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table score_cert_maxno
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(ScoreCertMaxNoEntity record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table score_cert_maxno
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(ScoreCertMaxNoEntity record);
}