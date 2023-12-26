package com.nhom6.noteapp.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.nhom6.noteapp.R;
import com.nhom6.noteapp.databinding.ItemLanguageBinding;
import com.nhom6.noteapp.model.LanguageModel;

import java.util.List;

public class LanguageAdapter extends RecyclerView.Adapter<LanguageAdapter.LanguageViewHolder> {
    private List<LanguageModel> languageModelList;
    private ILanguageItem iLanguageItem;
    private Context context;

    public LanguageAdapter(List<LanguageModel> languageModelList, ILanguageItem listener, Context context) {
        this.languageModelList = languageModelList;
        this.iLanguageItem = listener;
        this.context = context;
    }

    @NonNull
    @Override
    public LanguageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_language, parent, false);
        return new LanguageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LanguageViewHolder holder, int position) {
        LanguageModel languageModel = languageModelList.get(position);
        if (languageModel == null) {
            return;
        }
        holder.binding.tvLang.setText(languageModel.getName());
        if (languageModel.getActive()) {
            holder.binding.itemLang.setBackground(context.getDrawable(R.drawable.lang_bg_item));
        } else {
            holder.binding.itemLang.setBackground(context.getDrawable(R.drawable.lang_bg_item_notselect));
        }

        switch (languageModel.getCode()) {
            case "en":
                Glide.with(context).asBitmap()
                        .load(R.drawable.ic_flag_english)
                        .into(holder.binding.icLang);
                break;
            case "hi":
                Glide.with(context).asBitmap()
                        .load(R.drawable.ic_flag_vn)
                        .into(holder.binding.icLang);
                break;

        }

        holder.binding.getRoot().setOnClickListener(v -> {
            setCheck(languageModel.getCode());
            iLanguageItem.onClickItemLanguage(languageModel.getCode());
            notifyDataSetChanged();
        });

    }

    @Override
    public int getItemCount() {
        if (languageModelList != null) {
            return languageModelList.size();
        } else {
            return 0;
        }
    }

    public void setCheck(String code) {
        for (LanguageModel item : languageModelList) {
            item.setActive(item.getCode().equals(code));
        }
        notifyDataSetChanged();
    }

    public class LanguageViewHolder extends RecyclerView.ViewHolder {

        ItemLanguageBinding binding;

        public LanguageViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemLanguageBinding.bind(itemView);
        }
    }
    public interface ILanguageItem {
        void onClickItemLanguage(String code);
    }
}

