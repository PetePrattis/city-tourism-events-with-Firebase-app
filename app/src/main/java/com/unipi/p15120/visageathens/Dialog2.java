package com.unipi.p15120.visageathens;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RatingBar;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static java.lang.Math.round;

public class Dialog2 extends DialogFragment {
    private String title;
    private String where;
    private Integer howmany;
    private Double stars, previous_stars;
    private Boolean doesnt_exists;

    FirebaseDatabase firedb;
    DatabaseReference dbRef;

    RatingBar rt;

    public Dialog2(){}

    @SuppressLint("ValidFragment")
    public Dialog2(String title, String where, Integer howmany, Double stars, Boolean doesnt_exists, Double previous_stars)
    {
        this.title = title;
        this.where = where;
        this.howmany = howmany;
        this.stars = stars;
        this.doesnt_exists = doesnt_exists;
        this.previous_stars = previous_stars;
        firedb = FirebaseDatabase.getInstance();
        dbRef = firedb.getReference(where);

    }

    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.rating_dialog, null);


// 2. Chain together various setter methods to set the dialog characteristics

        builder.setView(view).setTitle(title);

        rt =  view.findViewById(R.id.rating_dialog);

        if (doesnt_exists){
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dbRef.child(title).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Double sum_stars = stars * howmany;
                            sum_stars = sum_stars + rt.getRating();
                            howmany = howmany + 1;
                            stars = sum_stars / howmany;
                            //thelw na parw to athroisma twn asteriwn

                            if(stars == null || stars < 0.0)
                                stars =0.0;

                            dbRef.child(title).child("stars").setValue(stars);
                            dbRef.child(title).child("howmany").setValue(howmany);
                            //dataSnapshot.getRef().child("stars").setValue(round(stars));
                            //dataSnapshot.getRef().child("howmany").setValue(howmany);
                            MainActivity.add_rating(title,MainActivity.username,rt.getRating());
                            //Toast.makeText(getActivity().getApplicationContext(), "rating added", Toast.LENGTH_SHORT).show();

                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });}
        else if (!doesnt_exists){
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dbRef.child(title).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            //if(stars == null)
                                //stars =0.0;

                            Double sum_stars = stars * howmany;
                            sum_stars = sum_stars - previous_stars + rt.getRating();
                            //afairo tin bathmologia toy proigoumenou kai prostheto tin nea
                            stars = sum_stars / howmany;
                            //stars = (sum_stars + 1) / howmany;
                            //thelw na parw to athroisma twn asteriwn

                            if(stars == null || stars < 0.0)
                                stars =0.0;

                            stars = Double.parseDouble(String.format("%.2f", stars));
                            dbRef.child(title).child("stars").setValue(stars);
                            dbRef.child(title).child("howmany").setValue(howmany);
                            //dataSnapshot.getRef().child("stars").setValue(round(stars));
                            //dataSnapshot.getRef().child("howmany").setValue(howmany);
                            MainActivity.update_rating(title,MainActivity.username,rt.getRating());
                            //Toast.makeText(getActivity().getApplicationContext(), "rating changed", Toast.LENGTH_SHORT).show();

                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }
            });

            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
        }
// 3. Get the <code><a href="/reference/android/app/AlertDialog.html">AlertDialog</a></code> from <code><a href="/reference/android/app/AlertDialog.Builder.html#create()">create()</a></code>
        AlertDialog dialog = builder.create();
        return dialog;
    }
}
