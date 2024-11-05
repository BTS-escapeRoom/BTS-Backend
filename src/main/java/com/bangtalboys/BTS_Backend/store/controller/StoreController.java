package com.bangtalboys.BTS_Backend.store.controller;

import com.bangtalboys.BTS_Backend.config.error.exception.BusinessBaseException;
import com.bangtalboys.BTS_Backend.store.dto.StoreListResponse;
import com.bangtalboys.BTS_Backend.store.dto.StoreResponse;
import com.bangtalboys.BTS_Backend.store.service.StoreService;
import com.bangtalboys.BTS_Backend.utils.Response.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("v1/stores")
public class StoreController {
    private final StoreService storeService;

    @GetMapping("")
    public ResponseEntity<Response<List<StoreListResponse>>> getAllStore() {

        try {
            List<StoreListResponse> storeListResponses = storeService.getAllStore();
            return ResponseEntity.ok(Response.ok(storeListResponses));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Response.error("Internal Server Error", "500"));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response<StoreResponse>> getStore(
            @PathVariable Long id
    ) {

        try {
            StoreResponse storeResponse = storeService.getOneStore(id);
            return ResponseEntity.ok(Response.ok(storeResponse));
        } catch (BusinessBaseException e) {
            return ResponseEntity.status(e.getErrorCode().getStatus())
                    .body(Response.error(e.getMessage(), e.getErrorCode().getCode()));
        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Response.error("Internal Server Error", "500"));
        }
    }
}
