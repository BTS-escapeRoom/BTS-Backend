package com.bangtalboys.BTS_Backend.store.service;

import com.bangtalboys.BTS_Backend.config.error.exception.NotFoundException;
import com.bangtalboys.BTS_Backend.store.domain.Store;
import com.bangtalboys.BTS_Backend.store.dto.StoreListResponse;
import com.bangtalboys.BTS_Backend.store.dto.StoreResponse;
import com.bangtalboys.BTS_Backend.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StoreService {

    private final StoreRepository storeRepository;

    public List<StoreListResponse> getAllStore() {
        List<Store> stores = storeRepository.findAll();
        return stores.stream().map(StoreListResponse::new).collect(Collectors.toList());
    }

    public StoreResponse getOneStore(Long id) {
        Optional<Store> store = storeRepository.findById(id);
        return store.map(StoreResponse::new).orElseThrow(NotFoundException::new);
    }
}
