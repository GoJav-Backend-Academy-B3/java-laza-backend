package com.phincon.laza.model.entity;


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
    private String province_id;
    private String province;
    @OneToMany(mappedBy="provinces")
    private List<City> cities;

}
