
import android.os.AsyncTask;

import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Created by JavaDroid.ir on 4/17/2015.
 */


public class AsyncPostData extends AsyncTask<HashMap, Void, String> {


    private String link = "";
    String response = "";
    public AsyncResponse delegate = null;


    public static interface AsyncResponse {
        void processFinish(String output);
    }

    public AsyncPostData(
            String link, AsyncResponse list) {
        this.link = link;
        this.delegate = list;
    }

    protected void onPreExecute() {
    }

    @Override
    protected String doInBackground(HashMap... maps) {
         OkHttpClient client = new OkHttpClient();

        MultipartBody.Builder form = new MultipartBody.Builder();
        form.setType(MultipartBody.FORM);

        Set set = maps[0].entrySet();
        for (Object aSet : set) {
            Map.Entry me = (Map.Entry) aSet;
            form.addFormDataPart(me.getKey().toString(), me.getValue().toString());
        }

        RequestBody requestBody = form.build();

        Request request = new Request.Builder()
                .url(this.link)
                .method("POST", RequestBody.create(null, new byte[0]))
                .post(requestBody)
                .build();


        try {
            Response response = client.newCall(request).execute();
            response = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }


    @Override
    protected void onPostExecute(String result) {
        if (delegate != null)
            delegate.processFinish(result);

    }

}
