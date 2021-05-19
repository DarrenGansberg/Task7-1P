package com.darrengansberg.noteapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.darrengansberg.noteapp.models.Note;

/* The fragment that provides the app activity's view when the use wants to browse their notes. */

public class BrowseNotesFragment extends Fragment {

    NoteManager noteManager;
    int noteCount = 0;


    public BrowseNotesFragment() {
        // Required empty public constructor
    }

    public static BrowseNotesFragment newInstance() {
        BrowseNotesFragment fragment = new BrowseNotesFragment();
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
        return inflater.inflate(R.layout.fragment_browse_notes, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        noteManager = new NoteManager();
        noteManager.openDatabaseReadOnly(getContext());
        noteCount = noteManager.getCount(getContext());
        RecyclerView notesView = view.findViewById(R.id.notes_recycler_view);
        notesView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        notesView.setAdapter(new NotesRecyclerAdapter());
    }

    private class NotesRecyclerAdapter extends RecyclerView.Adapter<NotesRecyclerAdapter.ViewHolder> {

        private class ViewHolder extends RecyclerView.ViewHolder{

            AppCompatTextView noteContentView;
            CardView noteContainer;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                noteContentView = itemView.findViewById(R.id.note_content_view);
                noteContainer = itemView.findViewById(R.id.note_cardView);
            }

            public AppCompatTextView getNoteContentView()
            {
                return noteContentView;
            }
            public CardView getNoteContainer(){ return noteContainer;}
        }

        @NonNull
        @Override
        public NotesRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.note_layout, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull NotesRecyclerAdapter.ViewHolder holder, int position) {

            Note note = noteManager.getByIndex(getContext(),position);
            holder.getNoteContentView().setText(note.getContent());
            holder.getNoteContainer().setOnClickListener(new NoteCardOnClickListener(note.getId()));

        }

        @Override
        public int getItemCount() {
            return noteManager.getCount(getContext());
        }

        private class NoteCardOnClickListener implements View.OnClickListener{

            private long noteId = 0;

            public NoteCardOnClickListener(long noteId)
            {
                this.noteId = noteId;
            }

            @Override
            public void onClick(View v) {
                NavController controller = Navigation.findNavController(v);
                Bundle args = new Bundle();
                args.putLong("noteId", noteId);
                controller.navigate(R.id.action_browseNotesFragment_to_editNoteFragment, args);
            }
        }
    }
}