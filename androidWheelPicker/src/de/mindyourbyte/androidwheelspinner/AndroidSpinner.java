/**
 * 
 */
package de.mindyourbyte.androidwheelspinner;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
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

	/**
	 * @param context
	 */
	public AndroidSpinner(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param context
	 * @param attrs
	 */
	public AndroidSpinner(Context context, AttributeSet attrs) {
		super(context, attrs);
		initAttributeSet(attrs, context);
	}

	/**
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	public AndroidSpinner(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initAttributeSet(attrs, context);
	}

	private void initAttributeSet(AttributeSet attrs, Context context) {
		TypedArray a = context.obtainStyledAttributes(attrs,
				R.styleable.androidWheelSpinner);
		// Get Value Array from the in the XML file specified Resource
		values = a.getTextArray(R.styleable.androidWheelSpinner_values);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View#onDraw(android.graphics.Canvas)
	 */
	@Override
	protected void onDraw(Canvas canvas) {
	}
}
