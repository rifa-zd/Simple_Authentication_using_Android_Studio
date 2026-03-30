package com.example.cse489_2022_1_60_265;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import java.util.ArrayList;

public class CustomListAdapter extends ArrayAdapter<StudentAttendance> {

    private final Context context;
    private final ArrayList<StudentAttendance> values;

    public CustomListAdapter(@NonNull Context context, @NonNull ArrayList<StudentAttendance> items) {
        super(context, -1, items);
        this.context = context;
        this.values = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.row_attendance, parent, false);

        TextView tvSN = rowView.findViewById(R.id.tvSN);
        TextView tvStudentName = rowView.findViewById(R.id.tvStudentName);
        TextView etRemarks = rowView.findViewById(R.id.etRemarks);
        RadioButton rbAbsent = rowView.findViewById(R.id.rbAbsent);
        RadioButton rbPresent = rowView.findViewById(R.id.rbPresent);



        StudentAttendance sa = values.get(position);
        tvSN.setText(""+(position+1));
        tvStudentName.setText(sa.name);
        etRemarks.setText(sa.remarks);
        rbPresent.setChecked(sa.status);
        rbAbsent.setChecked(!sa.status);


//        upon clicking present button this will be called
        rbPresent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               values.get(position).status = true;
            }
        });

        rbAbsent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                values.get(position).status = false;
            }
        });

        return rowView;
    }
}
