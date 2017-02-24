/**
 * 
 */
package com.jackpan.TaiwanpetadoptionApp;

/**
 * @author Hyxen-Arthur 2015/5/13
 *
 */
public class ResponObj {
	
	public Result result = new Result();

	public static class Result {
		public int DriverState;
		public int StateCode = -1;
		public String StateMessage;
		public StateObject StateObject;
	}
	
	public boolean isSuccess() {
		if(this.result == null) return false;
		if(this.result.StateCode == 0) return true;
		return false;
	}
	
	public String getStateMessage() {
		if(this.result == null) return "目前伺服器沒有回應...";
		return this.result.StateMessage;
	}
	
	public static class StateObject {
		public String TaskID ;
		public String TaskState ;
	}

	public StateObject getStateObject() {
		return this.result.StateObject;
	}
}
