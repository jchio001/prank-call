package bachmanity.prank_call.API.Services.Callbacks;

import org.greenrobot.eventbus.EventBus;

import bachmanity.prank_call.Misc.APIConstants;
import bachmanity.prank_call.Misc.SPSingleton;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivationCallback implements Callback<Void> {
    @Override
    public void onResponse(Call<Void> call, Response<Void> response) {
        System.out.println(response.code());
        switch (response.code()) {
            case APIConstants.HTTP_STATUS_OK:
                EventBus.getDefault().post(true);
                break;
            default:
                EventBus.getDefault().post(false);
                break;
        }
    }

    @Override
    public void onFailure(Call<Void> call, Throwable t) {
        EventBus.getDefault().post(false);
    }
}
