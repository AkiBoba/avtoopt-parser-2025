package com.example.armtek_parcer_new.aliance.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "aliance_good_info")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AlianceGoodInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "producer")
    private String producer;
    @Column(name = "code")
    private String code;
    @Column(name = "article")
    private String article;
    @Column(name = "name")
    private String name;
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
    @Column(name = "width")
    private String width;
    @Column(name = "height")
    private String height;
    @Column(name = "length")
    private String length;
    @Column(name = "weight")
    private String weight;
    @Column(name = "code_tnved")
    private String codeTnVED;

}
