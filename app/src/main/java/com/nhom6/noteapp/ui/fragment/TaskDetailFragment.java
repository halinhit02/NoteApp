package com.nhom6.noteapp.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.nhom6.noteapp.Constance;
import com.nhom6.noteapp.R;
import com.nhom6.noteapp.databinding.FragmentTaskDetailDoingBinding;
import com.nhom6.noteapp.extension.Format;
import com.nhom6.noteapp.model.dao.TaskDAO;
import com.nhom6.noteapp.model.dto.Task;
import com.nhom6.noteapp.ui.dialog.DialogConfirm;
import com.nhom6.noteapp.ui.dialog.DialogUpdate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;

public class TaskDetailFragment extends Fragment implements DialogConfirm.OnClickConfirmTask, DialogUpdate.OnClickUpdateTask {
    private FragmentTaskDetailDoingBinding binding;
    private Task task;
    private TaskDAO taskDAO;
    DialogUpdate dialogUpdate;
    DialogConfirm dialogConfirm;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_task_detail_doing, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        taskDAO = new TaskDAO(requireContext());
        Bundle data = getArguments();
        if (data != null) {
            task = (Task) data.getSerializable(Constance.KEY_TASK);
        }
        setUpView();
        onClick();
        dialogUpdate = new DialogUpdate(requireContext(),this);
        dialogConfirm = new DialogConfirm(this);
        setUpSpinner();
    }

    private void setUpSpinner() {
        ArrayAdapter<Integer> score = new ArrayAdapter<>(requireContext(),
                com.google.android.material.R.layout.support_simple_spinner_dropdown_item,
                new ArrayList<>(Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10)));
        score.setDropDownViewResource(androidx.constraintlayout.widget.R.layout.support_simple_spinner_dropdown_item);
        binding.spinnerScore.setAdapter(score);
    }

    private void setUpView() {
        binding.titleNameTask.setText(task.getTitle());
        binding.desTask.setText(task.getDes());
        binding.nameTask.setText(task.getTitle());
        binding.timeTask.setText(Format.formatDateTimeToString(task.getTime(), task.getDate()));
        binding.noteTask.setText(task.getNote());

    }

    private void onClick() {
        binding.btnBack.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().popBackStack();
        });
        binding.btnSuccess.setOnClickListener(v -> {
            if (binding.noteTask.getText().toString().isEmpty()) {
                Toast.makeText(requireActivity(), "Phải phải chấm điểm và nhập ghi chú để hoàn thành", Toast.LENGTH_LONG).show();
            } else if (!dialogConfirm.isAdded()
                    && !dialogConfirm.isVisible()
                    && !dialogConfirm.isRemoving()) {
                dialogConfirm.show(getChildFragmentManager(), getClass().getName());
            }
        });
        binding.btnUpdate.setOnClickListener(v -> {
            if (!dialogUpdate.isAdded()
                    && !dialogUpdate.isVisible()
                    && !dialogUpdate.isRemoving()) {
                dialogUpdate.show(getChildFragmentManager(), getClass().getName(),task);
            }
        });
    }

    @Override
    public void onClickConfirmDoneTaskSuccess() {
        String a = binding.spinnerScore.getSelectedItem().toString();
        task.setScore(a);
        task.setDone(1);
        task.setNote(binding.noteTask.getText().toString());
        taskDAO.update(task);
        dialogConfirm.dismiss();
        Toast.makeText(requireContext(), "Đã cập nhật công việc", Toast.LENGTH_LONG).show();
        requireActivity().getSupportFragmentManager().popBackStack();
    }

    @Override
    public void onClickUpdateTaskSuccess(LocalDateTime localDateTime) {
        dialogUpdate.dismiss();
        Toast.makeText(requireContext(), "Đã cập nhật công việc", Toast.LENGTH_LONG).show();
        requireActivity().getSupportFragmentManager().popBackStack();
    }
    private void replaceFragment(Fragment fragment) {
        fragment.setArguments(null);
        requireActivity()
                .getSupportFragmentManager()
                .beginTransaction()
                .addToBackStack(null)
                .replace(R.id.fragment, fragment)
                .commit();
    }
}
