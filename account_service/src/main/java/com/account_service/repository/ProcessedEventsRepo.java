package com.account_service.repository;

import com.account_service.entity.ProcessedEvents;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProcessedEventsRepo extends JpaRepository<ProcessedEvents, Long> {
    ProcessedEvents findByEventId(String eventId);
    List<ProcessedEvents> findAllByUserId(String userId);
    Boolean existsByEventId(String eventId);
}
