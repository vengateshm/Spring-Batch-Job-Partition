package com.vengateshm.springbatchsample.config;

import com.vengateshm.springbatchsample.entity.JobInfo;
import org.springframework.batch.item.ItemProcessor;

public class JobInfoItemProcessor implements ItemProcessor<JobInfo, JobInfo> {
    @Override
    public JobInfo process(JobInfo jobInfo) throws Exception {
        /*if (jobInfo.getCompanySize().equalsIgnoreCase("L"))
            return jobInfo;
        return null;*/
        return jobInfo;
    }
}
