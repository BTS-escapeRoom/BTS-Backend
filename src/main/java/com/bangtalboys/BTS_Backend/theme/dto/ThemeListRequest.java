package com.bangtalboys.BTS_Backend.theme.dto;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ThemeListRequest {

    private String keyword;
    private Integer peoples;
    private Integer minDiff;
    private Integer maxDiff;
    private List<Long> genreIdList;
    private List<Long> districtIdList;
    private List<Long> cityIdList;
    private String sort;
    private Double latitude;
    private Double longitude;
    @Min(value = 1, message = "page 값은 1 이상이어야 합니다.")
    private Integer page;
}
