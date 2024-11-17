package com.bangtalboys.BTS_Backend.store.controller;

import com.bangtalboys.BTS_Backend.store.dto.StoreListResponse;
import com.bangtalboys.BTS_Backend.store.dto.StoreResponse;
import com.bangtalboys.BTS_Backend.store.service.StoreService;
import com.bangtalboys.BTS_Backend.utils.response.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name="매장 API")
@RequestMapping("v1/stores")
public class StoreController {
    private final StoreService storeService;

    @Operation(summary = "매장 리스트 조회")
    @GetMapping("")
    public ResponseEntity<Response<List<StoreListResponse>>> getAllStore() {

        return ResponseEntity.ok(Response.ok(storeService.getAllStore()));
    }
    @Operation(summary = "매장 단건 조회")
    @GetMapping("/{id}")
    public ResponseEntity<Response<StoreResponse>> getStore(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(Response.ok(storeService.getOneStore(id)));
    }
}
