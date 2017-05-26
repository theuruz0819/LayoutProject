package com.example.user.layoutproject.fragment;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;

import com.example.user.layoutproject.R;
import com.example.user.layoutproject.model.Book;

public class BookParserResultDialogFragment extends DialogFragment {

    interface BookParsorResDialogListener {
        void saveBook(Book book);
    }

    private Book book;
    private BookParsorResDialogListener mListener;

    public static BookParserResultDialogFragment newInstance(Book book, BookParsorResDialogListener listener) {
        BookParserResultDialogFragment fragment = new BookParserResultDialogFragment();
        fragment.book = book;
        fragment.mListener = listener;
        return fragment;
    }
    public void onCreate(Bundle state) {
        super.onCreate(state);
        setRetainInstance(true);
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.fragment_book_parser_result_dialog, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);
        builder.setTitle(book.getTitle());
        builder.setPositiveButton("SAVE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mListener.saveBook(book);
                dismiss();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dismiss();
            }
        });

        return builder.create();
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
    }
}
