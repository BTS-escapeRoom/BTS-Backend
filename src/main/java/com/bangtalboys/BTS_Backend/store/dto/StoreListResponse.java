package com.bangtalboys.BTS_Backend.store.dto;

import com.bangtalboys.BTS_Backend.store.domain.Store;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class StoreListResponse {

    private Long id;
    private String name;
    private String thumbnail;
    private String location;

    @Builder
    public StoreListResponse(Store store) {
        this.id = store.getId();
        this.name = store.getName();
        this.thumbnail = store.getThumbnail();
        this.location = store.getLocation();
    }
}
