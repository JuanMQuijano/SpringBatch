package com.batch.SpringBatchTasklet.persistence.repositories;

import com.batch.SpringBatchTasklet.persistence.entities.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IPersonRepository extends JpaRepository<Person, Long> {
}
