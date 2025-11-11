package project;

import java.util.*;

public class SimulationEngine {
 private final java.util.List<CPUCore> cores = new ArrayList<>();
 private final LoadBalancer balancer = new LoadBalancer();
 private final Metrics metrics = new Metrics();
 private final java.util.List<task> allTasks = new ArrayList<>();
 private final TaskFactory factory = new TaskFactory();

 private final int balanceEvery;  
 private final int quantum;       
 private final String policy;     
 private int nowTick = 0;

 public SimulationEngine(int coreCount, String policy, int quantum, int balanceEvery) {
     this.policy = policy.toUpperCase(java.util.Locale.ROOT);
     this.quantum = quantum;
     this.balanceEvery = balanceEvery;

     for (int i = 0; i < coreCount; i++) {
         SchedulerPolicy p = switch (this.policy) {
             case "PRIO" -> new PriorityScheduler();
             case "RR"   -> new RoundRobinScheduler(quantum);
             default -> throw new IllegalArgumentException("Unknown policy: " + policy);
         };
         cores.add(new CPUCore(i, p));
     }
 }

 	public void seedInitialTasks(int n, int minBurst, int maxBurst, int prioMin, int prioMax) {
     java.util.List<task> init = factory.initial(n, minBurst, maxBurst, prioMin, prioMax);
     allTasks.addAll(init);
     int i = 0;
     for (task t : init) {
         cores.get(i % cores.size()).enqueue(t, nowTick);
         i++;
     }
 }

 	public void spawnDynamically(int maxNewPerTick, int minBurst, int maxBurst, int prioMin, int prioMax) {
     java.util.List<task> newOnes = factory.maybeArrive(nowTick, maxNewPerTick, minBurst, maxBurst, prioMin, prioMax);
     if (!newOnes.isEmpty()) {
         allTasks.addAll(newOnes);
         for (task t : newOnes) {
             CPUCore best = cores.get(0);
             for (CPUCore c : cores) {
                 if (c.queueSize() < best.queueSize()) best = c;
             }
             best.enqueue(t, nowTick);
         }
     }
 }

 	public String step() {
 		StringBuilder line = new StringBuilder();
 		for (CPUCore c : cores) {
         String log = c.tick(nowTick, metrics);
         if (line.length() > 0) line.append("  |  ");
         line.append(log);
     }

     if (nowTick > 0 && nowTick % balanceEvery == 0) {
         balancer.rebalance(cores, nowTick);
     }


     metrics.sampleQueues(cores);
     nowTick++;
     return "Tick " + nowTick + ": " + line;
 }

 	public boolean allDone() {
 		for (CPUCore c : cores) {
         if (!c.isIdle()) return false;
     }
     return true;
 }

 	public String report() {
 		return metrics.report(allTasks, cores);
 }


 public int getNowTick() {
	 return nowTick; }
 
 public java.util.List<CPUCore> getCores() { 
	 return Collections.unmodifiableList(cores); }
 
 public Metrics getMetrics() {
	 return metrics; }
 
 public String getPolicy() {
	 return policy; }
 
 public int getQuantum() { 
	 return quantum; }
 
 public int getBalanceEvery() {
	 return balanceEvery; }
}

