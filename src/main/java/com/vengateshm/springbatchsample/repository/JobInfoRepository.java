package com.vengateshm.springbatchsample.repository;

import com.vengateshm.springbatchsample.entity.JobInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobInfoRepository extends JpaRepository<JobInfo, Integer> {
}
