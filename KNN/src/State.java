import java.util.LinkedList;

public class State {
	private String clusterOrigin;
	private String stateId;
	private int weight; 
	private LinkedList<SubState> subStateList;

	public State(String tempId) {
		subStateList = new LinkedList<>();
		stateId = tempId;
		clusterOrigin = "none";
	}
	
	public State(String tempId,String clustOrig) {
		subStateList = new LinkedList<>();
		stateId = tempId;
		clusterOrigin = clustOrig;
	}

	public void addSubState(SubState sS) {
		subStateList.add(sS);
	}

	public LinkedList<SubState> getAllSubStates() {
		return subStateList;
	}

	public SubState getSubType(String tempSubStateId) {
		SubState toBeReturned = null;
		for (SubState sS : subStateList) {
			if (sS.getSubStateName().equals(tempSubStateId)) {
				toBeReturned = sS;
			}
		}
		return toBeReturned;
	}

	public void printInfo() {
		System.out.println("State Name: " + stateId);
		for (SubState sS : subStateList) {
			sS.printInfo();
		}
	}

	public void setClusterOrigin(String tempName) {
		clusterOrigin = tempName;
	}

	public String getClusterOrigin() {
		return clusterOrigin;
	}
	
	public void setWeight(){
		//testing the classifier
		for(SubState sS:subStateList){
			weight+=sS.getSubStateValue();
		}
	}
	public int getWeight(){
		return weight; 
	}
	
	public String getStateId(){
		return stateId;
	}
}
