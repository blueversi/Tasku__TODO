package pl.bluepito.goalertodolist.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import pl.bluepito.goalertodolist.Controllers.DatabaseHelper;
import pl.bluepito.goalertodolist.R;

public class SettingsFragment extends Fragment {

    private static final String TAG = "SettingsFragment";
    private DatabaseHelper databaseHelper;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_settings, container, false);





        return view;
    }

}
