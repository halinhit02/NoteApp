package com.nhom6.noteapp.ui.dialog;

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

import com.nhom6.noteapp.databinding.DialogTaskEndBinding;
import com.nhom6.noteapp.extension.Format;
import com.nhom6.noteapp.model.dto.Task;

import java.util.Objects;

public class DialogTaskEnd extends DialogFragment {
    DialogTaskEndBinding viewBinding;
    Task task;
    public DialogTaskEnd() {
        super();
    }

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewBinding = DialogTaskEndBinding.inflate(getLayoutInflater());
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

        viewBinding.btnSuccess.setOnClickListener( v-> {
            dismiss();
        });
        setUpView();
    }

    private void setUpView() {
        viewBinding.setTask(this.task);
        viewBinding.timeTask.setText(Format.formatDateTimeToString(task.getTime(),task.getDate()));
    }

    public void show(@NonNull FragmentManager manager, @Nullable String tag, Task task) {
        super.show(manager,tag);
        this.task = task;
    }

}

