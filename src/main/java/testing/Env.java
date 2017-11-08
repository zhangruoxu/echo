package testing;

import org.openqa.selenium.Dimension;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;

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
	
	private AndroidDriver<AndroidElement> driver;
	private Dimension dimension;
	private int width;
	private int height;
}
