package com.example.SpringDemoBotParfums.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.sql.Timestamp;
@Data
@Entity
@Table(name = "users_data_table")
public class User {

    @Id
    private Long ChatId;
    private String firstName;
    private String lastName;
    private String userName;
    private String phone;
    private Timestamp registeredAt;


    @Override
    public String toString() {
        return "User{" +
                "ChatId=" + ChatId +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", userName='" + userName + '\'' +
                ", phone='" + phone + '\'' +
                ", registeredAt=" + registeredAt +
                '}';
    }
}

