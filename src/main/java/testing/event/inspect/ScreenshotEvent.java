package testing.event.inspect;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;

import javax.imageio.ImageIO;

import org.openqa.selenium.OutputType;

import testing.AppInfoWrapper;
import testing.Env;

/**
 * This event takes a screenshot of current screen. Its overhead is approximate 0.8s on my machine.
 * This event can be used to check the side-effects of the previous events.
 * 
 * @author yifei
 */
public class ScreenshotEvent extends InspectEvent {

	@Override
	public void injectEvent(AppInfoWrapper info, Env env) {
		try {
			byte[] imgBytes = env.driver().getScreenshotAs(OutputType.BYTES);
			BufferedImage img = ImageIO.read(new ByteArrayInputStream(imgBytes));
			System.out.println("# Height: " + img.getHeight());
			System.out.println("# Width: " + img.getWidth());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	@Override
	public String toString() {
		return "[ScreenshotEvent]";
	}
}
