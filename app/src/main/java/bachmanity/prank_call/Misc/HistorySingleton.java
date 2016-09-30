package bachmanity.prank_call.Misc;

import java.util.ArrayList;
import java.util.List;

import bachmanity.prank_call.API.Models.History;

public class HistorySingleton {
    private static HistorySingleton instance;
    private boolean load;
    private List<History> historyList;

    public static HistorySingleton getInstance() {
        if (instance == null) {
            instance = new HistorySingleton();
        }

        return instance;
    }

    private HistorySingleton() {
        historyList = new ArrayList<>();
        load = true;
    }


    //cache only a page
    public boolean cacheHistoryPage(List<History> historyPage) {
        if (historyList.size() == 0 && historyPage.size() == 0)
            return false;

        historyList.addAll(historyPage);
        return true;
    }

    //when cached history size < 10 and making a call locally
    public List<History> addHistoryPage(List<History> historyPage) {
        int lastIndex = historyList.size() -1;

        if (historyList.get(lastIndex).getTimestamp().equals("dud")) {
            historyList.remove(historyList.size() - 1);
        }

        historyList.addAll(historyPage);
        return this.historyList;
    }

    public List<History> refreshHistory(List<History> historyPage) {
        historyList.clear();
        historyList.addAll(historyPage);
        return this.historyList;
    }

    public boolean loadMore() {
        return historyList.get(historyList.size() - 1).getTimestamp().equals("dud");
    }

    public void deleteHistory() {
        historyList.clear();
    }

    public boolean isLoad() {
        return load;
    }

    public void setLoad(boolean load) {
        this.load = load;
    }

    public List<History> getHistoryList() {
        return historyList;
    }

    public void setHistoryList(List<History> historyList) {
        this.historyList = historyList;
    }
}
