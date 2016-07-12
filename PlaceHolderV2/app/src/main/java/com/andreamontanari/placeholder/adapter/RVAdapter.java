package com.andreamontanari.placeholder.adapter;

/**
 * Created by andreamontanari on 08/07/16.
 */

        import android.content.Context;
        import android.graphics.Color;
        import android.support.annotation.NonNull;
        import android.support.annotation.Nullable;
        import android.support.v7.widget.CardView;
        import android.support.v7.widget.RecyclerView;
        import android.util.Log;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ImageView;
        import android.widget.TextView;


        import com.andreamontanari.placeholder.Place;
        import com.andreamontanari.placeholder.PlacesActivity;
        import com.andreamontanari.placeholder.R;
        import com.andreamontanari.placeholder.utils.PlaceMisc;
        import com.google.android.gms.vision.text.Text;

        import java.util.List;

        import io.realm.OrderedRealmCollection;
        import io.realm.RealmRecyclerViewAdapter;

/**
 * Created by andreamontanari on 18/11/15.
 */
 

public class RVAdapter extends RealmRecyclerViewAdapter<Place, RVAdapter.PlacesViewHolder> {


    private final PlacesActivity activity;

    public RVAdapter(PlacesActivity activity, OrderedRealmCollection<Place> data) {
        super(activity ,data, true);
        this.activity = activity;
    }

    @Override
    public PlacesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.place_item, parent, false);
        PlacesViewHolder svh = new PlacesViewHolder(v);
        return svh;
    }

    @Override
    public void onBindViewHolder(final PlacesViewHolder holder, final int position) {
        holder.streetName.setText(getData().get(position).getStreetName());
        holder.latlng.setText(getData().get(position).getLatLng());
        holder.latitude.setText(Double.toString(getData().get(position).getLatitude()));
        holder.longitude.setText(Double.toString(getData().get(position).getLongitude()));
        if (getData().get(position).getPlaceComment() == "" || getData().get(position).getPlaceComment() == null) {
            holder.comments.setText(context.getString(R.string.place_comment_intro));
        }
        else {
            holder.comments.setText(getData().get(position).getPlaceComment());
        }

        holder.comments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView tv = (TextView) v.findViewById(R.id.comments);

                double lat = Double.parseDouble(holder.latitude.getText().toString());
                double lng = Double.parseDouble(holder.longitude.getText().toString());;
                String commentPlace = tv.getText().toString();
                PlaceMisc.storePlaceNote(commentPlace, lat, lng,  context);
            }
        });
        
         holder.v.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                // show alert dialog
                
                // on okay
                // delete selected item from Realm.io
                Place selected = getData().get(position);
                selected.deleteFromRealm()
                return true;
                
                // else -- dismiss
            }
        });
        
    }

    @Override
    public int getItemCount() {
        return getData().size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

     class PlacesViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView streetName;
        TextView latlng;
        TextView savedDate;
        TextView comments;
        ImageView icon;
        TextView latitude, longitude;

        PlacesViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            streetName = (TextView)itemView.findViewById(R.id.streetname);
            latlng = (TextView)itemView.findViewById(R.id.latlng);
            //savedDate = (TextView)itemView.findViewById(R.id.saved_date);
            comments = (TextView)itemView.findViewById(R.id.comments);
            icon = (ImageView)itemView.findViewById(R.id.icon);
            latitude = (TextView) itemView.findViewById(R.id.latitude);
            longitude = (TextView) itemView.findViewById(R.id.longitude);

        }
    }





}
