package com.example.armtek_parcer_new.aliance.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "producers")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Producer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name")
    private String name;
    @Column(name = "clean_name")
    private String cleanName;
    @Column(name = "checked")
    private boolean checked;
    @Column(name = "url")
    private String url;
    @ManyToOne
//    @ToString.Exclude
    @JoinColumn(name = "owner_id")
    private Owner owner;
}
