package project;

import java.util.ArrayDeque;
import java.util.Deque;

public class PriorityScheduler implements SchedulerPolicy{

	private final Deque<task> queue=new ArrayDeque<>();
	
	
	public void offer(task t,int nowTick) {
		queue.addLast(t);
	}
	
	public task selectNext(int nowTick) {
		if(queue.isEmpty()) {
			return null;
		}
		task best=null;
		for(task t: queue) {
			if(best==null||t.getPriority()<best.getPriority())
				best=t;
		}
		
		queue.remove(best);
		return best;
	}
	
	public boolean isIdle() {
		return queue.isEmpty();
	}
	
	public int queueSize() {
		return queue.size();
	}
	
	public int timeQuantum() {
		return 1;
	}
	
	public Deque<task>queueRef(){
		return queue;
	}
}
