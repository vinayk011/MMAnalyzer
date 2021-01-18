package com.hug.mma.fragments;

import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.hug.mma.R;
import com.hug.mma.activity.HomeActivity;
import com.hug.mma.adapters.LanguageListAdapter;
import com.hug.mma.adapters.SpinnerItemAdapter;
import com.hug.mma.databinding.FragmentSettingsBinding;
import com.hug.mma.databinding.ViewTextPickerBinding;
import com.hug.mma.listener.ValueSelectedListener;
import com.hug.mma.preference.AppPrefConstants;
import com.hug.mma.preference.AppPreference;
import com.hug.mma.util.CountryCodeUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;

public class SettingsFragment extends BaseFragment<FragmentSettingsBinding> {


    private AlertDialog languageDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = (FragmentSettingsBinding) DataBindingUtil.inflate(inflater, R.layout.fragment_settings, container, false);
        observeClick(binding.getRoot());
        init(container);
        return binding.getRoot();
    }

    private void init(ViewGroup container) {
        binding.setLanguage(CountryCodeUtil.getLanguageByCode(AppPreference.getInstance().getString(AppPrefConstants.PREF_LANGUAGE, "en")));
        binding.layoutLanguage.setOnClickListener(v -> {
            if (languageDialog == null || !languageDialog.isShowing()) {
                languageDialog = initDialog(container);
                languageDialog.show();
            }
        });
    }

    private AlertDialog initDialog(ViewGroup container) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
        final LanguageListAdapter languageListAdapter = new LanguageListAdapter(this.getContext(), CountryCodeUtil.getSupportedLanguages(), new ValueSelectedListener() {
            @Override
            public void onValueSelected(int index) {

            }

            @Override
            public void onValueSelected(String value) {
                setLocale(value);
                dismissDialog();
            }
        });
        ViewTextPickerBinding viewTextPickerBinding = DataBindingUtil.inflate(LayoutInflater.from(this.getContext()),
                R.layout.view_text_picker, container, false);
        viewTextPickerBinding.setTitle(getString(R.string.choose_language));
        viewTextPickerBinding.listView.setAdapter(languageListAdapter);
        viewTextPickerBinding.cancelButton.setOnClickListener(view -> {
            dismissDialog();
        });
        builder.setView(viewTextPickerBinding.getRoot());
        return builder.create();
    }

    private void dismissDialog() {
        if (languageDialog != null && languageDialog.isShowing()) {
            languageDialog.dismiss();
        }
    }

    private void setLocale(String locale) {
        ((HomeActivity) context).setLocale(locale);
        ((HomeActivity) context).recreate();
    }

}
