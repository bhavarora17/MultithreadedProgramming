package framework;

import framework.resource.Machine;
import framework.resource.Machines;
import framework.resource.Table;
import framework.resource.Tables;

public class Cook implements Runnable {

	private int id;		// id of the cook
	private Table tableServing;
	private Order order;
	private Thread th;
	public int timeBurgerMacihineWasUsed;
	public int timeFriesMachineWasUsed;
	public int timeSodaMachineWasUsed;
	
	public Cook(int i) {
		id = i;
		th = new Thread(this, "Cook-"+id);
		th.start();
	}
	
	public int getId() {
		return id;
	}

	public void run() {
		while(Clock.getInstance().getTime() <= 120 || Diners.getInstance().getNumCurrentDiners() > 0) {
				tableServing = Tables.getInstance().getTableForCook();
				if(tableServing != null) {
					this.timeFriesMachineWasUsed = -1;
					this.timeSodaMachineWasUsed = -1;
					tableServing.assignCook(this);
					tableServing.waitOnOrder();
					order = tableServing.getOrder();
					Machines machines = Machines.getInstance();
					while(!order.isComplete()) {
						Machine machine = machines.getMachine(order);
						machine.cook(order, this);
					}
					tableServing.timeBurgerMacihineWasUsed = this.timeBurgerMacihineWasUsed;
					tableServing.timeFriesMachineWasUsed = this.timeFriesMachineWasUsed;
					tableServing.timeSodaMachineWasUsed = this.timeSodaMachineWasUsed;
					tableServing.serveFood();
				}
		}
	}
}
