package br.com.auwaca.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import br.com.auwaca.R;
import br.com.auwaca.databinding.FragmentHomeBinding;
import br.com.auwaca.models.Resumo;
import br.com.auwaca.models.Sensor;

public class HomeFragment extends Fragment {
    ImageView homeLogoWithName;
    private FragmentHomeBinding binding;
    LinearLayout squad1;
    LinearLayout squad2;
    private LayoutInflater inflater;
    private ViewGroup container;

    TextView descCardHome;
    TextView percMenorPico;
    TextView percUmidadeMedia;

    private DatabaseReference mDatabase;
    private FirebaseDatabase database;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        this.inflater = inflater;
        this.container = container;

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        descCardHome = (TextView) root.findViewById(R.id.descCardHomeId);
        percMenorPico = root.findViewById(R.id.percMenorPicoId);
        percUmidadeMedia = root.findViewById(R.id.percUmidadeMediaId);

//        final TextView textView = binding.textHome;
//        homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        homeLogoWithName = (ImageView) root.findViewById(R.id.home_logo);
        homeLogoWithName.setImageResource(R.drawable.app_logo_with_name);

        database = FirebaseDatabase.getInstance();


        System.out.println("DADOS DO FIREBASE");
//        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase = database.getReference().child("auwaca");

        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Sensor sensor = new Sensor(snapshot.child("status").getValue(String.class), snapshot.child("statusMotor").getValue(Integer.class), snapshot.child("statusNivel").getValue(Integer.class));

                Resumo resumo = new Resumo(0,0);

                int res =  (1024-sensor.getStatusNivel())*100/1024;
                String desc = descCardHome.getText()+" "+res+"%.";
                descCardHome.setText(desc);

                percMenorPico.setText(resumo.getMenorPico().toString());
                percUmidadeMedia.setText(resumo.getUmidadeMedia().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}