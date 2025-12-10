package com.bangtalboys.BTS_Backend.board.dto.request;

import com.bangtalboys.BTS_Backend.utils.enums.BoardType;
import com.bangtalboys.BTS_Backend.utils.enums.SortType;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BoardListRequest {
    private String keyword;
    private boolean isRecruiting;
    private BoardType boardType;
    private SortType sortType;
    @Min(value = 1, message =  "page 값은 1 이상이어야 합니다.")
    private Integer page;
}
