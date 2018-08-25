import java.util.Scanner;

public class ConsoleGUI {
	private KNNAnalytics engine;

	public ConsoleGUI(KNNAnalytics e) {
		engine = e;
		this.mainConsole();
	}

	private void mainConsole() {
		Scanner in = new Scanner(System.in);
		System.out.println("Please Choose one of the following options. ");
		System.out.println("Press r, to setup the program");
		System.out.println("Press c, to create a new Cluster.");
		System.out.println("Press s, to create a new State.");
		System.out.println("Press v, to view all the Clusters.");
		// String text = in.next();
		boolean x = true;
		while (x) {
			String text = in.next();
			if (text.equals("q")) {
				x = false;
			} else if(text.equals("c")){
				this.createCluster();
			}else if (text.equals("s")){
				this.createState();
			} else if(text.equals("v")){
				this.viewClusters();
			}
			else if(text.equals("a")) {
				
			} else if(text.equals("r")){
				engine.classifierSetup();
			}
		}
	}

	private void createCluster() {
		Scanner in = new Scanner(System.in);
		System.out.println("Please enter the name of the Cluster: ");
		String clustName = in.next();
		engine.createCluster(clustName);
		System.out.println("<---");
	}

	private void createState() {
		Scanner in = new Scanner(System.in);
		System.out.println("Please enter the ID/Name of the State: ");
		String stateId = in.next();
		System.out.println("Please enter the name of the Cluster you would like to add the state to, or type 1 if the state is to be classified: ");
		String clusterName = in.next();
		System.out.println("The state requires SubStates to be created.");
		System.out.println("The state requires SubStates to be created.");
		System.out.println("Please enter the number of Substates you would like to create: ");
		int noSubStates = in.nextInt();
		String[][] tempSubStateData = this.createSubStates(noSubStates);
		if(engine.createState(stateId, clusterName, tempSubStateData,noSubStates,(clusterName.equals("1"))?true:false)){
			System.out.println("State Created");
		} else {
			System.out.println("State could not be created.");
		} 
		
	}
	
	private String[][] createSubStates(int noSubStates){
		Scanner in = new Scanner(System.in);
		String[][] subStateData=new String[noSubStates][2];
		for(int i=0;i<noSubStates;i++){
			System.out.println("State - "+ i);
			System.out.println("Please Enter SubStateId: ");
			String subStateId = in.next();
			subStateData[i][0]=subStateId;
			System.out.println("Please Enter the SubState value: ");
			String subStateVlaue = in.next();
			subStateData[i][1]=subStateVlaue;
		}
		return subStateData;
	}

	private void viewClusters() {
		engine.printClusters();
	}
}
