package br.com.auwaca.ui.home;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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

import br.com.auwaca.MainActivity;
import br.com.auwaca.R;
import br.com.auwaca.databinding.FragmentHomeBinding;
import br.com.auwaca.models.Registro;
import br.com.auwaca.models.Sensor;

public class HomeFragment extends Fragment {
    ImageView homeLogoWithName;
    private FragmentHomeBinding binding;
    LinearLayout squad1;
    LinearLayout squad2;
    LinearLayout realTimeCard;
    private LayoutInflater inflater;
    private ViewGroup container;

    TextView descCardHome;
    TextView percMenorPico;
    TextView percUmidadeMedia;
    TextView cardDate;
    TextView cardTitle;
    TextView realTimeCardDesc;


    int uMediaSoma = 0;
    int uMediaDividendo = 1;

    Button wcButton;
    int status = 0;

    private DatabaseReference mDatabase;
    private FirebaseDatabase database;
    Sensor sensor;

    Registro resumo;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        this.inflater = inflater;
        this.container = container;

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        resumo = new Registro(100,0);
        descCardHome = (TextView) root.findViewById(R.id.descCardHomeId);
        percMenorPico = root.findViewById(R.id.percMenorPicoId);
        percUmidadeMedia = root.findViewById(R.id.percUmidadeMediaId);
        cardDate = root.findViewById(R.id.cardDateId);
        cardTitle = root.findViewById(R.id.cardTitleId);
        realTimeCard = root.findViewById(R.id.realTimeCard_id);
        realTimeCardDesc = root.findViewById(R.id.realTimeCardDesc_id);

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
            @SuppressLint("ResourceAsColor")
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                sensor = new Sensor(snapshot.child("status").getValue(String.class), snapshot.child("statusMotor").getValue(Integer.class), Integer.parseInt(snapshot.child("statusNivel").getValue(String.class)));

                switch (sensor.getStatus()){
                    case "Seco":
                        realTimeCard.setBackgroundResource(R.drawable.card3);
                        realTimeCardDesc.setTextColor(0xFFFFFFFF);
                        descCardHome.setTextColor(0xFFFFFFFF);
                        cardDate.setTextColor(0xFFFFFFFF);
//                        wcButton.setTextColor(getResources().getColor(R.color.brown));
                        break;
                    case "Moderada":
                        realTimeCard.setBackgroundResource(R.drawable.card);
                        realTimeCardDesc.setTextColor(0xFFFFFFFF);
                        descCardHome.setTextColor(0xFFFFFFFF);
                        cardDate.setTextColor(0xFFFFFFFF);
                        break;
                    case "Úmido":
                        realTimeCard.setBackgroundResource(R.drawable.card2);
                        realTimeCardDesc.setTextColor(0xFFFFFFFF);
                        descCardHome.setTextColor(0xFFFFFFFF);
                        cardDate.setTextColor(0xFFFFFFFF);
//                        wcButton.setTextColor(getResources().getColor(R.color.light_green));
                        break;
                }

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
                cardDate.setText(dateString.replaceAll(" ","\nàs "));

                if(res < resumo.getMenorPico()){
                    resumo.setMenorPico(res);
                    percMenorPico.setText(res+"%");
                }

                //MÉDIA SEMANAL
                uMediaSoma+=res;
                percUmidadeMedia.setText((uMediaSoma/uMediaDividendo)+"%");
                uMediaDividendo+=1;
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