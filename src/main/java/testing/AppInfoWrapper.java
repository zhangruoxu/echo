package testing;

import java.io.File;
import java.util.List;
import java.util.regex.Pattern;

import util.AppPathResolver;
import util.Config;
import util.ManifestParser;

/**
 * This class includes info of an app
 * 
 * @author yifei
 */
public class AppInfoWrapper {
	public AppInfoWrapper(int id) {
		appPath = AppPathResolver.resolveAppPath(Config.APPDIR, id);
		init();
	}
	
	public AppInfoWrapper(String appPath) {
		this.appPath = appPath;
		init();
	}
	
	private void init() {
		String[] temp = appPath.split(Pattern.quote(File.separator));
		assert temp.length > 0;
		appFileName = temp[temp.length - 1];
		ManifestParser parser = new ManifestParser(appPath);
		pkgName = parser.getPackageName();
		activityNames = parser.getActivityNames();
		assert ! activityNames.isEmpty();
	}
	
	public String getAppPath() {
		return appPath;
	}
	
	public String getAppFileName() {
		return appFileName;
	}
	
	public String getPkgName() {
		return pkgName;
	}
	
	public List<String> getActivityNames() {
		return activityNames;
	}
	
	public boolean contains(String activityName) {
		return activityNames.contains(activityName);
	}
	
	public boolean equals(Object o) {
		if(this == o)
			return true;
		if(o instanceof AppInfoWrapper) {
			AppInfoWrapper another = (AppInfoWrapper) o;
			return this.appPath.equals(another.appPath);
		}
		return false;			
	}
	
	public int hashCode() {
		return appPath.hashCode();
	}
	
	private String appPath;
	private String appFileName;
	private String pkgName;
	private List<String> activityNames;
}
