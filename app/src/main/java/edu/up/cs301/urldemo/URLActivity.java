package edu.up.cs301.urldemo;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.view.Menu;
import android.widget.TextView;

public class URLActivity extends Activity {

    private final static String urlString = "http://faculty.up.edu/vegdahl/cs301ExampleFile.txt";
	private ArrayList<String> messages = new ArrayList<String>();
	
	TextView[] textViews;
	
	Handler mainHandler;

	int[] codes = {
            R.id.tv0,
            R.id.tv1,
            R.id.tv2,
            R.id.tv3,
            R.id.tv4,
            R.id.tv5,
            R.id.tv6,
            R.id.tv7,
            R.id.tv8,
            R.id.tv9,
    };
	
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_url);
        
        mainHandler = new Handler();

        textViews = new TextView[codes.length];
        for (int i = 0; i < codes.length; i++) {
            textViews[i] = (TextView)findViewById(codes[i]);
        }
        
       Thread t = new Thread(new NetworkRunner());
       t.start();
        
    }


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.url, menu);
//        return true;
//    }
    
    private class NetworkRunner implements Runnable {
    	public void run() {
            try {
            	URL url = new URL(urlString);
            	InputStream inStream = url.openStream();
            	BufferedReader reader = new BufferedReader(new InputStreamReader(inStream));
            	for (;;) {
            	    String line = reader.readLine();
            	    if (line == null) break;
            	    messages.add(line);
                }
            	reader.close();
            } catch (Exception ex) {
            	messages.add("error occurred "+ex);
            }
            
            Runnable myRunner = new Runnable(){
            	public void run(){
            	    int limit = Math.min(messages.size(),textViews.length);
            	    for (int i = 0; i < limit; i++) {
            	        textViews[i].setText(messages.get(i));
                    }
            	}
            };
            mainHandler.post(myRunner);
    	}
    }
}

