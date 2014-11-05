package me.leonardociocan.xkcd;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class DownloadImageTask extends AsyncTask<String, Void, JSONObject> {
    ImageView bmImage;
    MainActivity activity;

    public DownloadImageTask(MainActivity activity) {
        this.bmImage = activity.imageView;
        this.activity = activity;
    }
    Bitmap mIcon11;
    protected JSONObject doInBackground(String... urls) {
        DefaultHttpClient httpclient = new DefaultHttpClient(new BasicHttpParams());
        HttpPost httppost = new HttpPost(urls[0]);
// Depends on your web service
        httppost.setHeader("Content-type", "application/json");

        InputStream inputStream = null;
        String result = null;
        try {
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();

            inputStream = entity.getContent();
            // json is UTF-8 by default
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);
            StringBuilder sb = new StringBuilder();

            String line = null;
            while ((line = reader.readLine()) != null)
            {
                sb.append(line + "\n");
            }
            result = sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            try{if(inputStream != null)inputStream.close();}catch(Exception squish){}
        }

        JSONObject jObject = null;
        String url = null;
        try {
            jObject = new JSONObject(result);
            url= jObject.getString("img");
        } catch (JSONException e) {
            e.printStackTrace();
        }


        String urldisplay = url;
         mIcon11 = null;
        try {
            InputStream in = new java.net.URL(urldisplay).openStream();
            mIcon11 = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        return jObject;
    }

    protected void onPostExecute(JSONObject result) {
        bmImage.setImageBitmap(mIcon11);
        try {
            activity.comic_num.setText(  result.getString("num"));
            activity.title.setText(  result.getString("safe_title"));
            activity.alt.setText(  result.getString("alt"));
            activity.imageView.startAnimation(activity.newComicAnimAfter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
