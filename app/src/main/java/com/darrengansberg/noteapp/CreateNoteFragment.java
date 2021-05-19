package com.darrengansberg.noteapp;

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

import org.jetbrains.annotations.NotNull;

/* The fragment that provides the app activity's view when the use wants to create a note. */

public class CreateNoteFragment extends Fragment {


    public CreateNoteFragment() {
        // Required empty public constructor
    }

    public static CreateNoteFragment newInstance() {
        CreateNoteFragment fragment = new CreateNoteFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) { }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_note, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button saveButton = view.findViewById(R.id.save_note_button);
        saveButton.setOnClickListener(new SaveButtonOnClickListener());
    }

    private class SaveButtonOnClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {

            try {
                NoteManager manager = new NoteManager();
                Note newNote = new Note();
                AppCompatEditText noteView = v.getRootView().findViewById(R.id.create_note_content_view);

                newNote.setContent(noteView.getText().toString());
                manager.add(v.getContext(), newNote);

            } catch (IllegalStateException e) {

                if (e.getMessage() == "Adding note to database failed")
                {
                    Toast.makeText(v.getContext(),e.getMessage(), Toast.LENGTH_SHORT).show();
                    return;
                }
                throw e;
            }
            NavController controller = Navigation.findNavController(v);
            controller.popBackStack();
        }
    }
}