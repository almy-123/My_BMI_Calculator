package sg.edu.rp.c346.id19037610.mybmicalculator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    EditText etWeight, etHeight;
    Button btnCalc, btnReset;
    TextView tvDate, tvBMI, tvDesc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etWeight = findViewById(R.id.etWeight);
        etHeight = findViewById(R.id.etHeight);
        btnCalc = findViewById(R.id.btnCalculate);
        btnReset = findViewById(R.id.btnReset);
        tvDate = findViewById(R.id.tvDate);
        tvBMI = findViewById(R.id.tvBMI);
        tvDesc = findViewById(R.id.tvRate);

        etWeight.requestFocus();


        btnCalc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculate();
                etWeight.setText("");
                etHeight.setText("");
                onResume();

            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etWeight.setText("");
                etHeight.setText("");
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                SharedPreferences.Editor editor = preferences.edit();
                editor.clear();
                editor.commit();
                onResume();
            }
        });
    }

    protected void calculate(){
        Float weight = Float.parseFloat(etWeight.getText().toString());
        Float height = Float.parseFloat(etHeight.getText().toString());
        Float BMI = weight/(height*height);
        String message = "";

        Calendar now = Calendar.getInstance();
        String datetime = now.get(Calendar.DAY_OF_MONTH)+"/"+
                (now.get(Calendar.MONTH)+1)+"/"+
                now.get(Calendar.YEAR)+" "+
                now.get(Calendar.HOUR_OF_DAY)+":"+
                now.get(Calendar.MINUTE);

        if(BMI<18.5){
            message = "You are underweight";
        }else if(BMI>=18.5 && BMI<=24.9){
            message = "Your BMI is normal";
        }else if(BMI>=25 && BMI<=29.9){
            message = "You are overweight";
        }else if(BMI>=30){
            message = "You are obese";
        }


        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        SharedPreferences.Editor editing = prefs.edit();

        editing.putFloat("BMI", BMI);
        editing.putString("date", datetime);
        editing.putString("desc", message);

        editing.commit();
    }

    @Override
    protected void onPause() {
        super.onPause();
        calculate();

    }


    @Override
    protected void onResume() {
        super.onResume();

        String message = "";

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        Float lastBMI = prefs.getFloat("BMI", 0);
        String lastDate = prefs.getString("date", "");
        String msg = prefs.getString("desc", "");

        tvDate.setText(getString(R.string.last_calculated_date)+" "+lastDate);
        tvBMI.setText(getString(R.string.last_calculated_bmi)+lastBMI);
        tvDesc.setText(msg);

    }


}
