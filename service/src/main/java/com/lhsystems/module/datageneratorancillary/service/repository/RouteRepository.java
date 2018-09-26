package com.lhsystems.module.datageneratorancillary.service.repository;

import com.lhsystems.module.datageneratorancillary.service.data.Airport;
import com.lhsystems.module.datageneratorancillary.service.data.Route;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data repository used for save route entities
 */
@Repository
public interface RouteRepository extends JpaRepository<Route, Long> {

    /**
     * Query for checking that is existing route between airports
     *
     * @param originAirport
     *        origin airport
     * @param destinationAirport
     *        destination airport
     */
    @Query("Select r from Route r where r.destinationAirport =:destinationIata and r.originAirport =:originIata")
    Route isRouteExists(@Param("originIata") Airport originAirport, @Param("destinationIata") Airport destinationAirport);

}
