package bachmanity.prank_call.API;

import bachmanity.prank_call.API.Services.MatchingService;
import bachmanity.prank_call.API.Services.UserService;
import bachmanity.prank_call.Misc.Constants;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitSingleton {
    private static RetrofitSingleton instance;
    private UserService userService;
    private MatchingService matchingService;

    public static RetrofitSingleton getInstance() {
        if (instance == null) {
            instance = new RetrofitSingleton();
        }

        return instance;
    }

    private RetrofitSingleton() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        userService = retrofit.create(UserService.class);
        matchingService = retrofit.create(MatchingService.class);
    }

    public UserService getUserService() {
        return userService;
    }

    public MatchingService getMatchingService() {
        return matchingService;
    }
}
