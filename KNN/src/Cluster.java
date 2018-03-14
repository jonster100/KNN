import java.util.LinkedList;

public class Cluster {
	private String clusterName;
	private LinkedList<State> stateList;
	private LinkedList<SubStateList> clusterSubStateList;

	public Cluster(String tempName) {
		clusterName = tempName;
		stateList = new LinkedList<>();
		clusterSubStateList = new LinkedList<>();
	}

	public void addState(State tempState) {
		stateList.add(tempState);
		LinkedList<SubState> tempList = tempState.getAllSubStates();

		for (SubState sS : tempList) {
			SubStateList tempSubStateList = this.getSubStateList(sS.getSubStateName());
			if (tempSubStateList != null) {
				tempSubStateList.addSubStateToList(tempState);
			} else {
				String tempSubStateName = sS.getSubStateName();
				this.createSubStateList(tempSubStateName);
				this.getSubStateList(tempSubStateName).addSubStateToList(tempState);
				;
			}
		}
	}

	public String getClusterId() {
		return clusterName;
	}

	public void printSates() {
		System.out.println("Cluster Name: " + clusterName);
		for (State s : stateList) {
			s.printInfo();
		}
	}

	public void createSubStateList(String tempName) {
		SubStateList list = new SubStateList(tempName);
		clusterSubStateList.add(list);
	}

	private SubStateList getSubStateList(String tempName) {
		SubStateList list = null;
		for (SubStateList s : clusterSubStateList) {
			if (s.getSubStateListName().equals(tempName)) {
				list = s;
			}
		}
		return list;
	}
}
