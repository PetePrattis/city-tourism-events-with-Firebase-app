package com.unipi.p15120.visageathens;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;
import static android.database.sqlite.SQLiteDatabase.openOrCreateDatabase;
import static java.security.AccessController.getContext;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.myViewHolder> {

    Context context;
    String where, title;
    Double stars, previous_stars;
    Integer howmany;
    ArrayList<Info> info;
    Boolean pressed = false;


    Dialog dia;

    public MyAdapter(String w, Context c, ArrayList<Info> m)
    {
        where = w;
        context = c;
        info = m;

    }

    @NonNull
    @Override
    public MyAdapter.myViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if (where.equals("Exodos"))
            return new myViewHolder(LayoutInflater.from(context).inflate(R.layout.item_exodos, viewGroup, false));
        if (where.equals("Psychagwgia"))
            return new myViewHolder(LayoutInflater.from(context).inflate(R.layout.item_psychagwgia, viewGroup, false));
        if (where.equals("Ekdhlwseis")) {
                return new myViewHolder(LayoutInflater.from(context).inflate(R.layout.item_ekdhlwseis, viewGroup, false));
        }
        return new myViewHolder(LayoutInflater.from(context).inflate(R.layout.problem, viewGroup,false));
    }


    @Override
    public void onBindViewHolder(@NonNull final MyAdapter.myViewHolder myViewHolder, int i) {

        myViewHolder.title.setText(info.get(i).getName());
        myViewHolder.descr.setText(info.get(i).getDescr());
        myViewHolder.image.setImageDrawable(context.getResources().getDrawable(context.getResources().getIdentifier(info.get(i).getImage(), "drawable", context.getPackageName())));
        myViewHolder.text_shown.setText(info.get(i).getText());
        myViewHolder.main_cont.setText(info.get(i).getMain());
        double d = info.get(i).getStars();
        float d1 = (float) d;
        myViewHolder.rt.setRating(d1);
        myViewHolder.rt_nb.setText(info.get(i).getStars().toString());
        title = info.get(myViewHolder.getAdapterPosition()).getName();
        stars = info.get(myViewHolder.getAdapterPosition()).getStars();
        howmany = info.get(myViewHolder.getAdapterPosition()).getHowmany();
        //Toast.makeText(context, info.get(myViewHolder.getAdapterPosition()).getName(), Toast.LENGTH_SHORT).show();



        myViewHolder.imgb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //SHOWO MORE INFO ON CARD VIEW
                //myViewHolder.rt.setVisibility(View.VISIBLE);
                //pressed = true;
                if (!pressed) {
                    myViewHolder.main_cont.setVisibility(View.VISIBLE);
                    myViewHolder.imgb.setBackground(context.getResources().getDrawable(R.drawable.ic_arrow_drop_up_black_24dp));

                    pressed = true;
                }
                else
                {
                    myViewHolder.main_cont.setVisibility(View.GONE);
                    myViewHolder.imgb.setBackground(context.getResources().getDrawable(R.drawable.ic_arrow_drop_down_black_24dp));

                    pressed = false;
                }
            }
        });
        myViewHolder.rate_b.setOnClickListener(new View.OnClickListener() { //RATE ITEM
            @Override
            public void onClick(View v) {
                if (MainActivity.check_if_exist(title,MainActivity.username) == false) {//epistrefei an exei psifisei o xristis, an den exei psifisei
                    Toast.makeText(context, "USER HASNT VOTED YET", Toast.LENGTH_SHORT).show();

                    Dialog2 dialog2 = new Dialog2(title, where, howmany, stars,true, 0.0);
                    dialog2.show(((FragmentActivity) context).getSupportFragmentManager(), "ha");


                }
                else{//an exei psifisei
                    Toast.makeText(context, "USER HAS VOTED", Toast.LENGTH_SHORT).show();
                    previous_stars = MainActivity.get_previous_stars(title,MainActivity.username);
                    Toast.makeText(context, previous_stars.toString(), Toast.LENGTH_SHORT).show();
                    Dialog2 dialog2 = new Dialog2(title, where, howmany, stars,false, previous_stars);
                    dialog2.show(((FragmentActivity) context).getSupportFragmentManager(), "ha");

                }
            }
        });

        myViewHolder.fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)context).addf(title, MainActivity.username);

            }
        });
