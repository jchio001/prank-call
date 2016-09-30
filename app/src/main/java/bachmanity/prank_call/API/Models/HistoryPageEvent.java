package bachmanity.prank_call.API.Models;

import java.util.List;

public class HistoryPageEvent {
    private String eventType;
    private List<History> historyPage;

    public HistoryPageEvent(String eventType, List<History> historyPage) {
        this.eventType = eventType;
        this.historyPage = historyPage;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public List<History> getHistoryPage() {
        return historyPage;
    }

    public void setHistoryPage(List<History> historyPage) {
        this.historyPage = historyPage;
    }
}
