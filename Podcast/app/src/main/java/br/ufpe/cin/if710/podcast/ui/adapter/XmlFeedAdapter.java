package br.ufpe.cin.if710.podcast.ui.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import br.ufpe.cin.if710.podcast.R;
import br.ufpe.cin.if710.podcast.domain.ItemFeed;
import br.ufpe.cin.if710.podcast.services.EpisodeDownloadService;
import br.ufpe.cin.if710.podcast.services.MusicPlayerService;

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
        Button item_action;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(getContext(), linkResource, null);
            holder = new ViewHolder();
            holder.item_title = (TextView) convertView.findViewById(R.id.item_title);
            holder.item_date = (TextView) convertView.findViewById(R.id.item_date);
            holder.item_action = (Button)convertView.findViewById(R.id.item_action);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        holder.item_title.setText(getItem(position).getTitle());
        holder.item_date.setText(getItem(position).getPubDate());

        if(getItem(position).getUri()!=null){
            holder.item_action.setEnabled(true);
            holder.item_action.setText("Ouvir");
        }else{
            //https://stackoverflow.com/questions/41256172/changing-one-item-only-in-a-listview-or-recycleview
            holder.item_action.setText("Download");
        }

        holder.item_action.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                //when button clicked without uri, download Podcast episode
                if(getItem(position).getUri()==null){
                    ((Button)view).setEnabled(false);
                    Intent downloadService = new Intent(getContext(),EpisodeDownloadService.class);
                    String downloadLink = getItem(position).getDownloadLink();
                    //passing the position and uri to downloadService
                    downloadService.setData(Uri.parse(downloadLink));
                    downloadService.putExtra("selectedItem",position);
                    getContext().startService(downloadService);
                }else{
                    //if has uri, play podcast, starting on paused point
                    Intent musicIntent = new Intent(getContext(),MusicPlayerService.class);
                    musicIntent.setData(Uri.parse(getItem(position).getUri()));
                    musicIntent.putExtra("selectedPosition",position);
                    musicIntent.putExtra("currentPosition", getItem(position).getCurrentPosition());
                    getContext().startService(musicIntent);
                    holder.item_action.setText("Pausar");
                }
            }


        });
        return convertView;
    }
}