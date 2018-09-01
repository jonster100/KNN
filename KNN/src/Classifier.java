import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.LinkedList;
import java.util.Comparator;
import java.util.PriorityQueue;

public class Classifier {
	private LinkedList<SubStateList> hierSubStateLists;
	private PriorityQueue<State> orderedStateList;

	/**
	 * This class is used to order the nodes that get put into the Priority
	 * queue "characterList", which uses the Comparator class and overriding a
	 * compare method.
	 **/
	public class subStateCompareter implements Comparator<State> {
		/**
		 * @param x,y
		 *            Identifiers for the 'SubState' objects to be passed
		 *            through the method
		 * @return int Returns the output of a comparison between two 'SubState'
		 *         objects e.g -1,0,1
		 **/
		@Override
		public int compare(State tempX, State tempY) {
			int x = tempX.getWeight();
			int y = tempY.getWeight();

			return (x < y) ? -1 : (x > y) ? 1 : 0;
		}
	}

	/**
	 * This class will be used to add new states into a pre-existing clusters.
	 */
	public Classifier() {
		hierSubStateLists = new LinkedList<>();
		orderedStateList = new PriorityQueue<State>(125, new subStateCompareter());
	}

	/**
	 * This function will add the subStateLists to another list, so that all the
	 * states in the "subStateList"'s can be taken out and ordered in another
	 * list. Making it easier to classify incoming States into a cluster.
	 */
	public void addSubStateList(LinkedList<SubStateList> clusterSubStateList) {
		for (SubStateList sS : clusterSubStateList) {
			SubStateList hierList = this.getList(sS.getSubStateListName());
			if (hierList == null) {
				this.createSubStateList(sS.getSubStateListName(), sS);
				this.mergeHierList(sS.getSubStateListName(), sS);
			} else {
				this.mergeHierList(sS.getSubStateListName(), sS);
			}
		}
		this.setupOrderedStateList();
	}

	private void mergeHierList(String listName, SubStateList subStateList) {
		SubStateList hierList = this.getList(listName);
		for (State s : subStateList.getSubStatesList()) {
			hierList.addSubStateToList(s);
		}
	}

	private void createSubStateList(String listName, SubStateList subStateList) {
		SubStateList newHierList = new SubStateList(listName);
		hierSubStateLists.add(newHierList);
	}

	private SubStateList getList(String listTypeName) {
		SubStateList toBeReturned = null;
		for (SubStateList sS : hierSubStateLists) {
			if (sS.getSubStateListName().equals(listTypeName)) {
				toBeReturned = sS;
			}
		}
		return toBeReturned;
	}

	private void setupOrderedStateList() {
		for (SubStateList sSL : hierSubStateLists) {
			for (State sS : sSL.getSubStatesList()) {
				orderedStateList.offer(sS);
			}
		}
	}

	private SubStateList findSubStateList(String subStateTypeName) {
		SubStateList returnSubStateList = null;
		for (SubStateList ssL : hierSubStateLists) {
			if (ssL.getSubStateListName().equals(subStateTypeName)) {
				returnSubStateList = ssL;
			}
		}
		return returnSubStateList;
	}

	private State[] returnOrderedStateList(PriorityQueue<State> orderedList) {
		State[] tempArray1 = new State[orderedList.size()];
		State[] tempArray2 = orderedList.toArray(tempArray1);
		return tempArray2;
	}

	private State[] getNearestStates(State[] orderedStates, int targetPosition) {
		State[] toBeReturned = null;
		int noNeighbors = 4;
		boolean leftRightOutOfBounds = false;
		boolean outOfBounds = false;
		boolean run = true;
		while (run) {
			boolean tempOutOfBounds = false;
			State[] nearestStates = (orderedStates.length <= noNeighbors) ? orderedStates : new State[noNeighbors];
			if (orderedStates.length > noNeighbors) {
				for (int y = 0; y < noNeighbors; y++) {
					try {
						nearestStates[y] = orderedStates[(outOfBounds == false) ? targetPosition - (noNeighbors/2)
								: (leftRightOutOfBounds == true) ? y : orderedStates.length - y];
					} catch (ArrayIndexOutOfBoundsException e) {
						if (y == 0) {
							leftRightOutOfBounds = true;
						}
						outOfBounds = true;
						tempOutOfBounds=true;
						System.out.println("Array out of bounds trying to find nearest neighbor.");
						break;
					}
				}
			}
			if (tempOutOfBounds == false) {
				toBeReturned = nearestStates;
				run=false;
			}
		}

		return toBeReturned;
	}

	public String startClassifier(State s) {
		LinkedList<SubState> newStateList = s.getAllSubStates();
		String[] closestClusters = new String[newStateList.size()];
		for (int i = 0; i < newStateList.size(); i++) {
			SubStateList tempList = this.findSubStateList(newStateList.get(i).getSubStateName());
			if (tempList != null) {
				tempList.addSubStateToList(s);
				State[] orderedStates = this.returnOrderedStateList(tempList.getSubStatesList());
				for (int x = 0; x < orderedStates.length; x++) {
					if (orderedStates[x].getClusterOrigin().equals(s.getClusterOrigin())
							&& orderedStates[x].getStateId().equals(s.getStateId())) {
						// State targetState = orderedStates[x];
						closestClusters[i] = this.clarifyNearestNeighbors(this.getNearestStates(orderedStates, x));
					}
				}
			}
		}
		return this.returnClosestCluster(closestClusters);
	}

	/*
	 * private State[] mergeArrayList(State[] merge1,State[] merge2,int
	 * sizeOfNewArray){ State [] newList=new State[sizeOfNewArray]; for(){}
	 * return newList; }
	 */

	private String returnClosestCluster(String[] listClustNames) {
		String cluster = "";
		int noClustStates = 0;
		Map<String, Integer> clusterNameTally = new HashMap<String, Integer>();
		for (int i = 0; i < listClustNames.length; i++) {
			String clusterName = listClustNames[i];
			if (clusterName.equals(clusterNameTally.containsKey(clusterName))) {
				int tempClustTally = clusterNameTally.get(clusterName) + 1;
				clusterNameTally.put(clusterName, tempClustTally);
			} else {
				clusterNameTally.put(clusterName, 1);
			}
		}
		for (String s : clusterNameTally.keySet()) {
			if (clusterNameTally.get(s) > noClustStates) {
				cluster = s;
				noClustStates = clusterNameTally.get(s);
			}
		}
		return cluster;
	}

	private String clarifyNearestNeighbors(State[] neighbors) {
		String cluster = "";
		int noClustStates = 0;
		Map<String, Integer> clusterStateTally = new HashMap<String, Integer>();
		for (int i = 0; i < neighbors.length; i++) {
			String clusterName = neighbors[i].getClusterOrigin();
			if (clusterName.equals("none")) {
				// is meant to be empty
			} else {
				if (clusterStateTally.containsKey(clusterName)) {
					int tempClustTally = clusterStateTally.get(clusterName) + 1;
					clusterStateTally.put(clusterName, tempClustTally);
				} else {
					clusterStateTally.put(clusterName, 1);
				}
			}
		}
		for (String s : clusterStateTally.keySet()) {
			if (clusterStateTally.get(s) > noClustStates) {
				cluster = s;
				noClustStates = clusterStateTally.get(s);
			}
		}
		return cluster;
	}

}
