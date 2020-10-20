package ru.dankoy.otus.jetty.core.model;

import javax.persistence.*;

/**
 * @author ezelenin
 */
@Entity
@Table(name = "tPhones")
public class PhoneDataSet {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private long id;

    private String number;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    @Override
    public String toString() {
        return "PhoneDataSet{" +
                "id=" + id +
                ", number='" + number + '\'' +
//                ", user=" + user +
                '}';
    }

    public PhoneDataSet() {
    }

    public PhoneDataSet(String number) {

        this.number = number;

    }

}
