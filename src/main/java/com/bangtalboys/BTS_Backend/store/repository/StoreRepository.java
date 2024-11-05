package com.bangtalboys.BTS_Backend.store.repository;

import com.bangtalboys.BTS_Backend.store.domain.Store;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreRepository extends JpaRepository<Store, Long> {
}
