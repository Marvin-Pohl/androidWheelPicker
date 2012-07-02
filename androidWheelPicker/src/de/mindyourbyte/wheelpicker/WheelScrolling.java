/**
 * 
 */
package de.mindyourbyte.wheelpicker;

import java.util.Timer;
import java.util.TimerTask;

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
	private float scrollFactor = 1;
	Timer timer;
	final long updateInterval = 16;
	View rootView;

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
		float snappingValue = 1f;
		synchronized (this) {
			if (spinSpeed < snappingValue && spinSpeed > -snappingValue) {
				// Increase the factor, that the Scrolling speed come to rest
				tempScrollFactor *= 5;

			}
			// Update Spinning Position about the elapsed Interval
			setSpinPos(getSpinPos() + getSpinSpeed() * elapsedTime);
			// Keep spinPos between the ArrayBounds
			if (spinPos > valuesLength) {
				// spinPos = 0;
			}
			if (spinPos <= 0) {
				spinPos = valuesLength;
			}
			// Decrease spinSpeed
			setSpinSpeed(getSpinSpeed() - getSpinSpeed() * elapsedTime
					* tempScrollFactor);

			// Calculate the Position of the current index
			// TODO fix broken State between Last and First Value
			setPosition(Math.round(getSpinPos()));

			if (spinSpeed < snappingValue && spinSpeed > -snappingValue
					&& snapping) {
				// Snap to the current Value

				float distance = getSpinPos() - getPosition();
				if (distance > 0.5f) {
					distance = 0.5f;
				} else if (distance < -0.5f) {
					distance = -0.5f;
				}
				setSpinPos(getSpinPos() - distance * elapsedTime
						* tempScrollFactor);
			}
			// Tell the view, that it should redraw itself
			rootView.postInvalidate();

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
}
