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
	
	public void setupOrderedStateList(){
		for(SubStateList sSL: hierSubStateLists){
			for(State sS:sSL.getSubStatesList()){
				orderedStateList.offer(sS);
			}
		}
	}
}
