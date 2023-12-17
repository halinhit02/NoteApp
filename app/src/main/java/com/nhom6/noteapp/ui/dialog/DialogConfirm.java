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

import com.nhom6.noteapp.databinding.DialogConfirmBinding;

import java.util.Objects;

public class DialogConfirm extends DialogFragment {
    DialogConfirmBinding viewBinding;
    OnClickConfirmTask onClickConfirmTask;

    public DialogConfirm(OnClickConfirmTask onClickConfirmTask) {
        super();
        this.onClickConfirmTask = onClickConfirmTask;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewBinding = DialogConfirmBinding.inflate(getLayoutInflater());
        setCancelable(false);
        return viewBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Objects.requireNonNull(Objects.requireNonNull(
                getDialog())
                .getWindow())
                .getDecorView()
                .setBackground(new ColorDrawable(Color.TRANSPARENT));

        viewBinding.btnCancel.setOnClickListener(v-> {
            dismiss();
        });
        viewBinding.btnSuccess.setOnClickListener(v-> {
            onClickConfirmTask.onClickConfirmDoneTaskSuccess();
        });
        getDialog().getWindow().getDecorView().setBackground(new ColorDrawable(Color.TRANSPARENT));
    }

    public interface OnClickConfirmTask {
        void onClickConfirmDoneTaskSuccess();
    }
}
