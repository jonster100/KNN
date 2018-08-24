import java.util.LinkedList;
import java.util.Scanner;

public class KNNAnalytics {
	private LinkedList<Cluster> clusterList;
	private Classifier stateClassifier;

	public KNNAnalytics() {
		clusterList = new LinkedList<>();
		stateClassifier = new Classifier();
		this.createCluster();
		this.addStateToCluster();
		this.printClusters();
		this.classifierSetup();
	}

	private void createCluster() {
		Scanner in = new Scanner(System.in);
		System.out.println("Cluster Name: ");
		String clustName = in.next();
		Cluster tempClust = new Cluster(clustName);
		clusterList.add(tempClust);
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
	private void addStateToCluster() {
		Scanner in = new Scanner(System.in);
		System.out.println("Cluster Name: ");
		String clustName = in.next();
		Cluster clusterToAddToo = this.findCluster(clustName);
		System.out.println("State ID: ");
		String stateId = in.next();
		State tempState = new State(stateId);
		boolean x = true;
		while (x) {
			System.out.println("SubState ID: ");
			String subStateId = in.next();
			System.out.println("SubState value: ");
			int subStateValue = in.nextInt();
			SubState tempSubState = new SubState(subStateId, subStateValue);
			if (subStateValue == 100) {
				for (Cluster c : clusterList) {
					clusterToAddToo.addState(tempState);
				}
				x = false;
			} else {
				tempState.addSubState(tempSubState);
			}
		}
	}

	private void printClusters() {
		for (Cluster c : clusterList) {
			c.printSates();
		}
	}

	private void classifierSetup() {
		stateClassifier = new Classifier();
		for(Cluster c: clusterList){
			LinkedList<SubStateList> tempList = c.getSubStateLists();
			stateClassifier.addSubStateList(tempList);
			/*for(SubStateList sS:tempList){
				stateClassifier.addSubStateList(sS);
			}*/
		}
		stateClassifier.setupOrderedStateList();
	}
}
