package com.batch.SpringBatchTasklet.persistence.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;

@Entity
@Table(name = "persons")
@Data
public class Person implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(name = "last_name")
    private String lastName;

    private String email;

    private int age;

    @Column(name = "insertion_date")
    private String insertionDate;

}
