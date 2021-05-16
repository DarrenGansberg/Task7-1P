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

import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;
import java.util.Vector;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BrowseNotesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BrowseNotesFragment extends Fragment {

    List<String> notes;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public BrowseNotesFragment() {
        // Required empty public constructor
        notes = new Vector<>();
        for (int i = 1, count = 1000; i <= count; i++)
        {
            notes.add("String " + String.valueOf(i));
        }
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BrowseNotesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BrowseNotesFragment newInstance(String param1, String param2) {
        BrowseNotesFragment fragment = new BrowseNotesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
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
            String note = notes.get(position);
            holder.getNoteContentView().setText(note);
            holder.getNoteContainer().setOnClickListener(new NoteCardOnClickListener());
        }

        @Override
        public int getItemCount() {
            return notes.size();
        }

        private class NoteCardOnClickListener implements View.OnClickListener{

            @Override
            public void onClick(View v) {
                NavController controller = Navigation.findNavController(v);
                controller.navigate(R.id.action_browseNotesFragment_to_editNoteFragment);
            }
        }
    }
}