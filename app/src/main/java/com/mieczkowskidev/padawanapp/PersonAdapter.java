package com.mieczkowskidev.padawanapp;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Patryk Mieczkowski on 14.05.16.
 */
public class PersonAdapter extends RecyclerView.Adapter<PersonAdapter.PersonViewHolder> {

    private List<Person> personList;

    public PersonAdapter(List<Person> personList) {
        this.personList = personList;
    }


    @Override
    public PersonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.person_item, parent, false);
        return new PersonViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PersonViewHolder holder, int position) {
        holder.name.setText(personList.get(position).name);
        holder.date.setText(personList.get(position).birth_year);
        holder.hairColor.setText(personList.get(position).hair_color);
        String mass = personList.get(position).mass + " kg";
        holder.mass.setText(mass);
        String height = personList.get(position).height + " cm";
        holder.height.setText(height);
    }

    @Override
    public int getItemCount() {
        return personList.size();
    }

    public static class PersonViewHolder extends RecyclerView.ViewHolder {

        TextView name, date, height, mass, hairColor;

        public PersonViewHolder(View itemView) {
            super(itemView);

            name = (TextView) itemView.findViewById(R.id.person_item_name);
            date = (TextView) itemView.findViewById(R.id.person_item_date);
            height = (TextView) itemView.findViewById(R.id.person_item_height);
            mass = (TextView) itemView.findViewById(R.id.person_item_mass);
            hairColor = (TextView) itemView.findViewById(R.id.person_hair_color);
        }
    }
}
