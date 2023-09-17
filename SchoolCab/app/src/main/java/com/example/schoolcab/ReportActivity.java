package com.example.schoolcab;


import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ReportActivity extends AppCompatActivity {
    private FirebaseFirestore db;
    private String TAG = "Report Activity";
    int rowNum=1;

    File path = Environment.getExternalStoragePublicDirectory(
    Environment.DIRECTORY_DOWNLOADS);
    File file = new File(path, "AttendanceReport.xls");

    Workbook wb = new HSSFWorkbook();
    Sheet sheet = wb.createSheet("sheet1");

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
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("Report Activity", "Started");

        setContentView(R.layout.activity_attendance_report);

        Button btn = findViewById(R.id.generate);
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


    private void fetchStudentData(){
        Row headerRow = sheet.createRow(0);
        List<Date> dates = getDates("01-09-2023", "30-09-2023");
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
//        Cell tempcell = headerRow.createCell(0);
//        tempcell.setCellValue(11);
        for(int i=0; i<dates.size();i++)
        {
            Cell cell = headerRow.createCell(i+1);
            cell.setCellValue(df.format(dates.get(i)));
        }

        db = FirebaseFirestore.getInstance();

        db.collection("students")
                .whereEqualTo("email", "charvi@gmail.com")
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
                                    List<String> atten = (List<String>) document.getData().get("attendance");
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
    }
}


