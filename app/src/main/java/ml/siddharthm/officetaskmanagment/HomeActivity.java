package ml.siddharthm.officetaskmanagment;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.util.Date;

import ml.siddharthm.officetaskmanagment.Model.Data;

public class HomeActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private FloatingActionButton fabBtn;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        toolbar=findViewById(R.id.toolbar_home);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Office Task Managment");

        mAuth = FirebaseAuth.getInstance();

        FirebaseUser mUser = mAuth.getCurrentUser();
        String Uid = mUser.getUid();

        mDatabase = FirebaseDatabase.getInstance().getReference().child("TaskNote").child(Uid);

        recyclerView = findViewById(R.id.recycler);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);


        fabBtn=findViewById(R.id.fab_btn);
        fabBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder myDialog = new AlertDialog.Builder(HomeActivity.this);
                LayoutInflater inflater = LayoutInflater.from(HomeActivity.this);
                View myView = inflater.inflate(R.layout.custominputfeild,null);
                myDialog.setView(myView);
                final AlertDialog dialog = myDialog.create();

                final EditText title = myView.findViewById(R.id.edt_title);
                final EditText note = myView.findViewById(R.id.edt_note);
                Button btnSave = myView.findViewById(R.id.btn_save);

                btnSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String mTitle = title.getText().toString().trim();
                        String mNote = note.getText().toString().trim();

                        if (TextUtils.isEmpty(mTitle)){
                            title.setError("Required Feild...");
                            return;
                        }
                        if (TextUtils.isEmpty(mNote)){
                            note.setError("Required Feild...");
                            return;
                        }

                        String id = mDatabase.push().getKey();
                        String datee = DateFormat.getDateInstance().format(new Date());
                        Data data = new Data(mTitle,mNote,datee,id);
                        mDatabase.child(id).setValue(data);
                        Toast.makeText(getApplicationContext(),"Data Insert",Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });


                dialog.show();


            }
        });
    }

}
