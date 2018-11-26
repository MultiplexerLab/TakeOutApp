package multiplexer.lab.takeout.Helper;

import com.android.volley.toolbox.StringRequest;

public class EndPoints {

    public static final String BASE_URL = "http://api.bdtakeout.com/";
    public static final String SIGNUP_URL = BASE_URL+"api/account/register";
    public static final String SIGNIN_URL = BASE_URL+"token";
    public static final String GET_POINT_URL = BASE_URL+"api/invoice/getPoints";
}
