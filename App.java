package project;

public class App {

	public static void main(String[] args) {
		String policy=args.length>0 ? args[0] :"RR";
		int cores = args.length >1 ? Integer.parseInt(args[1]):4;
		int quantum =args.length>2 ? Integer.parseInt(args[2]):2;
		int balanceEv=args.length >3 ?Integer.parseInt(args[3]):3;
		int tasks =args.length >4 ? Integer.parseInt(args[4]):16;
		
		SimulationEngine engine=new SimulationEngine(cores,policy,quantum,balanceEv);
		engine.seedInitialTasks(tasks, 2, 8, 1, 9);
		
		javax.swing.SwingUtilities.invokeLater(()->{
			new SimulatorFrame(engine).setVisible(true);
		});

	}

}
