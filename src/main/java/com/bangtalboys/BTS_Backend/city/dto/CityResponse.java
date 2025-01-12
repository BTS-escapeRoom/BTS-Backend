package com.bangtalboys.BTS_Backend.city.dto;

import com.bangtalboys.BTS_Backend.city.domain.City;
import com.bangtalboys.BTS_Backend.city.domain.District;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class CityResponse {

    private Long id;
    private String name;
    private List<DistrictResponse> districtList;

    public CityResponse(City city, List<District> filteredDistricts) {
        this.id = city.getId();
        this.name = city.getName();
        this.districtList = filteredDistricts.stream().map(DistrictResponse::new).collect(Collectors.toList());
    }

}
