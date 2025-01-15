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

    /*
     *
     * Creamos Beans con cada uno de los pasos que habiamos creado previamente para inyectarlos posteriormente
     *
     * */

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

    /*
     *
     * Creamos la ejecución de cada uno de los pasos
     *
     * */

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

    /*
     *
     *  Establecemos el job enviando como parametros cada uno de los pasos que va a ejecutar y retornamos el job construido con el orden en el que se
     *  ejecutarán los pasos, primero descomprime, luego lee, luego procesa y por último escribe en BBDD
     *
     * */

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

