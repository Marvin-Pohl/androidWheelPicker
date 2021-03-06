/**
 * 
 */
package de.mindyourbyte.wheelpicker;

import java.util.Timer;
import java.util.TimerTask;

import android.view.MotionEvent;
import android.view.View;

/**
 * 
 * <p>
 * (c) 2012 - Marvin - <a
 * href="http://www.MindYourByte.de">www.MindYourByte.de</a>
 * </p>
 * 
 * @author Marvin
 * @since 12:30:36 - 02.07.2012
 */
public class WheelScrolling extends TimerTask {
	private boolean snapping = false;
	private int valuesLength;
	private int position;
	private float spinSpeed;
	private float spinPos;
	private float scrollFactor = 1.5f;
	Timer timer;
	final long updateInterval = 16;
	View rootView;
	private float previousY;

	/**
	 * 
	 */
	public WheelScrolling(int valuesLength, View view) {
		timer = new Timer();
		timer.scheduleAtFixedRate(this, updateInterval, updateInterval);
		this.setValuesLength(valuesLength);
		rootView = view;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.TimerTask#run()
	 */
	@Override
	public void run() {
		float elapsedTime = (updateInterval / 1000f);
		float tempScrollFactor = scrollFactor;
		float snappingValue = 1.5f;
		float distance = 0;
		synchronized (this) {
			if (spinSpeed < snappingValue && spinSpeed > -snappingValue) {
				// Increase the factor, that the Scrolling speed come to rest
				tempScrollFactor *= 5;

			} else {

				// Update Spinning Position about the elapsed Interval
				setSpinPos(getSpinPos() + getSpinSpeed() * elapsedTime);
				// Keep spinPos between the ArrayBounds
				if (spinPos >= valuesLength - 0.5f) {
					spinPos = -0.49f;
				}
				if (spinPos <= -0.5f) {
					spinPos = valuesLength - 0.51f;
				}
				// Decrease spinSpeed
				setSpinSpeed(getSpinSpeed() - getSpinSpeed() * elapsedTime
						* tempScrollFactor);
			}

			if (spinSpeed < snappingValue && spinSpeed > -snappingValue
					&& snapping) {
				// Snap to the current Value

				distance = getSpinPos() - getPosition();
				// Clamp the distance Vector,to get not to fast.
				if (distance > 0.5f) {
					distance = 0.5f;
				} else if (distance < -0.5f) {
					distance = -0.5f;
				}
				setSpinPos(getSpinPos() - distance * elapsedTime
						* tempScrollFactor);
			}
			// Calculate the Position of the current index
			setPosition(Math.round(getSpinPos()));

			// Tell the view, that it should redraw itself

			if ((getSpinSpeed() > snappingValue || getSpinSpeed() < -snappingValue)
					|| (snapping && (distance > 0.01f || distance < -0.01f))) {
				System.out.println("Post Invalidate " + distance);
				rootView.postInvalidate();
			}

		}

	}

	/**
	 * @return the spinSpeed
	 */
	float getSpinSpeed() {
		return spinSpeed;
	}

	/**
	 * @param spinSpeed
	 *            the spinSpeed to set
	 */
	void setSpinSpeed(float spinSpeed) {
		this.spinSpeed = spinSpeed;
	}

	/**
	 * @return the position
	 */
	int getPosition() {
		return position;
	}

	/**
	 * @param position
	 *            the position to set
	 */
	void setPosition(int position) {
		this.position = position;
	}

	/**
	 * @return the valuesLength
	 */
	int getValuesLength() {
		return valuesLength;
	}

	/**
	 * @param valuesLength
	 *            the valuesLength to set
	 */
	void setValuesLength(int valuesLength) {
		this.valuesLength = valuesLength;
	}

	/**
	 * @return the spinPos
	 */
	float getSpinPos() {
		return spinPos;
	}

	/**
	 * @param spinPos
	 *            the spinPos to set
	 */
	void setSpinPos(float spinPos) {
		this.spinPos = spinPos;
	}

	/**
	 * @return the scrollFactor
	 */
	float getScrollFactor() {
		return scrollFactor;
	}

	/**
	 * @param scrollFactor
	 *            the scrollFactor to set
	 */
	void setScrollFactor(float scrollFactor) {
		this.scrollFactor = scrollFactor;
	}

	/**
	 * @return the snapping
	 */
	boolean isSnapping() {
		return snapping;
	}

	/**
	 * @param snapping
	 *            the snapping to set
	 */
	void setSnapping(boolean snapping) {
		this.snapping = snapping;
	}

	public boolean onTouchEvent(MotionEvent event, float textScale) {
		int action = event.getAction();
		switch (action) {
		case MotionEvent.ACTION_MOVE:
			if (previousY == -1) {
				previousY = event.getY();
			} else {
				float spinSpeed = (previousY - event.getY()) / (textScale / 50);
				setSpinSpeed(spinSpeed);
				previousY = event.getY();

			}
			break;
		case MotionEvent.ACTION_DOWN:
			setSnapping(false);
			break;
		case MotionEvent.ACTION_UP:
			setSnapping(true);
			previousY = -1;
			break;
		}
		return true;
	}
}
