package com.character.microblogapp.util;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.character.microblogapp.BuildConfig;
import com.character.microblogapp.R;
import com.character.microblogapp.data.Constant;
import com.character.microblogapp.ui.page.BaseActivity;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static java.lang.StrictMath.max;

public class MediaManager {

    private final String TAG = "MediaManager";

    public final static int FAILED_BY_CRASH = 3000;
    public final static int FAILED_BY_SIZE_LIMIT = 3001;

    private final int MAX_RESOLUTION = 800;
    private final int MAX_IMAGE_SIZE = 5 * 1024;

    private Activity mActivity = null;
    private MediaCallback mCallback = null;

    private static Uri mUri = null;
    private static Uri mCropUri = null;

    public static final String[] PERMISSION_IMAGE = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
    public final static int SET_GALLERY = 1;
    public final static int SET_CAMERA = 2;
    public final static int SET_CAMERA_VIDEO = 3;
    public final static int CROP_IMAGE = 4;

    public static final int REQUEST_PERMISSION_ALBUM = 2002;
    public static final int REQUEST_PERMISSION_CAMERA = 2003;

    protected MediaManager() {

    }

    /************************************************************
     *  Public
     ************************************************************/

    public interface MediaCallback {
        void onSelected(Boolean isVideo, File file, Bitmap bitmap, String videoPath, String thumbPath);

        void onFailed(int code, String err);

        void onDelete();
    }

    public MediaManager(Activity activity) {
        mActivity = activity;
    }

    public void setMediaCallback(MediaCallback cb) {
        mCallback = cb;
    }

