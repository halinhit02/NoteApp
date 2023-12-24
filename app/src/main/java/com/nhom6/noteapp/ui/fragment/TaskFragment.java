package com.nhom6.noteapp.ui.fragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.nhom6.noteapp.R;
import com.nhom6.noteapp.databinding.DialogAddTaskBinding;
import com.nhom6.noteapp.databinding.FragmentTaskBinding;
import com.nhom6.noteapp.extension.Constance;
import com.nhom6.noteapp.extension.Format;
import com.nhom6.noteapp.model.dao.TaskDAO;
import com.nhom6.noteapp.model.dto.Category;
import com.nhom6.noteapp.model.dto.Task;
import com.nhom6.noteapp.ui.activity.SendActivity;
import com.nhom6.noteapp.ui.adapter.TaskAdapter;
import com.nhom6.noteapp.ui.dialog.DialogTaskEnd;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;


public class TaskFragment extends Fragment implements TaskAdapter.TaskClick {

    private FragmentTaskBinding binding;

    Category category;
    DialogTaskEnd dialogTaskEnd;

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
        dialogTaskEnd = new DialogTaskEnd();
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
    private int id_category;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        taskDAO = new TaskDAO(getContext());
        linearLayoutManager = new LinearLayoutManager(getContext());
        listTask = taskDAO.getAllByCategory(String.valueOf(category.getId()));
        taskAdapter = new TaskAdapter(listTask, getContext(), this);
        id_category = category.getId();
        binding.rcvTasks.setLayoutManager(linearLayoutManager);
        binding.rcvTasks.setAdapter(taskAdapter);

