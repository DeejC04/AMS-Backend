package com.ams.restapi.espCommunication;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Controller
@RequestMapping("/readers")
public class ReaderController {
    
    @Autowired
    private ReaderService readerService;

    //TODO: Add some service that ensures readerId and sectionId are valid

    @MessageMapping("/readerStatus")
    public void readerStatus(@Header("simpSessionId") String sessionId, @RequestBody(required = false) String sectionId) {
        readerService.broadcastUpdates();
    }

    @PostMapping
    public ResponseEntity<?> pingReader(@RequestParam(required = true) String readerId) {
        readerService.updateLastPing(readerId);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<Reader>> getReaders(@RequestParam(required = false) String sectionId) {
        return ResponseEntity.ok(readerService.getFilteredReaders(sectionId));
    }

    @PutMapping
    public ResponseEntity<?> updateReaderSection(@RequestParam(required = true) String readerId, @RequestParam(required = true) String sectionId) {
        readerService.updateReaderSection(readerId, sectionId);
        return ResponseEntity.ok().build();
    }
}
