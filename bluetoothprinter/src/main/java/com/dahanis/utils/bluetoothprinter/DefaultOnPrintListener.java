package com.dahanis.utils.bluetoothprinter;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.Toast;

import java.util.List;


public class DefaultOnPrintListener implements com.dahanis.utils.bluetoothprinter.OnPrintListener {

    private Context context;
    private ProgressDialog progressDialog;
    private com.dahanis.utils.bluetoothprinter.IBluetoothPrintUtil iBluetoothPrintUtil;

    public DefaultOnPrintListener(Context context, com.dahanis.utils.bluetoothprinter.IBluetoothPrintUtil iBluetoothPrintUtil) {
        this.context = context;
        this.iBluetoothPrintUtil = iBluetoothPrintUtil;
    }

    @Override
    public void onReceiveDevices(boolean isOnlyBoundedDevices, final List<BluetoothDevice> allAvailableDevices) {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("请选择蓝牙连接设备进行打印");
        String[] names = new String[allAvailableDevices.size()];
        for (int i = 0; i < allAvailableDevices.size(); i++) {
            names[i] = allAvailableDevices.get(i).getName() + "\n" + allAvailableDevices.get(i).getAddress();
        }

        builder.setItems(names, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final int position = which;
                if (!iBluetoothPrintUtil.isConnected()) {
                    iBluetoothPrintUtil.connectDevice(allAvailableDevices.get(position));
                } else {
                    iBluetoothPrintUtil.printData();
                }
            }
        });
        builder.setPositiveButton("找寻新设备", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                progressDialog = ProgressDialog.show(context, null, "搜寻中，请稍候...");
                iBluetoothPrintUtil.refreshDevice();
            }
        });
        builder.setNegativeButton("取消打印", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        AlertDialog dialog = builder.create();
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    @Override
    public void onInitBluetoothFunction() {
        progressDialog = ProgressDialog.show(context, null, "正在初始化中，请稍候...");
    }

    @Override
    public void onInitBluetoothFunctionError() {
        Toast.makeText(context,"蓝牙初始化失败",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStartConnectDevice() {
        progressDialog = ProgressDialog.show(context, null, "正在连接中，请稍候...");
    }

    @Override
    public void onConnectedDevice() {
        progressDialog.dismiss();
        Toast.makeText(context, "蓝牙连接成功",Toast.LENGTH_SHORT).show();
        iBluetoothPrintUtil.printData();
    }

    @Override
    public void onConnectedError(Throwable throwable) {
        progressDialog.dismiss();
        Toast.makeText(context, "蓝牙连接失败",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStartPrint() {
        progressDialog = ProgressDialog.show(context, null, "正在打印中，请稍候...");
    }

    @Override
    public void onSuccessPrint() {
        progressDialog.dismiss();
        Toast.makeText(context, "蓝牙打印成功",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPrintError(Throwable throwable) {
        progressDialog.dismiss();
        Toast.makeText(context, "蓝牙打印失败",Toast.LENGTH_SHORT).show();
        /**
         * 打印失败就断开连接，恢复成未初始化状态，但不清空数据
         * ，这样当下次调用IBluetoothPrintUtil.print(Context context)方法时框架会自动进行重新初始化蓝牙，并可以选择新设备进行连接
         */
        iBluetoothPrintUtil.finishBluetoothConnection();
    }
}