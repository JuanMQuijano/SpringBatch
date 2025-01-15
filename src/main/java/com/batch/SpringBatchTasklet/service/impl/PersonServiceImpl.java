package com.batch.SpringBatchTasklet.service.impl;

import com.batch.SpringBatchTasklet.persistence.entities.Person;
import com.batch.SpringBatchTasklet.persistence.repositories.IPersonRepository;
import com.batch.SpringBatchTasklet.service.IPersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PersonServiceImpl implements IPersonService {

    @Autowired
    private IPersonRepository personRepository;

    @Override
    @Transactional
    public void saveAll(List<Person> personList) {
        personRepository.saveAll(personList);
    }
}
