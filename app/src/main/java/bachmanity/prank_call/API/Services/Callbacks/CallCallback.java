package bachmanity.prank_call.API.Services.Callbacks;

import org.greenrobot.eventbus.EventBus;

import bachmanity.prank_call.Misc.APIConstants;
import bachmanity.prank_call.Misc.Constants;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by Jonathan Chiou on 8/25/2016.
 */
public class CallCallback implements Callback<Void> {

    @Override
    public void onResponse(Call<Void> call, Response<Void> response) {
        switch (response.code()) {
            case APIConstants.HTTP_STATUS_OK:
                EventBus.getDefault().post(Constants.MADE_CALL);
                break;
        }
    }

    //MODIFY THIS LATER
    @Override
    public void onFailure(Call<Void> call, Throwable t) {
        EventBus.getDefault().post("ERROR");
    }

}
