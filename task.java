package project;

public class task {
    private final String id;
    private final int burstTime;
    private int remainingTime;
    private final int arrivalTime;
    private int priority;

    private Integer startTime = null;
    private Integer finishTime = null;
    private int lastEnqueueTick = 0;

    public task(String id, int burstTime, int arrivalTime, int priority) {
        if (burstTime <= 0) throw new IllegalArgumentException("burstTime must be > 0");
        this.id = id;
        this.burstTime = burstTime;
        this.remainingTime = burstTime;
        this.arrivalTime = arrivalTime;
        this.priority = priority;
    }

 
    public boolean isFinished() {
    	return remainingTime <= 0; }
    
    public void workOneTick() {
    	if (remainingTime > 0) remainingTime--; }

    public String getId() {
    	return id; }
    
    public int getBurstTime() { 
    	return burstTime; }
    
    public int getRemainingTime() { 
    	return remainingTime; }
    
    public int getArrivalTime() { 
    	return arrivalTime; }

    public int getPriority() { 
    	return priority; }
    
    public void setPriority(int priority) { 
    	this.priority = priority; }

    public Integer getStartTime() { 
    	return startTime; }
    
    public void setStartTime(Integer startTime) { 
    	this.startTime = startTime; }

    public Integer getFinishTime() {
    	return finishTime; }
    
    public void setFinishTime(Integer finishTime) { 
    	this.finishTime = finishTime; }

    public int getLastEnqueueTick() { 
    	return lastEnqueueTick; }
    
    public void setLastEnqueueTick(int tick) { 
    	this.lastEnqueueTick = tick; }

    public String toString() {
        return "task{" + id + ", rem=" + remainingTime + "/" + burstTime + ", prio=" + priority + "}";
    }
}
