package org.fossasia.phimpme.editor.fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;

import org.fossasia.phimpme.MyApplication;
import org.fossasia.phimpme.R;
import org.fossasia.phimpme.editor.EditImageActivity;
import org.fossasia.phimpme.editor.font.FontPickerDialog;
import org.fossasia.phimpme.editor.task.StickerTask;
import org.fossasia.phimpme.editor.view.TextStickerView;

public class AddTextFragment extends BaseEditFragment implements TextWatcher, FontPickerDialog.FontPickerDialogListener {
    public static final int INDEX = 5;
    private View mainView;

    private EditText mInputText;
    private ImageView mTextColorSelector;
    private TextStickerView mTextStickerView;

    private InputMethodManager imm;
    private SaveTextStickerTask mSaveTask;

    public static AddTextFragment newInstance() {
        return new AddTextFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        mainView = inflater.inflate(R.layout.fragment_edit_image_add_text, null);
        return mainView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mTextStickerView = (TextStickerView) getActivity().findViewById(R.id.text_sticker_panel);

        View cancel = mainView.findViewById(R.id.text_cancel);
        View apply = mainView.findViewById(R.id.text_apply);
        ImageButton ibFontChoice = (ImageButton) mainView.findViewById(R.id.text_font);

        ((ImageButton) cancel).setColorFilter(Color.BLACK);
        ((ImageButton) apply).setColorFilter(Color.BLACK);

        mInputText = (EditText) mainView.findViewById(R.id.text_input);
        mTextColorSelector = (ImageView) mainView.findViewById(R.id.text_color);
        mTextColorSelector.setImageDrawable(new IconicsDrawable(activity).icon(GoogleMaterial.Icon.gmd_format_color_fill).sizeDp(24));

        cancel.setOnClickListener(new BackToMenuClick());
        apply.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                applyTextImage();
            }
        });
        mTextColorSelector.setOnClickListener(new SelectColorBtnClick());
        mInputText.addTextChangedListener(this);
        boolean focus = mInputText.requestFocus();
        if (focus) {
            imm.showSoftInput(mInputText, InputMethodManager.SHOW_IMPLICIT);
        }
        mTextStickerView.setEditText(mInputText);
        onShow();

        ibFontChoice.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showFontChoiceBox();
            }
        });
    }

    private void showFontChoiceBox() {
        DialogFragment dialogFragment = FontPickerDialog.newInstance(this);
        dialogFragment.show(getFragmentManager(), "fontPicker");
    }

    @Override
    public void afterTextChanged(Editable s) {
        String text = s.toString().trim();
        mTextStickerView.setText(text);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void onFontSelected(FontPickerDialog dialog) {
        mTextStickerView.setTextTypeFace(Typeface.createFromFile(dialog.getSelectedFont()));
    }

    private final class SelectColorBtnClick implements OnClickListener {
        @Override
        public void onClick(View v) {
        }

    }

    public void hideInput() {
        if (getActivity() != null && getActivity().getCurrentFocus() != null && isInputMethodShow()) {
            imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public boolean isInputMethodShow() {
        return imm.isActive();
    }

    private final class BackToMenuClick implements OnClickListener {
        @Override
        public void onClick(View v) {
            backToMain();
        }
    }

    public void backToMain() {
        hideInput();
        activity.changeMode(EditImageActivity.MODE_WRITE);
        activity.writeFragment.clearSelection();
        activity.changeBottomFragment(EditImageActivity.MODE_MAIN);
        activity.mainImage.setVisibility(View.VISIBLE);
        mTextStickerView.clearTextContent();
        mTextStickerView.setVisibility(View.GONE);
    }

    @Override
    public void onShow() {
        activity.changeMode(EditImageActivity.MODE_TEXT);
        activity.mainImage.setImageBitmap(activity.mainBitmap);
        activity.addTextFragment.getmTextStickerView().mainImage = activity.mainImage;
        activity.addTextFragment.getmTextStickerView().mainBitmap = activity.mainBitmap;
        mTextStickerView.setVisibility(View.VISIBLE);
        mInputText.clearFocus();
    }

    public TextStickerView getmTextStickerView(){return mTextStickerView;}

    public void applyTextImage() {
        if (mSaveTask != null) {
            mSaveTask.cancel(true);
        }

        mSaveTask = new SaveTextStickerTask(activity, activity.mainImage.getImageViewMatrix());
        mSaveTask.execute(activity.mainBitmap);
    }

    private final class SaveTextStickerTask extends StickerTask {

        public SaveTextStickerTask(EditImageActivity activity, Matrix imageViewMatrix) {
            super(activity, imageViewMatrix);
        }

        @Override
        public void handleImage(Canvas canvas, Matrix m) {
            float[] f = new float[9];
            m.getValues(f);
            int dx = (int) f[Matrix.MTRANS_X];
            int dy = (int) f[Matrix.MTRANS_Y];
            float scale_x = f[Matrix.MSCALE_X];
            float scale_y = f[Matrix.MSCALE_Y];
            canvas.save();
            canvas.translate(dx, dy);
            canvas.scale(scale_x, scale_y);
            mTextStickerView.drawText(canvas, mTextStickerView.layout_x,
                    mTextStickerView.layout_y, mTextStickerView.mScale, mTextStickerView.mRotateAngle);
            canvas.restore();
        }

        @Override
        public void onPostResult(Bitmap result) {
            mTextStickerView.clearTextContent();
            mTextStickerView.resetView();
            activity.changeMainBitmap(result);
            backToMain();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        resetTextStickerView();
    }

    private void resetTextStickerView() {
        if (null != mTextStickerView) {
            mTextStickerView.clearTextContent();
            mTextStickerView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mSaveTask != null && !mSaveTask.isCancelled()) {
            mSaveTask.cancel(true);
        }
        MyApplication.getRefWatcher(getActivity()).watch(this);
    }
}
