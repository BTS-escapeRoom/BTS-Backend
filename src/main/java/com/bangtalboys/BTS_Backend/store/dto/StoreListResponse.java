package com.bangtalboys.BTS_Backend.store.dto;

import com.bangtalboys.BTS_Backend.store.domain.Store;
import lombok.Data;


@Data
public class StoreListResponse {

    private Long id;
    private String name;
    private String thumbnail;
    private String location;

    public StoreListResponse(Store store) {
        this.id = store.getId();
        this.name = store.getName();
        this.thumbnail = store.getThumbnail();
        this.location = store.getLocation();
    }
}
