package com.android.attendance.activity;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.attendance.bean.AttendanceBean;
import com.android.attendance.bean.StudentBean;
import com.android.attendance.context.ApplicationContext;
import com.android.attendance.db.DBAdapter;
import com.example.androidattendancesystem.R;

import java.util.ArrayList;

public class AddAttendanceActivity extends Activity {

	ArrayList<StudentBean> studentBeanList;
	private ListView listView;
	private AttendanceAdapter listAdapter;
	int sessionId = 0;
	String status = "P";
	Button attendanceSubmit;
	DBAdapter dbAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.__listview_main);

		sessionId = getIntent().getIntExtra("sessionId", 0);

		listView = findViewById(R.id.listview);



		studentBeanList = ((ApplicationContext) getApplicationContext()).getStudentBeanList();


		Log.d("listtt", String.valueOf(studentBeanList.get(0).getStudent_firstname()));

		listAdapter = new AttendanceAdapter(studentBeanList);
		listView.setAdapter(listAdapter);

		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				parent.getChildAt(position).setBackgroundColor(Color.TRANSPARENT);
				view.setBackgroundColor(Color.parseColor("#334455"));

				final StudentBean studentBean = studentBeanList.get(position);
				final Dialog dialog = new Dialog(AddAttendanceActivity.this);
				dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				dialog.setContentView(R.layout.test_layout);

				RadioGroup radioGroup = dialog.findViewById(R.id.radioGroup);
				final RadioButton present = dialog.findViewById(R.id.PresentradioButton);
				RadioButton absent = dialog.findViewById(R.id.AbsentradioButton);

				radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						if (checkedId == R.id.PresentradioButton) {
							status = "P";
						} else if (checkedId == R.id.AbsentradioButton) {
							status = "A";
						}
					}
				});

				attendanceSubmit = dialog.findViewById(R.id.attendanceSubmitButton);
				attendanceSubmit.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						AttendanceBean attendanceBean = new AttendanceBean();
						attendanceBean.setAttendance_session_id(sessionId);
						attendanceBean.setAttendance_student_id(studentBean.getStudent_id());
						attendanceBean.setAttendance_status(status);

						dbAdapter = new DBAdapter(AddAttendanceActivity.this);
						dbAdapter.addNewAttendance(attendanceBean);

						dialog.dismiss();
					}
				});

				dialog.setCancelable(true);
				dialog.show();
			}
		});
	}

	private class AttendanceAdapter extends ArrayAdapter<StudentBean> {
		private ArrayList<StudentBean> studentList;

		public AttendanceAdapter(ArrayList<StudentBean> studentList) {
			super(AddAttendanceActivity.this, R.layout.add_student_attendance, studentList);
			this.studentList = studentList;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = super.getView(position, convertView, parent);
			TextView textView = view.findViewById(R.id.labelA);

			Log.d("listview123", String.valueOf(studentList.get(0)));
			// Get the studentBean for the current position
			StudentBean studentBean = studentList.get(position);
			float attendancePercentage = calculateAttendancePercentage(studentBean);

			/*if (attendancePercentage < 75) {
				//
				// If attendance percentage is below 75%, change the text color to red
				textView.setTextColor(Color.RED);
			} else {
				// Reset the text color to the default
				textView.setTextColor(getResources().getColor(R.color.default_text_color));
			}*/

			String status = getStatusForStudent(studentBean); // Assuming you have a method to retrieve the status for a student
			if (status.equals("F")) {
				// If the status is "F," change both the background color and text color to red
				view.setBackgroundColor(Color.RED);
				textView.setText(studentBean.getStudent_firstname());

				textView.setBackgroundColor(Color.RED);
			} else {
				// Reset the background color to the default
				view.setBackgroundColor(Color.TRANSPARENT);
				textView.setText(studentBean.getStudent_firstname());
			}

			return view;
		}
	}

	private float calculateAttendancePercentage(StudentBean studentBean) {
		// Calculate and return the attendance percentage for the student
		// Implement your logic here
		return 0.0f;
	}

	private String getStatusForStudent(StudentBean studentBean) {
		// Retrieve and return the attendance status for the student
		// Implement your logic here
		return "";
	}
}
