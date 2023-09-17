package com.example.schoolcab;


import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

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
import java.nio.file.Path;

public class ReportActivity extends AppCompatActivity {

    File path = Environment.getExternalStoragePublicDirectory(
    Environment.DIRECTORY_DOWNLOADS);
    File file = new File(path, "workbook.xls");
    Workbook wb = new HSSFWorkbook();
    Sheet sheet = wb.createSheet("sheet1");
    private ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    Log.d("Report Activity", "Permission Granted");
                    Row row = sheet.createRow(0);
                    // Create a cell and put a value in it.
                    Cell cell = row.createCell(0);
                    cell.setCellValue(1);

                    try (OutputStream fileOut = new FileOutputStream(file)) {
                        wb.write(fileOut);
                    } catch (FileNotFoundException e) {
                        throw new RuntimeException(e);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
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

        if(ContextCompat.checkSelfPermission(getApplicationContext(),"android.permission.WRITE_EXTERNAL_STORAGE")== PackageManager.PERMISSION_DENIED){
            Log.d("Report Activity", "Permission Denied");
            requestPermissionLauncher.launch("android.permission.WRITE_EXTERNAL_STORAGE");
        }
        else {
            Log.d("Report Activity", "Permission Already Granted");

            Row row = sheet.createRow(0);
            // Create a cell and put a value in it.
            Cell cell = row.createCell(0);
            cell.setCellValue(1);

            try (OutputStream fileOut = new FileOutputStream(file)) {
                wb.write(fileOut);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }
}


