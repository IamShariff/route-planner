package com.metro.routeplanner.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.metro.routeplanner.model.Line;

@Repository
public interface LineDao extends JpaRepository<Line, String> {

	boolean existsByName(String name);

}
