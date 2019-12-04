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
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Psychagwgia extends Fragment {


    ArrayList<Info> psy_info;
    FirebaseDatabase db = FirebaseDatabase.getInstance();
    DatabaseReference psydb = db.getReference("Psychagwgia");
    MyAdapter myAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View v=inflater.inflate(R.layout.psychagwgia, container, false);

        final RecyclerView recyclerView = v.findViewById(R.id.recview2);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        psy_info = new ArrayList<Info>();

        psydb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren())
                {
                    Info m = dataSnapshot1.getValue(Info.class);
                    psy_info.add(m);


                }
                myAdapter = new MyAdapter("Psychagwgia",getContext(),psy_info);
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
    }

    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);

    }
}
