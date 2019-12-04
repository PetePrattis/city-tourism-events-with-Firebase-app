package com.unipi.p15120.visageathens;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    //UI
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    MenuItem signoutitem,favouriteitem;

    //UI

    //DATABASE
    static SQLiteDatabase db;
    FirebaseDatabase firedb;
    DatabaseReference psydbRef;
    DatabaseReference ekdbRef;
    DatabaseReference exdbRef;
    //DATABASE

    //ACTIVITY
    Intent intent;
    //ACTIVITY


    //VARIABLES
    static String where, username;
    private String title;
    ArrayList<String> namelist = new ArrayList<String>();
    AlertDialog.Builder alertDialog;
    //VARIABLES

    //OBJECTS OF CLASSES
    Info info;
    //OBJECT OF CLASSES

    //SwipeRefreshLayout swipeRefreshLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //layout
        setContentView(R.layout.activity_main);
        //layout

        //UI
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

      /*  FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(getApplicationContext(), Favorites.class);
                intent.putExtra("who", "autos");
                startActivity(intent);
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                  //      .setAction("Action", null).show();
            }
        }); */


        //UI

        //variables
        where = getIntent().getStringExtra("where");
        username = getIntent().getStringExtra("username");

        //variables

        //database hard core

        db = openOrCreateDatabase("Favourites", Context.MODE_PRIVATE, null);//δημιουργια βασης
        db.execSQL("CREATE TABLE IF NOT EXISTS NAME(name TEXT, uname TEXT);");//δημιουργια  πινακα
        db.execSQL("CREATE TABLE IF NOT EXISTS USERS(name TEXT,rated TEXT, rating Double);");//δημιουργια  πινακα
        //db.execSQL("DELETE FROM NAME");
        //db.execSQL("DELETE FROM USERS");


        firedb = FirebaseDatabase.getInstance();
        psydbRef = firedb.getReference("Psychagwgia");
        ekdbRef = firedb.getReference("Ekdhlwseis");
        exdbRef = firedb.getReference("Exodos");

        //kathe fora pou sundeomai sthn efarmogh, h toulaxiston trexei background, elegxw thn hmeromhnia. an allaksei tote pyrodoteitai to parakatw if.
        //ekei ginetaai h antlhhs ths listas me edhlwseis, tainies klp, kathws kai midenizetai h sthlh stars, diladi imdenizontai ta reviews kathe mera
        Calendar calendar = Calendar.getInstance();
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
        SharedPreferences settings = getSharedPreferences("PREFS",0);
        int lastDay = settings.getInt("day",0);

        if (lastDay != currentDay)
        {
            SharedPreferences.Editor editor = settings.edit();
            editor.putInt("day", currentDay);
            editor.apply();//or editor.commit();
            makeToast(String.valueOf(currentDay) + String.valueOf(lastDay));
            //get movies every day in order to detect if a movie was added recently and zero column star
            //setReviewsWithZeroStars(0);

            //set 0 for stars and how many raters
            setExodosWithZeroStars(0);
            setEkdhlwseisWithZeroStars(0);
           // db.execSQL("DROP TABLE IF EXISTS USERS"); //drop table users because ratings will be deleted every day, so we need new entries
        }

        //database hardcore

        /*swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                (new Handler()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                },3000);
            }
        });*/

    }


    private void makeToast(String text)
    {
        Toast.makeText(MainActivity.this, text, Toast.LENGTH_SHORT).show();
    }

    private void setReviewsWithZeroStars(final Integer stars)
    {
        psydbRef.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                Info info = dataSnapshot.getValue(Info.class);
                //moviesList.add(movieForList.getName());
                //Log.e("Movie name", movieForList.getName());
                //movie = new Movies(0);
                psydbRef.child(dataSnapshot.getKey()).child("stars").setValue(0);
                psydbRef.child(dataSnapshot.getKey()).child("howmany").setValue(0);

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void setEkdhlwseisWithZeroStars(final Integer stars)
    {
        ekdbRef.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                Info info = dataSnapshot.getValue(Info.class);
                //moviesList.add(movieForList.getName());
                //Log.e("Movie name", movieForList.getName());
                //movie = new Movies(0);
                ekdbRef.child(dataSnapshot.getKey()).child("stars").setValue(0);
                ekdbRef.child(dataSnapshot.getKey()).child("howmany").setValue(0);

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void setExodosWithZeroStars(final Integer stars)
    {
        exdbRef.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                Info info = dataSnapshot.getValue(Info.class);
                //moviesList.add(movieForList.getName());
                //Log.e("Movie name", movieForList.getName());
                //movie = new Movies(0);
                exdbRef.child(dataSnapshot.getKey()).child("stars").setValue(0);
                exdbRef.child(dataSnapshot.getKey()).child("howmany").setValue(0);

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public static Boolean check_if_exist(String title, String username )
    {

        String usernamec, titlec;
        Double ratingc;
        Boolean exists = false;

        Cursor c = db.rawQuery("SELECT COUNT(*) FROM USERS",null);

         if (c != null) {
             c.moveToFirst();
             if (c.getInt(0) == 0){
                 //empty table
             }
             else
             {
                 Cursor c2 = db.rawQuery("SELECT * FROM USERS",null);
                 while (c2.moveToNext()) {//for every rating
                     titlec = c2.getString(1);
                     ratingc = c2.getDouble(2);
                     usernamec = c2.getString(0);
                     if (titlec.equals(title) && usernamec.equals(username)) { //&& ratingc != null
                         //exei dwsei vathomollogia
                         exists = true;
                     }
                 }
             }
         }
         return exists;
    }

    public static Double get_previous_stars(String title, String username )
    {

        String usernamec, titlec;
        Double ratingc, previous_rating = 0.0;

        Cursor c = db.rawQuery("SELECT COUNT(*) FROM USERS",null);

        if (c != null) {
            c.moveToFirst();
            if (c.getInt(0) == 0){
                //empty table
            }
            else
            {
                Cursor c2 = db.rawQuery("SELECT * FROM USERS",null);
                while (c2.moveToNext()) {//for every rating
                    titlec = c2.getString(1);
                    ratingc = c2.getDouble(2);
                    usernamec = c2.getString(0);
                    if (titlec.equals(title) && usernamec.equals(username)) { //&& ratingc != null
                        //exei dwsei vathomollogia
                        previous_rating = ratingc;
                    }
                }
            }
        }
        return previous_rating;
    }

    public static void add_rating(String title, String username, Float rating)
    {
        Double r = (double)rating;
        db.execSQL("INSERT INTO USERS VALUES ('" + username + "', '" + title + "', '" + r + "');");
    }

    public static void update_rating(String title, String username, Float rating)
    {
        db.delete("USERS","name=? AND rated=?", new String[]{username, title});
        Double r = (double)rating;
        db.execSQL("INSERT INTO USERS VALUES ('" + username + "', '" + title + "', '" + r + "');");
    }

    public void addf(String title, String username){
       // db = openOrCreateDatabase("Favourites",MODE_PRIVATE, null);//δημιουργια βασης
        if(!title.equals("")){//αν το edittext δεν ειναι κενο

            Cursor c = db.rawQuery("SELECT * FROM NAME",null);
            String name,uname;
            Boolean unique = true;
            while (c.moveToNext()) {//τσεκαρω αν στον πινακα υπαρχει ηδη το ονομα
                name = c.getString(0);
                uname = c.getString(1);
                if (name.equals(title) && uname.equals(username))
                    unique = false;//αν υπαρχει τοτε false η μεταβλητη
            }

            if(unique) {//αν εινα ιμοναδικο το ονομα
                db.execSQL("INSERT INTO NAME VALUES ('" + title+ "','" + username + "');");
                Toast.makeText(getApplicationContext(), "Προστέθηκε στα αγαπημένα!", Toast.LENGTH_SHORT).show();

            }
            else{//αν δεν ειναι μοναδικο
                db.delete("NAME", "name=? AND uname=?", new String[]{title, username});
                Toast.makeText(getApplicationContext(), "Αφαιρέθηκε από τα αγαπημένα!", Toast.LENGTH_SHORT).show();

            }


        }
        else
            Toast.makeText(this, "empty text", Toast.LENGTH_SHORT).show();
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        signoutitem = menu.findItem(R.id.signout);
        favouriteitem = menu.findItem(R.id.favourites);

        if (where.equals("visitor")) //visitor doesnt have a sign out option because he's/she's not signed in
        {
            signoutitem.setVisible(false);
            favouriteitem.setVisible(false);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.signout && !where.equals("visitor")) // we double check if we are not in visitors ui
        {
            signoutitem.setVisible(false);
            intent = new Intent(getApplicationContext(),SignUpInActivity.class); //go back to sign up/in form
            intent.putExtra("where","signout");
            startActivity(intent);

            //setUsername("");
            //setPassword("");
            //practically we lose favorites list and permission to vote


        }
        if (id == R.id.favourites && !where.equals("visitor"))
        {
            namelist.clear();
            Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM NAME", null);
            if (cursor != null){//τσεκαρω αρχικα αν δεν ειναι αδεια η βαση
                cursor.moveToFirst();
                if(cursor.getInt(0) == 0){//αδεια
                    Toast.makeText(this, "Δε βρέθηκαν αγαπημένα...", Toast.LENGTH_SHORT).show();
                }
                else{//οχι αδεια
                    Cursor c = db.rawQuery("SELECT * FROM NAME",null);
                    String name,uname;
                    while (c.moveToNext()){
                        name =c.getString(0);
                        uname =c.getString(1);
                        if(uname.equals(username))
                            namelist.add(name);
                    }

                    String list = "";
                    for (String values: namelist )
                    {
                        list += values + "\n";
                    }
                    if (list.equals("")){
                        Toast.makeText(this, "Δε βρέθηκαν αγαπημένα...", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        alertDialog = new AlertDialog.Builder(this);
                        alertDialog.setTitle("Αγαπημένα");
                        alertDialog.setMessage(list);
                        alertDialog.show();
                    }


                }
            }
        }


        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (alertDialog != null)
            alertDialog = null;

    }

    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        /*@Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.ekdhlwseis, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }*/
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.

            Fragment frag = null;
            switch (position) {
                case 0:
                    frag = new Ekdhlwseis();
                    break;
                case 1:
                    frag = new Psychagwgia();
                    break;
                case 2:
                    frag = new Exodos();
                    break;
            }
            return frag;//return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }
    }
}
