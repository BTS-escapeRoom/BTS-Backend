package com.bangtalboys.BTS_Backend.city.dto;

import com.bangtalboys.BTS_Backend.city.domain.City;
import com.bangtalboys.BTS_Backend.district.dto.DistrictResponse;
import jakarta.persistence.ElementCollection;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class CityResponse {

    private Long id;
    private String name;

    @ElementCollection
    private List<DistrictResponse> districtList;

    @Builder
    public CityResponse(City city) {
        this.id = city.getId();
        this.name = city.getName();
        this.districtList = city.getDistrictList().stream().map(DistrictResponse::new).collect(Collectors.toList());
    }

}
