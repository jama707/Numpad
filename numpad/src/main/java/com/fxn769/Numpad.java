package com.fxn769;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.text.Editable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.fxn769.AssociatedViewType.edit_text;
import static com.fxn769.AssociatedViewType.text_view;

/**
 * Created by akshay on 15/12/17.
 */

public class Numpad extends FrameLayout implements View.OnClickListener {
    private Context context;
    private ArrayList<TextView> num = new ArrayList<>();
    private ArrayList<ImageView> line = new ArrayList<>();
    private Map<TextView, CharSequence> associatedViews = new HashMap<TextView, CharSequence>();
    private EditText focusedEditText;
    private String digits = "";
    private int TextLengthLimit = 6;
    private float TextSize = 12;
    private int TextColor = Color.BLACK;
    private int BackgroundResource = R.drawable.numpad_background;
    private int ImageResource = R.drawable.ic_backspace;
    private boolean GridVisible = true;
    private int GridBackgroundColor = Color.GRAY;
    private int GridThickness = 3;
    private AssociatedViewType type = text_view;
    private String FontFaceString = "";
    private Typeface typeface;
    private ImageView delete;
    private FrameLayout delete_layout;
    private TextGetListner textGetListner;

    public Numpad( Context context) {
        super(context);
        initialise(context, null);
    }

    public Numpad( Context context,  AttributeSet attrs) {
        super(context, attrs);
        initialise(context, attrs);
    }

    public Numpad( Context context,  AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialise(context, attrs);
    }

    public Numpad setOnTextChangeListner(TextGetListner textGetListner) {
        this.textGetListner = textGetListner;
        setup();
        return this;
    }

    public Numpad setBackgroundRes(int BackgroundResource) {
        this.BackgroundResource = BackgroundResource;
        setup();
        return Numpad.this;
    }

    public Numpad setImageRes(int ImageResource) {
        this.ImageResource = ImageResource;
        setup();
        return Numpad.this;
    }

    public Numpad setFontFace(String FontFaceString) {
        this.FontFaceString = FontFaceString;
        typeface = Typeface.createFromAsset(context.getAssets(), this.FontFaceString);
        setup();
        return this;
    }

