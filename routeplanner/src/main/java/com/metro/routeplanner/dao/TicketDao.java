package com.metro.routeplanner.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.metro.routeplanner.model.Ticket;

@Repository
public interface TicketDao extends JpaRepository<Ticket, String> {

}
