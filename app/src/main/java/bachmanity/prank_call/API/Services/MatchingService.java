package bachmanity.prank_call.API.Services;

import bachmanity.prank_call.API.Models.CallBundle;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by jman0_000 on 8/21/2016.
 */
public interface MatchingService {
    @POST("/call/")
    Call<Void> call(@Body CallBundle callBundle);
}
