package com.anga.alcoholcompany;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private String currentDate,currentTime;
    private EditText tankid,type,wine,ballying,alcohol,temperature,sulphur,sugar;
    private Button submit;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        setIDs();
        listeners();
    }
    private void setIDs() {
        tankid = findViewById(R.id.tankid);
        type = findViewById(R.id.type);
        wine = findViewById(R.id.wine);
        ballying = findViewById(R.id.ballying);
        alcohol = findViewById(R.id.alcohol);
        temperature = findViewById(R.id.temperature);
        sulphur = findViewById(R.id.sulphur);
        sugar = findViewById(R.id.sugar);
        submit = findViewById(R.id.submit);
    }
    private void listeners() {
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validation();
            }
        });
    }
    private void validation() {

        String TankId = tankid.getText().toString();
        String Type = type.getText().toString();
        String Wine = wine.getText().toString();
        String Ballying = ballying.getText().toString();
        String Alcohol = alcohol.getText().toString();
        String Temperature = temperature.getText().toString();
        String Sulphur = sulphur.getText().toString();
        String Sugar = sugar.getText().toString();

        if(!validateTankID(TankId)){
            return;
        }else if(!validateType(Type)){
            return;
        }else if(!validateWine(Wine)){
            return;
        }else if(!validateBallying(Ballying)){
            return;
        }else if(!validateAlcohol(Alcohol)){
            return;
        }else if(!validateTemperature(Temperature)){
            return;
        }else if(!validateSurphur(Sulphur)){
            return;
        }else if(!validateSugar(Sugar)){
            return;
        }else {
            toFirebase(TankId,Type,Wine,Ballying,Alcohol,Temperature,Sulphur,Sugar);
        }
    }

    private void toFirebase(final String tankId, String type, String wine, String ballying, String alcohol, String temperature, String sulphur, String sugar) {

        //humu itume mpaka firebase

        //initialize date and time
        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDateFormat = new SimpleDateFormat("MMM dd, yyyy");
        currentDate = currentDateFormat.format(calForDate.getTime());

        Calendar calForTime = Calendar.getInstance();
        SimpleDateFormat currentTimeFormat = new SimpleDateFormat("hh:mm a");
        currentTime = currentTimeFormat.format(calForTime.getTime());

        final HashMap<String,Object> data = new HashMap();
        data.put("tankID",tankId);
        data.put("type",type);
        data.put("wine",wine);
        data.put("ballying",ballying);
        data.put("alcohol",alcohol);
        data.put("temperature",temperature);
        data.put("sulphur",sulphur);
        data.put("sugar",sugar);
        data.put("datesubmitted",currentDate);
        data.put("timesubmitted",currentTime);


        final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMessage("uploading data to server..");
        progressDialog.show();

        String uniqueKey =  FirebaseDatabase.getInstance().getReference().child("alcohol").child(tankId+" "+currentDate).push().getKey();
        FirebaseDatabase.getInstance().getReference().child("alcohol").child(tankId+" "+currentDate+" "+currentTime).child(uniqueKey).setValue(data)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        Snackbar.make(getWindow().getDecorView(),"Data is Successfully uploaded to server", BaseTransientBottomBar.LENGTH_LONG);
                        Toast.makeText(MainActivity.this, "Successfuly uploaded to Server..", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this, "Error : "+e.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });

//        FirebaseDatabase.getInstance().getReference().child("alcohol").addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                if(dataSnapshot.exists()){
//
//                    String key = "";
//                    for (DataSnapshot ds : dataSnapshot.getChildren()){
//                       key = ds.getKey();
//                        final String finalKey = key;
//                        FirebaseDatabase.getInstance().getReference().child("alcohol").child(key).addListenerForSingleValueEvent(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(DataSnapshot dataSnapshot) {
//                                String key2 = "";
//                                for (DataSnapshot datasnap : dataSnapshot.getChildren()){
//                                    key2 = datasnap.getKey();
//                                    FirebaseDatabase.getInstance().getReference().child("alcohol").child(finalKey).child(key2).addListenerForSingleValueEvent(new ValueEventListener() {
//                                        @Override
//                                        public void onDataChange(DataSnapshot dataSnapshot) {
//                                            //kama data zipo basi ikaedit
//                                            //retrieve
//                                            String alcohol = dataSnapshot.child("alcohol").getValue().toString();
//                                            Log.d("data zotee", "za : "+alcohol);
//                                        }
//
//                                        @Override
//                                        public void onCancelled(DatabaseError databaseError) {
//
//                                        }
//                                    });
//                                }
//                            }
//
//                            @Override
//                            public void onCancelled(DatabaseError databaseError) {
//                                Toast.makeText(MainActivity.this, "Error : "+databaseError.getMessage(), Toast.LENGTH_SHORT).show();
//                            }
//                        });
//                    }
//
//                }else {
//                    String uniqueKey =  FirebaseDatabase.getInstance().getReference().child("alcohol").child(tankId+" "+currentDate).push().getKey();
//                    FirebaseDatabase.getInstance().getReference().child("alcohol").child(tankId+" "+currentDate).child(uniqueKey).setValue(data)
//                            .addOnCompleteListener(new OnCompleteListener<Void>() {
//                                @Override
//                                public void onComplete(@NonNull Task<Void> task)
//                                {
//                                    // intent to recyclerviewer
//                                }
//                            }).addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception e) {
//                            Toast.makeText(MainActivity.this, "Error : "+e.getMessage(), Toast.LENGTH_SHORT).show();
//                        }
//                    });
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                Toast.makeText(MainActivity.this, "Error : "+databaseError.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });

    }

    private boolean validateSugar(String sugarr) {
        if (sugarr.isEmpty()) {
            sugar.setError("this Field can not be empty..");
            return false;
        } else {
            sugar.setError(null);
            return true;
        }
    }

    private boolean validateSurphur(String sulphurr) {
        if (sulphurr.isEmpty()) {
            sulphur.setError("this Field can not be empty..");
            return false;
        } else {
            sulphur.setError(null);
            return true;
        }
    }

    private boolean validateTemperature(String temperaturee) {
        if (temperaturee.isEmpty()) {
            temperature.setError("this Field can not be empty..");
            return false;
        } else {
            temperature.setError(null);
            return true;
        }
    }

    private boolean validateAlcohol(String alcoholl) {
        if (alcoholl.isEmpty()) {
            alcohol.setError("this Field can not be empty..");
            return false;
        } else {
            alcohol.setError(null);
            return true;
        }
    }

    private boolean validateBallying(String ballyingg) {
        if (ballyingg.isEmpty()) {
            ballying.setError("this Field can not be empty..");
            return false;
        } else {
            ballying.setError(null);
            return true;
        }
    }

    private boolean validateWine(String winee) {
        if (winee.isEmpty()) {
            wine.setError("this Field can not be empty..");
            return false;
        } else {
            wine.setError(null);
            return true;
        }
    }

    private boolean validateType(String typee) {
        if (typee.isEmpty()) {
            type.setError("this Field can not be empty..");
            return false;
        } else {
            type.setError(null);
            return true;
        }
    }

    private boolean validateTankID(String tankIdd) {
        if (tankIdd.isEmpty()) {
            tankid.setError("this Field can not be empty..");
            return false;
        } else {
            tankid.setError(null);
            return true;
        }
    }


}