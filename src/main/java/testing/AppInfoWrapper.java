package testing;

import java.util.List;

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
		ManifestParser parser = new ManifestParser(appPath);
		pkgName = parser.getPackageName();
		activityNames = parser.getActivityNames();
		assert ! activityNames.isEmpty();
	}
	
	public String getAppPath() {
		return appPath;
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
	private String pkgName;
	private List<String> activityNames;
}
