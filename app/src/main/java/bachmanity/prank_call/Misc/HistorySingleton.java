package bachmanity.prank_call.Misc;

import java.util.ArrayList;
import java.util.List;

import bachmanity.prank_call.API.Models.History;

public class HistorySingleton {
    private static HistorySingleton instance;
    private List<History> historyList;
    private boolean loadFromServer;

    public static HistorySingleton getInstance() {
        if (instance == null) {
            instance = new HistorySingleton();
        }

        return instance;
    }

    private HistorySingleton() {
        historyList = new ArrayList<>();
        loadFromServer = true;
    }

    public boolean isEmpty() {
        return historyList.isEmpty();
    }

    public void cacheHistoryPage(List<History> historyPage) {
        historyList.clear();
        historyList.addAll(historyPage);
    }

    public void deleteHistory() {
        historyList.clear();
    }

    public List<History> getHistoryList() {
        return historyList;
    }

    public boolean isLoadFromServer() {
        return loadFromServer;
    }

    public void setLoadFromServer(boolean loadFromServer) {
        this.loadFromServer = loadFromServer;
    }

    public void setHistoryList(List<History> historyList) {
        this.historyList = historyList;
    }
}
