package com.nhom6.noteapp.view.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SharedViewModel extends ViewModel {

    private MutableLiveData<String> title;

    public void setNameData(String nameData) {
        title.setValue(nameData);
    }

    public MutableLiveData<String> getNameData() {
        if (title == null) {
            title = new MutableLiveData<>();
        }

        return title;
    }
}