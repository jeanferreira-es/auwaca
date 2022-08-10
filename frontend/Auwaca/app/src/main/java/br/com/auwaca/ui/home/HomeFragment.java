package br.com.auwaca.ui.home;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import br.com.auwaca.R;
import br.com.auwaca.databinding.FragmentHomeBinding;
import br.com.auwaca.models.Registro;
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
    TextView cardDate;
    TextView cardTitle;

    Button wcButton;
    int status = 0;

    private DatabaseReference mDatabase;
    private FirebaseDatabase database;

    Registro resumo;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        this.inflater = inflater;
        this.container = container;

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        resumo = new Registro(0,0);
        descCardHome = (TextView) root.findViewById(R.id.descCardHomeId);
        percMenorPico = root.findViewById(R.id.percMenorPicoId);
        percUmidadeMedia = root.findViewById(R.id.percUmidadeMediaId);
        cardDate = root.findViewById(R.id.cardDateId);
        cardTitle = root.findViewById(R.id.cardTitleId);

        wcButton = root.findViewById(R.id.wcButtonId);

        wcButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleClick(wcButton);
            }
        });

//        final TextView textView = binding.textHome;
//        homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        homeLogoWithName = (ImageView) root.findViewById(R.id.home_logo);
        homeLogoWithName.setImageResource(R.drawable.app_logo_with_name);

        database = FirebaseDatabase.getInstance();

        System.out.println("DADOS DO FIREBASE");
//        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase = database.getReference().child("auwaca");

        mDatabase.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Sensor sensor = new Sensor(snapshot.child("status").getValue(String.class), snapshot.child("statusMotor").getValue(Integer.class), Integer.parseInt(snapshot.child("statusNivel").getValue(String.class)));


                int res =  (1024-sensor.getStatusNivel())*100/1024;
                String desc = res+"%";
                descCardHome.setText(desc);
                cardTitle.setText(sensor.getStatus());
                wcButton.setText(sensor.getStatusMotor() == 1 ? "DESATIVAR REGADOR" : "ATIVAR REGADOR");
                status = sensor.getStatusMotor();

                //DATA E HORA FORMATADA NO PADRAO BRASILEIRO
                Locale brasil = new Locale("pt","BR");
                DateTimeFormatter dateFormated = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm", brasil);
                String dateString = String.valueOf(dateFormated.format(LocalDateTime.now()));
                cardDate.setText(dateString.replaceAll(" "," às "));

                if(sensor.getStatusNivel() < resumo.getMenorPico()){
                    resumo.setMenorPico(sensor.getStatusNivel());
                    percMenorPico.setText(res+"%");
                }
                percUmidadeMedia.setText(resumo.getUmidadeMedia().toString()+"%");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return root;
    }

    public void handleClick(View view){
        mDatabase.child("statusMotor").setValue(status == 1 ? 0 : 1);
        status = status == 1 ? 0 : 1;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}