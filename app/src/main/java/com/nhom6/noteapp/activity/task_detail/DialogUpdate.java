package com.nhom6.noteapp.activity.task_detail;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.nhom6.noteapp.databinding.DialogUpdateBinding;

public class DialogUpdate extends Dialog {
    DialogUpdateBinding viewBinding;
    public DialogUpdate(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewBinding = DialogUpdateBinding.inflate(getLayoutInflater());
        setContentView(viewBinding.getRoot());
    }

}
