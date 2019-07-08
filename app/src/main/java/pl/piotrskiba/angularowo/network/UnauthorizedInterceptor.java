package pl.piotrskiba.angularowo.network;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import pl.piotrskiba.angularowo.interfaces.UnauthorizedResponseListener;

public class UnauthorizedInterceptor implements Interceptor {

    private static UnauthorizedResponseListener unauthorizedListener;

    @Override
    public Response intercept(Interceptor.Chain chain) throws IOException {
        Request request = chain.request();
        Response response = chain.proceed(request);

        if(response.code() == 401){
            unauthorizedListener.onUnauthorizedResponse();
        }

        return response;
    }

    public static void setUnauthorizedListener(UnauthorizedResponseListener listener){
        unauthorizedListener = listener;
    }
}