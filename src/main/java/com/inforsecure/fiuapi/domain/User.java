package com.inforsecure.fiuapi.domain;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Access(AccessType.FIELD)
@JsonIgnoreProperties
@Table(name="users",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "user_name"),
                @UniqueConstraint(columnNames = "email")
        })
public class User extends Audit {

    private static final long serialVersionUID = -1312828236169257851L;

    @Column(name="first_name")
    @Size(min= 4, max = 20)
    private String firstName;

    @Column(name="last_name")
    @Size(max = 20)
    private String lastName;

    @Column(name="user_name")
    @NotNull
    @Size(max = 30)
    private String userName;

    @Column(name="email")
    @NotBlank
    @Size(max = 60)
    @Email
    private String email;


    @Column(name="phone_no")
    @Size(max = 12)
    private String phoneNo;

    @Column(name="wealth_score")
    private int wealthScore;

}
