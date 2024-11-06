package com.bangtalboys.BTS_Backend.district.dto;

import com.bangtalboys.BTS_Backend.district.domain.District;
import lombok.Data;

@Data
public class DistrictResponse {
    private Long id;
    private String name;
    private int storeCount;

    public DistrictResponse(District district) {
        this.id = district.getId();
        this.name = district.getName();
        this.storeCount = district.getStoreList().size();
    }
}
