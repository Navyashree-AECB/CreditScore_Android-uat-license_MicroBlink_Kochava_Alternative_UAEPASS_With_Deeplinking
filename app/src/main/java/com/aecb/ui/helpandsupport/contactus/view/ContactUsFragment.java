package com.aecb.ui.helpandsupport.contactus.view;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.webkit.MimeTypeMap;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;

import com.aecb.R;
import com.aecb.base.mvp.BaseFragment;
import com.aecb.data.api.models.configuration.CaseTypesItem;
import com.aecb.data.api.models.contactus.ContactUsSubmitRequest;
import com.aecb.databinding.FragmentContactUsBinding;
import com.aecb.ui.helpandsupport.contactus.presenter.ContactUsContract;
import com.aecb.util.CameraUtils;
import com.aecb.util.Utilities;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;
import timber.log.Timber;

import static com.aecb.AppConstants.ActivityIntentCode.CAMERA_CAPTURE_IMAGE_REQUEST_CODE;
import static com.aecb.AppConstants.ActivityIntentCode.SELECT_IMAGE_FILE_REQUEST_CODE;
import static com.aecb.AppConstants.ActivityIntentCode.SELECT_IMAGE_GALLERY_REQUEST_CODE;
import static com.aecb.AppConstants.AppLanguage.ISO_CODE_ENG;
import static com.aecb.util.CameraUtils.MEDIA_TYPE_IMAGE;
import static com.aecb.util.FirebaseLogging.contactUsRegisteredUser;
import static com.aecb.util.varibles.IntegerConstants.BITMAP_QUALITY_SCORE;

