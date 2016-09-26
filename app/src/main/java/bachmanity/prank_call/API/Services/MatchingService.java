package bachmanity.prank_call.API.Services;

import java.util.List;

import bachmanity.prank_call.API.Models.CallBundle;
import bachmanity.prank_call.API.Models.History;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by jman0_000 on 8/21/2016.
 */
public interface MatchingService {
    @POST("/call")
    Call<Void> call(@Body CallBundle callBundle);

    @GET("/history")
    Call<List <History>> getHistory(@Query("from") String from, @Query("to") String to);
}
