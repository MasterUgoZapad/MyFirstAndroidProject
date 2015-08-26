package com.example.biokm_000.scino_library;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.app.Activity;

import java.util.List;

/**
 * Created by biokm_000 on 20.08.2015.
 */
public class BookAdapter extends BaseAdapter {
    private List<MyBook> elements;
    private Activity context;

    public BookAdapter(Activity c, List<MyBook> rows) {
        this.elements = rows;
        this.context = c;
    }
    @Override
    public int getCount() {
        return elements.size();
    }

    @Override
    public Object getItem(int position) {
        return elements.get(position);
    }

    @Override
    public long getItemId(int position) {
        return elements.get(position).GetId();
    }

    static class ViewHolder {
        public TextView textNameView;
        public TextView textAutorView;
        public TextView textGenreView;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final MyBook item = (MyBook) getItem(position);
        ViewHolder holder;
        View rowView = convertView;
        if (rowView == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            rowView = inflater.inflate(R.layout.list_item, null, true);
            holder = new ViewHolder();
            holder.textNameView = (TextView) rowView.findViewById(R.id.TextNameItem);
            holder.textAutorView = (TextView) rowView.findViewById(R.id.TextAutorItem);
            holder.textGenreView = (TextView) rowView.findViewById(R.id.TextGenreItem);
            rowView.setTag(holder);
        } else {
            holder = (ViewHolder) rowView.getTag();
        }

        holder.textNameView.setText(item.GetName());
        holder.textGenreView.setText(item.GetAutor());
        holder.textAutorView.setText(item.GetJenre());
        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showPopupMenu(v, item);
            }
        });

        int color;
        if (item.GetRead() == 1)
            color = getContext().getResources().getColor(R.color.unreaded_color);
        else
            color = getContext().getResources().getColor(R.color.readed_color);
        rowView.setBackgroundColor(color);
        return rowView;
    }

    private void showPopupMenu(View v, final MyBook book) {
        final Integer id = book.GetId();
        PopupMenu popupMenu = new PopupMenu(context, v);
        popupMenu.inflate(R.menu.popup_menu);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.menu1:
                        Integer r = book.GetRead();
                        String read;
                        if (r == 0)
                            read = "1";
                        else
                            read = "0";
                        MainActivity.mSqLiteDatabase.execSQL("UPDATE " + BookDatabase.BOOK_TABLE_NAME + " SET " + BookDatabase.READ + " = " + read + " WHERE " + BookDatabase.BOOK_ID + " = \"" + id.toString() + "\"");
                        MainActivity.mBookDatabase.ChangeGridParametrizedDB();
                        return true;
                    case R.id.menu2:
                        MainActivity.mSqLiteDatabase.execSQL("DELETE FROM " + BookDatabase.BOOK_TABLE_NAME + " WHERE " + BookDatabase.BOOK_ID + " = " + id.toString());
                        MainActivity.mBookDatabase.ChangeGridParametrizedDB();
                        return true;
                    case R.id.menu3:
                        Intent intent = new Intent(context, AddBookActivity.class);
                        intent.putExtra(BookDatabase.BOOK_ID, id);
                        context.startActivity(intent);
                        return true;
                    default:
                        return false;
                }
            }
        });
        popupMenu.show();
    }
    public Context getContext() {
        return context;
    }
}
