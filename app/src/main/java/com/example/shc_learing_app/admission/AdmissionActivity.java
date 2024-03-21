package com.example.shc_learing_app.admission;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.shc_learing_app.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import es.dmoral.toasty.Toasty;

public class AdmissionActivity extends AppCompatActivity {


    private EditText studentnameBangla, studentnameEnglish, studentbirhtNumber,
            studentdateofBirht, fatheNameBangla, fatheNameEnglish, fatheNID,
            fatheDateofBirth, fatherPhone, motherNameBangla, motherNameEnglish, motherNID, motherDateofBirth,
            District, Upazila, PostCode, AddressVillage, Oldschoolname,
            OldExamname, PassYear, BoardName, Roll, ResultGPA, Parstentes;


    private String bangla, english, numberdate, dateofbirth,
            fahtername, fatherenglish, fathernid, fatherdateofbirht, fahterphone,
            mothername, motherenglish, mothernid, motherdateofbirth,
            district, upzila, postcode, address,
            oldschool, oldexam, examyear, board, roll, result, parsents, downloadUrl = "";


    private ImageView addStudentImage;
    private Spinner addAdmissionCategory, addAllDivisionCategory, addAllGendercategory;
    private Button addAdmissionBtn;
    private final int REQ = 1;
    private Bitmap bitmap = null;
    private String category, divisionCategory, gendarCategory;

