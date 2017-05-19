package com.example.luigi.travelapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.luigi.travelapp.datamodel.DataStore;
import com.example.luigi.travelapp.datamodel.Event;

import static com.example.luigi.travelapp.costanti.Constants.EVENTNEW;
import static com.example.luigi.travelapp.costanti.Constants.EVENT_INDEX;
import static com.example.luigi.travelapp.costanti.Constants.EVENT_REFERENCE;

/**
 * Created by Bernardo on 09/05/2017.
 */

public class EventListActivity extends Activity{
    private DataStore dataStore = DataStore.getInstance();

    private ListView list;
    private FloatingActionButton addEvent;
    private EventListAdapter eventListAdapter;
    private Toolbar toolbar;
    private Menu menu;
    private final int CODE2 = 2;
    private final int CODE3 = 3;
    private int positione;

    String eventReference;

    private Intent intent;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_list);

        Bundle extras = getIntent().getExtras();
        eventReference = extras.getString(EVENT_REFERENCE);

        eventListAdapter = new EventListAdapter(this);
        dataStore.beginEventsObs(new DataStore.UpdateListener() {
            @Override
            public void tripsUpdated() {

            }

            @Override
            public void daysUpdated() {

            }

            @Override
            public void eventsUpdated() {
                eventListAdapter.update(dataStore.getEvents());
            }
        }, eventReference);

        addEvent = (FloatingActionButton)findViewById(R.id.AddEvent);

        toolbar = (Toolbar)findViewById(R.id.toolbar_event_list);
        toolbar.inflateMenu(R.menu.menu_list_events);
        menu = toolbar.getMenu();

        list = (ListView)findViewById(R.id.eventListView);
        list.setAdapter(eventListAdapter);

        addEvent.setImageResource(R.drawable.ic_action_name_add);

        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                EditToolbar();
                positione = position;
                return true;
            }
        });

        list.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                DefaulToolbar();
            }
        });

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.item_delete:
                        /*dataStore.deleteEvent(tripIndex, dayIndex, positione);
                        eventListAdapter.notifyDataSetChanged();*/
                        DefaulToolbar();
                        return true;

                    case R.id.item_edit:
                        DefaulToolbar();
                        /*intent= new Intent(EventListActivity.this, EventActivity.class);
                        intent.putExtra(EVENT, dataStore.getEventList(tripIndex, dayIndex).get(positione));
                        intent.putExtra(EVENTNEW, "NO");
                        Log.i("EventListActivity: ", "VALORE EVENT:" +EVENT);
                        startActivityForResult(intent, CODE3);*/
                }
                return false;
            }
        });

        addEvent.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DefaulToolbar();
                intent = new Intent(EventListActivity.this, EventActivity.class);
                intent.putExtra(EVENTNEW, "yes");
                startActivityForResult(intent, CODE2);
            }
        }));

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               DefaulToolbar();
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CODE2) {
            if (resultCode == Activity.RESULT_OK) {
                Event event = (Event)data.getSerializableExtra(EVENT_INDEX);
                dataStore.addEvent(event, eventReference);
            }
        }
        if(requestCode==CODE3){
            if(resultCode==Activity.RESULT_OK){
                //Event event = (Event)data.getSerializableExtra(EVENT_INDEX);
                //dataStore.updateEvent(tripIndex, dayIndex,positione, event);
                //eventListAdapter.notifyDataSetChanged();
                DefaulToolbar();
            }
        }
    }

    private void DefaulToolbar(){
        //toolbar.setBackgroundColor(getColor(R.color.colorPrimary));
        toolbar.setTitle(R.string.titleEvents);
        toolbar.setNavigationIcon(null);
        menu.findItem(R.id.item_edit).setVisible(false);
        menu.findItem(R.id.item_delete).setVisible(false);

    }

    private void EditToolbar(){
        toolbar.setBackgroundColor(Color.GRAY);
        toolbar.setTitle("");
        toolbar.setNavigationIcon(R.drawable.ic_action_name_back);
        menu.findItem(R.id.item_edit).setVisible(true);
        menu.findItem(R.id.item_delete).setVisible(true);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dataStore.endEventsObs();
    }
}
