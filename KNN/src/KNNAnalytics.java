import java.util.LinkedList;
import java.util.Scanner;

public class KNNAnalytics {
	private LinkedList<Cluster> clusterList;
	private Classifier stateClassifier;

	public KNNAnalytics() {
		clusterList = new LinkedList<>();
		stateClassifier = new Classifier();
		/*
		 * this.createCluster(); this.addStateToCluster(); this.printClusters();
		 * this.classifierSetup();
		 */
	}

	// custom creation
	public void createCluster(String clusterName) {
		Cluster tempClust = new Cluster(clusterName);
		clusterList.add(tempClust);
	}

	// custom creation
	public boolean createState(String stateId, String clustName, String[][] subStateData, int noSubStates,
			boolean classify) {
		Cluster tempClust = this.findCluster(clustName);
		boolean toBeReturned = true;// turns to false if tempCLust is null
		if (tempClust == null) {
			toBeReturned = false;
			System.out.println("!- "+clustName+" Cluster Does not exist.");
		} else {
			State tempState = (clustName.equals("1"))?new State(stateId):new State(stateId,clustName);//changes the type of state name, so that
			for (int i = 0; i < noSubStates; i++) {
				try {
					this.createSubStates(subStateData[i][0], Integer.parseInt(subStateData[i][1]));
				} catch (NumberFormatException e) {
					System.out.println("!-SubState " + subStateData[i][0] + " is not an integer.");
					toBeReturned = false;
				}
			}
			if (classify == true) {
				this.classifyState(tempState);
			} else {
				tempClust.addState(tempState);
			}
		}
		return toBeReturned;
	}

	private SubState createSubStates(String sSId, int value) {
		SubState tempSs = new SubState(sSId, value);
		return tempSs;
	}

	private Cluster findCluster(String clustName) {
		Cluster toBeReturned = null;
		for (Cluster c : clusterList) {
			if (c.getClusterId().equals(clustName)) {
				toBeReturned = c;
			}
		}
		return toBeReturned;
	}

	// test
	/*
	 * private void addStateToCluster() { Scanner in = new Scanner(System.in);
	 * System.out.println("Cluster Name: "); String clustName = in.next();
	 * Cluster clusterToAddToo = this.findCluster(clustName);
	 * System.out.println("State ID: "); String stateId = in.next(); State
	 * tempState = new State(stateId); boolean x = true; while (x) {
	 * System.out.println("SubState ID: "); String subStateId = in.next();
	 * System.out.println("SubState value: "); int subStateValue = in.nextInt();
	 * SubState tempSubState = new SubState(subStateId, subStateValue); if
	 * (subStateValue == 100) { for (Cluster c : clusterList) {
	 * clusterToAddToo.addState(tempState); } x = false; } else {
	 * tempState.addSubState(tempSubState); } } }
	 */

	public void printClusters() {
		for (Cluster c : clusterList) {
			c.printSates();
		}
	}

	private void classifierSetup() {
		stateClassifier = new Classifier();
		for (Cluster c : clusterList) {
			LinkedList<SubStateList> tempList = c.getSubStateLists();
			stateClassifier.addSubStateList(tempList);
			/*
			 * for(SubStateList sS:tempList){
			 * stateClassifier.addSubStateList(sS); }
			 */
		}
		//stateClassifier.setupOrderedStateList();
	}

	private void classifyState(State tempState) {
		stateClassifier.startClassifier(tempState);
	}
}
