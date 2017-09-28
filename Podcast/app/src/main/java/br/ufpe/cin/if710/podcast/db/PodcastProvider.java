package br.ufpe.cin.if710.podcast.db;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

public class PodcastProvider extends ContentProvider {
    //database instance
    PodcastDBHelper podcastDB;

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // run a delete query in a readable database with the respective params.
        return podcastDB.getWritableDatabase().delete(PodcastDBHelper.DATABASE_TABLE, selection,selectionArgs);
    }

    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        // run an insert in Podcast Database with the respective params.
        long id = podcastDB.getWritableDatabase().insert(PodcastDBHelper.DATABASE_TABLE, null, values);
        return Uri.withAppendedPath(PodcastProviderContract.EPISODE_LIST_URI, Long.toString(id));
    }

    @Override
    public boolean onCreate() {
        //creating databse instance
        podcastDB = PodcastDBHelper.getInstance(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        // run a query in a readable database with the respective params.
        return podcastDB.getReadableDatabase().query(PodcastDBHelper.DATABASE_TABLE, projection, selection, selectionArgs, null, null, sortOrder);
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        //run an update in Podcast database with the respective params
        return podcastDB.getWritableDatabase().update(PodcastDBHelper.DATABASE_TABLE,values,selection,selectionArgs);
    }
}
