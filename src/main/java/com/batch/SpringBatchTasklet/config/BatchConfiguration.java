package com.batch.SpringBatchTasklet.config;

import com.batch.SpringBatchTasklet.steps.ItemDecompressStep;
import com.batch.SpringBatchTasklet.steps.ItemProcessorStep;
import com.batch.SpringBatchTasklet.steps.ItemReaderStep;
import com.batch.SpringBatchTasklet.steps.ItemWriterStep;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

    @Bean
    @JobScope
    public ItemDecompressStep itemDecompressStep() {
        return new ItemDecompressStep();
    }

    @Bean
    @JobScope
    public ItemReaderStep itemReaderStep() {
        return new ItemReaderStep();
    }

    @Bean
    @JobScope
    public ItemProcessorStep itemProcessorStep() {
        return new ItemProcessorStep();
    }

    @Bean
    @JobScope
    public ItemWriterStep itemWriterStep() {
        return new ItemWriterStep();
    }

    @Bean
    public Step decompressFileStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("decompressFileStep", jobRepository)
                .tasklet(itemDecompressStep(), transactionManager)
                .build();
    }

    @Bean
    public Step readFileStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("readFileStep", jobRepository)
                .tasklet(itemReaderStep(), transactionManager)
                .build();
    }

    @Bean
    public Step processDataStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("processDataStep", jobRepository)
                .tasklet(itemProcessorStep(), transactionManager)
                .build();
    }

    @Bean
    public Step writeDataStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("writeDataStep", jobRepository)
                .tasklet(itemWriterStep(), transactionManager)
                .build();
    }

    @Bean
    public Job readCSVJob(
            @Qualifier("decompressFileStep") Step decompressFileStep,
            @Qualifier("readFileStep") Step readFileStep,
            @Qualifier("processDataStep") Step processDataStep,
            @Qualifier("writeDataStep") Step writeDataStep,
            JobRepository jobRepository) {
        return new JobBuilder("readCSVJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .flow(decompressFileStep)
                .next(readFileStep)
                .next(processDataStep)
                .next(writeDataStep)
                .end().build();
    }
}

