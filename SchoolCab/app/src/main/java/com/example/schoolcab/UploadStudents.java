package com.example.schoolcab;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

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
    private static final int PICK_EXCEL_REQUEST = 100;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_upload_students);
//
//        firestore = FirebaseFirestore.getInstance();
//
//        Button uploadButton = findViewById(R.id.uploadButton);
//        uploadButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Open the file picker to select an Excel file
//                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//                intent.setType("*/*");
//                startActivityForResult(intent, PICK_EXCEL_REQUEST);
//            }
//        });
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode == PICK_EXCEL_REQUEST && resultCode == RESULT_OK) {
//            if (data != null && data.getData() != null) {
//                // Handle the selected Excel file
//                handleExcelFile(data.getData());
//            }
//        }
//    }
//
//    private void handleExcelFile(android.net.Uri fileUri) {
//        try {
//            FileInputStream excelFile = new FileInputStream(new File(fileUri.getPath()));
//            Workbook workbook = new XSSFWorkbook(excelFile);
//            Sheet sheet = workbook.getSheetAt(0); // Assuming the data is in the first sheet
//
//            List<NewStudent> studentList = new ArrayList<>();
//
//            for (Row row : sheet) {
//                if (row.getRowNum() == 0) {
//                    // Skip the header row
//                    continue;
//                }
//
//                NewStudent student = new NewStudent();
//                student.setName(row.getCell(0).getStringCellValue());
//                student.setRollNo(row.getCell(1).getStringCellValue());
//                student.setGuardian(row.getCell(2).getStringCellValue());
//                student.setPhoneNo((int)row.getCell(3).getNumericCellValue());
//                student.setAddress(row.getCell(4).getStringCellValue());
//                student.setDefaultAddress(row.getCell(5).getStringCellValue());
//                student.setStandard((int) row.getCell(6).getNumericCellValue());
//                student.setEmail(row.getCell(7).getStringCellValue());
//                student.setSection(row.getCell(8).getStringCellValue());
//                student.setSex(row.getCell(9).getStringCellValue());
//                student.setAge((int) row.getCell(10).getNumericCellValue());
//                student.setWeight((int) row.getCell(11).getNumericCellValue());
//
//                // Add other fields as needed
//
//                studentList.add(student);
//            }
//
//            // Now, studentList contains your data from the Excel file
//
//            // Write student data to Firestore
//            CollectionReference studentsCollection = firestore.collection("students");
//
//            for (NewStudent student : studentList) {
//                studentsCollection.add(student)
//                        .addOnSuccessListener(documentReference -> {
////                            Toast.makeText(this, "Student registered successfully!", Toast.LENGTH_SHORT).show();
//                        })
//                        .addOnFailureListener(e -> {
//                            Log.e("StudentRegistration", "Error registering student", e);
////                            Toast.makeText(this, "Error registering student", Toast.LENGTH_SHORT).show();
//                        });
//            }
//
//            // Close the workbook
//            workbook.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }
}