package testing;

import org.openqa.selenium.Dimension;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import testing.event.Event;

/**
 * This class represents the testing environments.
 * 
 * @author yifei
 */
public class Env {
	public Env(AndroidDriver<AndroidElement> driver) {
		this.driver = driver;
		this.dimension =  driver.manage().window().getSize();
		this.width = dimension.getWidth();
		this.height = dimension.getHeight();
	}
	
	public AndroidDriver<AndroidElement> driver() {
		return driver;
	}
	
	public Dimension dimension() {
		return dimension;
	}
	
	public int width() {
		return width;
	}
	
	public int height() {
		return height;
	}
	
	
	public Event getPrevEvent() {
		return prevEvent;
	}

	public void setPrevEvent(Event prevEvent) {
		this.prevEvent = prevEvent;
	}

	// Testing driver
	private AndroidDriver<AndroidElement> driver;
	// The dimension of the screen
	private Dimension dimension;
	private int width;
	private int height;
	// Previous event that has been successfully injected
	private Event prevEvent;
}
