package com.metro.routeplanner.dao;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.metro.routeplanner.model.Station;

@Repository
public interface StationDao extends JpaRepository<Station, String> {

	@Query(value = "SELECT * FROM station s WHERE EXISTS (SELECT 1 FROM jsonb_array_elements(s.line_info) AS elem WHERE elem->>'lineName' = :lineName)", nativeQuery = true)
	List<Station> findStationsByLineName(@Param("lineName") String lineName);

    // Native query to find all stations for a specific line and order by the order index
    @Query(value = "SELECT * FROM station s, jsonb_array_elements(s.line_info) AS elem WHERE elem->>'lineName' = :lineName ORDER BY (elem->>'index')::int", nativeQuery = true)
    List<Station> findAllByLineOrderByOrderIndex(@Param("lineName") String lineName);

    // Native query to find stations that are on multiple lines
    @Query(value = "SELECT * FROM station s WHERE EXISTS (SELECT 1 FROM jsonb_array_elements(s.line_info) AS elem WHERE elem->>'lineName' IN (:lines))", nativeQuery = true)
    List<Station> findStationsByLines(@Param("lines") Set<String> lines);

    // JPA query to find station by name
    Optional<Station> findByName(String name);
}
