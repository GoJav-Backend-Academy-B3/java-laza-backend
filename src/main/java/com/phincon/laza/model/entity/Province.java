package com.phincon.laza.model.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.*;

import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@Table(name = "provinces")
@Entity
public class Province {

    @Id
    private String provinceId;
    private String province;

    @OneToMany(mappedBy="provinces")
    @JsonBackReference
    private List<City> cities;

}
