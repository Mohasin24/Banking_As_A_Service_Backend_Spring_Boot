package com.account_service.servicesImpl;

import com.account_service.entity.ProcessedEvents;
import com.account_service.repository.ProcessedEventsRepo;
import com.account_service.service.ProcessedEventsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProcessedEventsServiceImpl implements ProcessedEventsService
{
    private final ProcessedEventsRepo processedEventsRepo;

    @Autowired
    public ProcessedEventsServiceImpl(ProcessedEventsRepo processedEventsRepo){
        this.processedEventsRepo=processedEventsRepo;
    }

    @Override
    public ProcessedEvents getProcessedEvent(String eventId) {
        return processedEventsRepo.findByEventId(eventId);
    }

    @Override
    public List<ProcessedEvents> getAllProcessedEvents(String userId) {
        return processedEventsRepo.findAllByUserId(userId);
    }

    @Override
    public ProcessedEvents saveProcessedEvent(ProcessedEvents events) {
        return processedEventsRepo.saveAndFlush(events);
    }

    @Override
    public Boolean checkEventExists(String eventId) {
        return processedEventsRepo.existsByEventId(eventId);
    }
}
