package bachmanity.prank_call.API.Services.Callbacks;

import org.greenrobot.eventbus.EventBus;

import bachmanity.prank_call.API.Models.LoginRespBundle;
import bachmanity.prank_call.Misc.APIConstants;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Jonathan Chiou on 8/26/2016.
 */
public class CreateAccountCallback implements Callback<LoginRespBundle> {

    @Override
    public void onResponse(Call<LoginRespBundle> call, Response<LoginRespBundle> resp) {
        System.out.println(resp.code());
        switch (resp.code()) {
            case APIConstants.HTTP_STATUS_OK:
                System.out.println("OK");
                EventBus.getDefault().post(resp.body());
                break;
            default:
                System.out.println("NOT OK");
                EventBus.getDefault().post(-1);
                break;
        }

    }

    //MODIFY THIS LATER
    @Override
    public void onFailure(Call<LoginRespBundle> call, Throwable t) {
        EventBus.getDefault().post("ERROR");
    }


}
