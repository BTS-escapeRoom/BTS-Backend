package com.bangtalboys.BTS_Backend.district.dto;

import com.bangtalboys.BTS_Backend.district.domain.District;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DistrictResponse {
    private Long id;
    private String name;
    private int storeCount;

    @Builder
    public DistrictResponse(District district) {
        this.id = district.getId();
        this.name = district.getName();
        this.storeCount = district.getStoreList().size();
    }
}
