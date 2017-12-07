package testing;

import java.io.File;
import java.util.Deque;
import java.util.LinkedList;

import org.openqa.selenium.Dimension;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import testing.event.Event;
import util.Config;

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
		eventTrace = new LinkedList<>();
		activityTrance = new LinkedList<>();
		layoutTrace = new LinkedList<>();
		initOutputDirectory();
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

	public File getOutputDirectory() {
		return outputDirectory;
	}

	/**
	 * Obtain the last event;
	 * append current event into the event trace;
	 * get the event trace.
	 */
	public Event getLastEvent() {
		return eventTrace.peekLast();
	}

	public void appendEvent(Event event) {
		eventTrace.addLast(event);
	}

	public Deque<Event> getEventTrace() {
		return eventTrace;
	}

	/**
	 * Obtain last activity;
	 * append current activity to the activity trace;
	 * get the activity transition trace.
	 */
	public String getFirstActivity() {
		return activityTrance.peekFirst();
	}

	public String getLastActivity() {
		return activityTrance.peekLast();
	}

	public void appendActivity(String activity) {
		String lastAct = activityTrance.peekLast();
		if(lastAct == null || ! activity.equals(lastAct))
			activityTrance.addLast(activity);
	}

	public Deque<String> getActivityTrace() {
		return activityTrance;
	}

	/**
	 * Obtain last layout;
	 * append current layout to the layout trace;
	 * obtain the layout trace.
	 */
	public Layout getLastLayout() {
		return layoutTrace.peekLast();
	}

	public void appendLayout(Layout layout) {
		layoutTrace.addLast(layout);
	}

	public Deque<Layout> getLayoutTrace() {
		return layoutTrace;
	}

	// Initialize output directory
	private void initOutputDirectory() {
		AppInfoWrapper appInfo = new AppInfoWrapper(TestingOptions.v().getAppPaths().get(0));
		String appName = appInfo.getAppFileName().split(".apk")[0];
		assert ! appName.isEmpty();
		String output = Config.v().get(Config.OUTPUT);
		String outputDirName = String.join(File.separator, output, appName);
		File outputDir = new File(outputDirName);
		if(outputDir.exists() && outputDir.isDirectory()) {
			// Remove the directory tree
			System.out.println("# Remove old output directory. ");
			for(String s : outputDir.list()) {
				File f = new File(outputDir.getPath(), s);
				f.delete();
			}
			outputDir.delete();
		} 
		outputDir.mkdirs();
		outputDirectory = outputDir;
	}

	// Testing driver
	private AndroidDriver<AndroidElement> driver;
	// The dimension of the screen
	private Dimension dimension;
	private int width;
	private int height;
	//
	private File outputDirectory;
	// Testing event traces
	private Deque<Event> eventTrace;
	// The activity transitions during testing
	private Deque<String> activityTrance;
	// Layout trace
	private Deque<Layout> layoutTrace;
}
