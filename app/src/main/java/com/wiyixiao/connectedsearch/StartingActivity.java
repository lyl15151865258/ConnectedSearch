package com.wiyixiao.connectedsearch;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public class StartingActivity extends AppCompatActivity {

    private final int animTime = 4000; // 动画时长

    public final int PERMISSION_REQUEST_CODE = 1;

    private boolean isAnimFinish = false;

    private final String[] permissions = new String[] { // 需要申请的权限列表
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
    };
    private final String[] permissionNames = new String[] { // 需要申请的权限名称
            "Location",
            "Location",
    };

    private List<String> permissionNot = new ArrayList<>();

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starting);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { // 判断版本以是否动态获取权限
            initPermission(); // 动态获取权限
        } else {
            toNextActivity(); // 跳转页面
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void toNextActivity() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    public void initPermission() {
        permissionNot.clear(); // 清空没有通过的权限
        for (String permission : permissions) { // 逐个判断需要的权限是否已经通过
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                permissionNot.add(permission); // 添加还未授予的权限
            }
        }
        if (permissionNot.size() > 0) { // 有权限没有通过，需要申请
            ActivityCompat.requestPermissions(this, permissions, PERMISSION_REQUEST_CODE);
        } else { // 说明权限都已经通过
            toNextActivity();
        }
    }

    /**
     * 请求权限后回调的方法
     * 参数： requestCode  自定义的权限请求码
     * 参数： permissions  请求的权限名称数组
     * 参数： grantResults 弹出页面后是否允许权限的标识数组，数组的长度对应的是权限名称数组的长度，数组的数据0表示允许权限，-1表示我们点击了禁止权限
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean hasPermissionDismiss = false; // 是否权限没有通过
        if (PERMISSION_REQUEST_CODE == requestCode) {
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] == -1) {
                    hasPermissionDismiss = true;
                    permissionNot.add(permissions[i]);
                }
            }
            if (hasPermissionDismiss) { // 有权限没有被允许
                showPermissionDialog(); // 跳转到系统设置权限页面
            }
            else { // 全部权限通过
                toNextActivity();
            }
        }
    }

    AlertDialog permissionDialog;
    private void showPermissionDialog() {
        if (permissionDialog == null) {
            String permissionNotName = "";
            for (int i = 0; i < permissions.length; i++) {
                if (permissions[i].equals(permissionNot.get(0))) {
                    permissionNotName = permissionNames[i];
                }
            }
            permissionDialog = new AlertDialog.Builder(this)
                    .setMessage(permissionNotName + "permission unauthorised")
                    .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            cancelPermissionDialog();
                            finish();
                            Uri packageURI = Uri.parse(String.format("package:%s", StartingActivity.this.getPackageName()));
                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, packageURI);
                            startActivity(intent);
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) { // 关闭页面
                            cancelPermissionDialog();
                            finish();
                        }
                    })
                    .create();
        }
        permissionDialog.show();
    }

    private void cancelPermissionDialog() { // 关闭对话框
        permissionDialog.cancel();
    }

}
