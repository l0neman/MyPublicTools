package io.l0neman.utilstest.app;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import android.widget.Toast;

import com.runing.urilslibtest.R;

import io.l0neman.utils.app.Permission;

import java.util.Set;

public class PermissionUtilsCallTest extends Activity {

  private PermissionUtilsCallTest self() { return this; }

  private Permission getmPermission = Permission.newInstance(
      new Permission.MultiCallBack() {
        @Override public void onGranted(int requestCode, String permission) {
          /* 每个通过的权限都会从这里回调 */
        }

        @Override public void onDenied(int requestCode, String permission) {
          /* 每个被拒绝的权限都会从这里回调 */
        }

        @Override public void onRationale(int requestCode, String permission) {
          /* 每个需要被解释的权限都会从这里回调 */
        }

        @Override public void onGrantedAll(int requestCode) {
          /* 全部权限通过 */
        }

        @Override public void onGrantedPart(int requestCode, Set<String> grantedPermissions) {
          /* 部分权限通过 */
        }
      });

  @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
  private Permission mPermission = Permission.newInstance(
      new Permission.SingleCallback() {
        @Override public void onGranted(int requestCode, String permission) {
          /* 权限通过 */
          Toast.makeText(self(), "granted permissions.", Toast.LENGTH_SHORT).show();
        }

        @Override public void onDenied(int requestCode, String permission) {
          /* 权限被拒绝 */
          Toast.makeText(self(), "denied permissions.", Toast.LENGTH_SHORT).show();
        }

        @Override public void onRationale(int requestCode, String permission) {
          Toast.makeText(self(), "we need permissions.", Toast.LENGTH_SHORT).show();
          /* 解释权限 */
          new AlertDialog.Builder(PermissionUtilsCallTest.this)
              .setPositiveButton("again", new DialogInterface.OnClickListener() {
                @Override public void onClick(DialogInterface dialog, int which) {
                  /* 再次请求 */
                  mPermission.checkAndRequest(self(), 1,
                      true, Manifest.permission.READ_EXTERNAL_STORAGE);
                }
              })
              .setNegativeButton("no", new DialogInterface.OnClickListener() {
                @Override public void onClick(DialogInterface dialog, int which) {
                  Toast.makeText(self(), "no", Toast.LENGTH_SHORT).show();
                }
              })
              .create()
              .show();
        }
      }
  );

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
      mPermission.checkAndRequest(self(), 1, false,
          Manifest.permission.READ_EXTERNAL_STORAGE);
    }
  }

  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                         @NonNull int[] grantResults) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
      mPermission.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
  }
}
