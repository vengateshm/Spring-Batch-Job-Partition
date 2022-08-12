package com.vengateshm.springbatchsample.config;

import com.vengateshm.springbatchsample.entity.JobInfo;
import com.vengateshm.springbatchsample.repository.JobInfoRepository;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class JobInfoWriter implements ItemWriter<JobInfo> {

    @Autowired
    private JobInfoRepository jobInfoRepository;

    @Override
    public void write(List<? extends JobInfo> list) throws Exception {
        System.out.println("Thread name : " + Thread.currentThread().getName());
        jobInfoRepository.saveAll(list);
    }
}
