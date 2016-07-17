package com.andreamontanari.placeholder.adapter;

/**
 * Created by andreamontanari on 08/07/16.
 */

        import android.content.ClipData;
        import android.content.ClipboardManager;
        import android.content.Context;
        import android.content.DialogInterface;
        import android.content.Intent;
        import android.graphics.Color;
        import android.support.annotation.NonNull;
        import android.support.annotation.Nullable;
        import android.support.v7.app.AlertDialog;
        import android.support.v7.widget.CardView;
        import android.support.v7.widget.RecyclerView;
        import android.util.Log;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.ImageView;
        import android.widget.TextView;
        import android.widget.Toast;


        import com.andreamontanari.placeholder.Place;
        import com.andreamontanari.placeholder.PlacesActivity;
        import com.andreamontanari.placeholder.R;
        import com.andreamontanari.placeholder.utils.PlaceMisc;
        import com.google.android.gms.vision.text.Text;

        import java.util.List;

        import io.realm.OrderedRealmCollection;
        import io.realm.Realm;
        import io.realm.RealmConfiguration;
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

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                double lat = Double.parseDouble(holder.latitude.getText().toString());
                double lng = Double.parseDouble(holder.longitude.getText().toString());
                String streetName = holder.streetName.getText().toString();
                ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("label", streetName + ", (" + lat +", " + lng +")" );
                clipboard.setPrimaryClip(clip);
                Toast.makeText(context, context.getResources().getString(R.string.copied_place), Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        holder.comments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView tv = (TextView) v.findViewById(R.id.comments);
                double lat = Double.parseDouble(holder.latitude.getText().toString());
                double lng = Double.parseDouble(holder.longitude.getText().toString());
                String commentPlace = tv.getText().toString();
                PlaceMisc.storePlaceNote(commentPlace, lat, lng,  context);
            }
        });

        holder.nav_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double lat = Double.parseDouble(holder.latitude.getText().toString());
                double lng = Double.parseDouble(holder.longitude.getText().toString());
                PlaceMisc.getDirections(lat, lng, activity.getApplicationContext());
            }
        });

        holder.share_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double lat = Double.parseDouble(holder.latitude.getText().toString());
                double lng = Double.parseDouble(holder.longitude.getText().toString());
                String streetName = holder.streetName.getText().toString();
                String comment = holder.comments.getText().toString() == activity.getApplicationContext().getString(R.string.place_comment_intro) ? "" : holder.comments.getText().toString();
                Intent sendIntent = new Intent();
                sendIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "\"" + comment + "\" - " + streetName + ", (" + lat +", " + lng +")" );
                sendIntent.setType("text/plain");
                activity.getApplicationContext().startActivity(sendIntent);
            }
        });

        holder.delete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LayoutInflater inflater = LayoutInflater.from(context);
                final View dialogLayout = inflater.inflate(R.layout.dialog_place_deletion, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(context);

                builder.setView(dialogLayout);

                final AlertDialog customAlertDialog = builder.create();
                customAlertDialog.show();

                Button undo_btn = (Button) dialogLayout.findViewById(R.id.undo_btn);
                undo_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        customAlertDialog.dismiss();
                    }
                });

                Button confirm_btn = (Button) dialogLayout.findViewById(R.id.confirm_btn);
                confirm_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        RealmConfiguration realmConfig = new RealmConfiguration.Builder(context)
                                .deleteRealmIfMigrationNeeded()
                                .build();
                        // Open the Realm for the UI thread.
                        Realm realm = Realm.getInstance(realmConfig);
                        realm.beginTransaction();
                        Place selected = getData().get(position);
                        selected.deleteFromRealm();
                        realm.commitTransaction();
                        customAlertDialog.dismiss();
                        Toast.makeText(context, context.getString(R.string.place_deleted), Toast.LENGTH_SHORT).show();
                    }
                });
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
        ImageView icon, delete_btn;
        TextView latitude, longitude;
        Button share_btn, nav_btn;

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
            share_btn = (Button) itemView.findViewById(R.id.share_btn);
            nav_btn  = (Button) itemView.findViewById(R.id.direction_btn);
            delete_btn = (ImageView) itemView.findViewById(R.id.delete_btn);
        }
    }





}
