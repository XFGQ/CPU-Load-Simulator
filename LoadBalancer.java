package project;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class LoadBalancer {

	public LoadBalancer() {}
	

	public void rebalance(List<CPUCore> cores, int nowTick) {
		if(cores==null|| cores.size()<=1) {
			return;
		}
		
		List<CPUCore> donors=new ArrayList<>(cores);
		donors.sort((a,b) -> Integer.compare(b.queueSize(), a.queueSize()));
		
		List<CPUCore> receivers=new ArrayList<>(cores);
		receivers.sort(Comparator.comparingInt(CPUCore::queueSize));
		
		int i=0;
		int j=0;
		
		while(i<donors.size()&&j<receivers.size()) {
			CPUCore from=donors.get(i);
			CPUCore to=receivers.get(j);
			
			
			if(from==to) {
				i++;
				j++;
				continue;
			}
			
			int diff=from.queueSize()-to.queueSize();
			if(diff<=1) {
				break;
			}
			
			int moved=from.stealHalfTo(to,nowTick);
			i++;
			j++;
		}
		
	}
}
