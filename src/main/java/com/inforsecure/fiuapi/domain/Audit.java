package com.inforsecure.fiuapi.domain;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@MappedSuperclass
public class Audit implements Serializable{

    private static final long serialVersionUID = -7334190359914963456L;

    @Id
    @Column(name= "id", columnDefinition = "uuid", updatable = false)
    @GeneratedValue
    @org.hibernate.annotations.Type(type="pg-uuid")
    private UUID Id;

    @Column(name= "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Version
    @Column(name="version")
    private long version;


    @PrePersist
    public void prePersist(){
        LocalDateTime utcTime = LocalDateTime.now(Clock.systemUTC());
        System.out.println(utcTime);
        this.createdAt = this.createdAt == null ? utcTime: this.createdAt;
        this.updatedAt = this.updatedAt == null ? utcTime: this.updatedAt;
    }

    @PreUpdate
    public void preUpdate(){
        this.updatedAt = LocalDateTime.now(Clock.systemUTC());
    }

}
