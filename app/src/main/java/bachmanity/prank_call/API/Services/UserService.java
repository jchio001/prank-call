package bachmanity.prank_call.API.Services;

import bachmanity.prank_call.API.Models.AccountBundle;
import bachmanity.prank_call.API.Models.LoginRespBundle;
import bachmanity.prank_call.API.Models.UserId;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by Jonathan Chiou on 8/26/2016.
 */
public interface UserService {
    @POST("/createAccount")
    Call<LoginRespBundle> createAccount(@Body AccountBundle accountBundle);

    @POST("/login")
    Call<LoginRespBundle> login(@Body AccountBundle accountBundle);
}
