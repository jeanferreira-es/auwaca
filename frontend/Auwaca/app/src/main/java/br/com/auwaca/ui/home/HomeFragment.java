package br.com.auwaca.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import br.com.auwaca.R;
import br.com.auwaca.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {
    ImageView homeLogoWithName;
    private FragmentHomeBinding binding;
    LinearLayout squad1;
    LinearLayout squad2;
    private LayoutInflater inflater;
    private ViewGroup container;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        this.inflater = inflater;
        this.container = container;

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

//        final TextView textView = binding.textHome;
//        homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        homeLogoWithName = (ImageView) root.findViewById(R.id.home_logo);
        homeLogoWithName.setImageResource(R.drawable.app_logo_with_name);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}