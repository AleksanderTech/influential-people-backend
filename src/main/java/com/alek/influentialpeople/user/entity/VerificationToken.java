package com.alek.influentialpeople.user.entity;

import lombok.*;

import javax.persistence.*;

import java.util.Date;

@Entity
@Table
@Getter
@Setter
@Builder
@AllArgsConstructor
public class VerificationToken {

    private static final int VALIDITY_TIME = 24 * 60 * 60 * 1000;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String value;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "username", referencedColumnName = "username")
    private User user;

    private Date expireDate;
}
