package com.darrengansberg.noteapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.jetbrains.annotations.NotNull;

/*The manage notes fragment provides the app's view of the
app's activity when the user first starts the app.
 */
public class ManageNotesFragment extends Fragment {

    public ManageNotesFragment() {
        // Required empty public constructor
    }

    public static ManageNotesFragment newInstance() {
        ManageNotesFragment fragment = new ManageNotesFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {  }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_manage_notes, container, false);
    }

    @Override
    public void onViewCreated(@NotNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupCreateNewNoteButton(view.findViewById(R.id.create_new_note_button));
        setupShowNotesButton(view.findViewById(R.id.show_notes_button));

    }

    protected void setupCreateNewNoteButton(@NonNull AppCompatButton button)
    {
        button.setOnClickListener(new CreateNewNoteButtonOnClickListener());
    }

    protected void setupShowNotesButton(@NonNull AppCompatButton button)
    {
        button.setOnClickListener(new ShowNotesButtonOnClickListener());
    }

    private class CreateNewNoteButtonOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            NavController controller = Navigation.findNavController(v);
            controller.navigate(R.id.action_manageNotesFragment_to_createNoteFragment, null);
        }
    }

    private class ShowNotesButtonOnClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            NavController controller = Navigation.findNavController(v);
            controller.navigate(R.id.action_manageNotesFragment_to_browseNotesFragment, null);
        }
    }
}