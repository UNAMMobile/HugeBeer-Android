package com.chelathon.unammobile.hugebeer;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener,
        NavigationView.OnNavigationItemSelectedListener{

    private Firebase mFirebaseRef;
    private Firebase mEventsFirebaseRef;

    private ValueEventListener eventsValueEventListener;
    private ChildEventListener childEventListener;
    private List<String> events;

    private Context context;

    private ImageButton butTest;

    private GridView gridView;
    private List<String> names;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context=this;

        Firebase.setAndroidContext(context);
        mFirebaseRef = new Firebase(PreLoginActivity.BASE_URL);
        mEventsFirebaseRef=mFirebaseRef.child("Eventos");

        butTest=(ImageButton) findViewById(R.id.button_main_testEvent);
        butTest.setOnClickListener(this);

        gridView = (GridView) findViewById(R.id.gridView1);
        gridView.setNumColumns(2);

        // Set custom adapter (GridAdapter) to gridview
        names=new ArrayList<>();

        gridView.setOnItemClickListener(this);
        events=new ArrayList<>();
        childEventListener=new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                names.remove(dataSnapshot.getKey());
                gridView.setAdapter(  new GridAdapter(context, names ) );
                gridView.setSelection(names.size() - 1);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        };
        eventsValueEventListener=new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot d:dataSnapshot.getChildren()){
                    if(d.child("anfitrion").exists() && d.child("nombre").exists() && d.child("anfitrion").getValue().equals(PreLoginActivity.accessToken.getUserId())) {
                        if(!names.contains(d.getKey())){
                            names.add(d.getKey());
                        }
                    }
                    else {
                        for (DataSnapshot dc:d.child("lista_invitados").getChildren()){
                            if(dc.getKey().equals(PreLoginActivity.accessToken.getUserId())){
                                if(!names.contains(dc.getKey())){
                                    names.add(d.getKey());
                                }
                            }
                        }
                    }
                }
                gridView.setAdapter(  new GridAdapter(context, names ) );
                gridView.setSelection(names.size()-1);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }

        };
        mEventsFirebaseRef.addValueEventListener(eventsValueEventListener);
        mEventsFirebaseRef.addChildEventListener(childEventListener);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //Side Menu
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout1);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void createEvent(String name, String lugar, double lat, double lon, String description,
                             Date inicio_evento, List<Invitado> lista_invitados){
        Firebase f=mEventsFirebaseRef.push();
        f.child("anfitrion").setValue(PreLoginActivity.accessToken.getUserId());
        f.child("nombre").setValue(name);
        f.child("lugar").setValue(lugar);
        f.child("fecha_inicio").setValue(inicio_evento);
        f.child("ubicacion").child("lat").setValue(lat);
        f.child("ubicacion").child("lon").setValue(lon);
        f.child("status_recompensa").setValue(0);
        f.child("descripcion").setValue(description);
        for(int i=0; i<lista_invitados.size(); i++){
            f.child("lista_invitados").child(lista_invitados.get(i).getFbId()).
                    child("facebook_id").setValue(lista_invitados.get(i).getNombre());
            f.child("lista_invitados").child(lista_invitados.get(i).getFbId()).
                    child("confirmado").setValue(false);
            Random r=new Random();
            f.child("lista_invitados").child(lista_invitados.get(i).getFbId()).
                    child("pago").setValue(r.nextInt(100));
        }

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout1);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout1);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_main_testEvent:
                List<Invitado> lista=new ArrayList<>();
                lista.add(new Invitado("César","sgfetb55ysdfsdfd"));
                lista.add(new Invitado("Vicente","sgfetb5hfjfgnfgxhd"));
                lista.add(new Invitado("Aida","sgfetb55ysafsfahd"));
                lista.add(new Invitado("Alex","sgfasdasdy6u64hxhd"));
                lista.add(new Invitado("Luis","sgfetb55y6apghdsfd"));

                createEvent("Peda en casa\n de Álvaro", "Casa de ÁLvaro", 99.2323, 46.23132, "Hasta morir",
                        new Date(10000),lista);
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}
