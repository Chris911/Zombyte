package com.game.store;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

import com.game.zombyte.R;

public class StoreActivity extends Activity {

	public Context storeContext;
	public Activity storeActivity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);
        
        storeContext = this;
        storeActivity = this;
        
        final Button button = (Button) findViewById(R.id.transitionButton);
        
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	storeActivity.finish();
                //overridePendingTransition(R.anim.slide_out_bot, R.anim.slide_in_top);
            }
        });
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_store, menu);
        return true;
    }
    
    @Override
    public void onBackPressed() {
    	super.onBackPressed();
        //overridePendingTransition(R.anim.slide_in_top, R.anim.slide_out_bot);
    }
}
