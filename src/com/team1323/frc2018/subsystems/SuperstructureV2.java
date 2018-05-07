package com.team1323.frc2018.subsystems;

import java.util.Arrays;
import java.util.List;

import com.team1323.frc2018.loops.Loop;
import com.team1323.frc2018.loops.Looper;
import com.team1323.frc2018.subsystems.IntakeV2.IntakeState;

public class SuperstructureV2 extends Subsystem{
	
	public IntakeV2 intake = IntakeV2.getInstance();
	
	private Request pendingIntakeRequest = intake.intakeStateRequest(IntakeState.OFF);
	private RequestList activeRequests;
	private List<RequestList> queuedRequests;
	private Request currentRequest;
	
	private boolean newRequests = false;
	private boolean activeRequestsCompleted = false;
	private boolean allRequestsCompleted = false;
	
	public boolean requestsCompleted(){ return allRequestsCompleted; }
	
	private void setActiveRequests(RequestList requests){
		activeRequests = requests;
		newRequests = true;
		activeRequestsCompleted = false;
		allRequestsCompleted = false;
	}
	
	private void clearActiveRequests(){
		setActiveRequests(new RequestList());
	}
	
	private void setQueuedRequests(RequestList requests){
		queuedRequests = Arrays.asList(requests);
	}
	
	private void setQueuedRequests(List<RequestList> requests){
		queuedRequests = requests;
	}
	
	public void request(Request r){
		setActiveRequests(new RequestList(Arrays.asList(r), false));
		setQueuedRequests(new RequestList());
	}
	
	public void request(Request active, Request queue){
		setActiveRequests(new RequestList(Arrays.asList(active), false));
		setQueuedRequests(new RequestList(Arrays.asList(queue), false));
	}
	
	public void request(List<Request> requests, boolean parallel){
		setActiveRequests(new RequestList(requests, parallel));
		setQueuedRequests(new RequestList());
	}
	
	public void request(List<Request> actives, boolean activesParallel, List<Request> queues, boolean queuesParallel){
		setActiveRequests(new RequestList(actives, activesParallel));
		setQueuedRequests(new RequestList(queues, queuesParallel));
	}
	
	public void queue(Request request){
		queuedRequests.add(new RequestList(Arrays.asList(request), false));
	}
	
	public void queue(List<Request> requests, boolean parallel){
		queuedRequests.add(new RequestList(requests, parallel));
	}
	
	public void replaceQueue(Request request, boolean parallel){
		setQueuedRequests(new RequestList(Arrays.asList(request), parallel));
	}
	
	public void replaceQueue(List<Request> requests, boolean parallel){
		setQueuedRequests(new RequestList(requests, parallel));
	}
	
	private final Loop loop = new Loop(){

		@Override
		public void onStart(double timestamp) {
			
		}

		@Override
		public void onLoop(double timestamp) {
				if(!activeRequestsCompleted){
					if(newRequests){
						if(activeRequests.isParallel()){
							for(Request request : activeRequests.getRequests()){
								request.act();
							}
						}else{
							currentRequest = activeRequests.remove();
							currentRequest.act();
						}
					}
					if(activeRequests.isParallel()){
						boolean done = true;
						for(Request request : activeRequests.getRequests()){
							done &= request.isFinished();
						}
						activeRequestsCompleted = done;
					}else{
						if(currentRequest.isFinished()){
							if(activeRequests.isEmpty()){
								activeRequestsCompleted = true;
							}else{
								newRequests = true;
								activeRequestsCompleted = false;
							}
						}
					}
				}else{
					if(!queuedRequests.isEmpty()){
						setActiveRequests(queuedRequests.remove(0));
					}else{
						allRequestsCompleted = true;
					}
				}
		}

		@Override
		public void onStop(double timestamp) {
			
		}
		
	};

	@Override
	public void stop() {
		
	}

	@Override
	public void zeroSensors() {
		
	}

	@Override
	public void registerEnabledLoops(Looper enabledLooper) {
		
	}

	@Override
	public void outputToSmartDashboard() {
		
	}

}
