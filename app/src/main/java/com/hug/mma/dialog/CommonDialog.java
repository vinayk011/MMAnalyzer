package com.hug.mma.dialog;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Window;

import com.hug.mma.R;
import com.hug.mma.databinding.DialogCommonBinding;
import com.hug.mma.listener.DialogListener;
import com.hug.mma.model.Dialog;

import androidx.databinding.DataBindingUtil;

public class CommonDialog extends BaseDialog<DialogCommonBinding> {

    private DialogListener dialogListener;
    private Dialog dialog;

    public CommonDialog(Context context, Dialog dialog, DialogListener dialogListener) {
        super(context);
        this.setCancelable(false);
        this.setCanceledOnTouchOutside(false);
        this.dialog = dialog;
        this.dialogListener = dialogListener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.dialog_common, null, false);
        setContentView(binding.getRoot());
        binding.setCallback(listener);
        binding.setDialog(dialog);
    }

    @Override
    public void dismiss() {
        super.dismiss();
        dialogListener = null;
    }

    private DialogListener listener = new DialogListener() {
        @Override
        public void ok() {
            dialogListener.ok();
            dismiss();
        }

        @Override
        public void cancel() {
            dialogListener.cancel();
            dismiss();
        }
    };

}
