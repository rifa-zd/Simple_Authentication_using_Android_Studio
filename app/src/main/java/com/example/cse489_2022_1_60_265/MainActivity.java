package com.example.cse489_2022_1_60_265;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.NameValuePair;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.message.BasicNameValuePair;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private EditText etDate;
    private Button btnExit, btnReport, btnSave;

    private ListView lvAttendance;
    private CustomListAdapter cla;

    private String[] students = {"Rifa", "Azad", "Yunsu", "Chea-e", "Wooseok"};

    private ArrayList<StudentAttendance> stdAttendence = new ArrayList<>();

    Handler handler = new Handler(Looper.getMainLooper());



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String userEmail = getIntent().getStringExtra("_USER_ID");
        TextView tvEmail = findViewById(R.id.tvEmail);
        tvEmail.setText("Welcome, " + userEmail);

        etDate = findViewById((R.id.etDate));
        btnExit = findViewById((R.id.btnExit));
        btnReport = findViewById((R.id.btnReport));
        btnSave = findViewById((R.id.btnSave));
        lvAttendance = findViewById((R.id.lvAttendance));

        for (String s : students){
            stdAttendence.add(new StudentAttendance(s, false, ""));
        }

        cla = new CustomListAdapter(this, stdAttendence);
        lvAttendance.setAdapter(cla); //populate with current sata

        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishAffinity();
            }
        });
        btnReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //write code to show report activity
                String keys[] = {"action", "sid", "semester"};
                String values[] = {"restore", "2022-1-60-265", "2025-2"};
                httpRequest(keys, values);
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveAttendance();
            }
        });
    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//    }

    private void saveAttendance(){
        //write code to get info of attendance & Save
        String _date = etDate.getText().toString().trim(); //"2025-12-25"
        String course = "CSE489-01";
        long dateTime = convertToDatetime(_date);
        System.out.println("DateTime" + dateTime);
        if(dateTime > 0) {
            JSONArray json = new JSONArray();

            AttendanceDB adb = new AttendanceDB(this);
//            insert info for all class
            for (int i = 0; i < stdAttendence.size(); i++) {
                StudentAttendance sa = cla.getItem(i);
//                for that position of that student will get here
                View row = lvAttendance.getChildAt(i);
                EditText etRemarks = row.findViewById(R.id.etRemarks);
                sa.remarks = etRemarks.getText().toString();

                System.out.println("Remakrs Check" + sa.remarks);

                try {
                    adb.insert(sa, dateTime, course);
                } catch (Exception e) {
//              if there is any other PK
//                    e.printStackTrace();
                    adb.update(sa, dateTime, course);
                }
//                this method will return this object reference , the one we overwrote
                System.out.println("Uploading Successfully");
                String s=course+";"+sa.toString();
                System.out.println(s);
                json.put(course + ";" + sa.toString());
            }
            adb.close();

            String uniqueKey = "2022-1-60-265" + ":" + dateTime;
            System.out.println("Before Row value" + uniqueKey);
            String rowValue = json.toString();

//            will convert the whole attendance as string, as a single value, name of the key = AttendenceInfo
            String keys[] = {"action", "sid", "semester", "key", "value"};
            String values[] = {"backup", "2022-1-60-265", "2025-2", uniqueKey, rowValue}; //rowValue should be a json array

            httpRequest(keys, values);

            Toast.makeText(this, "Attendance has been stored.", Toast.LENGTH_SHORT).show();
        } else if(_date == null || _date.isEmpty()) {
            Toast.makeText(this, "Please input a date", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(this, "Invalid Date Format", Toast.LENGTH_SHORT).show();
        }
    }


    private long convertToDatetime(String date)
    {
        // implement code here to check the date fotrmat ("YYYY-MM-DD")
        // if valid, then convert the date to time stamp and return it

//        Date d=new Date();
//        d.getTime();

        SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd");
        try{
            Date d= sdf.parse(date);
            return d.getTime();
        }catch ( Exception e){
        }
        return -1;
    }

//    private long convertToDatetime(String date) {
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
////        sdf.setLenient(false);  // to make sure invalid dates are rejected
//        try {
//            Date d = sdf.parse(date);
//            return d.getTime();
//        } catch (ParseException e) {
//            e.printStackTrace();
//            return -1;
//        }
//    }

    @SuppressLint("StaticFieldLeak")
    private void httpRequest(final String keys[], final String values[]){ //can be used as either load or store
        new AsyncTask<Void,Void,String>(){
            @Override
            protected String doInBackground(Void... voids) {
                List<NameValuePair> params=new ArrayList<NameValuePair>();
                for (int i=0; i<keys.length; i++){
                    params.add(new BasicNameValuePair(keys[i],values[i]));
                }
                String url= "https://www.muthosoft.com/univ/cse489/key_value.php";
                try {
                    String data= RemoteAccess.getInstance().makeHttpRequest(url,"POST",params);
                    System.out.println("Before returning Data: "+data);
                    return data;
                } catch (Exception e) {
                    System.out.println("Not returning data");
                    e.printStackTrace();
                }
                return null;
            }
            @Override
            protected void onPostExecute(String data){
                System.out.println("1st check" + data);//1st check
                if(data!=null){
                    try {
                        System.out.println("Data 1");
                        JSONObject jobj = new JSONObject(data);
                        if(jobj.has("msg")){
                            System.out.println("Ok 2");
                            String msg = jobj.getString("msg");
                            System.out.println(msg);
                            if (msg.equals("OK")) { //if data comes
                                System.out.println("chek 3");
                                 if(jobj.has("key-value")){ //previous data load
                                     System.out.println("Check 4");
                                     JSONArray keyValues = jobj.getJSONArray("key-value"); //this array has different date attndance
                                     //AS EACH ARE KEY-VALUE pair
                                     String _date = etDate.getText().toString().trim();
                                     long  dateTime = convertToDatetime(_date);
                                     String myStudentID = "2022-1-60-265";
                                     String uniqueKey = myStudentID+":"+dateTime;
                                     System.out.println("uniq key check: "+uniqueKey);//fixed here fix-1
                                     for(int i =0; i<keyValues.length();i++){
                                         JSONObject key_value = keyValues.getJSONObject(i);

                                         String key = key_value.getString("key");
                                         String value = key_value.getString("value");
                                         System.out.println("Key "+key);
                                         if (key.equals(uniqueKey)){
                                             JSONArray attendanceInfo = new JSONArray(value);
                                             System.out.println("Check after key");
                                             stdAttendence.clear();
                                             for(int j =0; j < attendanceInfo.length(); j++){
                                                 System.out.println("2nd check" + attendanceInfo.getString(j));//2nd check
                                                 String[] stdAttInfo = attendanceInfo.getString(j).split(";");
                                                 String course = stdAttInfo[0];
                                                 String name = stdAttInfo[1];
                                                 boolean status = Boolean.parseBoolean(stdAttInfo[2]);
                                                 String remarks = "";
                                                 if(stdAttInfo.length > 3){
                                                     remarks = stdAttInfo[3];
                                                 }
                                                 StudentAttendance sa = new StudentAttendance(name,status,remarks);
                                                 stdAttendence.add(sa);
                                                 System.out.println(course);
                                                 System.out.println(name);
                                                 System.out.println(status);
                                                 System.out.println(remarks);
                                             }
                                             Toast.makeText(MainActivity.this, _date + " Report is shown.", Toast.LENGTH_SHORT).show();

                                             System.out.println("3rd check" + stdAttendence.size());//3rd check
                                             cla.notifyDataSetChanged();
                                             break;
                                         }
                                     }

                                 }
                            } 
                        }else {
                            Toast.makeText(getApplicationContext(), "Something Toasted Problem", Toast.LENGTH_SHORT).show();
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "Error parsing data: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }
        }.execute(); //returns jspn string
    }
}