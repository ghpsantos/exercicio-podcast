package br.ufpe.cin.if710.podcast.ui.adapter;

import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import br.ufpe.cin.if710.podcast.R;
import br.ufpe.cin.if710.podcast.domain.ItemFeed;
import br.ufpe.cin.if710.podcast.services.DownloadService;

public class XmlFeedAdapter extends ArrayAdapter<ItemFeed> {

    int linkResource;

    public XmlFeedAdapter(Context context, int resource, List<ItemFeed> objects) {
        super(context, resource, objects);
        linkResource = resource;
    }

    /**
     * public abstract View getView (int position, View convertView, ViewGroup parent)
     * <p>
     * Added in API level 1
     * Get a View that displays the data at the specified position in the data set. You can either create a View manually or inflate it from an XML layout file. When the View is inflated, the parent View (GridView, ListView...) will apply default layout parameters unless you use inflate(int, android.view.ViewGroup, boolean) to specify a root view and to prevent attachment to the root.
     * <p>
     * Parameters
     * position	The position of the item within the adapter's data set of the item whose view we want.
     * convertView	The old view to reuse, if possible. Note: You should check that this view is non-null and of an appropriate type before using. If it is not possible to convert this view to display the correct data, this method can create a new view. Heterogeneous lists can specify their number of view types, so that this View is always of the right type (see getViewTypeCount() and getItemViewType(int)).
     * parent	The parent that this view will eventually be attached to
     * Returns
     * A View corresponding to the data at the specified position.
     */


	/*
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.itemlista, parent, false);
		TextView textView = (TextView) rowView.findViewById(R.id.item_title);
		textView.setText(items.get(position).getTitle());
	    return rowView;
	}
	/**/

    //http://developer.android.com/training/improving-layouts/smooth-scrolling.html#ViewHolder
    static class ViewHolder {
        TextView item_title;
        TextView item_date;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(getContext(), linkResource, null);
            holder = new ViewHolder();
            holder.item_title = (TextView) convertView.findViewById(R.id.item_title);
            holder.item_date = (TextView) convertView.findViewById(R.id.item_date);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        holder.item_title.setText(getItem(position).getTitle());
        holder.item_date.setText(getItem(position).getPubDate());
        //setting On click button.
        final Button downloadButton = (Button)convertView.findViewById(R.id.item_action);

        downloadButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(),"Clicou no item: " + position, Toast.LENGTH_LONG).show();
                downloadButton.setEnabled(false);
                Intent downloadService = new Intent(getContext(),DownloadService.class);
                String downloadLink = getItem(position).getDownloadLink();
                downloadService.setData(Uri.parse(downloadLink));
                downloadService.putExtra("selectedItem",position);
                //registering receiver of downloadCompleteEvent
//                IntentFilter f = new IntentFilter(DownloadService.DOWNLOAD_COMPLETE);
//                LocalBroadcastManager.getInstance(getContext()).registerReceiver(onDownloadCompleteEvent, f);

                getContext().startService(downloadService);
            }

//            private BroadcastReceiver onDownloadCompleteEvent=new BroadcastReceiver() {
//
//                //onReceive unregister the onDownloadCOmpleteEvent and reimplement onClickButton.
//                public void onReceive(Context ctxt, Intent i) {
//                    LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(onDownloadCompleteEvent);
//                    downloadButton.setEnabled(true);
//                    downloadButton.setText("Ouvir");
//                    Toast.makeText(getContext(), i.getData().toString(), Toast.LENGTH_LONG).show();
//                    downloadButton.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//
////                            LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(onDownloadCompleteEvent);
////                            Toast.makeText(getContext(), "????", Toast.LENGTH_LONG).show();
//                        }
//                    });
//                    Toast.makeText(ctxt, "Download finalizado!", Toast.LENGTH_LONG).show();
//                }
//            };

        });
        return convertView;
    }


//    @Override
//    protected void onResume() {
//        super.onResume();
//        IntentFilter f = new IntentFilter(DownloadService.DOWNLOAD_COMPLETE);
//        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(onDownloadCompleteEvent, f);
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(onDownloadCompleteEvent);
//    }
//
//    private BroadcastReceiver onDownloadCompleteEvent=new BroadcastReceiver() {
//        public void onReceive(Context ctxt, Intent i) {
//            downloadButton.setEnabled(true);
//            Toast.makeText(ctxt, "Download finalizado!", Toast.LENGTH_LONG).show();
//        }
//    };
}