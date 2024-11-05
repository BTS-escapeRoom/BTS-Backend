package com.bangtalboys.BTS_Backend.city.repository;

import com.bangtalboys.BTS_Backend.city.domain.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CityRepository extends JpaRepository<City, Long> {
    @Query("SELECT DISTINCT c FROM City c " +
            "JOIN c.districtList d " +
            "JOIN d.storeList s ")
    List<City> findAllCitiesWithDistrictsHavingStores();
}
