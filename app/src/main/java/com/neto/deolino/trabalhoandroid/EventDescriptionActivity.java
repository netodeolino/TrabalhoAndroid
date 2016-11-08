package com.neto.deolino.trabalhoandroid;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.neto.deolino.trabalhoandroid.dao.EventDAO;
import com.neto.deolino.trabalhoandroid.model.Event;
import com.neto.deolino.trabalhoandroid.model.EventType;
import com.neto.deolino.trabalhoandroid.model.User;
import com.neto.deolino.trabalhoandroid.util.DateHelper;

/**
 * Created by deolino on 27/10/16.
 */
public class EventDescriptionActivity extends AppCompatActivity {

    User user;

    ListView personsParticipatingListView;
    TextView tvEventDate;
    TextView tvEventTime;
    ImageView ivEventType;
    TextView tvEventStart;
    TextView tvEventEnd;
    TextView tvEventDescription;

    Context context;
    Intent intent;

    Event event;
    int eventID;

    ProgressBar pbEventDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_description);

        context = this;
        intent = getIntent();
        //get from intent
        eventID = intent.getIntExtra("eventID", 0 );
        event = new Event();
        personsParticipatingListView = (ListView) findViewById(R.id.lvPersonsInEvent);
        tvEventDate = (TextView) findViewById(R.id.tvEventTime);
        tvEventTime = (TextView) findViewById(R.id.tvEventDate);
        ivEventType = (ImageView) findViewById(R.id.ivEventType);
        tvEventStart = (TextView) findViewById(R.id.tvEventStart);
        tvEventEnd = (TextView) findViewById(R.id.tvEventEnd);
    }

    @Override
    protected void onResume() {
        super.onResume();

        event = new Event();
        event = new EventDAO(this).findById(eventID);

        tvEventDate.setText(DateHelper.dateToString(event.getDate()));
        tvEventTime.setText(DateHelper.timeToString(event.getDate()));
        EventType.Type type = event.getType().getType();
        int img = (type==EventType.Type.HIKE) ? R.drawable.hike : (type==EventType.Type.RUN) ? R.drawable.running : R.drawable.bike;
        ivEventType.setImageResource(img);
        tvEventStart.setText(event.getDeparture().getAddress());
        tvEventEnd.setText(event.getArrival().getAddress());
        tvEventDescription.setText(event.getDescription());
    }

    private void populatePersonsList(){

    }

    /*
This method is executed when the activity is created to populate the ActionBar with actions
*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (event.getConfirmedUsers().contains(user)) {
            MenuItem participate = menu.findItem(R.id.menuParticipate);
            if (participate != null) {
                participate.setVisible(false);
            }
        }
        if (event.isSecret() && !user.equals(event.getOrganizer())) {
            MenuItem invite = menu.findItem(R.id.menuInvite);
            if (invite != null) {
                invite.setVisible(false);
            }
        }
        getMenuInflater().inflate(R.menu.event_desc_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.menuInvite:
                Log.d("EvenDescriptionActivity", "Invite Pressed!");
                intent = new Intent(context, InviteActivity.class);
                intent.putExtra("eventID", eventID );
                startActivity(intent);
                return true;

            case R.id.menuShowRoute:
                Log.d("EvenDescriptionActivity", "Show Route Pressed!");
                return true;

            case R.id.menuParticipate:
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }

    public void eventDescMapButtonPressed(View view){
        Log.d("EvenDescriptionActivity", "Map Button Pressed!");

        Intent intent = new Intent(this, MapsActivity.class);
        intent.putExtra("eventID", eventID );
        startActivity(intent);

    }
}