    public void showMediaManager(final String imageName, final Bitmap bitmap, final boolean forDelete) {
        try {
            String[] items;
            ArrayAdapter<String> adapter;
            AlertDialog.Builder builder;
            AlertDialog dialog;

            String title = mActivity.getResources().getString(R.string.profile);

            String option1 = mActivity.getResources().getString(R.string.gallery);
            String option2 = mActivity.getResources().getString(R.string.camera);
            String option3 = mActivity.getResources().getString(R.string.delete);

            if ((imageName.length() > 0 || bitmap != null) && forDelete) {
                items = new String[]{option1, option2, option3};
            } else {
                items = new String[]{option1, option2};
            }
            adapter = new ArrayAdapter<>(mActivity, android.R.layout.select_dialog_item, items);
            builder = new AlertDialog.Builder(mActivity);

            builder.setTitle(null);
            builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int item) {
                    if (item == 0) { // gallery
                        getMediaFromGallery();
                    } else if (item == 1) { // camera
                        getImageFromCamera();
                    } else { // delete
                        // TODO: delete bitmap.
                        mCallback.onDelete();
                    }
                    dialog.cancel();
                }
            });

            dialog = builder.create();
            dialog.show();

        } catch (final Exception ex) {
            Log.d(TAG, ex.toString());
        }
    }

    public void getMediaFromGallery() {
        if (Util.checkPermissions(mActivity, PERMISSION_IMAGE)) {
            getMediaFromGallery(false);
        } else {
            new AlertDialog.Builder(mActivity).setTitle(R.string.gallery).setMessage(R.string.req_album_desc).setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                @TargetApi(Build.VERSION_CODES.M)
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    mActivity.requestPermissions(PERMISSION_IMAGE, REQUEST_PERMISSION_ALBUM);
                }
            }).setNegativeButton(R.string.cancel, null).show();
        }
    }

    public void getMediaFromGallery(boolean video) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE); // | MediaStore.Video.Media.CONTENT_TYPE
        if (video) {
            intent.setType("image/* video/*");
        } else {
            intent.setType("image/*");
        }
        mActivity.startActivityForResult(intent, SET_GALLERY);
    }

    public void getImageFromCamera() {
        if (Util.checkPermissions(mActivity, PERMISSION_IMAGE)) {
            File file = createFile(false);
            mUri = getUri(mActivity, file);

            try {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, mUri);
                mActivity.startActivityForResult(intent, SET_CAMERA);

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            new AlertDialog.Builder(mActivity).setTitle(R.string.camera).setMessage(R.string.req_camera_desc).setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                @TargetApi(Build.VERSION_CODES.M)
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    ((Activity) mActivity).requestPermissions(PERMISSION_IMAGE, REQUEST_PERMISSION_CAMERA);
                }
            }).setNegativeButton(R.string.cancel, null).show();
        }
    }

    public void getVideoFromCamera() {
        File file = createFile(true);
        mUri = getUri(mActivity, file);

        try {
            Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, mUri);
            mActivity.startActivityForResult(intent, SET_CAMERA_VIDEO);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (Util.convertPermissionResult(grantResults)) {
            switch (requestCode) {
                case REQUEST_PERMISSION_ALBUM:
                    getMediaFromGallery();
                    break;
                case REQUEST_PERMISSION_CAMERA:
                    getImageFromCamera();
                    break;
            }
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        if (requestCode == SET_GALLERY) {
            Uri uri = data.getData();
            if (uri.toString().contains("image")) {
                CropImage(uri);
            } else if (uri.toString().contains("video")) { // 갤러리에서 선택된것이 동영상인 경우.
                String path = ImageFilePathUtil.getPath(mActivity, uri);
                String thumb = getThumbnail(path);

                if (mCallback != null) {
                    mCallback.onSelected(true, null, null, path, thumb);
                }
            }
        } else if (requestCode == SET_CAMERA) { // 카메라로 사진을 캡쳐한 경우.
            BitmapFactory.Options options = getBitmapFactory(mUri);
            if (options.outWidth == -1 || options.outHeight == -1)
                return;

            double ratio = getRatio(options);
            Bitmap bitmap = resizeBitmap(mUri, ratio);

            if (checkHighSDK()) {
                File file = getFile(mUri);
                bitmap = checkRotate(bitmap, file.getAbsolutePath());
                saveBitmap(bitmap, file.getAbsolutePath());
                mUri = getUriFromFile(mActivity, file);
            } else {
                bitmap = checkRotate(bitmap, mUri.getPath());
                saveBitmap(bitmap, mUri.getPath());
            }

            try {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
                    mActivity.sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://" + getFolderPath())));
                } else {
                    Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                    File file = new File(getFolderPath());
                    intent.setData(Uri.fromFile(file));
                    mActivity.sendBroadcast(intent);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            CropImage(mUri);
        } else if (requestCode == SET_CAMERA_VIDEO) { // 카메라로 동영상을 캡쳐한 경우.
            String path = getFile(mUri).getPath();
            String thumb = getThumbnail(path);

            if (mCallback != null) {
                mCallback.onSelected(true, null, null, path, thumb);
            }
        } else if (requestCode == CROP_IMAGE) {
            BitmapFactory.Options option = getBitmapFactory(mCropUri);
            if (option.outWidth == -1 || option.outHeight == -1)
                return;

            double ratio = getRatio(option);
            Bitmap bitmap = resizeBitmap(mCropUri, ratio);

            storeCropImage(bitmap, mCropUri.getPath());

            // ---------------Used Media Scanner-----------
            try {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
                    mActivity.sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://" + getFolderPath())));
                } else {
                    Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                    File file = new File(getFolderPath());
                    intent.setData(Uri.fromFile(file));
                    mActivity.sendBroadcast(intent);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            File file = new File(mUri.getPath());
            if (file.exists()) {
                file.delete();
            }
            return;
        } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            Bundle extras = data.getExtras();

            if (extras != null) {
                BitmapFactory.Options option = getBitmapFactory(mCropUri);
                if (option.outWidth == -1 || option.outHeight == -1)
                    return;
                double ratio = getRatio(option);

                Bitmap bitmap = resizeBitmap(mCropUri, ratio);
                storeCropImage(bitmap, mCropUri.getPath());

                // ---------------Used Media Scanner-----------
                try {
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
                        mActivity.sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://" + getFolderPath())));
                    } else {
                        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                        File file = new File(getFolderPath());
                        intent.setData(Uri.fromFile(file));
                        mActivity.sendBroadcast(intent);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                File file = new File(mUri.getPath());
                if (file.exists()) {
                    file.delete();
                }
            }

            return;
        }
    }

    /************************************************************
     *  Helper
     ************************************************************/

    private File createFile(boolean isVideo) {
        File folder = new File(getFolderPath());
        if (!folder.exists())
            folder.mkdirs();

        Long tsLong = System.currentTimeMillis() / 1000;
        String ext = isVideo ? ".mp4" : ".png";
        String filename = tsLong.toString() + ext;
        return new File(folder.toString(), filename);
    }

    private String getFolderPath() {
        return Environment.getExternalStorageDirectory() + "/" + Constant.SDCARD_FOLDER;
    }

    private Uri getUri(Context context, File file) {
        if (checkHighSDK()) {
            return getUriFromFile(context, file);
        } else {
            return Uri.fromFile(file);
        }
    }

    private boolean checkHighSDK() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.N;
    }

    private Uri getUriFromFile(Context context, File file) {
        Log.d(TAG, "file:" + file);
        Log.d(TAG, "BuildConfig.APPLICATION_ID:" + BuildConfig.APPLICATION_ID);
        return FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", file);
    }

    public void CropImage(Uri uri) {
        mUri = uri;

        // commented by Gambler 2019-02-22
        // 여러 기기에서 이미지크롭시 앱이 크래시되는 이슈가 발생하여 이미지크롭을 CropImage 라이브러리를 이용하여 구현하도록 수정
        File file = createFile(false);
        mCropUri = getUri(mActivity, file);
        Log.e("char_debug", "mCropUri : " + mCropUri);
        try {
            CropImage.activity(mUri)
                    .setOutputUri(mCropUri)
                    .setAspectRatio(1, 1)
                    // .setFixAspectRatio(true)
                    .setInitialRotation(0)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .start(mActivity);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 아래의 부분이 이전의 디폴트크롭기능이다. 크래시와 관련하여 주석처리를 하였다.
        /*Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image*//*");

        List<ResolveInfo> list = mActivity.getPackageManager().queryIntentActivities(intent, 0);
        if (checkHighSDK()) {
            mActivity.grantUriPermission(list.get(0).activityInfo.packageName, uri,
                    Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }

        int size = list.size();
        if (size == 0) {
            Log.d(TAG, "Can not find image crop app!");
            return;
        } else {
            if (checkHighSDK()) {
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            }

            //intent.putExtra("aspectX", 1);
            //intent.putExtra("aspectY", 1);
            intent.putExtra("scale", true);

            File file = createFile(false);
            mCropUri = getUri(mActivity, file);

            intent.putExtra("return-data", false);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, mCropUri);
            intent.putExtra("outputFormat", CompressFormat.PNG.toString());

            Intent newIntent = new Intent(intent);
            ResolveInfo res = list.get(0);

            if (checkHighSDK()) {
                newIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                newIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

                mActivity.grantUriPermission(res.activityInfo.packageName, mCropUri,
                        Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
            }

            newIntent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            mActivity.startActivityForResult(newIntent, CROP_IMAGE);
        }*/
    }

    private String getThumbnail(String videoFile) {
        File thumbFile = createFile(false);

        Bitmap bmp = ThumbnailUtils.createVideoThumbnail(videoFile, MediaStore.Images.Thumbnails.MINI_KIND);

        Util.saveBitmapToFile((BaseActivity) mActivity, bmp, thumbFile.getPath());

        return thumbFile.getPath();
    }

    private BitmapFactory.Options getBitmapFactory(Uri uri) {
        InputStream input = null;

        try {
            input = mActivity.getContentResolver().openInputStream(uri);

        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        }

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        options.inDither = true; // optional
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;// optional

        BitmapFactory.decodeStream(input, null, options);

        try {
            input.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return options;
    }

    private double getRatio(BitmapFactory.Options options) {
        int size = max(options.outWidth, options.outHeight);
        return max(size / MAX_RESOLUTION, 1);
    }

    private Bitmap resizeBitmap(Uri uri, double ratio) {
        InputStream input = null;

        try {
            input = mActivity.getContentResolver().openInputStream(uri);

        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        }

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = getPowerOfTwoForSampleRatio(ratio);
        options.inDither = true;// optional
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;// optional

        Bitmap bitmap = BitmapFactory.decodeStream(input, null, options);

        try {
            input.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return bitmap;
    }

    private static int getPowerOfTwoForSampleRatio(double ratio) {
        int k = Integer.highestOneBit((int) Math.floor(ratio));
        if (k == 0)
            return 1;
        else
            return k;
    }

    private File getFile(Uri url) {
        File folder = new File(getFolderPath());
        return new File(folder, new File(url.getPath()).getName());
    }

    private Bitmap checkRotate(Bitmap bitmap, String filename) {
        int orientation = -1;

        try {
            ExifInterface ei = new ExifInterface(filename);
            orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

        } catch (IOException e) {
            e.printStackTrace();
        }

        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                bitmap = rotateImage(bitmap, 90);
                break;

            case ExifInterface.ORIENTATION_ROTATE_180:
                bitmap = rotateImage(bitmap, 180);
                break;

            case ExifInterface.ORIENTATION_ROTATE_270:
                bitmap = rotateImage(bitmap, 270);
                break;
        }
        return bitmap;
    }

    private static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

    public void saveBitmap(Bitmap bitmap, String path) {
        OutputStream stream = null;

        try {
            File file = new File(path);
            stream = new FileOutputStream(file);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        bitmap.compress(CompressFormat.PNG, 100, stream);

        try {
            stream.flush();
            stream.close();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void storeCropImage(Bitmap bitmap, String path) {
        BufferedOutputStream stream = null;
        File newFile = null;

        try {
            if (checkHighSDK()) {
                newFile = createFile(false);
            } else {
                newFile = new File(path);
                newFile.createNewFile();
            }

            stream = new BufferedOutputStream(new FileOutputStream(newFile));
            bitmap.compress(CompressFormat.PNG, 100, stream);

            int size = Integer.parseInt(String.valueOf(newFile.length() / 1024));
            if (size > MAX_IMAGE_SIZE) {
                mCallback.onFailed(FAILED_BY_SIZE_LIMIT, mActivity.getResources().getString(R.string.photo_max_size));
                if (newFile != null && newFile.exists()) {
                    newFile.delete();
                }
            }

            stream.flush();
            stream.close();

            Uri fileUri = Uri.fromFile(newFile);

            /*
            * Commented by Crazy on 5/19/2018
            * */

//            File file = new File(mCropUri.getPath());
//            if (file.exists()) {
//                file.delete();
//            }

            if (mCallback != null)
                Log.e("char_debug", "onSelected : " + newFile.getPath());
                mCallback.onSelected(false, newFile, bitmap, "", "");
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "Sorry, Camera Crashed in createNewFile");
            mCallback.onFailed(FAILED_BY_CRASH, e.toString());
            if (newFile != null && newFile.exists()) {
                newFile.delete();
            }
        }
    }

    private String getRealPath(Uri contentUri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = mActivity.getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }

    private void RemoveImage(String filename) {
        File folder = new File(getFolderPath());
        if (!folder.exists())
            folder.mkdirs();

        File file = new File(folder.toString(), filename);
        if (file != null && file.exists()) {
            Log.d(TAG, "Remove previous temp profile file.");
            file.delete();
        }
    }

    private Bitmap resizeBitmap(Bitmap source, int maxResolution) {
        int width = source.getWidth();
        int height = source.getHeight();
        int newWidth = width;
        int newHeight = height;
        float rate = 0;

        if (width > height) {
            if (maxResolution < width) {
                rate = maxResolution / (float) width;
                newHeight = (int) (height * rate);
                newWidth = maxResolution;
            }
        } else {
            if (maxResolution < height) {
                rate = maxResolution / (float) height;
                newWidth = (int) (width * rate);
                newHeight = maxResolution;
            }
        }

        return Bitmap.createScaledBitmap(source, newWidth, newHeight, true);
    }

}
