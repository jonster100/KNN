import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.LinkedList;
import java.util.Scanner;

public class KNNAnalytics {
	private LinkedList<Cluster> clusterList;
	private Classifier stateClassifier;

	public KNNAnalytics() {
		clusterList = new LinkedList<>();
		this.addDataToLookup();
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
		if (tempClust == null && classify == false) {
			toBeReturned = false;
			System.out.println("!- " + clustName + " Cluster Does not exist.");
		} else {
			State tempState = (clustName.equals("1")) ? new State(stateId) : new State(stateId, clustName);
			for (int i = 0; i < noSubStates; i++) {
				try {
					tempState.addSubState(
							this.createSubStates(subStateData[i][0], Integer.parseInt(subStateData[i][1])));
				} catch (NumberFormatException e) {
					System.out.println("!-SubState " + subStateData[i][0] + " is not an integer.");
					toBeReturned = false;
				}
			}
			if (classify == true) {
				if (stateClassifier == null) {
					this.classifierSetup();
				}
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

	public void classifierSetup() {
		stateClassifier = new Classifier();
		for (Cluster c : clusterList) {
			LinkedList<SubStateList> tempList = c.getSubStateLists();
			stateClassifier.addSubStateList(tempList);
			/*
			 * for(SubStateList sS:tempList){
			 * stateClassifier.addSubStateList(sS); }
			 */
		}
		// stateClassifier.setupOrderedStateList();
	}

	private void classifyState(State tempState) {
		String clusterName = stateClassifier.startClassifier(tempState);
		System.out.println("classifier clust name: " + clusterName);
		this.findCluster(clusterName).addState(tempState);
		tempState.setClusterOrigin(clusterName);
	}

	public void addDataToLookup() {
		try {
			String fileName = "lookupdata.txt";
			Scanner infile = new Scanner(new FileReader(fileName));
			infile.useDelimiter(":|\r?\n|\r");
			int noDataEntries = infile.nextInt();
			for (int i = 0; i < noDataEntries; i++) {
				int ifClust = infile.nextInt();
				if (ifClust == 1) {
					String clustName = infile.next();
					this.createCluster(clustName);
				} else if (ifClust == 2) {
					String stateId = infile.next();
					String clustName = infile.next();
					String[][] tempData = new String[2][2];
					tempData[0][0] = infile.next();
					tempData[0][1] = infile.next();
					tempData[1][0] = infile.next();
					tempData[1][1] = infile.next();
					this.createState(stateId, clustName, tempData, 2, false);
				} else if (ifClust == 3) {
					String stateId = infile.next();
					String clustName = infile.next();
					int noSubStateData = infile.nextInt();
					String[][] tempData = new String[noSubStateData][2];
					for (int x = 0; x < noSubStateData; x++) {
						tempData[x][0] = infile.next();
						tempData[x][1] = infile.next();
					}
					this.createState(stateId, clustName, tempData, 2, true);
				}
			}
			System.out.println("Data Loaded"); // testing if the data was loaded
			infile.close();
		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage()); // if the files was not found
												// print message
		}
	}

}
