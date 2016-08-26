package bachmanity.prank_call.API.Services.Callbacks;

import org.greenrobot.eventbus.EventBus;

import bachmanity.prank_call.API.Models.UserId;
import bachmanity.prank_call.Misc.APIConstants;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Jonathan Chiou on 8/26/2016.
 */
public class CreateAccountCallback implements Callback<UserId> {
    private UserId user;

    @Override
    public void onResponse(Call<UserId> call, Response<UserId> response) {
        switch (response.code()) {
            case APIConstants.HTTP_STATUS_OK:
                EventBus.getDefault().post(response.body().getUserId());
                break;
            default:
                EventBus.getDefault().post(-1);
                break;
        }

    }

    //MODIFY THIS LATER
    @Override
    public void onFailure(Call<UserId> call, Throwable t) {
        EventBus.getDefault().post("ERROR");
    }


}
