package com.efida.sports.dmp.dao.entity;

import java.util.Date;

public class ReportMatchSourceEntity {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column report_match_source.id
     *
     * @mbggenerated
     */
    private Integer id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column report_match_source.match_code
     *
     * @mbggenerated
     */
    private String  matchCode;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column report_match_source.match_name
     *
     * @mbggenerated
     */
    private String  matchName;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column report_match_source.unit_code
     *
     * @mbggenerated
     */
    private String  unitCode;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column report_match_source.unit_name
     *
     * @mbggenerated
     */
    private String  unitName;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column report_match_source.enroll_total_count
     *
     * @mbggenerated
     */
    private Integer enrollTotalCount;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column report_match_source.organizer_count
     *
     * @mbggenerated
     */
    private Integer organizerCount;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column report_match_source.official_count
     *
     * @mbggenerated
     */
    private Integer officialCount;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column report_match_source.create_date
     *
     * @mbggenerated
     */
    private Date    applyTime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column report_match_source.create_date_day
     *
     * @mbggenerated
     */
    private Date    applyTimeDay;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column report_match_source.gmt_create
     *
     * @mbggenerated
     */
    private Date    gmtCreate;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column report_match_source.gmt_modified
     *
     * @mbggenerated
     */
    private Date    gmtModified;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column report_match_source.id
     *
     * @return the value of report_match_source.id
     *
     * @mbggenerated
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column report_match_source.id
     *
     * @param id the value for report_match_source.id
     *
     * @mbggenerated
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column report_match_source.match_code
     *
     * @return the value of report_match_source.match_code
     *
     * @mbggenerated
     */
    public String getMatchCode() {
        return matchCode;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column report_match_source.match_code
     *
     * @param matchCode the value for report_match_source.match_code
     *
     * @mbggenerated
     */
    public void setMatchCode(String matchCode) {
        this.matchCode = matchCode == null ? null : matchCode.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column report_match_source.match_name
     *
     * @return the value of report_match_source.match_name
     *
     * @mbggenerated
     */
    public String getMatchName() {
        return matchName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column report_match_source.match_name
     *
     * @param matchName the value for report_match_source.match_name
     *
     * @mbggenerated
     */
    public void setMatchName(String matchName) {
        this.matchName = matchName == null ? null : matchName.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column report_match_source.unit_code
     *
     * @return the value of report_match_source.unit_code
     *
     * @mbggenerated
     */
    public String getUnitCode() {
        return unitCode;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column report_match_source.unit_code
     *
     * @param unitCode the value for report_match_source.unit_code
     *
     * @mbggenerated
     */
    public void setUnitCode(String unitCode) {
        this.unitCode = unitCode == null ? null : unitCode.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column report_match_source.unit_name
     *
     * @return the value of report_match_source.unit_name
     *
     * @mbggenerated
     */
    public String getUnitName() {
        return unitName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column report_match_source.unit_name
     *
     * @param unitName the value for report_match_source.unit_name
     *
     * @mbggenerated
     */
    public void setUnitName(String unitName) {
        this.unitName = unitName == null ? null : unitName.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column report_match_source.enroll_total_count
     *
     * @return the value of report_match_source.enroll_total_count
     *
     * @mbggenerated
     */
    public Integer getEnrollTotalCount() {
        return enrollTotalCount;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column report_match_source.enroll_total_count
     *
     * @param enrollTotalCount the value for report_match_source.enroll_total_count
     *
     * @mbggenerated
     */
    public void setEnrollTotalCount(Integer enrollTotalCount) {
        this.enrollTotalCount = enrollTotalCount;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column report_match_source.organizer_count
     *
     * @return the value of report_match_source.organizer_count
     *
     * @mbggenerated
     */
    public Integer getOrganizerCount() {
        return organizerCount;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column report_match_source.organizer_count
     *
     * @param organizerCount the value for report_match_source.organizer_count
     *
     * @mbggenerated
     */
    public void setOrganizerCount(Integer organizerCount) {
        this.organizerCount = organizerCount;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column report_match_source.official_count
     *
     * @return the value of report_match_source.official_count
     *
     * @mbggenerated
     */
    public Integer getOfficialCount() {
        return officialCount;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column report_match_source.official_count
     *
     * @param officialCount the value for report_match_source.official_count
     *
     * @mbggenerated
     */
    public void setOfficialCount(Integer officialCount) {
        this.officialCount = officialCount;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column report_match_source.apply_time
     *
     * @return the value of report_match_source.apply_time
     *
     * @mbggenerated
     */
    public Date getApplyTime() {
        return applyTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column report_match_source.apply_time
     *
     * @param createDate the value for report_match_source.apply_time
     *
     * @mbggenerated
     */
    public void setApplyTime(Date applyTime) {
        this.applyTime = applyTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column report_match_source.apply_time_day
     *
     * @return the value of report_match_source.apply_time_day
     *
     * @mbggenerated
     */
    public Date getApplyTimeDay() {
        return applyTimeDay;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column report_match_source.apply_time_day
     *
     * @param createDateDay the value for report_match_source.apply_time_day
     *
     * @mbggenerated
     */
    public void setApplyTimeDay(Date applyTimeDay) {
        this.applyTimeDay = applyTimeDay;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column report_match_source.gmt_create
     *
     * @return the value of report_match_source.gmt_create
     *
     * @mbggenerated
     */
    public Date getGmtCreate() {
        return gmtCreate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column report_match_source.gmt_create
     *
     * @param gmtCreate the value for report_match_source.gmt_create
     *
     * @mbggenerated
     */
    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column report_match_source.gmt_modified
     *
     * @return the value of report_match_source.gmt_modified
     *
     * @mbggenerated
     */
    public Date getGmtModified() {
        return gmtModified;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column report_match_source.gmt_modified
     *
     * @param gmtModified the value for report_match_source.gmt_modified
     *
     * @mbggenerated
     */
    public void setGmtModified(Date gmtModified) {
        this.gmtModified = gmtModified;
    }
}