        binding.searchTask.clearFocus();
        binding.searchTask.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                FinterList(newText);
                return true;
            }
        });

        binding.imgSend.setOnClickListener(v -> {
            Intent intent = new Intent(requireActivity(), SendActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("categoryName", category.getName());
            bundle.putInt("categoryId", category.getId());
            intent.putExtras(bundle);
            startActivity(intent);
        });

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
            SimpleDateFormat sdf1 = new SimpleDateFormat("HH:mm");

            bindingDialog.tvTime.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TimePickerDialog timePickerDialog = new TimePickerDialog(
                            getContext(),
                            new TimePickerDialog.OnTimeSetListener() {
                                @Override
                                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                    String formattedTime = String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute);
                                    bindingDialog.tvTime.setText(formattedTime);
                                }
                            },
                            calendar.get(Calendar.HOUR_OF_DAY),
                            calendar.get(Calendar.MINUTE),
                            true
                    );

                    timePickerDialog.show();
                }
            });

            bindingDialog.tvDate.setOnClickListener(v1 -> {
                DatePickerDialog dialog1 = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                        GregorianCalendar c = new GregorianCalendar(year, month, dayOfMonth);
                        bindingDialog.tvDate.setText(sdf.format(c.getTime()));
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE));
                dialog1.show();
            });

            bindingDialog.imgCancel.setOnClickListener(v12 -> dialog.cancel());

            bindingDialog.btnAddTask.setOnClickListener(v13 -> {
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
                    task.setTitle(bindingDialog.edtTask.getText().toString().trim());
                    task.setDate(bindingDialog.tvDate.getText().toString().trim());
                    task.setTime(bindingDialog.tvTime.getText().toString().trim());
                    task.setDes(bindingDialog.edtDesTask.getText().toString().trim());
                    task.setScore("0/10");
                    task.setNote("");
                    task.setId_category(id_category);
                    task.setDone(0);
                    task.setNotified(0);
                    Toast.makeText(getActivity(), task.getId_category() + "", Toast.LENGTH_SHORT).show();
                    long res = taskDAO.insert(task);
                    if (res > 0) {
                        Toast.makeText(getContext(), "Added task successfully", Toast.LENGTH_SHORT).show();
                        listTask.clear();
                        listTask.addAll(taskDAO.getAllByCategory(String.valueOf(category.getId())));
                        taskAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(getContext(), "Add failure task", Toast.LENGTH_SHORT).show();
                    }
                    dialog.dismiss();
                }
            });


            dialog.show();
        });

        binding.imgBack.setOnClickListener(v -> {
            requireActivity()
                    .getSupportFragmentManager()
                    .popBackStack();
        });

        registerForContextMenu(binding.imgFilter);

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.contentmenu, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_alll:
                taskAdapter.setFilteredList(listTask);
                return true;
            case R.id.menu_completed:
                ArrayList<Task> completedList=new ArrayList<>();

                for (Task task: listTask){
                    if (task.getDone() == 1){
                        completedList.add(task);
                    }

                }
                if (completedList.isEmpty()){
                    Toast.makeText(this.getContext(), "no data", Toast.LENGTH_SHORT).show();
                }else {
                    taskAdapter.setFilteredList(completedList);
                }
                return true;
            case R.id.menu_not_completed:
                ArrayList<Task> notcompletedList=new ArrayList<>();

                for (Task task: listTask){
                    if (task.getDone() == 0){
                        notcompletedList.add(task);
                    }

                }
                if (notcompletedList.isEmpty()){
                    Toast.makeText(this.getContext(), "no data", Toast.LENGTH_SHORT).show();
                }else {
                    taskAdapter.setFilteredList(notcompletedList);
                }
                return true;
            case R.id.menu_late:
                ArrayList<Task> lateList=new ArrayList<>();

                for (Task task: listTask){
                    Calendar localCalendar = Calendar.getInstance();
                    String givenTime = task.getDate() + " " + task.getTime();
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");

                    try {
                        Date givenDate = dateFormat.parse(givenTime);
                        Calendar givenCalendar = Calendar.getInstance();
                        givenCalendar.setTime(givenDate);
                        if (task.getDone() == 0 && localCalendar.after(givenCalendar) ){
                            lateList.add(task);
                        }
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                    if (lateList.isEmpty()){
                        Toast.makeText(this.getContext(), "no data", Toast.LENGTH_SHORT).show();
                    }else {
                        taskAdapter.setFilteredList(lateList);
                    }
                }
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    private void FinterList(String text) {
        ArrayList<Task> filteredList=new ArrayList<>();

        for (Task task: listTask){
            if (task.getTitle().toLowerCase().contains(text.toLowerCase())){
                filteredList.add(task);
            }

        }
        if (filteredList.isEmpty()){
            Toast.makeText(this.getContext(), "no data", Toast.LENGTH_SHORT).show();
        }else {
            taskAdapter.setFilteredList(filteredList);
        }
    }

    private void replaceFragment(Fragment fragment, Bundle data) {
        fragment.setArguments(data);
        requireActivity()
                .getSupportFragmentManager()
                .beginTransaction()
                .addToBackStack(null)
                .replace(R.id.fragment, fragment)
                .commit();
    }

    @Override
    public void onLongClick(Task item) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Are you sure you want to delete ?");
        builder.setPositiveButton("OK", (dialogInterface, i) -> {
            int check = taskDAO.delete(item.getId());
            switch (check) {
                case 1:
                    listTask.clear();
                    listTask.addAll(taskDAO.getAllByCategory(String.valueOf(id_category)));
                    taskAdapter.notifyDataSetChanged();
                    Toast.makeText(getActivity(), "Deleted successfully", Toast.LENGTH_SHORT).show();
                    break;
                case 0:
                    Toast.makeText(getActivity(), "Delete failed", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        });
        builder.setNegativeButton("Cancle", (dialogInterface, i) -> dialogInterface.cancel());
        builder.show();
    }

    @Override
    public void onClick(Task item) {
        Bundle data = new Bundle();
        data.putSerializable(Constance.KEY_TASK,item);
        if(Format.formatDateTimeToDate(item.getTime(),item.getDate()).isAfter(LocalDateTime.now()) && item.getDone() == 0) {
            replaceFragment(new TaskDetailFragment(),data);
        }
        else {
            dialogTaskEnd.show(requireActivity().getSupportFragmentManager(),getClass().getName(),item);
        }
    }
}