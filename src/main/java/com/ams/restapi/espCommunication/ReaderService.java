package com.ams.restapi.espCommunication;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class ReaderService {

    @Autowired
    private SimpMessageSendingOperations messagingTemplate;

    private final Map<String, Reader> readers = new ConcurrentHashMap<>();

    public void broadcastUpdates() {
        messagingTemplate.convertAndSend("/topic/readers", getFilteredReaders(null));
    }

    public void updateLastPing(String readerId) {
        readers.put(readerId, new Reader(Instant.now(), readers.getOrDefault(readerId, new Reader(null, null)).getSectionId()));
        broadcastUpdates();
    }

    public List<Reader> getFilteredReaders(String sectionId) {
        Instant cutoffTime = Instant.now().minusSeconds(60); //can be adjusted
        return readers.entrySet().stream()
                .filter(entry -> entry.getValue().getLastPingTimestamp().isAfter(cutoffTime)
                        && (entry.getValue().getSectionId() == null || entry.getValue().getSectionId().equals(sectionId)))
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());
    }

    public void updateReaderSection(String readerId, String newSectionId) {
        if (readers.containsKey(readerId)) {
            Reader reader = readers.get(readerId);
            reader.setSectionId(newSectionId);
        }
        broadcastUpdates();
    }

    @Scheduled(fixedDelay = 60000)
    public void cleanupExpiredReaders() {
        Instant cutoffTime = Instant.now().minusSeconds(300); //5 minutes as expiration time
        readers.entrySet().removeIf(entry -> entry.getValue().getLastPingTimestamp().isBefore(cutoffTime));
        broadcastUpdates();
    }
}
