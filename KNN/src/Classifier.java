import java.util.LinkedList;

public class Classifier {
	private LinkedList<SubStateList> hierSubStateLists;

	public Classifier() {
		hierSubStateLists = new LinkedList<>();
	}
	
	public void addSubStateList(LinkedList<SubStateList> clusterSubStateList) {
		for (SubStateList sS : clusterSubStateList) {
			SubStateList hierList = this.getList(sS.getSubStateListName());
			if (hierList==null) {
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
}
