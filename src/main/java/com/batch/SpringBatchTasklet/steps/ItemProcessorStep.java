package com.batch.SpringBatchTasklet.steps;

import com.batch.SpringBatchTasklet.persistence.entities.Person;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
public class ItemProcessorStep implements Tasklet {

    /*
     * Este paso obtiene la lista del contexto global, establece un valor nuevo para cada uno de los registros que hay en la lista
     * y actualiza la lista en el contexto global
     *
     * */

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {

        log.info("-------------------> Inicio del paso de procesamiento <-------------------");

        List<Person> personList = (List<Person>) chunkContext
                .getStepContext()
                .getStepExecution()
                .getJobExecution()
                .getExecutionContext()
                .get("personList");

        List<Person> personFinalList = personList.stream().map(p -> {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
            p.setInsertionDate(formatter.format(LocalDateTime.now()));
            return p;
        }).toList();

        chunkContext.getStepContext()
                .getStepExecution()
                .getJobExecution()
                .getExecutionContext()
                .put("personList", personFinalList);

        log.info("-------------------> Fin del paso de procesamiento <-------------------");
        return RepeatStatus.FINISHED;
    }

}
