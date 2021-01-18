package com.hug.mma.fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.snackbar.Snackbar;
import com.hug.mma.M2AApplication;
import com.hug.mma.R;
import com.hug.mma.activity.HomeActivity;
import com.hug.mma.adapters.DevicesAdapter;
import com.hug.mma.databinding.FragmentDeviceManagementBinding;
import com.hug.mma.db.room.entity.Device;
import com.hug.mma.db.room.viewmodels.DeviceListViewModel;
import com.hug.mma.dialog.AddDeviceDialog;
import com.hug.mma.dialog.CommonDialog;
import com.hug.mma.listener.AddDeviceListener;
import com.hug.mma.listener.DialogListener;
import com.hug.mma.listener.ListItemListener;
import com.hug.mma.model.Dialog;
import com.hug.mma.util.Trace;
import com.hug.mma.viewmodel.AddDeviceModel;

import java.util.ArrayList;
import java.util.Collections;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

public class DeviceManagementFragment extends BaseFragment<FragmentDeviceManagementBinding> {

    private DeviceListViewModel devicesViewModel;
    private DevicesAdapter devicesAdapter;
    private ArrayList<Device> devicesList = new ArrayList<>();
    private boolean hasChangesToSave = false;
    private Paint p = new Paint();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_device_management, container, false);
        binding.setListChanged(hasChangesToSave);
        observeClick(binding.getRoot());
        setHasOptionsMenu(true);
        listenData();
        init();
        return binding.getRoot();
    }


    public void resume() {
        if (!hasChangesToSave)
            attachObservers();
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_device_mngmt, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (R.id.add_device == item.getItemId()) {
            Trace.i("Add device dialog");
            //Todo show device management screen
            new AddDeviceDialog(context, true, new Device(), new AddDeviceListener() {
                @Override
                public void addDevice(Device device) {
                    devicesList.add(device);
                    binding.setHasDevices(devicesList != null && !devicesList.isEmpty());
                    devicesAdapter.setDevices(devicesList, true);
                }
            }).show();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }

    }


    private void listenData() {
        devicesViewModel = ViewModelProviders.of(this).get(DeviceListViewModel.class);
        devicesViewModel.getDevices().observe(this, devicesInfo -> {
            devicesList = (ArrayList<Device>) devicesInfo.getDevices();
            binding.setHasDevices(devicesList != null && !devicesList.isEmpty());
            Collections.sort(devicesList, (d1, d2) -> d1.getName().compareTo(d2.getName()));
            devicesAdapter.setDevices(devicesList, false);
        });
    }

    private void init() {
        setRecyclerView();
        binding.saveChanges.setOnClickListener(v -> {
            if (hasChangesToSave) {
                showLoading(getString(R.string.saving));
                updateDevicesToServer();
            } else {
                M2AApplication.snackBarView(context, getString(R.string.no_changes)).show();
            }
        });
        binding.getRoot().setFocusableInTouchMode(true);
        binding.getRoot().requestFocus();
        binding.getRoot().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                Trace.i("keyCode: " + keyCode);
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                    if (hasChangesToSave) {
                        new CommonDialog(context, new Dialog(getString(R.string.alert), getString(R.string.device_change_alert), getString(R.string.ok)), new DialogListener() {
                            @Override
                            public void ok() {
                                ((HomeActivity) context).onBackPressed();
                            }

                            @Override
                            public void cancel() {

                            }
                        }).show();
                    } else {
                        ((HomeActivity) context).onBackPressed();
                    }
                    return true;
                }
                return false;
            }
        });
    }

    private void setRecyclerView() {
        devicesAdapter = new DevicesAdapter(devicesList, context, new ListItemListener() {
            @Override
            public void onItemClick(int position) {
                new AddDeviceDialog(context, false, devicesList.get(position), device1 -> {
                    //Todo
                    devicesList.set(position, device1);
                    devicesAdapter.setDevices(devicesList, true);
                }).show();
            }

            @Override
            public void onChanged(boolean isChanged) {
                Trace.i("List changed" + isChanged);
                updateUi(isChanged);
            }
        });
        binding.recyclerView.setHasFixedSize(true);
        binding.recyclerView.setAdapter(devicesAdapter);
        enableSwipe();
    }

    private void attachObservers() {
        if (devicesViewModel != null) {
            devicesViewModel.run(this);
        }
    }

    private void updateDevicesToServer() {
        AddDeviceModel deviceModel = new AddDeviceModel(1);
        deviceModel.run(context, devicesList).getData().observe(this, integer -> {
            dismissLoading();
            binding.setHasDevices(devicesList != null && !devicesList.isEmpty());
            if (integer != null && integer == 0) {
                Trace.i(getString(R.string.device_update_success));
                updateUi(false);
                M2AApplication.snackBarView(context, getString(R.string.device_update_success)).show();
            } else {
                M2AApplication.snackBarView(context, getString(R.string.device_update_failed)).show();
                Trace.i(getString(R.string.device_update_failed));
            }
        });
    }

    private void updateUi(boolean isChanged) {
        hasChangesToSave = isChanged;
        binding.setListChanged(hasChangesToSave);

    }


    private void enableSwipe() {
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();

                if (direction == ItemTouchHelper.LEFT) {
                    new CommonDialog(context, new Dialog(getString(R.string.delete), getString(R.string.delete_desc), getString(R.string.delete)), new DialogListener() {
                        @Override
                        public void ok() {
                            final Device deletedModel = devicesList.get(position);
                            devicesList.remove(position);
                            devicesAdapter.setDevices(devicesList, true);
                            final int deletedPosition = position;
                            // showing snack bar with Undo option
                            //Snackbar snackbar = Snackbar.make(getActivity().getWindow().getDecorView().getRootView(), " removed from Recyclerview!", Snackbar.LENGTH_LONG);
                            Snackbar snackbar = M2AApplication.snackBarView(context, " Device removed from list.");
                            snackbar.setAction("UNDO", view -> {
                                // undo is selected, restore the deleted item
//                                devicesAdapter.restoreItem(deletedModel, deletedPosition);
                                devicesList.add(deletedPosition, deletedModel);
                                devicesAdapter.setDevices(devicesList, true);
                            });
                            snackbar.setActionTextColor(ResourcesCompat.getColor(getResources(), R.color.colorAccent, null));
                            snackbar.show();
                        }

                        @Override
                        public void cancel() {
                            devicesAdapter.notifyDataSetChanged();
                        }
                    }).show();

                }
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

                Bitmap icon;
                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {

                    View itemView = viewHolder.itemView;
                    float height = (float) itemView.getBottom() - (float) itemView.getTop();
                    float width = height / 3;

                    if (dX < 0) {
                        p.setColor(Color.parseColor("#D32F2F"));
                        RectF background = new RectF((float) itemView.getRight() + dX, (float) itemView.getTop(), (float) itemView.getRight(), (float) itemView.getBottom());
                        c.drawRect(background, p);
                        icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_delete);
                        RectF icon_dest = new RectF((float) itemView.getRight() - 2 * width, (float) itemView.getTop() + width, (float) itemView.getRight() - width, (float) itemView.getBottom() - width);
                        c.drawBitmap(icon, null, icon_dest, p);
                    }
                }
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(binding.recyclerView);
    }

}
