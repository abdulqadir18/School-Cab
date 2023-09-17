package com.example.schoolcab;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
//import org.apache.poi.ss.usermodel.*;
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UploadStudents extends AppCompatActivity {
    private ActivityResultLauncher<Intent> filePickerLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_students);

        Button chooseFileButton = findViewById(R.id.uploadButton);

        // Initialize the ActivityResultLauncher
        filePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            Uri fileUri = data.getData();
                            // Read the selected Excel file
                            List<NewStudent> student = readExcelFile(fileUri);
                            // Process and store the data as needed
                        }
                    }
                });

        chooseFileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open a file picker
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"); // for .xlsx files
                filePickerLauncher.launch(intent);
            }
        });
    }

    // Define the readExcelFile method here or in a separate class
    private List<NewStudent> readExcelFile(Uri fileUri) {
        List<NewStudent> students = new ArrayList<>();

        try {
            // Open the Excel file using JExcelApi
            InputStream inputStream = getContentResolver().openInputStream(fileUri);
            Workbook workbook = Workbook.getWorkbook(inputStream);

            // Assuming data is in the first sheet
            Sheet sheet = workbook.getSheet(0);

            for (int row = 1; row < sheet.getRows(); row++) {
                Cell[] cells = sheet.getRow(row);

//               parsing the rows one by one
                String name = cells[0].getContents();
                String rollNo = cells[1].getContents();
                String guardian = cells[2].getContents();
                String phoneNo = cells[3].getContents();
                String email = cells[4].getContents();
                String address = cells[5].getContents();
                int standard = Integer.parseInt(cells[6].getContents());
                String section = cells[7].getContents();
                int age = Integer.parseInt(cells[8].getContents());
                int weight = Integer.parseInt(cells[9].getContents());
                String defaultAddress = cells[10].getContents();
                String sex = cells[11].getContents();

                Log.d("StudentRegistration", row+"        Name: " + name + ", Password: " + rollNo);

                NewStudent student = new NewStudent(); // Create a Student object
                student.setName(name);
                student.setRollNo(rollNo);
                student.setGuardian(guardian);
                student.setPhoneNo(phoneNo);
                student.setAddress(address);
                student.setDefaultAddress(defaultAddress);
                student.setStandard(standard);
                student.setSection(section);
                student.setSex(sex);
                student.setAge(age);
                student.setWeight(weight);
                student.setEmail(email);

                students.add(student);
            }

            workbook.close();
        } catch (IOException | BiffException e) {
            e.printStackTrace();
        }

        return students;
    }
}