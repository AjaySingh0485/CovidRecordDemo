package com.example.covidrecord;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class CovidRecordAdapter extends RecyclerView.Adapter<CovidRecordAdapter.Myholder> {
    Context context;
    List<CovidList>covidLists;

    public CovidRecordAdapter(Context context, List<CovidList> covidLists) {
        this.context = context;
        this.covidLists = covidLists;
    }

    @NonNull
    @Override
    public Myholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_layout, parent, false);
        CovidRecordAdapter.Myholder myViewHolder = new CovidRecordAdapter.Myholder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull Myholder holder, int position) {
      /*  Date date = null;
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        SimpleDateFormat format2 = new SimpleDateFormat("EEE, d MMM yyyy");*/

        String strDate  = covidLists.get(position).getDate();
        DateFormat to   = new SimpleDateFormat("MMM d, yyyy"); // wanted format
        DateFormat from = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'"); // current format
        try {
            String dateConvert=to.format(from.parse(strDate));
            holder.textViewDate.setText(dateConvert);
            System.out.println(to.format(from.parse(strDate)));
        } catch (ParseException e) {
            e.printStackTrace();
        }


        holder.tvConfirmedCase.setText(covidLists.get(position).getConfirmed()+"");
        holder.tvRecoveredCase.setText(covidLists.get(position).getRecovered()+"");
        holder.tvDeath.setText(covidLists.get(position).getDeaths()+"");
        holder.tvActiveCase.setText(covidLists.get(position).getActive()+"");
    }

    @Override
    public int getItemCount() {
        return covidLists.size();
    }

    public class Myholder extends RecyclerView.ViewHolder {
        TextView textViewDate,tvConfirmedCase,tvRecoveredCase,tvDeath, tvActiveCase;
        public Myholder(@NonNull View itemView) {
            super(itemView);
            textViewDate=itemView.findViewById(R.id.textView);
            tvConfirmedCase=itemView.findViewById(R.id.text_cases);
            tvRecoveredCase=itemView.findViewById(R.id.text_recovered);
            tvDeath=itemView.findViewById(R.id.tvdeath);
            tvActiveCase=itemView.findViewById(R.id.tvactive);

        }
    }
}