    private void initialise(Context context, AttributeSet attrs) {
        this.context = context;
        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.Numpad, 0, 0);
        digits = attributes.getString(R.styleable.Numpad_numpad_digits);
        TextLengthLimit = attributes.getInt(R.styleable.Numpad_numpad_text_limit, 5);
        TextSize = attributes.getDimension(R.styleable.Numpad_numpad_text_size, 12.0f);
        TextColor = attributes.getColor(R.styleable.Numpad_numpad_text_color, Color.BLACK);
        BackgroundResource = attributes.getResourceId(R.styleable.Numpad_numpad_background_resource, R.drawable.numpad_background);
        ImageResource = attributes.getResourceId(R.styleable.Numpad_numpad_image_resource, R.drawable.ic_backspace);
        GridVisible = attributes.getBoolean(R.styleable.Numpad_numpad_grid_visible, false);
        GridBackgroundColor = attributes.getColor(R.styleable.Numpad_numpad_grid_background_color, Color.GRAY);
        GridThickness = (int) attributes.getDimension(R.styleable.Numpad_numpad_grid_line_thickness, 3);
        FontFaceString = attributes.getString(R.styleable.Numpad_numpad_fontpath);
        type = AssociatedViewType.of(attributes.getInt(R.styleable.Numpad_numpad_type, text_view.getCode()));
        if (digits == null) {
            digits = "";
        }

//        int associatedViewsResourceId = attributes.getResourceId(R.styleable.Numpad_numpad_associated_with, 0);
//        if (associatedViewsResourceId != 0) {
//            int[] viewIds = attributes.getResources().getIntArray(associatedViewsResourceId);
//            for (int viewId : viewIds) {
//                associatedViews.put(viewId, "");
//            }
//        }
        final View v = LayoutInflater.from(context).inflate(R.layout.numlock_view, this, false);
        num.add((TextView) v.findViewById(R.id.one));
        num.add((TextView) v.findViewById(R.id.two));
        num.add((TextView) v.findViewById(R.id.three));
        num.add((TextView) v.findViewById(R.id.four));
        num.add((TextView) v.findViewById(R.id.five));
        num.add((TextView) v.findViewById(R.id.six));
        num.add((TextView) v.findViewById(R.id.seven));
        num.add((TextView) v.findViewById(R.id.eight));
        num.add((TextView) v.findViewById(R.id.nine));
        num.add((TextView) v.findViewById(R.id.zero));
        line.add((ImageView) v.findViewById(R.id.line1));
        line.add((ImageView) v.findViewById(R.id.line2));
        line.add((ImageView) v.findViewById(R.id.line3));
        line.add((ImageView) v.findViewById(R.id.line4));
        line.add((ImageView) v.findViewById(R.id.line5));
        line.add((ImageView) v.findViewById(R.id.line6));
        line.add((ImageView) v.findViewById(R.id.line7));
        line.add((ImageView) v.findViewById(R.id.line8));
        line.add((ImageView) v.findViewById(R.id.line9));
        line.add((ImageView) v.findViewById(R.id.line10));
        line.add((ImageView) v.findViewById(R.id.line11));
        delete = v.findViewById(R.id.delete);
        delete_layout = v.findViewById(R.id.delete_layout);
        if (FontFaceString == null) {
            typeface = Typeface.DEFAULT;
        } else {
            typeface = Typeface.createFromAsset(context.getAssets(), FontFaceString);
        }
        setup();
        addView(v);
    }

    private void setup() {

        for (TextView textView : num) {
            textView.setOnClickListener(this);
            textView.setTextSize(TextSize);
            textView.setTextColor(TextColor);
            textView.setBackgroundResource(BackgroundResource);
            textView.setTypeface(typeface);
        }

        if (GridVisible) {
            for (ImageView imageView : line) {
                imageView.setVisibility(VISIBLE);
                imageView.setBackgroundColor(GridBackgroundColor);
            }
            line.get(0).setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, GridThickness));
            line.get(1).setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, GridThickness));
            line.get(2).setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, GridThickness));
            line.get(3).setLayoutParams(new LinearLayout.LayoutParams(GridThickness, LayoutParams.MATCH_PARENT));
            line.get(4).setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, GridThickness));
            line.get(5).setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, GridThickness));
            line.get(6).setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, GridThickness));
            line.get(7).setLayoutParams(new LinearLayout.LayoutParams(GridThickness, LayoutParams.MATCH_PARENT));
            line.get(8).setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, GridThickness));
            line.get(9).setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, GridThickness));
            line.get(10).setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, GridThickness));
        }

        delete_layout.setOnClickListener(this);
        delete_layout.setBackgroundResource(BackgroundResource);
        delete.setImageResource(ImageResource);
    }

    public String getDigits() {
        return digits;
    }

    public int getTextLengthLimit() {
        return TextLengthLimit;
    }

    public Numpad setTextLengthLimit(int TextLengthLimit) {
        this.TextLengthLimit = TextLengthLimit;
        setup();
        return this;
    }

    public float getTextSize() {
        return TextSize;
    }

    public Numpad setTextSize(int TextSize) {
        this.TextSize = TextSize;
        setup();
        return this;
    }

    public int getTextColor() {
        return TextColor;
    }

    public Numpad setTextColor(int TextColor) {
        this.TextColor = TextColor;
        setup();
        return this;
    }

    public int getBackgroundResource() {
        return BackgroundResource;
    }

    public int getImageResource() {
        return ImageResource;
    }

    public boolean isGridVisible() {
        return GridVisible;
    }

    public Numpad setGridVisible(boolean GridVisible) {
        this.GridVisible = GridVisible;
        setup();
        return Numpad.this;
    }

    public int getGridBackgroundColor() {
        return GridBackgroundColor;
    }

    public Numpad setGridBackgroundColor(int GridBackgroundColor) {
        this.GridBackgroundColor = GridBackgroundColor;
        setup();
        return Numpad.this;
    }

    public int getGridThickness() {
        return GridThickness;
    }

    public Numpad setGridThickness(int GridThickness) {
        this.GridThickness = GridThickness;
        setup();
        return Numpad.this;
    }

    public Typeface getTypeface() {
        return typeface;
    }

    public ImageView getImageResourceView() {
        return delete;
    }

    public TextGetListner getTextGetListner() {
        return textGetListner;
    }



    public void associateWithAndFocus(EditText editText) {
        this.focusedEditText = editText;
        associateWith(editText);
    }

    public void associateWith(EditText editText) {
        this.type = edit_text;

        associatedViews.put(editText, editText.getText());

        editText.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                focusedEditText = (EditText) v;
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            editText.setShowSoftInputOnFocus(false);
        }
    }

    @Override
    public void onClick(final View view) {
        if (type == text_view) {

            if (view instanceof TextView && digits.length() < TextLengthLimit) {
                digits += ((TextView) view).getText();
            } else if (view instanceof FrameLayout && digits.length() >= 1) {
                digits = digits.substring(0, digits.length() - 1);
            }
        } else if (type == edit_text) {

            Editable currentText = focusedEditText.getText();
            int cursorPosition = focusedEditText.getSelectionStart();

            if (view instanceof TextView && currentText.length() < TextLengthLimit) {
                CharSequence clickedNumber = ((TextView) view).getText();
                focusedEditText.setText(currentText.insert(cursorPosition, clickedNumber));
                focusedEditText.setSelection(cursorPosition + 1);
            } else if (view instanceof FrameLayout && currentText.length() >= 1) {
                if (cursorPosition != 0) {
                    focusedEditText.setText(currentText.delete(cursorPosition - 1, cursorPosition));
                    focusedEditText.setSelection(cursorPosition - 1);
                }
            }
            digits = focusedEditText.getText().toString();
        }


        if (textGetListner != null) {

            textGetListner.onTextChange(digits, TextLengthLimit - digits.length());
        }
    }


}
