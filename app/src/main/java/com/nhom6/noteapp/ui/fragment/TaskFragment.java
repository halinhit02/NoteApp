package com.nhom6.noteapp.ui.fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.nhom6.noteapp.Constance;
import com.nhom6.noteapp.R;
import com.nhom6.noteapp.adapter.TaskAdapter;
import com.nhom6.noteapp.databinding.DialogAddTaskBinding;
import com.nhom6.noteapp.databinding.FragmentTaskBinding;
import com.nhom6.noteapp.model.DAO.TaskDAO;
import com.nhom6.noteapp.model.DTO.Category;
import com.nhom6.noteapp.model.DTO.Task;
import com.nhom6.noteapp.ui.viewmodel.SharedViewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;


public class TaskFragment extends Fragment {

    private FragmentTaskBinding binding;
    private SharedViewModel sharedViewModel;

    Category category;

    public TaskFragment() {
        // Required empty public constructor
    }

    public static TaskFragment newInstance(String param1, String param2) {
        TaskFragment fragment = new TaskFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        sharedViewModel.getNameData().observe(this, nameObserver);

        Bundle data = getArguments();
        if(data!= null) {
            category = (Category) data.getSerializable(Constance.KEY_CATEGORY);
        }
    }

    Observer<String> nameObserver = new Observer<String>() {
        @Override
        public void onChanged(String name) {
            binding.tvTitle.setText(name);
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_task, container, false);
        return binding.getRoot();
    }

    private TaskDAO taskDAO;
    private ArrayList<Task> listTask;
    private TaskAdapter taskAdapter;
    private LinearLayoutManager linearLayoutManager;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        taskDAO = new TaskDAO(getContext());
        linearLayoutManager = new LinearLayoutManager(getContext());
        listTask = taskDAO.getAll(String.valueOf(category.getId()));
        taskAdapter = new TaskAdapter(listTask, getContext());

        binding.rcvTasks.setLayoutManager(linearLayoutManager);
        binding.rcvTasks.setAdapter(taskAdapter);

        binding.imgAddTask.setOnClickListener(v -> {

            Dialog dialog = new Dialog(getContext());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            DialogAddTaskBinding bindingDialog = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.dialog_add_task, null, false);
            dialog.setContentView(bindingDialog.getRoot());
            Window window = dialog.getWindow();
            if (window == null) {
                return;
            }
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            WindowManager.LayoutParams windowacc = window.getAttributes();
            windowacc.gravity = Gravity.NO_GRAVITY;
            window.setAttributes(windowacc);
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat sdf1 = new SimpleDateFormat("HH:mm:ss");

            bindingDialog.tvTime.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    int mHour = calendar.get(Calendar.HOUR_OF_DAY);
                    int mMinute = calendar.get(Calendar.MINUTE);
                    TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                            bindingDialog.tvTime.setText(hourOfDay + ":" + minute);
                        }
                    }, mHour, mMinute, false);
                    timePickerDialog.show();
                }
            });

            bindingDialog.tvDate.setOnClickListener(v1 -> {
                DatePickerDialog dialog1 = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        int myear = year;
                        int mmonth = month;
                        int mdayOfMonth = dayOfMonth;
                        GregorianCalendar c = new GregorianCalendar(myear, mmonth, mdayOfMonth);
                        bindingDialog.tvDate.setText(sdf.format(c.getTime()));
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE));
                dialog1.show();
            });

            bindingDialog.imgCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.cancel();
                }
            });

            bindingDialog.btnAddTask.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (bindingDialog.tvTime.getText().toString().trim().isEmpty()) {
                        bindingDialog.tvTime.setError("Select time to limit");
                    }
                    if (bindingDialog.tvDate.getText().toString().trim().isEmpty()) {
                        bindingDialog.tvDate.setError("select the date to limit");
                    }
                    if (bindingDialog.edtTask.getText().toString().trim().isEmpty()) {
                        bindingDialog.edtTask.setError("You have not entered a task yet");
                    }
                    if (bindingDialog.edtDesTask.getText().toString().trim().isEmpty()) {
                        bindingDialog.edtDesTask.setError("You have not entered a description yet");
                    } else {
                        Task task = new Task();
                        int id_category = category.getId();
                        task.setTitle(bindingDialog.edtTask.getText().toString().trim());
                        task.setDate(bindingDialog.tvDate.getText().toString().trim());
                        task.setTime(bindingDialog.tvTime.getText().toString().trim());
                        task.setDes(bindingDialog.edtDesTask.getText().toString().trim());
                        task.setScore("0/10");
                        task.setNote("");
                        task.setId_category(id_category);
                        task.setDone(0);
                        long res = taskDAO.insert(task);
                        if (res > 0) {
                            Toast.makeText(getContext(), "Added task successfully", Toast.LENGTH_SHORT).show();
                            listTask.clear();
                            listTask.addAll(taskDAO.getAll(String.valueOf(category.getId())));
                            taskAdapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(getContext(), "Add failure task", Toast.LENGTH_SHORT).show();
                        }
                        dialog.dismiss();
                    }
                }
            });


            dialog.show();
        });

        binding.imgBack.setOnClickListener(v -> {
            replaceFragment(new CategoryFragment());
        });

    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment, fragment);
        fragmentTransaction.commit();
    }

}