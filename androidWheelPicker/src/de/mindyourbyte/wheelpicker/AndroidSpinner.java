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
import android.view.MotionEvent;
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
	float textScale = 40;
	final float gradientPercentage = 0.3f;
	float previousY = -1;
	WheelScrolling scrolling;

	/**
	 * @param context
	 */
	public AndroidSpinner(Context context) {
		super(context);
		values = new String[0];
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
		scrolling = new WheelScrolling(values.length, this);
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
		Paint paint = getTextPaint();
		String value;
		float x, y;
		float scrollPos;
		float yAddition;
		int selectedPos;
		synchronized (scrolling) {

			scrollPos = scrolling.getSpinPos() % (values.length);
			selectedPos = Math.round(scrolling.getSpinPos()) % (values.length);

			for (int i = 0; i < values.length; i++) {
				// Mark the current selected value.
				if (i == selectedPos) {
					paint.setARGB(255, 255, 0, 0);
				} else {
					paint = getTextPaint();
				}
				value = (String) values[i];
				// Fix Values to display selected Value in the center of the
				// View.
				yAddition = getHeight() / 2f + textScale / 2f
						- paint.getFontMetrics().descent;
				// Draw the value at index i
				x = getWidth() / 2 - paint.measureText(value) / 2;
				y = (i - scrollPos) * textScale + yAddition;
				canvas.drawText(value, x, y, paint);

				paint = getTextPaint();
				// Draw the array above and below the main array, to prevent
				// jumping while trespassing array Bounds.
				y = ((i + values.length) - scrollPos) * textScale + yAddition;
				canvas.drawText(value, x, y, paint);
				y = ((i - values.length) - scrollPos) * textScale + yAddition;
				canvas.drawText(value, x, y, paint);
			}
			// System.out.println("### Redrawing");
		}

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

			if (values.length > 0) {
				Paint paint = getTextPaint();
				int tempRes = 0;
				result = (int) paint.measureText((String) values[0]);
				for (int i = 1; i < values.length; i++) {
					tempRes = (int) paint.measureText((String) values[i]);
					if (result < tempRes) {
						result = tempRes;
					}
				}
			}
			result += getPaddingLeft() + getPaddingRight();
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View#onTouchEvent(android.view.MotionEvent)
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return scrolling.onTouchEvent(event, textScale);
	}
}
