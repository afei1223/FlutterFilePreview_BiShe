package com.example.flutter_file_preview;

import android.Manifest;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.widget.Toast;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import io.flutter.Log;
import io.flutter.embedding.android.FlutterActivity;
import io.flutter.embedding.engine.FlutterEngine;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugins.GeneratedPluginRegistrant;

public class MainActivity extends FlutterActivity {


    MethodChannel.Result result1 = null;
    @Override
    public void configureFlutterEngine(@NonNull FlutterEngine flutterEngine) {
        GeneratedPluginRegistrant.registerWith(flutterEngine);

        /**
         * start
         * 文件预览
         * */
        //创建和flutter的消息通道
        MethodChannel channel = new MethodChannel(flutterEngine.getDartExecutor().getBinaryMessenger(), "yiyun_file_preview");
        channel.setMethodCallHandler(
                new MethodChannel.MethodCallHandler() {
                    @Override
                    public void onMethodCall(MethodCall call, MethodChannel.Result result) {
                        result1 = result;
                        switch (call.method){
                            case "can_open_file":
                                String path= call.argument("path");
                                Boolean res = YiyunFilePreview.CanOpenFile(path,MainActivity.this);
                                result.success(res);
                                break;
                            case "if_file_exists":
                                String path0= call.argument("path");
                                Boolean res0 = YiyunFilePreview.IfFileExists(path0,MainActivity.this);
                                result.success(res0);
                                break;
                            case "get_file_path":
                                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                                //系统调用Action属性
                                intent.setType("*/*");
                                //设置文件类型
                                intent.addCategory(Intent.CATEGORY_OPENABLE);
                                // 添加Category属性
                                MainActivity.this.startActivityForResult(intent,519);
                                break;
                            case "permission_write_read":
                                //获取权限
                                if(!(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED))
                                {
                                    result.success(false);
                                }else {
                                    result.success(true);
                                }
                                break;
                            case "permission_write_read_request":
                                ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1101);
                                break;
                            case "fileRead":
                                String path1 = call.argument("path");
                                Intent intent1 = new Intent(MainActivity.this,FileReadActivity.class);
                                intent1.putExtra("path",path1);
                                MainActivity.this.startActivity(intent1);
                                break;
                            default:
                                result.notImplemented();
                                break;
                        }

                    }
                }
        );

        /**
         * end
         * */

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 519){
            if(resultCode == RESULT_OK){
                Uri uri = data.getData();
                // Get the path
                String path = null;
                Log.i("TAG", String.valueOf(uri));
                Log.i("TAG",uri.getEncodedPath()+"########");
                Log.i("TAG",uri.getPath()+"@@@@@@@@@@@@");
                Log.i("TAG",uri.getLastPathSegment());
                path = getPath(this,uri);
                Log.i("TAG",path+"！！！！！！！！！！！！！！！");
                if(path == null){
                    Toast.makeText(this,"不要用下载文件夹里的文件",Toast.LENGTH_SHORT).show();
                }
                result1.success(path);
            }
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static String getPath(final Context context, final Uri uri) {

        // check here to KITKAT or new version
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {

            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/"
                            + split[1];
                }
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);

                if (id.startsWith("raw:")) {
                    final String path = id.replaceFirst("raw:", "");
                    return path;
                }
                Uri contentUri = uri;
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
                    contentUri = ContentUris.withAppendedId(
                            Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                }
//                final Uri contentUri = ContentUris.withAppendedId(
//                        Uri.parse("content://downloads/public_downloads"),
//                        Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[] { split[1] };

                return getDataColumn(context, contentUri, selection,
                        selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }
    public static String getDataColumn(Context context, Uri uri,
                                       String selection, String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = { column };

        try {
            cursor = context.getContentResolver().query(uri, projection,
                    selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                Log.i("TAG",cursor.getString(index)+"!@#######@#%!@%^&!@%*$#@&!(@&(*");
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri
                .getAuthority());
    }

    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri
                .getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri
                .getAuthority());
    }
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri
                .getAuthority());
    }


    @Override
    protected void onDestroy() {

        super.onDestroy();
    }
}
