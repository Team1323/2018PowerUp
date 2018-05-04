package com.team1323.frc2018.subsystems;

import java.util.ArrayList;
import java.util.List;

public class RequestList {

	List<Request> requests;
	boolean parallel = false;
	
	public RequestList(){
		this(new ArrayList<>(0), false);
	}
	
	public RequestList(List<Request> requests, boolean parallel){
		this.requests = requests;
		this.parallel = parallel;
	}
	
	public boolean isParallel(){
		return parallel;
	}
	
	public List<Request> getRequests(){
		return requests;
	}
	
	public void add(Request request){
		requests.add(request);
	}
	
	public Request remove(){
		return requests.remove(0);
	}
	
	public boolean isEmpty(){
		return requests.isEmpty();
	}
	
}
