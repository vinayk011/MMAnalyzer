package com.hug.mma.activity;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.hug.mma.M2AApplication;
import com.hug.mma.dialog.LoadingDialog;
import com.hug.mma.permission.Permission;
import com.hug.mma.permission.PermissionCallback;
import com.hug.mma.permission.PermissionUtils;
import com.hug.mma.util.Trace;

import java.util.Set;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.databinding.ViewDataBinding;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

public class BaseActivity<T extends ViewDataBinding> extends AppCompatActivity {
    public Context appContext;
    public Context context;
    public Activity activity;
    public M2AApplication application;
    public LoadingDialog loadingDialog;
    public T binding;

    private static final int PERMISSION_REQUEST_CODE = 27;
    private PermissionCallback permissionCallback;

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        application = M2AApplication.getInstance();
        appContext = getApplicationContext();
        context = this;
        activity = this;
        loadingDialog = new LoadingDialog(context, true);
        if (getIntent().getExtras() != null) {
            Bundle bundle = getIntent().getExtras();
            Set<String> keys = bundle.keySet();

            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("IntentDump \n");
            stringBuilder.append("-------------------------------------------------------------\n");

            for (String key : keys) {
                stringBuilder.append(key).append("=").append(bundle.get(key)).append("\n");
            }

            stringBuilder.append("-------------------------------------------------------------\n");
            Trace.i(stringBuilder.toString());
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onResume() {
        super.onResume();
        hideKeyboard(this);
        resume();
    }

    public void resume() {
    }

    @Override
    protected void onPause() {
        hideKeyboard(this);
        pause();
        super.onPause();
    }

    public void pause() {
    }

    @Override
    protected void onStart() {
        super.onStart();
        start();
    }

    public void bluetoothStatus(boolean on) {
    }

    public void start() {
    }

    @Override
    protected void onStop() {
        stop();
        super.onStop();
    }

    public void stop() {
    }

    @Override
    protected void onDestroy() {
        destroy();
        loadingDialog.dismiss();
        super.onDestroy();
    }

    public void destroy() {
    }

    public void afterDismiss() {
    }

    public static void hideKeyboard(Activity activity) {
        if (activity != null && activity.getWindow() != null && activity.getWindow().getDecorView() != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(activity.getWindow().getDecorView().getWindowToken(), 0);
            }
        }
    }

    public void showLoading(String... message) {
        loadingDialog.show(message);
    }

    public void dismissLoading() {
        loadingDialog.dismiss();
    }

    public void toastView(String msg) {
        hideKeyboard(this);
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

    public void requestPermission(Permission permission, PermissionCallback permissionCallback) {
        this.permissionCallback = permissionCallback;
        if (permissionCallback != null && !isFinishing()) {
            if (!PermissionUtils.isGranted(this, permission)) {
                ActivityCompat.requestPermissions(this, new String[]{permission.toString()}, PERMISSION_REQUEST_CODE);
            } else {
                permissionCallback.onPermissionResult(true, false);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        onPermissionsResult(requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void onPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE && grantResults != null && permissions != null) {
            for (int i = 0; i < grantResults.length; i++) {
                if (permissionCallback != null) {
                    permissionCallback.onPermissionResult(grantResults[i] == PackageManager.PERMISSION_GRANTED,
                            !PermissionUtils.shouldShowRequestPermissionRationale(this, permissions[i]));
                }
            }
        }
    }

}
