package ru.dankoy.otus.jpql.core.model;

import javax.persistence.*;

/**
 * @author ezelenin
 */
@Entity
@Table(name = "tPhones")
public class PhoneDataSet {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private long id;

    private String number;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private User user;

}
