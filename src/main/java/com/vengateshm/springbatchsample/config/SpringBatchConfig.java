package com.vengateshm.springbatchsample.config;

import com.vengateshm.springbatchsample.entity.JobInfo;
import com.vengateshm.springbatchsample.partition.ColumnRangePartitioner;
import com.vengateshm.springbatchsample.repository.JobInfoRepository;
import lombok.AllArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.partition.PartitionHandler;
import org.springframework.batch.core.partition.support.TaskExecutorPartitionHandler;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableBatchProcessing
@AllArgsConstructor
public class SpringBatchConfig {

    private JobBuilderFactory jobBuilderFactory;

    private StepBuilderFactory stepBuilderFactory;

    private JobInfoRepository jobInfoRepository;

    private JobInfoWriter jobInfoWriter;

    @Bean
    public FlatFileItemReader<JobInfo> reader() {
        FlatFileItemReader<JobInfo> itemReader = new FlatFileItemReader<>();
        itemReader.setResource(new FileSystemResource("src/main/resources/ds_salaries.csv"));
        itemReader.setName("csvReader");
        itemReader.setLinesToSkip(1);
        itemReader.setLineMapper(lineMapper());
        return itemReader;
    }

    private LineMapper<JobInfo> lineMapper() {
        DefaultLineMapper<JobInfo> lineMapper = new DefaultLineMapper<>();
        DelimitedLineTokenizer delimitedLineTokenizer = new DelimitedLineTokenizer();
        delimitedLineTokenizer.setDelimiter(",");
        delimitedLineTokenizer.setStrict(false);
        delimitedLineTokenizer.setNames("id", "workYear", "experienceLevel", "employmentType", "jobTitle", "salary", "salaryCurrency", "salaryInUsd", "employeeResidence", "remoteRatio", "companyLocation", "companySize");

        BeanWrapperFieldSetMapper<JobInfo> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(JobInfo.class);

        lineMapper.setLineTokenizer(delimitedLineTokenizer);
        lineMapper.setFieldSetMapper(fieldSetMapper);
        return lineMapper;
    }

    @Bean
    public JobInfoItemProcessor processor() {
        return new JobInfoItemProcessor();
    }

    /*@Bean
    public RepositoryItemWriter<JobInfo> writer() {
        RepositoryItemWriter<JobInfo> writer = new RepositoryItemWriter<>();
        writer.setRepository(customerRepository);
        writer.setMethodName("save");
        return writer;
    }*/

    @Bean
    public ColumnRangePartitioner partitioner() {
        return new ColumnRangePartitioner();
    }

    @Bean
    public PartitionHandler partitionHandler() {
        TaskExecutorPartitionHandler handler = new TaskExecutorPartitionHandler();
        handler.setGridSize(2);
        handler.setTaskExecutor(taskExecutor());
        handler.setStep(slaveStep());
        return handler;
    }

    @Bean
    public Job runJob() {
        return jobBuilderFactory.get("importJobInfo")
                .flow(masterStep())
                .end().build();
    }

    @Bean
    public Step slaveStep() {
        StepBuilder builder = stepBuilderFactory.get("csv-step");
        return builder
                .<JobInfo, JobInfo>chunk(300)
                .reader(reader())
                .processor(processor())
                .writer(/*writer()*/jobInfoWriter)
                .taskExecutor(taskExecutor())
                .build();
    }

    @Bean
    public Step masterStep() {
        StepBuilder builder = stepBuilderFactory.get("master-step");
        return builder
                .partitioner(slaveStep().getName(), partitioner())
                .partitionHandler(partitionHandler())
                .build();
    }

    @Bean
    public TaskExecutor taskExecutor() {
        /*SimpleAsyncTaskExecutor asyncTaskExecutor = new SimpleAsyncTaskExecutor();
        asyncTaskExecutor.setConcurrencyLimit(10);
        return asyncTaskExecutor;*/
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setMaxPoolSize(4);
        taskExecutor.setCorePoolSize(4);
        taskExecutor.setQueueCapacity(4);
        return taskExecutor;
    }
}
