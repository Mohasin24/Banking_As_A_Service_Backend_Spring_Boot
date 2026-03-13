package com.account_service.service;

import com.account_service.entity.ProcessedEvents;

import java.util.List;

public interface ProcessedEventsService
{
    ProcessedEvents getProcessedEvent(String eventId);
    List<ProcessedEvents> getAllProcessedEvents(String userId);
    ProcessedEvents saveProcessedEvent(ProcessedEvents events);
    Boolean checkEventExists(String eventId);
}
