package project;
import java.util.ArrayDeque;
import java.util.Deque;

public class RoundRobinScheduler implements SchedulerPolicy{
	private final int quantum;
	private final Deque<task>readyQueue=new ArrayDeque<>();
	
	public RoundRobinScheduler(int quantum) {
		this.quantum=quantum;
	}
	
	public void offer(task t, int nowTick) {
		readyQueue.addLast(t);
	}
	
	public task selectNext(int nowTick) {
		return readyQueue.pollFirst();
	}
	
	public boolean isIdle() {
		return readyQueue.isEmpty();
	}
	
	public int queueSize() {
		return readyQueue.size();
	}
	
	public int timeQuantum() {
		return quantum;
	}
	
	public Deque<task>queueRef(){
		return readyQueue;
	}
	
	
	
	
}
