package project;

import java.util.Deque;

import java.util.*;

 
public class CPUCore {
	
	private final int id;
	private final SchedulerPolicy scheduler;
	private task currentTask=null;
	private int sliceRemaining=0;
	public long busyTicks=0;
	
	
	public CPUCore(int id, SchedulerPolicy scheduler) {
		this.id=id;
		this.scheduler=scheduler;
	}
	
	public void enqueue(task t, int nowTick) {
		scheduler.offer(t, nowTick);
	}
	
	public String tick(int nowTick, Metrics metrics) {
		StringBuilder log=new StringBuilder();
	
	
		if(currentTask==null) {
			currentTask=scheduler.selectNext(nowTick);
			if(currentTask!=null) {
				if(currentTask.getStartTime()==null) {
					metrics.onTaskStart(currentTask,nowTick);
				}
				sliceRemaining=scheduler.timeQuantum();
			}
		}
		
		if(currentTask!=null) {
			currentTask.workOneTick();
			busyTicks++;
			sliceRemaining--;
			log.append("Core").append(id).append("->").append(currentTask.getId());
		
		
			if(currentTask.isFinished()) {
				metrics.onTaskFinish(currentTask,nowTick+1);
				currentTask=null;
			}
			
			else if(sliceRemaining==0) {
				scheduler.offer(currentTask, nowTick+1);
				currentTask=null;
			}
		}
		
		else {
			log.append("Core").append(id).append("idle");
		}
		return log.toString();
	}
	
	public int queueSize() {
		return scheduler.queueSize();
	}
	
	public int getId() {
		return id;
	}
	
	
	public int stealHalfTo(CPUCore target, int nowTick) {
		int moved=0;
		int total=scheduler.queueSize();
		int toMove=total/2;
		
		Deque<task>qref=scheduler.queueRef();
		while(moved < toMove && !qref.isEmpty()) {
			task t=qref.pollLast();
			if(t==null) {
				break;
			}
			
			target.enqueue(t,nowTick);
			moved++;
		}
		return moved;
	}
	
	public boolean isIdle() {
		return scheduler.isIdle()&&currentTask==null;
	}
	
	public SchedulerPolicy getScheduler() {
		return scheduler;
	}
}
