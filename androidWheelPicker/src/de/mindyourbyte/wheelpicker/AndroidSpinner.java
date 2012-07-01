/**
 * 
 */
package de.mindyourbyte.wheelpicker;

import android.content.Context;
import android.content.res.Resources.NotFoundException;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

/**
 * 
 * <p>
 * (c) 2012 - Marvin Pohl - <a
 * href="http://www.MindYourByte.de">www.MindYourByte.de</a>
 * </p>
 * 
 * @author Marvin Pohl
 * @since 22:17:54 - 29.06.2012
 */

public class AndroidSpinner extends View {
	private CharSequence[] values;
	float textScale = 0;
	final float gradientPercentage = 0.3f;

	/**
	 * @param context
	 */
	public AndroidSpinner(Context context) {
		super(context);
		initView();
	}

	/**
	 * @param context
	 * @param attrs
	 */
	public AndroidSpinner(Context context, AttributeSet attrs) {
		super(context, attrs);
		initAttributeSet(attrs, context);
		initView();
	}

	/**
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	public AndroidSpinner(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initAttributeSet(attrs, context);
		initView();
	}

	private void initAttributeSet(AttributeSet attrs, Context context) {
		TypedArray a = context.obtainStyledAttributes(attrs,
				R.styleable.androidWheelSpinner);
		// Get Value Array from the in the XML file specified Resource
		values = a.getTextArray(R.styleable.androidWheelSpinner_values);
		if (values == null) {
			// If resource is not available create dummy values.
			values = new String[0];
		}
		textScale = a.getDimension(R.styleable.androidWheelSpinner_textScale,
				-new Paint().ascent());

	}

	private void initView() {
		setBackgroundDrawable(getResources().getDrawable(
				R.drawable.wheelspinnerbggradient));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View#onDraw(android.graphics.Canvas)
	 */
	@Override
	protected void onDraw(Canvas canvas) {
		canvas.save();
		try {
			drawEntries(canvas);

			drawTopBotShadows(canvas);
		} catch (NotFoundException e) {
			// Display info if resource is not found (Currently for the
			// Designer, that its working).
			String text = "Wheel Picker View";

			Paint paint = getTextPaint();
			canvas.drawText(text, getWidth() / 2f - paint.measureText(text)
					/ 2f, getHeight() / 2f - paint.ascent() / 2f, paint);
		}

		canvas.restore();
	}

	/**
	 * Draws the Shadows of the WheelPicker at the Top and Bottom.
	 * 
	 * @param canvas
	 */
	public void drawTopBotShadows(Canvas canvas) {
		Drawable topBottomgradient;
		topBottomgradient = getResources().getDrawable(
				R.drawable.wheelspinnergradienttop);
		// Set the bounds of the Gradient to the percentage of the View
		// height.
		topBottomgradient.setBounds(0, 0, this.getWidth(),
				(int) (getHeight() * gradientPercentage));
		// Draw the upper gradient.
		topBottomgradient.draw(canvas);
		// Rotate the canvas about 180° around the center.
		canvas.rotate(180, getWidth() / 2f, getHeight() / 2f);
		// Draw the lower gradient
		topBottomgradient.draw(canvas);
	}

	/**
	 * Draws the entries in the WheelPicker
	 * 
	 * @param canvas
	 */
	public void drawEntries(Canvas canvas) {
		// TODO drawEntries
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View#onMeasure(int, int)
	 */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		setMeasuredDimension(measureWidth(widthMeasureSpec),
				measureHeight(heightMeasureSpec));
	}

	/**
	 * Measure the needed width of the component.
	 * 
	 * @param widthMeasureSpec
	 * @return the measured Width.
	 */
	private int measureWidth(int widthMeasureSpec) {
		int result = 0;
		int width = MeasureSpec.getSize(widthMeasureSpec);
		int mode = MeasureSpec.getMode(widthMeasureSpec);
		if (mode == MeasureSpec.EXACTLY) {
			result = width;
		} else {
			// TODO calculate width of the spinner.
			result = getPaddingLeft() + getPaddingRight() + 100;
			if (mode == MeasureSpec.AT_MOST) {
				result = Math.min(width, result);
			}
		}
		return result;
	}

	/**
	 * Measures the needed height of the component.
	 * 
	 * @param heightMeasureSpec
	 * @return the measured Height.
	 */
	private int measureHeight(int heightMeasureSpec) {
		int result = 0;
		int height = MeasureSpec.getSize(heightMeasureSpec);
		int mode = MeasureSpec.getMode(heightMeasureSpec);
		if (mode == MeasureSpec.EXACTLY) {
			result = height;
		} else {
			// Set the height to 5 times (to display 5 values) the line height
			// of the text.
			result = getPaddingTop() + getPaddingBottom()
					+ (int) (5 * textScale);
			if (mode == MeasureSpec.AT_MOST) {
				result = Math.min(height, result);
			}
		}
		return result;
	}

	/**
	 * Creates a Paint with the textScale from the XML textScale value.
	 * 
	 * @return
	 */
	public Paint getTextPaint() {
		Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		paint.setTextSize(textScale);
		return paint;
	}

}
