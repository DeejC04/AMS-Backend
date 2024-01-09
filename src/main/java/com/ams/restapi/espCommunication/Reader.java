package com.ams.restapi.espCommunication;

import java.time.Instant;

class Reader {
    private Instant lastPingTimestamp;
    private String sectionId;

    public Reader(Instant lastPingTimestamp, String sectionId) {
        this.lastPingTimestamp = lastPingTimestamp;
        this.sectionId = sectionId;
    }

    public Instant getLastPingTimestamp() {
        return lastPingTimestamp;
    }

    public void setLastPingTimestamp(Instant lastPingTimestamp) {
        this.lastPingTimestamp = lastPingTimestamp;
    }

    public String getSectionId() {
        return sectionId;
    }

    public void setSectionId(String sectionId) {
        this.sectionId = sectionId;
    }
}
