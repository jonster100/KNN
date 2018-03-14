import java.util.Comparator;
import java.util.PriorityQueue;

public class SubStateList {
	private String subStateTypeName;
	private PriorityQueue<State> subStatesList;

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
			SubState x = tempX.getSubType(subStateTypeName);
			SubState y = tempY.getSubType(subStateTypeName);

			return (x.getSubStateValue() < y.getSubStateValue()) ? -1
					: (x.getSubStateValue() > y.getSubStateValue()) ? 1 : 0;
		}
	}

	public SubStateList(String tempName) {
		subStateTypeName = tempName;
		subStatesList = new PriorityQueue<State>(125, new subStateCompareter());
	}

	public void addSubStateToList(State sS) {
		subStatesList.offer(sS);
	}
	
	public String getSubStateListName(){
		return subStateTypeName;
	}
}
