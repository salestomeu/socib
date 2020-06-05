package com.socib.integrationSocib;

import java.io.IOException;
import java.util.function.Function;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class MockInterceptor implements Interceptor {

    private String response = null;
    private Function<Request, String> responseByRequest = null;

    public MockInterceptor(String response){
        this.response = response;
    }

    public MockInterceptor(Function<Request, String> responseByRequest) {
        this.responseByRequest = responseByRequest;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {

        String responseBody = this.getResponse(chain.request());

        return new Response.Builder()
                .code(200)
                .message("Mocked response")
                .request(chain.request())
                .protocol(Protocol.HTTP_1_0)
                .body(ResponseBody.create(MediaType.parse("application/json"), responseBody))
                .build();
    }

    private String getResponse(Request request){
        return responseByRequest != null
                ? responseByRequest.apply(request)
                : response;
    }
}
