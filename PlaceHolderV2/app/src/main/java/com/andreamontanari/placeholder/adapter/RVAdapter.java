package com.andreamontanari.placeholder.adapter;

/**
 * Created by andreamontanari on 08/07/16.
 */

        import android.graphics.Color;
        import android.support.v7.widget.CardView;
        import android.support.v7.widget.RecyclerView;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ImageView;
        import android.widget.TextView;


        import com.andreamontanari.placeholder.Place;
        import com.andreamontanari.placeholder.R;
        import com.google.android.gms.vision.text.Text;

        import java.util.List;

/**
 * Created by andreamontanari on 18/11/15.
 */
 

public class RVAdapter extendsRealmRecyclerViewAdapter<Place, RVAdapter.PlacesViewHolder> {
                                        
    public static class PlacesViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView streetName;
        TextView latlng;
        TextView savedDate;
        TextView comments;
        ImageView icon;

        PlacesViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            streetName = (TextView)itemView.findViewById(R.id.streetname);
            latlng = (TextView)itemView.findViewById(R.id.latlng);
            //savedDate = (TextView)itemView.findViewById(R.id.saved_date);
            comments = (TextView)itemView.findViewById(R.id.comments);
            icon = (ImageView)itemView.findViewById(R.id.icon);
        }
    }

    List<Place> places;

    public RVAdapter(PlacesActivity activity, OrderedRealmCollection<Place> data) {
        super(activity ,data, true);
        this.activity = activity;
    }

    RVAdapter() { }

    @Override
    public PlacesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.place_item, parent, false);
        PlacesViewHolder svh = new PlacesViewHolder(v);
        return svh;
    }

    @Override
    public void onBindViewHolder(PlacesViewHolder holder, int position) {
        holder.streetName.setText(places.get(position).getStreetName());
        holder.latlng.setText(places.get(position).getLatLng());
        if (places.get(position).getPlaceComment() == "" || places.get(position).getPlaceComment() == null) {
            holder.comments.setVisibility(View.GONE);
        }
        else {
            holder.comments.setText(places.get(position).getPlaceComment());
        }
        //holder.savedDate.setText(places.get(position).getSavedOn());
        //holder.icon.setImageResource(persons.get(i).photoId);
        /*
        if (scheduled.get(position).is_scheduled()) {
            holder.cv.setCardBackgroundColor(Color.parseColor("#8BC34A"));
        } else {
            holder.cv.setCardBackgroundColor(Color.parseColor("#EF5350"));
        }
        */
    }

    @Override
    public int getItemCount() {
        return places.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

}
