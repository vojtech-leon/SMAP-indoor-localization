package cz.uhk.vojtele1.indoorpositiontest.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import cz.uhk.vojtele1.indoorpositiontest.R;
import cz.uhk.vojtele1.indoorpositiontest.model.Scan;

import java.util.List;

public class ScanListAdapter extends RecyclerView.Adapter<ScanListAdapter.ScanHolder> {

    private final LayoutInflater inflater;
    private List<Scan> scans; // Cached copy of scans

    public ScanListAdapter(Context context) { inflater = LayoutInflater.from(context); }

    @Override
    public ScanHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.recyclerview_item, parent, false);
        return new ScanHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ScanHolder holder, int position) {
        if (scans != null) {
            Scan current = scans.get(position);
            holder.textViewWifiScan.setText(current.getWifiScans().toString());
            holder.textViewBleScan.setText(current.getBleScans().toString());
            holder.textViewTimestamp.setText(String.valueOf(current.getTimestamp()));
            holder.textViewX.setText(String.valueOf(current.getX()));
            holder.textViewY.setText(String.valueOf(current.getY()));
        } else {
            // Covers the case of data not being ready yet.
            holder.textViewWifiScan.setText(R.string.loading);
            holder.textViewBleScan.setText(R.string.loading);
            holder.textViewTimestamp.setText(R.string.loading);
            holder.textViewX.setText(R.string.loading);
            holder.textViewY.setText(R.string.loading);
        }
    }

    public void setScans(List<Scan> scans){
        this.scans = scans;
        notifyDataSetChanged();
    }

    // getItemCount() is called many times, and when it is first called,
    // wifiScans has not been updated (means initially, it's null, and we can't return null).
    @Override
    public int getItemCount() {
        if (scans != null)
            return scans.size();
        else return 0;
    }

    class ScanHolder extends RecyclerView.ViewHolder {
        private final TextView textViewWifiScan;
        private final TextView textViewBleScan;
        private final TextView textViewTimestamp;
        private final TextView textViewX;
        private final TextView textViewY;

        private ScanHolder(View itemView) {
            super(itemView);
            textViewWifiScan = itemView.findViewById(R.id.textViewWifiScans);
            textViewBleScan = itemView.findViewById(R.id.textViewBleScans);
            textViewTimestamp = itemView.findViewById(R.id.textViewTimestamp);
            textViewX = itemView.findViewById(R.id.textViewX);
            textViewY = itemView.findViewById(R.id.textViewY);
        }
    }
}