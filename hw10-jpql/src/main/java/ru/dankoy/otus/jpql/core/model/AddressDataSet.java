package ru.dankoy.otus.jpql.core.model;

import javax.persistence.*;

/**
 * @author ezelenin
 */
@Entity
@Table(name = "tAddress")
public class AddressDataSet {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private long id;

    private String street;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "AddressDataSet{" +
                "id=" + id +
                ", street='" + street + '\'' +
//                ", user=" + user +
                '}';
    }

    public AddressDataSet() {
    }

    public AddressDataSet(String street) {
        this.street = street;
    }

}
