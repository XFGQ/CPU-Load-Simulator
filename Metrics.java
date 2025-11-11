package project;

import java.util.*;
public class Metrics {

	private final Map<String, Integer> start=new HashMap<>();
	private final Map<String, Integer> finish=new HashMap<>();
	private final List<Integer> queueSpreadSamples=new ArrayList<>();
	
	
	private long totalTicks=0;
	
	public void onTaskStart(task t, int now) {
		if(!start.containsKey(t.getId())) {
			start.put(t.getId(), now);
			t.setStartTime(now);
		}
	}
	
	public void onTaskFinish(task t,int now) {
		finish.put(t.getId(), now);
		t.setFinishTime(now);
	}
	
	public void sampleQueues(java.util.List<CPUCore>cores) {
		int max=0, min=Integer.MAX_VALUE;
		
		for(CPUCore c:cores) {
			int q=c.queueSize();
			max=Math.max(max,q);
			min=Math.min(min, q);
		}
		
		if(min==Integer.MAX_VALUE)min=0;
		queueSpreadSamples.add(max-min);
		totalTicks++;
	}
	
	
	public String report(java.util.List<task> allTasks, java.util.List<CPUCore> cores) {
		double avgWaiting=0.0 , avgTurnaround=0.0;
		int done=0;
		for(task t:allTasks) {
			Integer st=t.getStartTime();
			Integer fin=t.getFinishTime();
			
			if(st!=null && fin!=null) {
				avgWaiting+=(st-t.getArrivalTime());
				avgTurnaround+= (fin-t.getArrivalTime());
				done++;
			}
		}
		if(done>0) {
			avgWaiting/=done;
			avgTurnaround/=done;
		}
		
		StringBuilder sb=new StringBuilder();
		sb.append("---REPORT---\n");
		
		for(CPUCore c: cores) {
			double util = totalTicks == 0?0:(100.0*c.busyTicks/totalTicks);
			sb.append(String.format(java.util.Locale.US, "Core %d util: %.1f%% (queue=%d)\n",
			c.getId(),util,c.queueSize()));
		}
		
		double balanceIndex = queueSpreadSamples.stream().mapToInt(i->i).average().orElse(0.0);
		sb.append(String.format(java.util.Locale.US, "Avg waiting: %.2f\n",avgWaiting));
		sb.append(String.format(java.util.Locale.US, "Avg turnaround: %.2f\n",avgTurnaround));
		sb.append(String.format(java.util.Locale.US, "Balance index(avg queue spread): %.2f\n",balanceIndex));
		return sb.toString();
	}
	
	public long getTotalTicks() {
		return totalTicks;
	}
}
