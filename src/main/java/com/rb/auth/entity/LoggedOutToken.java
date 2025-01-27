package com.rb.auth.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "logged_out_tokens")
@Data
public class LoggedOutToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer Id;
    @Column(length = 100000)
    private String token;
}
