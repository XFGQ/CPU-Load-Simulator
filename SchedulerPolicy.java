package project;

import java.util.Deque;

public interface SchedulerPolicy {

	void offer(task t, int nowTick);
	task selectNext(int nowTick);
	boolean isIdle();
	int queueSize();
	int timeQuantum();
	Deque<task>queueRef();
}