    private ProgressDialog pd;
    private StorageReference storageReference;
    private DatabaseReference reference, dbRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admission);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setTitle("ভর্তি ফরম");

        studentnameBangla = findViewById(R.id.studentnameBangla);
        studentnameEnglish = findViewById(R.id.studentnameEnglish);
        studentbirhtNumber = findViewById(R.id.studentbirhtNumber);
        studentdateofBirht = findViewById(R.id.studentdateofBirht);

        fatheNameBangla = findViewById(R.id.fatheNameBangla);
        fatheNameEnglish = findViewById(R.id.fatheNameEnglish);
        fatheNID = findViewById(R.id.fatheNID);
        fatheDateofBirth = findViewById(R.id.fatheDateofBirth);
        fatherPhone = findViewById(R.id.fatherPhone);

        motherNameBangla = findViewById(R.id.motherNameBangla);
        motherNameEnglish = findViewById(R.id.motherNameEnglish);
        motherNID = findViewById(R.id.motherNID);
        motherDateofBirth = findViewById(R.id.motherDateofBirth);

        District = findViewById(R.id.District);
        Upazila = findViewById(R.id.Upazila);

        PostCode = findViewById(R.id.PostCode);
        AddressVillage = findViewById(R.id.AddressVillage);

        Oldschoolname = findViewById(R.id.Oldschoolname);
        OldExamname = findViewById(R.id.OldExamname);
        PassYear = findViewById(R.id.PassYear);
        BoardName = findViewById(R.id.BoardName);
        Roll = findViewById(R.id.Roll);
        ResultGPA = findViewById(R.id.ResultGPA);
        Parstentes = findViewById(R.id.Parstentes);

        addAdmissionCategory = findViewById(R.id.addAdmissionCategory);
        addAllDivisionCategory = findViewById(R.id.addAllDivisionCategory);
        addAllGendercategory = findViewById(R.id.addAllGendercategory);

        addStudentImage = findViewById(R.id.addStudentImage);
        addAdmissionBtn = findViewById(R.id.addAdmissionBtn);
        pd = new ProgressDialog(this);

        reference = FirebaseDatabase.getInstance().getReference().child("Admission");
        storageReference = FirebaseStorage.getInstance().getReference();

        //Division category
        String[] ite = new String[]{"লিঙ্গ", "পুরুষ", "নারী", "অন্যান্য"};
        addAllGendercategory.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, ite));

        addAllGendercategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                gendarCategory = addAllGendercategory.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        //Division category
        String[] item = new String[]{"বিভাগ", "রাজশাহী বিভাগ", "রংপুর বিভাগ", "ঢাকা বিভাগ", "খুলনা বিভাগ", "বরিশাল বিভাগ", "সিলেট বিভাগ", "চট্টগ্রাম বিভাগ", "ময়মনসিংহ বিভাগ"};
        addAllDivisionCategory.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, item));

        addAllDivisionCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                divisionCategory = addAllDivisionCategory.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        //class spinner
        String[] items = new String[]{"ক্লাস নির্বাচন করুন", "ক্লাস ৬", "ক্লাস ৭", "ক্লাস ৮", "ক্লাস ৯"};
        addAdmissionCategory.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items));

        addAdmissionCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                category = addAdmissionCategory.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        addStudentImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        addAdmissionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkValidation();
            }
        });

    }

    private void checkValidation() {

        bangla = studentnameBangla.getText().toString();
        english = studentnameEnglish.getText().toString();
        numberdate = studentbirhtNumber.getText().toString();
        dateofbirth = studentdateofBirht.getText().toString();

        fahtername = fatheNameBangla.getText().toString();
        fatherenglish = fatheNameEnglish.getText().toString();
        fathernid = fatheNID.getText().toString();
        fatherdateofbirht = fatheDateofBirth.getText().toString();
        fahterphone = fatherPhone.getText().toString();

        mothername = motherNameBangla.getText().toString();
        motherenglish = motherNameEnglish.getText().toString();
        mothernid = motherNID.getText().toString();
        motherdateofbirth = motherDateofBirth.getText().toString();

        district = District.getText().toString();
        upzila = Upazila.getText().toString();

        postcode = PostCode.getText().toString();
        address = AddressVillage.getText().toString();

        oldschool = Oldschoolname.getText().toString();
        oldexam = OldExamname.getText().toString();
        examyear = PassYear.getText().toString();
        board = BoardName.getText().toString();
        roll = Roll.getText().toString();
        result = ResultGPA.getText().toString();
        parsents = Parstentes.getText().toString();

        if (bangla.isEmpty()) {
            studentnameBangla.setError("শিক্ষার্থীর নাম (বাংলা)");
            studentnameBangla.requestFocus();
        } else if (english.isEmpty()) {
            studentnameEnglish.setError("শিক্ষার্থীর নাম (ইংরেজি)");
            studentnameEnglish.requestFocus();
        } else if (numberdate.isEmpty()) {
            studentbirhtNumber.setError("জন্ম সনদের নাম্বার");
            Toasty.warning(this, "১৭ সংখ্যার জন্ম সনদ নাম্বার", Toasty.LENGTH_SHORT).show();
            studentbirhtNumber.requestFocus();
        } else if (dateofbirth.isEmpty()) {
            studentdateofBirht.setError("জন্ম তারিখ");
            studentdateofBirht.requestFocus();
        } else if (gendarCategory.equals("লিঙ্গ")) {
            Toasty.error(this, "দয়া করে আপনারা লিঙ্গ সিলেক্ট করুন", Toasty.LENGTH_SHORT).show();
        } else if (fahtername.isEmpty()) {
            fatheNameBangla.setError("পিতার নাম (বাংলা)");
            fatheNameBangla.requestFocus();
        } else if (fatherenglish.isEmpty()) {
            fatheNameEnglish.setError("পিতার নাম (ইংরেজি)");
            fatheNameEnglish.requestFocus();
        } else if (fathernid.isEmpty()) {
            fatheNID.setError("পিতার এন আই ডি");
            Toast.makeText(this, "পিতার এন আই ডি", Toast.LENGTH_SHORT).show();
            fatheNID.requestFocus();
        } else if (fatherdateofbirht.isEmpty()) {
            fatheDateofBirth.setError("পিতার জন্ম তারিখ");
            fatheDateofBirth.requestFocus();
        } else if (fahterphone.isEmpty()) {
            fatherPhone.setError("পিতার মোবাইল নম্বর");
            fatherPhone.requestFocus();
        } else if (mothername.isEmpty()) {
            motherNameBangla.setError("মাতার নাম (বাংলা)");
            motherNameBangla.requestFocus();
        } else if (motherenglish.isEmpty()) {
            motherNameEnglish.setError("মাতার নাম (ইংরেজি)");
            motherNameEnglish.requestFocus();
        } else if (mothernid.isEmpty()) {
            motherNID.setError("মাতার এন আই ডি");
            motherNID.requestFocus();
        } else if (motherdateofbirth.isEmpty()) {
            motherDateofBirth.setError("মাতার জন্ম তারিখ");
            motherDateofBirth.requestFocus();
        } else if (divisionCategory.equals("বিভাগ")) {
            Toasty.error(this, "দয়া করে আপনারা বিভাগ সিলেক্ট করুন", Toasty.LENGTH_SHORT).show();
        } else if (district.isEmpty()) {
            District.setError("জেলা");
            District.requestFocus();
        } else if (upzila.isEmpty()) {
            Upazila.setError("উপজেলা");
            Upazila.requestFocus();
        } else if (postcode.isEmpty()) {
            PostCode.setError("পোস্ট কোড");
            PostCode.requestFocus();
        } else if (address.isEmpty()) {
            AddressVillage.setError("ঠিকানা / গ্রাম");
            AddressVillage.requestFocus();
        } else if (oldschool.isEmpty()) {
            Oldschoolname.setError("পূর্ববর্তী প্রতিষ্ঠানের নাম");
            Oldschoolname.requestFocus();
        } else if (oldexam.isEmpty()) {
            OldExamname.setError("পূর্ববর্তী পরীক্ষার নাম");
            OldExamname.requestFocus();
        } else if (examyear.isEmpty()) {
            PassYear.setError("উর্ত্তীন হওয়ার বছর");
            PassYear.requestFocus();
        } else if (board.isEmpty()) {
            BoardName.setError("বোর্ড");
            BoardName.requestFocus();
        } else if (roll.isEmpty()) {
            Roll.setError("রোল");
            Roll.requestFocus();
        } else if (result.isEmpty()) {
            ResultGPA.setError("ফলাফল (জিপিএ)");
            ResultGPA.requestFocus();
        } else if (parsents.isEmpty()) {
            Parstentes.setError("উপস্থিতির হার");
            Parstentes.requestFocus();
        } else if (category.equals("ক্লাস নির্বাচন করুন")) {
            Toasty.warning(this, "দয়া করে আপনারা ক্লাস সিলেক্ট করুন", Toasty.LENGTH_SHORT).show();
        } else if (bitmap == null) {
            insertData();
        } else {
            uploadImage();
        }
    }

    private void uploadImage() {
        pd.setMessage("Uploading......");
        pd.show();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        byte[] finalimg = baos.toByteArray();
        final StorageReference filePath;
        filePath = storageReference.child("Admission").child(finalimg + "jpg");
        final UploadTask uploadTask = filePath.putBytes(finalimg);
        uploadTask.addOnCompleteListener(AdmissionActivity.this, new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()) {
                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    downloadUrl = String.valueOf(uri);
                                    insertData();

                                }
                            });
                        }
                    });
                } else {
                    pd.dismiss();
                    Toasty.error(AdmissionActivity.this, "Something went wrong", Toasty.LENGTH_LONG).show();
                }

            }
        });


    }


    private void insertData() {

        dbRef = reference.child(category);
        final String uniqueKey = dbRef.push().getKey();

        AdmissionData admissionData = new AdmissionData(bangla, english, numberdate, dateofbirth,
                gendarCategory, fahtername, fatherenglish, fathernid, fatherdateofbirht, fahterphone,
                mothername, motherenglish, mothernid, motherdateofbirth,
                divisionCategory, district, upzila, postcode, address,
                oldschool, oldexam, examyear, board, roll, result, parsents, downloadUrl, uniqueKey);

        dbRef.child(uniqueKey).setValue(admissionData).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                pd.dismiss();
                Toasty.success(AdmissionActivity.this, "ভর্তি ফরমটি জমা  হয়েছে", Toasty.LENGTH_SHORT).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Toasty.error(AdmissionActivity.this, "Something went wrong", Toasty.LENGTH_SHORT).show();
            }
        });
    }

    private void openGallery() {
        Intent pickImage = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickImage, REQ);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            addStudentImage.setImageBitmap(bitmap);
        }
    }
}