package com.bangtalboys.BTS_Backend.store.dto;

import com.bangtalboys.BTS_Backend.store.domain.Store;
import com.bangtalboys.BTS_Backend.theme.dto.ThemeListResponse;
import jakarta.persistence.ElementCollection;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class StoreResponse extends Throwable {

    private Long id;
    private String name;
    private String thumbnail;
    private String description;
    private String location;
    private LocalDateTime registrationDate;
    @ElementCollection
    private List<ThemeListResponse> themeList;

    @Builder
    public StoreResponse(Store store) {
        this.id = store.getId();
        this.name = store.getName();
        this.thumbnail = store.getThumbnail();
        this.description = store.getDescription();
        this.location = store.getLocation();
        this.registrationDate = store.getRegistrationDate();
        this.themeList = store.getThemeList().stream().map(ThemeListResponse::new).collect(Collectors.toList());
    }
}
