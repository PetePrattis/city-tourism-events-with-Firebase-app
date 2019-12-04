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
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Ekdhlwseis extends Fragment {

    ArrayList<Info> ek_info;
    FirebaseDatabase db = FirebaseDatabase.getInstance();
    DatabaseReference ekdb = db.getReference("Ekdhlwseis");
    MyAdapter myAdapter;
    RatingBar rt;
    ImageView img;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.ekdhlwseis, container, false);

        final RecyclerView recyclerView = v.findViewById(R.id.recview3);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        ek_info = new ArrayList<Info>();
        ekdb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds: dataSnapshot.getChildren())
                {
                    Info m = ds.getValue(Info.class);
                    ek_info.add(m);

                }
                myAdapter = new MyAdapter("Ekdhlwseis",getContext(),ek_info);
                recyclerView.setAdapter(myAdapter);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return v;
    }

    //edw ginontai ola
    public void onActivityCreated(Bundle savedInst)

    {
        super.onActivityCreated(savedInst);
       // img = getActivity().findViewById(R.id.image3);

       /* img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rt.setEnabled(true);
            }
        });*/


    }

    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);

    }

    public void setColumnStars(final Float stars)
    {
        ekdb.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Info info = dataSnapshot.getValue(Info.class);
                ekdb.child(dataSnapshot.getKey()).child("stars").setValue(stars);
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
}
