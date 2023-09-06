package com.phincon.laza.controller;


import com.phincon.laza.model.dto.response.RajaongkirAllProvinceResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/rajaongkir")
public class RajaongkirController {

    @Value("${rajaongkir.key}")
    private String RAJAONGKIR_KEY;

    @Value("${rajaongkir.province.url}")
    private String RAJAONGKIR_PROVINCE_URL;
    @Autowired
    private RestTemplate restTemplate;


    @GetMapping("/provinces")
    public ResponseEntity<RajaongkirAllProvinceResponse> findAllProvince(){
        HttpHeaders headers = new HttpHeaders();
        headers.add("key", RAJAONGKIR_KEY);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<RajaongkirAllProvinceResponse> consumeResponse = restTemplate.exchange(
                RAJAONGKIR_PROVINCE_URL,
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<RajaongkirAllProvinceResponse>() {
                }
        );

        RajaongkirAllProvinceResponse result = consumeResponse.getBody();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
