package ru.dankoy.otus.jpql.core.model;

import javax.persistence.*;

/**
 * @author ezelenin
 */
@Entity
@Table(name = "tAddress")
public class AddressDataSet {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private long id;

    private String street;

    @OneToOne(mappedBy = "address", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private User user;

}
