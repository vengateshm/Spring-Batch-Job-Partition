package com.vengateshm.springbatchsample.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "JOB_INFO")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class JobInfo {
    @Id
    @Column(name = "JOB_ID")
    public int id;
    @Column(name = "WORK_YEAR")
    public String workYear;
    @Column(name = "EXPERIENCE_LEVEL")
    public String experienceLevel;
    @Column(name = "EMPLOYMENT_TYPE")
    public String employmentType;
    @Column(name = "JOB_TITLE")
    public String jobTitle;
    @Column(name = "SALARY")
    public String salary;
    @Column(name = "SALARY_CURRENCY")
    public String salaryCurrency;
    @Column(name = "SALARY_IN_USD")
    public String salaryInUsd;
    @Column(name = "EMPLOYEE_RESIDENCE")
    public String employeeResidence;
    @Column(name = "REMOTE_RATIO")
    public String remoteRatio;
    @Column(name = "COMPANY_LOCATION")
    public String companyLocation;
    @Column(name = "COMPANY_SIZE")
    public String companySize;
}
