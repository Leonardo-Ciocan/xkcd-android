package me.leonardociocan.xkcd;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.melnykov.fab.FloatingActionButton;

import java.sql.ParameterMetaData;


public class MainActivity extends Activity {


    public TextView comic_num;
    public TextView title;
    public TextView alt;
    public ImageView imageView;

    Integer x = 1000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);

        comic_num = (TextView)findViewById(R.id.comic_num);
        title = (TextView)findViewById(R.id.title);
        alt = (TextView)findViewById(R.id.alt);


        imageView = (ImageView)findViewById(R.id.image);
        new DownloadImageTask(this)
                .execute("https://xkcd.com/1005/info.0.json");

        final FloatingActionButton next = (FloatingActionButton)findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                x++;
                new DownloadImageTask(MainActivity.this)
                        .execute("https://xkcd.com/" + x.toString() + "/info.0.json");
            }
        });

        final FloatingActionButton prev = (FloatingActionButton)findViewById(R.id.prev);
        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                x--;
                new DownloadImageTask(MainActivity.this)
                        .execute("https://xkcd.com/" + x.toString() + "/info.0.json");
            }
        });


        final CardView cardView = (CardView)findViewById(R.id.card_view);
        cardView.setOnClickListener(new View.OnClickListener() {
            boolean exp = false;

            @Override
            public void onClick(View v) {
                exp = !exp;
                Animation a = new Animation() {

                    @Override
                    protected void applyTransformation(float interpolatedTime, Transformation t) {
                        ViewGroup.LayoutParams params = cardView.getLayoutParams();
                        params.height = 60 + (int)( (!exp ? 0: 240) * interpolatedTime);
                        if(exp){
                            params.height = 60 + (int)( 140 * interpolatedTime);
                            params.width =245 + (int)(50*interpolatedTime);
                            next.setAlpha(1-interpolatedTime);
                            prev.setAlpha(1-interpolatedTime);
                        }
                        else{
                            params.height = 200 - (int)( 140 * interpolatedTime);
                            params.width =295 - (int)(50*interpolatedTime);
                            next.setAlpha(interpolatedTime);
                            prev.setAlpha(interpolatedTime);
                        }
                        params.height = (int)convertDpToPixel((int)params.height,MainActivity.this);
                        params.width = (int)convertDpToPixel((int)params.width,MainActivity.this);

                        cardView.setLayoutParams(params);
                    }
                };
                a.setDuration(400);
                cardView.startAnimation(a);
            }
        });
    }





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public static float convertDpToPixel(float dp, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return px;
    }

}
