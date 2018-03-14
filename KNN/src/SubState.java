
public class SubState {
	private String subStateName;
	private int subStateValue;

	public SubState(String tempName,int tempValue) {
		subStateName = tempName;
		subStateValue = tempValue;
	}

	public String getSubStateName() {
		return subStateName;
	}

	public int getSubStateValue() {
		return subStateValue;
	}
	
	public void printInfo(){
		System.out.println("SubState Name: "+subStateName+" - "+"Value: "+subStateValue);
	}
}
