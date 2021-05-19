package com.darrengansberg.noteapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.darrengansberg.noteapp.models.Note;

/* The fragment that provides the app activity's view when the use wants to edit a note. */
public class EditNoteFragment extends Fragment {

    private static final String ARG_NOTE_ID = "noteId";
    private long noteId;

    public EditNoteFragment() {
        // Required empty public constructor
    }

    public static EditNoteFragment newInstance(long noteId) {
        EditNoteFragment fragment = new EditNoteFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_NOTE_ID, noteId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            noteId = getArguments().getLong(ARG_NOTE_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_note, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        NoteManager manager = new NoteManager();
        Note note = manager.getById(view.getContext(), noteId);
        AppCompatEditText contentView = view.findViewById(R.id.edit_note_content_view);
        contentView.setText(note.getContent());
        Button updateButton = view.findViewById(R.id.update_note_button);
        updateButton.setOnClickListener(new UpdateButtonOnClickListener(note.getId()));
        Button deleteButton = view.findViewById(R.id.delete_note_button);
        deleteButton.setOnClickListener(new DeleteButtonOnClickListener(note.getId()));
    }

    private class UpdateButtonOnClickListener implements View.OnClickListener{

        private long noteId;

        public UpdateButtonOnClickListener(long noteId)
        {
            this.noteId = noteId;
        }

        @Override
        public void onClick(View v) {
            NoteManager manager = new NoteManager();
            Note note = manager.getById(v.getRootView().getContext(),noteId);
            AppCompatEditText contentView = (v.getRootView())
                    .findViewById(R.id.edit_note_content_view);
            String updatedContent = contentView.getText().toString();
            note.setContent(updatedContent);
            manager.update(v.getRootView().getContext(), note);
            NavController controller = Navigation.findNavController(v);
            controller.popBackStack();

        }
    }

    private class DeleteButtonOnClickListener implements View.OnClickListener{

        private long noteId;

        public DeleteButtonOnClickListener(long noteId){
            this.noteId = noteId;
        }

        @Override
        public void onClick(View v) {

            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getContext());
            alertBuilder.setTitle("Delete Note")
                        .setMessage("Delete this note?");
            alertBuilder.setNegativeButton(R.string.cancel_button_text, (dialog, which) -> dialog.cancel());
            alertBuilder.setPositiveButton(R.string.delete_note_button_text, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    try {
                        NoteManager manager = new NoteManager();
                        manager.delete(getContext(), noteId);
                        NavController controller = Navigation.findNavController(v);
                        controller.popBackStack();
                    } catch (IllegalArgumentException ex){
                        Toast.makeText(getContext(), "Unable to delete note", Toast.LENGTH_SHORT).show();
                        NavController controller = Navigation.findNavController(v);
                        controller.popBackStack();
                    }
                }
            });
            AlertDialog dialog = alertBuilder.show();

        }
    }
}