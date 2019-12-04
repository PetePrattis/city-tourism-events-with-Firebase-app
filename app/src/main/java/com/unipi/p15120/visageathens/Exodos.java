package com.unipi.p15120.visageathens;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Exodos extends Fragment {

    ArrayList<Info> ex_info;
    FirebaseDatabase db = FirebaseDatabase.getInstance();
    DatabaseReference exodosdb = db.getReference("Exodos");
    MyAdapter myAdapter;
    RatingBar rating;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.exodos, container, false);


        final RecyclerView recyclerView = v.findViewById(R.id.recview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        ex_info = new ArrayList<Info>();

        exodosdb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren())
                {
                    Info m = dataSnapshot1.getValue(Info.class);
                    ex_info.add(m);
                }
                myAdapter = new MyAdapter("Exodos",getActivity().getApplicationContext(),ex_info);
                recyclerView.setAdapter(myAdapter);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        return v;

       // return inflater.inflate(R.layout.exodos, container, false);
    }

    //edw ginontai ola
    public void onActivityCreated(Bundle savedInst)
    {
        super.onActivityCreated(savedInst);

       /*  rating = getActivity().findViewById(R.id.rating);
        rating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setColumnStars(rating.getRating());
            }
        });*/

    }

    public void setColumnStars(final Float stars)
    {
        exodosdb.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Info info = dataSnapshot.getValue(Info.class);
                exodosdb.child(dataSnapshot.getKey()).child("stars").setValue(stars);
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
    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);

    }
}
