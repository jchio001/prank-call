package bachmanity.prank_call.API.Services.Callbacks;

import android.provider.ContactsContract;

import org.greenrobot.eventbus.EventBus;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import bachmanity.prank_call.Misc.APIConstants;
import bachmanity.prank_call.Misc.Constants;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CallCallback implements Callback<Void> {

    @Override
    public void onResponse(Call<Void> call, Response<Void> response) {
        switch (response.code()) {
            case APIConstants.HTTP_STATUS_OK:
                EventBus.getDefault().post(Constants.MADE_CALL);
                break;
            default:
                System.out.println(response.code());
                EventBus.getDefault().post(Constants.CALL_NOT_MADE);
                break;
        }
    }

    //MODIFY THIS LATER
    @Override
    public void onFailure(Call<Void> call, Throwable t) {
        EventBus.getDefault().post("ERROR");
    }

}
