
package application.share;

public class DictInfo {
	public static class Info {
		public final String name;
		
		public Info(String name){
			this.name=name;
		}
	}
	
	public static Info[] info = new Info[]{
		new Info("百度"),
		new Info("有道"),
		new Info("金山")
	};
}

