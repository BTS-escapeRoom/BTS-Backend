package com.bangtalboys.BTS_Backend.city.service;

import com.bangtalboys.BTS_Backend.city.domain.City;
import com.bangtalboys.BTS_Backend.city.dto.CityResponse;
import com.bangtalboys.BTS_Backend.city.repository.CityRepository;
import com.bangtalboys.BTS_Backend.config.error.exception.NotFoundException;
import com.bangtalboys.BTS_Backend.city.domain.District;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CityService {
    private final CityRepository cityRepository;

    public List<CityResponse> getAllCity() {
        List<City> cities = cityRepository.findAllCitiesWithDistrictsHavingStores();
        return cities.stream()
                .map(city -> {
                    // District를 필터링하여 Store가 있는 District만 남기기
                    List<District> filteredDistricts = city.getDistrictList().stream()
                            .filter(district -> !district.getStoreList().isEmpty())
                            .collect(Collectors.toList());

                    // 필터링된 District를 CityResponse에 설정
                    return new CityResponse(city, filteredDistricts);
                })
                .filter(cityResponse -> !cityResponse.getDistrictList().isEmpty())
                .collect(Collectors.toList());
    }

    public CityResponse getOneCity(Long id) {
        Optional<City> city = cityRepository.findById(id);
        return city
                .map(c -> {
                    List<District> filteredDistrict = c.getDistrictList().stream()
                            .filter(district -> !district.getStoreList().isEmpty())
                            .collect(Collectors.toList());

                    return new CityResponse(c, filteredDistrict);
                })
                .orElseThrow(NotFoundException::new);
    }
}
