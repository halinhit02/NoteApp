package com.nhom6.noteapp.ui.activity;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.nhom6.noteapp.databinding.ActivityLanguageBinding;
import com.nhom6.noteapp.model.LanguageModel;
import com.nhom6.noteapp.ui.activity.base.BaseActivity;
import com.nhom6.noteapp.ui.adapter.LanguageAdapter;
import com.nhom6.noteapp.utils.SystemUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class LanguageActivity extends BaseActivity<ActivityLanguageBinding> {
    Boolean startFist = false;
    List<LanguageModel> listLanguage;
    String codeLang;
    @Override
    protected ActivityLanguageBinding setViewBinding() {
        return ActivityLanguageBinding.inflate(LayoutInflater.from(this));
    }

    @Override
    protected void initView() {
        startFist = getIntent().getExtras()==null?false:true;
        if(startFist){
            binding.btnBack.setVisibility(View.INVISIBLE);
        }else{
            binding.btnBack.setVisibility(View.VISIBLE);
        }

        initListLanguage();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        LanguageAdapter languageAdapter = new LanguageAdapter(listLanguage, new LanguageAdapter.ILanguageItem() {
            @Override
            public void onClickItemLanguage(String code) {
                codeLang = code;
            }
        }, this);
        String codeLangDefault = SystemUtils.getPreLanguage(getBaseContext());
        String[] langDefault = {"en", "vi"};
        if (!Arrays.asList(langDefault).contains(codeLangDefault)) codeLang = "en";

        languageAdapter.setCheck(codeLangDefault);
        binding.rvLanguageStart.setLayoutManager(linearLayoutManager);
        binding.rvLanguageStart.setAdapter(languageAdapter);

    }

    @Override
    protected void viewListener() {
        binding.btnDone.setOnClickListener(view -> {
            SystemUtils.setPreLanguage(LanguageActivity.this,codeLang);
            startActivity(new Intent(LanguageActivity.this, MainActivity.class));
            finishAffinity();
        });
    }

    @Override
    protected void dataObservable() {
    }

    private void initListLanguage(){
        codeLang = Locale.getDefault().getLanguage();
        listLanguage = new ArrayList<>();
        String lang = Locale.getDefault().getLanguage();
        listLanguage.add(new LanguageModel("English", "en"));
        listLanguage.add(new LanguageModel("Vietnamese", "vi"));


        for (int i = 0; i < listLanguage.size(); i++) {
            if (listLanguage.get(i).getCode().equals(lang)) {
                listLanguage.add(0, listLanguage.get(i));
                listLanguage.remove(i + 1);
            }
        }
    }

}