/*@RuntimePermissions*/
public class ContactUsFragment extends BaseFragment<ContactUsContract.View, ContactUsContract.Presenter>
        implements ContactUsContract.View, TextWatcher, View.OnClickListener {

    private static String imageStoragePath;
    @Inject
    ContactUsContract.Presenter mPresenter;
    private FragmentContactUsBinding contactUsBinding;
    private ArrayList<CaseTypesItem> allReasons = new ArrayList<>();
    private ArrayList<String> reasonNames = new ArrayList<>();
    private String selectedReason = "";
    private ContactUsSubmitRequest contactUsSubmitRequest;

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_contact_us;
    }

    @Override
    public ContactUsContract.Presenter createPresenter() {
        return mPresenter;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().runOnUiThread(new Runnable() {
            public void run() {
                mPresenter.getReasons();
            }
        });
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        contactUsBinding = DataBindingUtil.inflate(
                inflater, getLayoutRes(), container, false);
        View view = contactUsBinding.getRoot();
        enableBinding();
        return view;
    }

    private void enableBinding() {
        contactUsBinding.edtMessage.addTextChangedListener(this);
        contactUsBinding.edtSubjectName.addTextChangedListener(this);
        contactUsBinding.btnSubmit.setOnClickListener(this);
        contactUsBinding.routUploadFile.setOnClickListener(this);
        contactUsBinding.llCall.setOnClickListener(this);
        contactUsSubmitRequest = new ContactUsSubmitRequest();
        editTextFocusListeners();
        contactUsRegisteredUser();
        contactUsBinding.edtSubjectName.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_NEXT) {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
                View view = getActivity().getCurrentFocus();
                if (view == null) {
                    view = new View(getActivity());
                }
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                contactUsBinding.spinnerReason.expand();
            }
            return false;
        });
    }


    @Override
    public void setReasons(List<CaseTypesItem> caseTypes, String currentLanguage) {
        allReasons.clear();
        reasonNames.clear();
        allReasons.addAll(caseTypes);
        reasonNames.add(getString(R.string.select_reason));

        for (CaseTypesItem caseTypesItem : allReasons) {
            if (currentLanguage.equals(ISO_CODE_ENG)) {
                reasonNames.add(caseTypesItem.getLabelEnglish());
            } else {
                reasonNames.add(caseTypesItem.getLabelArabic());
            }
        }
        contactUsBinding.spinnerReason.setItems(reasonNames);
        contactUsBinding.spinnerReason.setOnItemSelectedListener((view, position, id, item, parent) -> {
            contactUsBinding.edtMessage.requestFocus();
            if (position != 0) {
                selectedReason = (String) item;
                contactUsSubmitRequest.setReason(allReasons.get(position - 1).getCode());
            } else {
                contactUsSubmitRequest.setReason("");
            }
        });
    }

    @Override
    public void resetData() {
        contactUsBinding.edtSubjectName.setText(null);
        contactUsBinding.edtMessage.setText(null);
        selectedReason = "";
        contactUsBinding.spinnerReason.setItems(new ArrayList<>());
        contactUsBinding.ivUpload.setVisibility(View.VISIBLE);
        contactUsBinding.selectedImage.setVisibility(View.GONE);
        contactUsBinding.tvUpload.setText(getString(R.string.upload_file));
        contactUsSubmitRequest.setFilecontent("");
        contactUsSubmitRequest.setFilemimetype("");
        contactUsSubmitRequest.setFilename("");
        contactUsSubmitRequest.setReason("");
        contactUsBinding.spinnerReason.setItems(reasonNames);
    }

    private void editTextFocusListeners() {
        contactUsBinding.edtSubjectName.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                contactUsBinding.txtInputLoutSubjectName.setHint(getString(R.string.lbl_subject));
            } else if (!contactUsBinding.edtSubjectName.getText().toString().isEmpty()) {
                contactUsBinding.txtInputLoutSubjectName.setHint(getString(R.string.lbl_subject));
            } else {
                contactUsBinding.txtInputLoutSubjectName.setHint(getString(R.string.lbl_subject));
            }
        });
        contactUsBinding.edtMessage.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                contactUsBinding.txtInputLoutMessage.setHint(getString(R.string.your_message));
            } else if (!contactUsBinding.edtMessage.getText().toString().isEmpty()) {
                contactUsBinding.txtInputLoutMessage.setHint(getString(R.string.your_message));
            } else {
                contactUsBinding.txtInputLoutMessage.setHint(getString(R.string.your_message));
            }
        });
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (!contactUsBinding.edtSubjectName.getText().toString().isEmpty() &&
                !contactUsBinding.edtMessage.getText().toString().isEmpty()) {
            contactUsBinding.btnSubmit.setEnabled(true);
            contactUsBinding.btnSubmit.setBackground(getResources().getDrawable(R.drawable.bg_blue_button));
        } else {
            contactUsBinding.btnSubmit.setEnabled(false);
            contactUsBinding.btnSubmit.setBackground(getResources().getDrawable(R.drawable.btn_disable_state));
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
    }

    @Override
    public void showMessage(String message) {
        localValidationError(getString(R.string.error), message);
    }

    @Override
    public void showQueryMessagesValidError() {
        showMessage(getString(R.string.txt_msg_validate_alphanumeric));
    }

    @Override
    public void showQueryMessagesError() {
        showMessage(getString(R.string.txt_msg_select_query_message));
    }

    @Override
    public void showQuerySubjectValidError() {
        showMessage(getString(R.string.txt_msg_validate_alphanumeric));
    }

    @Override
    public void showQuerySubjectError() {
        showMessage(getString(R.string.txt_msg_select_query_subject));
    }

    @Override
    public void showQueryTypeError() {
        showMessage(getString(R.string.txt_msg_select_reason));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSubmit:
                contactUsSubmitRequest.setChannel(3);
                contactUsSubmitRequest.setDescription(contactUsBinding.edtMessage.getText().toString());
                contactUsSubmitRequest.setSubject(contactUsBinding.edtSubjectName.getText().toString());
                contactUsSubmitRequest.setType("contactus");
                mPresenter.submitContactUsQuery(contactUsSubmitRequest);
                break;
            case R.id.routUploadFile:
                //ContactUsFragmentPermissionsDispatcher.askPermissionForCameraAndFilesWithPermissionCheck(this);
                break;
            case R.id.llCall:
                Utilities.makeCall(getActivity(), "800287328");
                break;
        }
    }

    /*@NeedsPermission({Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE})
    public void askPermissionForCameraAndFiles() {
        selectImage();
    }

    @OnPermissionDenied({Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE})
    public void showPermissionDeniedForCameraAndFiles() {
        new android.app.AlertDialog.Builder(getActivity())
                .setMessage(R.string.permission_storage_denied)
                .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                }).show();
    }

    @OnNeverAskAgain({Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE})
    public void showNeverAskForStorage() {
        new android.app.AlertDialog.Builder(getActivity())
                .setMessage(R.string.permission_storage_permanent_denial)
                .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                }).show();
    }

    @OnShowRationale({Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE})
    public void showRationaleForStorage(final PermissionRequest request) {
        new android.app.AlertDialog.Builder(getActivity())
                .setMessage(R.string.permission_storage_rationale)
                .setPositiveButton(R.string.allow, (dialog, which) -> request.proceed())
                .setNegativeButton(R.string.deny, (dialog, which) -> request.cancel()).show();
    }*/

    private void selectImage() {
        try {
            final CharSequence[] options = {getString(R.string.take_photo), getString(R.string.open_gallery),
                    getString(R.string.file_manager), getString(R.string.txt_cancel)};
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.MyDialogTheme);
            String SELECT_CATEGORY = getString(R.string.choose_categories);
            builder.setTitle(SELECT_CATEGORY);
            builder.setItems(options, (dialog, item) -> {
                if (options[item].equals(getString(R.string.take_photo))) {
                    captureImage();
                } else if (options[item].equals(getString(R.string.open_gallery))) {
                    Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(i, SELECT_IMAGE_GALLERY_REQUEST_CODE);
                } else if (options[item].equals(getString(R.string.file_manager))) {
                    Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                    intent.setType("image/*");
                    startActivityForResult(intent, SELECT_IMAGE_FILE_REQUEST_CODE);
                } else if (options[item].equals(getString(R.string.txt_cancel))) {
                    dialog.dismiss();
                }
            });
            builder.show();
        } catch (Exception e) {
            Timber.d("Exception : " + e.toString());
        }
    }

    private void captureImage() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File file = CameraUtils.getOutputMediaFile(MEDIA_TYPE_IMAGE);
        if (file != null) {
            imageStoragePath = file.getAbsolutePath();
        }
        Uri fileUri = CameraUtils.getOutputMediaFileUri(getActivity(), file);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (resultCode == Activity.RESULT_OK) {
                if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
                    CameraUtils.refreshGallery(getActivity(), imageStoragePath);
                    contactUsBinding.ivUpload.setVisibility(View.GONE);
                    contactUsBinding.selectedImage.setVisibility(View.GONE);
                    contactUsBinding.selectedImage.setImageURI(Uri.parse(imageStoragePath));
                    Bitmap resizedBitmap = BitmapFactory.decodeFile(imageStoragePath);
                    try {
                        File file = Utilities.saveBitmapToFile(new File(imageStoragePath));
                        String filePath = file.getPath();
                        resizedBitmap = BitmapFactory.decodeFile(filePath);
                    } catch (Exception e) {
                        Timber.d("Exception : " + e.toString());
                    }
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    resizedBitmap.compress(Bitmap.CompressFormat.JPEG, BITMAP_QUALITY_SCORE, baos);
                    byte[] byteArrayImage = baos.toByteArray();
                    String encodedImage = Base64.encodeToString(byteArrayImage, Base64.DEFAULT);
                    contactUsSubmitRequest.setFilecontent(encodedImage);
                    String type = null;
                    String extension = MimeTypeMap.getFileExtensionFromUrl(Uri.parse(imageStoragePath).toString());
                    if (extension != null && !extension.equals("")) {
                        type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
                        contactUsSubmitRequest.setFilemimetype(type);
                    } else {
                        contactUsSubmitRequest.setFilemimetype("image/jpeg");
                    }
                    contactUsSubmitRequest.setFilename(Uri.parse(imageStoragePath).getLastPathSegment());
                    contactUsBinding.tvUpload.setText(Uri.parse(imageStoragePath).getLastPathSegment());
                } else if (requestCode == SELECT_IMAGE_GALLERY_REQUEST_CODE) {
                    Uri selectedImage = data.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
                    if (selectedImage != null) {
                        Cursor cursor = getActivity().getContentResolver().query(selectedImage, filePathColumn, null,
                                null, null);
                        if (cursor != null) {
                            cursor.moveToFirst();
                            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                            String picturePath = cursor.getString(columnIndex);
                            Bitmap resizedBitmap = BitmapFactory.decodeFile(picturePath);
                            try {
                                File file = Utilities.saveBitmapToFile(new File(picturePath));
                                String filePath = file.getPath();
                                resizedBitmap = BitmapFactory.decodeFile(filePath);
                            } catch (Exception e) {
                                Timber.d("Exception : " + e.toString());
                            }
                            contactUsBinding.ivUpload.setVisibility(View.GONE);
                            contactUsBinding.selectedImage.setVisibility(View.GONE);
                            contactUsBinding.selectedImage.setImageBitmap(resizedBitmap);
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            resizedBitmap.compress(Bitmap.CompressFormat.JPEG, BITMAP_QUALITY_SCORE, baos);
                            byte[] byteArrayImage = baos.toByteArray();
                            String encodedImage = Base64.encodeToString(byteArrayImage, Base64.DEFAULT);
                            contactUsSubmitRequest.setFilecontent(encodedImage);
                            String type = null;
                            String extension = MimeTypeMap.getFileExtensionFromUrl(picturePath);
                            if (extension != null && !extension.equals("")) {
                                type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
                                contactUsSubmitRequest.setFilemimetype(type);
                            } else {
                                contactUsSubmitRequest.setFilemimetype("image/jpeg");
                            }
                            String fileName = picturePath.substring(picturePath.lastIndexOf("/") + 1);
                            contactUsSubmitRequest.setFilename(fileName);
                            contactUsBinding.tvUpload.setText(fileName);
                            cursor.close();
                        }
                    }
                } else {
                    Uri uri = data.getData();
                    try {
                        final InputStream imageStream = getActivity().getContentResolver().openInputStream(uri);
                        Bitmap resizedBitmap = BitmapFactory.decodeStream(imageStream);
                        try {
                            File fileForBitmap = Utilities.createBitmapToFile(getActivity(), resizedBitmap);
                            File file = Utilities.saveBitmapToFile(fileForBitmap);
                            String filePath = file.getPath();
                            resizedBitmap = BitmapFactory.decodeFile(filePath);
                        } catch (Exception e) {
                            Timber.d("Exception : " + e.toString());
                        }
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        resizedBitmap.compress(Bitmap.CompressFormat.JPEG, BITMAP_QUALITY_SCORE, baos);
                        byte[] byteArrayImage = baos.toByteArray();
                        String encodedImage = Base64.encodeToString(byteArrayImage, Base64.DEFAULT);
                        contactUsSubmitRequest.setFilecontent(encodedImage);
                        String type = null;
                        String extension = MimeTypeMap.getFileExtensionFromUrl(data.getData().toString());
                        if (extension != null && !extension.equals("")) {
                            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
                            contactUsSubmitRequest.setFilemimetype(type);
                        } else {
                            extension = ".jpg";
                            contactUsSubmitRequest.setFilemimetype("image/jpeg");
                        }
                        String fileName = data.getData().getLastPathSegment();
                        fileName = fileName.substring(fileName.lastIndexOf("/") + 1);
                        contactUsSubmitRequest.setFilename("IMG" + System.currentTimeMillis() + extension);
                        contactUsBinding.tvUpload.setText("IMG" + System.currentTimeMillis() + extension);
                    } catch (FileNotFoundException e) {
                        Timber.d("Exception : " + e.toString());
                    }
                    contactUsBinding.ivUpload.setVisibility(View.GONE);
                    contactUsBinding.selectedImage.setVisibility(View.GONE);
                    contactUsBinding.selectedImage.setImageURI(uri);
                }
            }
        } catch (Exception e) {
            Timber.d("Exception : " + e.toString());
        }
    }
}