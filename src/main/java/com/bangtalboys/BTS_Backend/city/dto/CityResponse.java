package com.bangtalboys.BTS_Backend.city.dto;

import com.bangtalboys.BTS_Backend.city.domain.City;
import com.bangtalboys.BTS_Backend.district.domain.District;
import com.bangtalboys.BTS_Backend.district.dto.DistrictResponse;
import jakarta.persistence.ElementCollection;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class CityResponse {

    private Long id;
    private String name;
    @ElementCollection
    private List<DistrictResponse> districtList;

    public CityResponse(City city, List<District> filteredDistricts) {
        this.id = city.getId();
        this.name = city.getName();
        this.districtList = filteredDistricts.stream().map(DistrictResponse::new).collect(Collectors.toList());
    }

}
