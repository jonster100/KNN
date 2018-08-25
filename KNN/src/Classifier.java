import java.util.Comparator;
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
		State[] tempArray1 = new State[100];
		State[] tempArray2 = orderedList.toArray(tempArray1);
		return tempArray2;
	}

	public String startClassifier(State s) {
		String toBeReturned = "";
		LinkedList<SubState> newStateList = s.getAllSubStates();
		for (int i = 0; i < newStateList.size(); i++) {
			SubStateList tempList = this.findSubStateList(newStateList.get(i).getSubStateName());
			tempList.addSubStateToList(s);
			State[] orderedStates = this.returnOrderedStateList(tempList.getSubStatesList());
			for (int x = 0; x < orderedStates.length; x++) {
				if (orderedStates[x].getClusterOrigin().equals(s.getClusterOrigin())
						&& orderedStates[x].getStateId().equals(s.getStateId())) {
					State targetState = orderedStates[x];
					int noNeighbors = 6;
					State[] nearestStates = new State[noNeighbors];
					for (int y = 0; y < noNeighbors; y++) {
						try {
							nearestStates[y] = orderedStates[x - (noNeighbors / 2)];
						} catch (ArrayIndexOutOfBoundsException e) {
							System.out.println("Array out of bounds trying to find neaest neighbor.");
						}
					}
				}
			}
		}
		return toBeReturned;
	}

	private String clarifyNearestNeighbor(State[] neighbors) {
		String toBeReturned = "";
		//need to finsish this function to tally the states, to find the correct substate
		return toBeReturned;
	}

}
