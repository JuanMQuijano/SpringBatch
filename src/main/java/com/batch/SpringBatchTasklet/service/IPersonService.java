package com.batch.SpringBatchTasklet.service;

import com.batch.SpringBatchTasklet.persistence.entities.Person;

import java.util.List;

public interface IPersonService {

    void saveAll(List<Person> personList);

}
