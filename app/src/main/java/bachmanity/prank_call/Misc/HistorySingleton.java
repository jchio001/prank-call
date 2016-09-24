package bachmanity.prank_call.Misc;

import java.util.ArrayList;
import java.util.List;

import bachmanity.prank_call.API.Models.History;
import bachmanity.prank_call.API.RetrofitSingleton;

public class HistorySingleton {
    private static HistorySingleton instance;
    private boolean initialLoad;
    private List<History> historyList;

    public static HistorySingleton getInstance() {
        if (instance == null) {
            instance = new HistorySingleton();
        }

        return instance;
    }

    private HistorySingleton() {
        historyList = new ArrayList<>();
        initialLoad = true;
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
        historyList.addAll(historyPage);
        return this.historyList;
    }

    public void refreshHistory(List<History> historyPage) {
        historyList.clear();
        historyList.addAll(historyPage);
    }

    public boolean isInitialLoad() {
        return initialLoad;
    }

    public void setInitialLoad(boolean initialLoad) {
        this.initialLoad = initialLoad;
    }

    public List<History> getHistoryList() {
        return historyList;
    }

    public void setHistoryList(List<History> historyList) {
        this.historyList = historyList;
    }
}