/*
        if (where.equals("Exodos")) {


            myViewHolder.title.setText(info.get(i).getName());
            myViewHolder.descr.setText(info.get(i).getDescr());
            myViewHolder.image.setImageDrawable(context.getResources().getDrawable(context.getResources().getIdentifier(info.get(i).getImage(), "drawable", context.getPackageName())));
        }
        if (where.equals("Psychagwgia"))
        {
            myViewHolder.title.setText(info.get(i).getName());
            myViewHolder.descr.setText(info.get(i).getDescr());
            myViewHolder.image.setImageDrawable(context.getResources().getDrawable(context.getResources().getIdentifier(info.get(i).getImage(), "drawable", context.getPackageName())));

        }
        if (where.equals("Ekdhlwseis"))
        {
            myViewHolder.title.setText(info.get(i).getName());
            myViewHolder.descr.setText(info.get(i).getDescr());
            myViewHolder.image.setImageDrawable(context.getResources().getDrawable(context.getResources().getIdentifier(info.get(i).getImage(), "drawable", context.getPackageName())));


                myViewHolder.imgb.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //myViewHolder.rt.setVisibility(View.VISIBLE);
                        //pressed = true;
                        if (!pressed) {
                            myViewHolder.rt.setVisibility(View.VISIBLE);
                            myViewHolder.main_cont.setVisibility(View.VISIBLE);
                            myViewHolder.imgb.setBackground(context.getResources().getDrawable(R.drawable.ic_arrow_drop_up_black_24dp));
                            pressed = true;
                        }
                        else
                        {
                            myViewHolder.rt.setVisibility(View.GONE);
                            myViewHolder.main_cont.setVisibility(View.GONE);
                            myViewHolder.imgb.setBackground(context.getResources().getDrawable(R.drawable.ic_arrow_drop_down_black_24dp));
                            pressed = false;
                        }
                    }
                });
            //}
        }
        */
    }

    class myViewHolder extends  RecyclerView.ViewHolder
    {
        CardView mCardView;
        TextView title, descr, text_shown, main_cont, rt_nb;
        ImageView image;
        ImageButton imgb, fav;
        RatingBar rt;
        Button rate_b;

        public myViewHolder(View itemView)
        {
            super(itemView);
            if (where.equals("Exodos"))
            {
                mCardView = itemView.findViewById(R.id.card_container);
                title = itemView.findViewById(R.id.name);
                descr = itemView.findViewById(R.id.descr);
                image = itemView.findViewById(R.id.image);
                imgb = itemView.findViewById(R.id.imgb);
                rt = itemView.findViewById(R.id.rating);
                text_shown = itemView.findViewById(R.id.text_shown);
                main_cont = itemView.findViewById(R.id.main_cont);
                rt_nb = itemView.findViewById(R.id.rt_nb);
                rate_b = itemView.findViewById(R.id.rate);
                fav = itemView.findViewById(R.id.fav);
                //details = itemView.findViewById(R.id.details);
            }
            if (where.equals("Psychagwgia"))
            {

                mCardView = itemView.findViewById(R.id.card_container2);
                title = itemView.findViewById(R.id.name2);
                descr = itemView.findViewById(R.id.descr2);
                image = itemView.findViewById(R.id.image2);
                imgb = itemView.findViewById(R.id.imgb2);
                rt = itemView.findViewById(R.id.rating2);
                text_shown = itemView.findViewById(R.id.text_shown2);
                main_cont = itemView.findViewById(R.id.main_cont2);
                rt_nb = itemView.findViewById(R.id.rt_nb2);
                rate_b = itemView.findViewById(R.id.rate2);
                fav = itemView.findViewById(R.id.fav2);
                // details = itemView.findViewById(R.id.details2);
            }

            if (where.equals("Ekdhlwseis"))
            {

                // expCardView = itemView.findViewById(R.id.expcard);
                mCardView = itemView.findViewById(R.id.card_container3);
                title = itemView.findViewById(R.id.name3);
                descr = itemView.findViewById(R.id.descr3);
                image = itemView.findViewById(R.id.image3);
                imgb = itemView.findViewById(R.id.imgb3);
                rt = itemView.findViewById(R.id.rating3);
                text_shown = itemView.findViewById(R.id.text_shown3);
                main_cont = itemView.findViewById(R.id.main_cont3);
                rt_nb = itemView.findViewById(R.id.rt_nb3);
                rate_b = itemView.findViewById(R.id.rate3);
                fav = itemView.findViewById(R.id.fav3);
                //details = itemView.findViewById(R.id.details3);
            }

        }

    }



    @Override
    public int getItemCount() {
        return info.size();
    }


    }
