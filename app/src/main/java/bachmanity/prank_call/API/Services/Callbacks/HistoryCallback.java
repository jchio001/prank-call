package bachmanity.prank_call.API.Services.Callbacks;

import org.greenrobot.eventbus.EventBus;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.List;

import bachmanity.prank_call.API.Models.History;
import bachmanity.prank_call.Misc.APIConstants;
import bachmanity.prank_call.Misc.Constants;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by jman0_000 on 9/22/2016.
 */
public class HistoryCallback implements Callback<List<History>> {

    @Override
    public void onResponse(Call<List<History>> call, Response<List<History>> resp) {
        System.out.println(resp.code());
        switch (resp.code()) {
            case APIConstants.HTTP_STATUS_OK:
                EventBus.getDefault().post(resp.body());
                break;
            default:
                System.out.println(resp.code());
                EventBus.getDefault().post(Constants.HISTORY_FAILED);
                break;
        }
    }

    @Override
    public void onFailure(Call<List<History>> call, Throwable t) {
        final Writer result = new StringWriter();
        final PrintWriter printWriter = new PrintWriter(result);
        t.printStackTrace(printWriter);
        System.out.println(result.toString());
        EventBus.getDefault().post(Constants.HISTORY_FAILED);
    }

}
