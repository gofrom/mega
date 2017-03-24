package pro.gofman.mega;

import android.os.AsyncTask;
import android.util.Log;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

/**
 * Created by gofman on 30.06.15.
 */


/*
    Надо придумать входной тип данных
    для выполнения одиночных и двойных запросов



 */

class RequestType {
    private String url;
    private long sleep;

    public RequestType() {
        this.sleep = 0;
    }

    public RequestType(String url) {
        this.url = url;
    }
    public RequestType(String url, long sleep) {
        this.url = url;
        this.sleep = sleep;
    }

    public String getUrl() {
        return this.url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public long getSleep() {
        return this.sleep;
    }
    public void setSleep(long sleep) {
        this.sleep = sleep;
    }
}


class HttpClientRequest extends AsyncTask<RequestType, Void, String> {

    @Override
    protected String doInBackground(RequestType... mdRequest) {
        OkHttpClient client = new OkHttpClient();

        Response response = null;

        Request request;
        try {

            for (int i = 0; i < mdRequest.length; i++) {

                request = new Request.Builder()
                        .url( mdRequest[i].getUrl() )
                        .build();

                if ( mdRequest[i].getSleep() > 0 ) {
                    Thread.sleep( mdRequest[i].getSleep() );
                }


                response = client.newCall(request).execute();
            }

            return response.body().string();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(String response) {
        super.onPostExecute(response);

    }
}
