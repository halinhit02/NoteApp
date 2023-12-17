package com.nhom6.noteapp.ui.dialog;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.nhom6.noteapp.databinding.DialogUpdateBinding;
import com.nhom6.noteapp.extension.Format;
import com.nhom6.noteapp.model.dao.TaskDAO;
import com.nhom6.noteapp.model.dto.Task;

import java.time.LocalDateTime;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Objects;


public class DialogUpdate extends DialogFragment {
    DialogUpdateBinding viewBinding;
    Task task;
    TaskDAO taskDAO;
    OnClickUpdateTask onClickUpdateTask;
    LocalDateTime localDateTime;
    public DialogUpdate(Context context, OnClickUpdateTask onClickUpdateTask) {
        super();
        this.onClickUpdateTask = onClickUpdateTask;
        taskDAO = new TaskDAO(context);
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewBinding = DialogUpdateBinding.inflate(getLayoutInflater());
        setCancelable(false);
        return viewBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Objects.requireNonNull(Objects
                .requireNonNull(getDialog())
                .getWindow()).getDecorView()
                .setBackground(new ColorDrawable(Color.TRANSPARENT));


        viewBinding.btnBack.setOnClickListener( v-> {
            dismiss();
            onDestroy();
        });
        viewBinding.btnUpdate.setOnClickListener(v-> {
            task.setDes(viewBinding.edtDesTask.getText().toString());
            task.setTime(Format.formattedTime(localDateTime.getHour(), localDateTime.getMinute()));
            task.setDate(Format.formatDate(localDateTime));
            task.setTitle(viewBinding.edtTask.getText().toString());
            taskDAO.update(task);
            onClickUpdateTask.onClickUpdateTaskSuccess(localDateTime);
        });
        timeDatePick();
        setUpView();
    }

    private void setUpView() {
        viewBinding.setData(task);
    }
    // custom 1 hàm show mới để show dialog lên, đồng thời nhận data từ màn trước đó
    public void show(@NonNull FragmentManager manager, @Nullable String tag, Task task) {
        super.show(manager,tag);
        this.task = task;
        localDateTime = Format.formatDateTimeToDate(task.getTime(), task.getDate());
    }


    public void timeDatePick() {
        viewBinding.tvTime.setOnClickListener(v -> {
            new TimePickerDialog(
                    getContext(),
                    (TimePickerDialog.OnTimeSetListener) (view, hourOfDay, minute) -> {
                        // Xử lý giờ và phút ở đây
                        localDateTime = localDateTime.withHour(hourOfDay).withMinute(minute);
                        String formattedTime = String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute);
                        viewBinding.tvTime.setText(formattedTime);
                    },
                    localDateTime.getHour(),
                    localDateTime.getMinute(),
                    true
            ).show();
        });
        viewBinding.tvDate.setOnClickListener(v1 -> new DatePickerDialog(requireContext(),
                (DatePickerDialog.OnDateSetListener) (view, year, month, dayOfMonth) -> {
                    GregorianCalendar c = new GregorianCalendar(year, month, dayOfMonth);
                    viewBinding.tvDate.setText(Format.sdf(c.getTime()));

                    localDateTime = localDateTime.withYear(year).withMonth(month+1).withDayOfMonth(dayOfMonth);
                },
                localDateTime.getYear(),
                localDateTime.getMonthValue(),
                localDateTime.getDayOfMonth())
                .show()
        );
    }

    @Override
    public void dismiss() {
        super.dismiss();
        onDestroy();
    }
    public interface OnClickUpdateTask {
        void onClickUpdateTaskSuccess(LocalDateTime localDateTime);
    }
}
