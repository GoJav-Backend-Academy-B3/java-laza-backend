package com.phincon.laza.model.dto.response;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class RajaongkirAllProvinceResponse {

    private Rajaongkir rajaongkir;
    public static class Rajaongkir{
        private List<Query> query;
        private Status status;
        private List<Province> results;
    }
    public static class Status{
        private Integer code;
        private String description;
    }

    public static class Query{
    }

    public static class Province{
        private String province_id;
        private String province;
    }
}
