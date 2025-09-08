package com.example.armtek_parcer_new.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ArmGoodInfo {
    private Long id;
    private String code;
    private String article;
    private String name;
    private String brand;
    private String width;
    private String height;
    private String length;
    private String weight;
    private String description;
    private String other;
    private String url;

    @Override
    public String toString() {
        return "ArmGoodInfo{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", article='" + article + '\'' +
                ", name='" + name + '\'' +
                ", brand='" + brand + '\'' +
                ", width='" + width + '\'' +
                ", height='" + height + '\'' +
                ", length='" + length + '\'' +
                ", weight='" + weight + '\'' +
                ", description='" + description + '\'' +
                ", other='" + other + '\'' +
                '}';
    }
}
