package com.batch.SpringBatchTasklet.steps;

import com.batch.SpringBatchTasklet.persistence.entities.Person;
import com.batch.SpringBatchTasklet.service.IPersonService;
import com.batch.SpringBatchTasklet.util.EmailSending;
import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Slf4j
public class ItemWriterStep implements Tasklet {

    @Autowired
    private IPersonService personService;

    @Autowired
    private EmailSending emailSending;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {

        log.info("-------------------> Inicio del paso de escritura <-------------------");

        List<Person> personList = (List<Person>) chunkContext.getStepContext().getStepExecution().getJobExecution().getExecutionContext().get("personList");

        personList.forEach(p -> {
            if (p != null) {
                try {
                    emailSending.sendEmail(p);
                } catch (MessagingException e) {
                    throw new RuntimeException(e);
                }
                log.info(p.toString());
            }
        });

        personService.saveAll(personList);

        log.info("-------------------> Fin del paso de escritura <-------------------");
        return RepeatStatus.FINISHED;
    }

}
