package com.example.schoolcab;


import static com.google.android.gms.common.internal.safeparcel.SafeParcelable.NULL;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ReportActivity extends AppCompatActivity {
    private FirebaseFirestore db;
    SharedPreferences sharedpreferences;
    private String TAG = "Report Activity";
    int rowNum=1;
    String busId = "";

    Workbook wb = new HSSFWorkbook();
    Sheet sheet = wb.createSheet("sheet1");

    String startDat="",endDat="",attendanceType;

    RadioGroup radioGroup;


    private ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    Log.d("Report Activity", "Permission Granted");
                    fetchStudentData();
                } else {
                    // Explain to the user that the feature is unavailable because the
                    // feature requires a permission that the user has denied. At the
                    // same time, respect the user's decision. Don't link to system
                    // settings in an effort to convince the user to change their
                    // decision.
                    Log.d(TAG, "Permission Not Granted: ");
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("Report Activity", "Started");

        setContentView(R.layout.activity_attendance_report);

        sharedpreferences = getSharedPreferences("shared_prefs", Context.MODE_PRIVATE);

        Button startDate = findViewById(R.id.idBtnPickStartDate);
        Button endDate = findViewById(R.id.idBtnPickEndDate);


        startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // on below line we are getting
                // the instance of our calendar.
                final Calendar c = Calendar.getInstance();

                // on below line we are getting
                // our day, month and year.
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                // on below line we are creating a variable for date picker dialog.
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        // on below line we are passing context.
                        ReportActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // on below line we are setting date to our text view.
                                startDat = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                                DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
                                try {
                                    Date parsedDate = df.parse(startDat);
                                    startDat = df.format(parsedDate);
                                } catch (ParseException e) {
                                    throw new RuntimeException(e);
                                }

                                startDate.setText(startDat);
                            }
                        },
                        // on below line we are passing year,
                        // month and day for selected date in our date picker.
                        year, month, day);
                // at last we are calling show to
                // display our date picker dialog.
                datePickerDialog.show();
            }
        });

        endDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // on below line we are getting
                // the instance of our calendar.
                final Calendar c = Calendar.getInstance();

                // on below line we are getting
                // our day, month and year.
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                // on below line we are creating a variable for date picker dialog.
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        // on below line we are passing context.
                        ReportActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // on below line we are setting date to our text view.
                                endDat = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                                DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
                                try {
                                    Date parsedDate = df.parse(endDat);
                                    endDat = df.format(parsedDate);
                                } catch (ParseException e) {
                                    throw new RuntimeException(e);
                                }

                                endDate.setText(endDat);
                            }
                        },
                        // on below line we are passing year,
                        // month and day for selected date in our date picker.
                        year, month, day);
                // at last we are calling show to
                // display our date picker dialog.
                datePickerDialog.show();
            }
        });


        Button btn = findViewById(R.id.edtGenerate);
        btn.setOnClickListener(v ->{
            if(ContextCompat.checkSelfPermission(getApplicationContext(),"android.permission.WRITE_EXTERNAL_STORAGE")== PackageManager.PERMISSION_DENIED){
                Log.d("Report Activity", "Permission Denied");
                requestPermissionLauncher.launch("android.permission.WRITE_EXTERNAL_STORAGE");
            }
            else {
                Log.d("Report Activity", "Permission Already Granted");
                fetchStudentData();


            }
        });
    }

    private static List<Date> getDates(String dateString1, String dateString2)
    {
        ArrayList<Date> dates = new ArrayList<Date>();
        DateFormat df1 = new SimpleDateFormat("dd-MM-yyyy");

        Date date1 = null;
        Date date2 = null;

        try {
            date1 = df1 .parse(dateString1);
            date2 = df1 .parse(dateString2);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);


        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);

        while(!cal1.after(cal2))
        {
            dates.add(cal1.getTime());
            cal1.add(Calendar.DATE, 1);
        }
        return dates;
    }


    @SuppressLint("ClickableViewAccessibility")
    private void fetchStudentData(){
        Row headerRow = sheet.createRow(0);

        List<Date> dates = getDates(startDat, endDat);
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
//        Cell tempcell = headerRow.createCell(0);
//        tempcell.setCellValue(11);
        for(int i=0; i<dates.size();i++)
        {
            Cell cell = headerRow.createCell(i+1);
            cell.setCellValue(df.format(dates.get(i)));
        }

        EditText bus_no = findViewById(R.id.edtBusNo);
        Integer busNo = Integer.parseInt(bus_no.getText().toString());


        String school = sharedpreferences.getString("sId",NULL);

        db = FirebaseFirestore.getInstance();
        db.collection("bus")
                .whereEqualTo("busNo",busNo)
                .whereEqualTo("schoolId",school)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "Task Successful");
                            Log.d(TAG, String.valueOf(task.getResult().size()));
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                //Log.d("Doc", String.valueOf(document));
                                busId=document.getId();
                            }

                            RadioGroup radioGroup = (RadioGroup)findViewById(R.id.groupradio);
                            radioGroup.setOnCheckedChangeListener(
                                    new RadioGroup
                                            .OnCheckedChangeListener() {
                                        @Override
                                        public void onCheckedChanged(RadioGroup group, int checkedId) {
                                            RadioButton
                                                    radioButton
                                                    = (RadioButton)group
                                                    .findViewById(checkedId);
                                        }
                                    });

                            int selectedId = radioGroup.getCheckedRadioButtonId();
                            if (selectedId == -1) {
                                Toast.makeText(ReportActivity.this, "Please select attendance type", Toast.LENGTH_SHORT).show();
                            }
                            else {
                               if(selectedId==R.id.arrival){
                                   attendanceType="arrival";
                               }
                               else {
                                   attendanceType="departure";
                               }
                            }

                            Log.d("ReportActivity", attendanceType);

                            db.collection("students")
                                    .whereEqualTo("busId", busId)
                                    .whereEqualTo("schoolId",school)
                                    .get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if (task.isSuccessful()) {
                                                Log.d(TAG, "Task Successful");
                                                Log.d(TAG, String.valueOf(task.getResult().size()));
                                                for (QueryDocumentSnapshot document : task.getResult()) {
                                                    //Log.d("Doc", String.valueOf(document));
                                                    Row row = sheet.createRow(rowNum);
                                                    Cell namecell = row.createCell(0);
                                                    namecell.setCellValue((String) document.getData().get("name"));
                                                    for(int i=0; i<dates.size();i++)
                                                    {
                                                        Cell cell = row.createCell(i+1);
                                                        List<String> atten = (List<String>) document.getData().get(attendanceType+"Attendance");
                                                        if(atten.contains(df.format(dates.get(i)))){
                                                            cell.setCellValue(1);
                                                        }
                                                        else{
                                                            cell.setCellValue(0);
                                                        }

                                                    }
                                                    //Log.d(TAG, document.getId() + " => " + document.getData());
                                                    rowNum++;
                                                }

                                                File path = Environment.getExternalStoragePublicDirectory(
                                                        Environment.DIRECTORY_DOWNLOADS);
                                                File file = new File(path, attendanceType+"AttendanceReport"+busNo.toString()+".xls");

                                                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                                                    try {
                                                        boolean result = Files.deleteIfExists(file.toPath());
                                                        Log.d(TAG, String.valueOf(result));
                                                    } catch (IOException e) {
                                                        throw new RuntimeException(e);
                                                    }
                                                }
                                                try (OutputStream fileOut = new FileOutputStream(file)) {
                                                    wb.write(fileOut);
                                                    Toast.makeText(ReportActivity.this, "Attendance Report Generated Succesfully", Toast.LENGTH_LONG).show();
                                                } catch (FileNotFoundException e) {
                                                    throw new RuntimeException(e);
                                                } catch (IOException e) {
                                                    throw new RuntimeException(e);
                                                }
                                            } else {
                                                Log.d(TAG, "Error while fetching student data: ", task.getException());
                                            }
                                        }
                                    });



                        } else {
                            Log.d(TAG, "Error while fetching bus data: ", task.getException());
                        }
                    }
                });
    }

}


