/**
 * @author Lei Zhang
 * 			Nov 7, 2002
 */
import java.io.*;
import java.awt.*;
import java.applet.*;
import java.net.*;
import java.util.*;
import java.lang.*;

public class Arc {
	public float flow, length, capacity, fftt, ffSpeed, speed, theta1, theta2, 
				toll, tt, vCRatio, additionalCost, generalizedCost, revenue,
				cumRevenue, disposableRevenue, maintenanceCost, expansionCost, expansionCost2,
				degenerationCost, expansionCostCoef, profit, plusCapacity, 
				plusCapacity2, minusCapacity, bCRatio;
	public float initFlow, initCapacity, initFftt, initFfSpeed, initSpeed, intiCost,
				initToll, initTt;
	public int oNode, dNode, type, numLanes, optimalExpansion;  
	public float[] tollHistory, profitHistory, demandHistory, gCostHistory;
	public static int HISTORY = 100;
	public int arcIteration = 0;


	public Arc(){}  
	
	public Arc(int oNode, int dNode, float length, float capacity, float fftt, float theta1, float theta2, float toll, int type, int numLanes){
		this.oNode = oNode;
		this.dNode = dNode;
		this.flow = 0;
		this.length = length;
		this.capacity = capacity;
		this.fftt = fftt;
		this.ffSpeed = 60*length/fftt;
		this.speed = 60*length/fftt;
		this.theta1 = theta1;
		this.theta2 = theta2;
		this.toll = toll;
		this.profit = 0;
		this.type = type;
		this.numLanes = numLanes;
		this.tt = fftt;
		this.generalizedCost = 0;
		this.revenue = 0;
		this.cumRevenue = 0;
		this.vCRatio = 0;
		this.maintenanceCost = 0;
		this.expansionCost = 0;
		this.expansionCost2 = 0;
		this.degenerationCost = 0;
		this.expansionCostCoef = 0;
		this.plusCapacity = laneCapacity(numLanes + 1) - capacity;
		this.plusCapacity2 = laneCapacity(numLanes + 2) - capacity;
		this.minusCapacity = laneCapacity(numLanes - 1) - capacity;
		this.bCRatio = 0;
		this.optimalExpansion = 0;
		this.tollHistory = new float[HISTORY];
		this.profitHistory = new float[HISTORY];
		this.demandHistory = new float[HISTORY];
		this.gCostHistory = new float[HISTORY];
		this.disposableRevenue = 0;
	}
	
	public float getOBACapacity(){
		if(capacity == 0){return(999999);}
		else{return capacity*10;}
	}
	

	public void updateMaintenanceCost(float mu, float alfa1, float alfa2, float alfa3){
		if(capacity == 0){
			maintenanceCost = 0;
		}
		else{
			//Speed change only- Bhanu's model
				//maintenanceCost = (float)(mu*Math.pow(length, alfa1)*Math.pow(flow, alfa2)*Math.pow(ffSpeed, alfa3));
			//Capacity and speed changes
				//maintenanceCost = (float)(mu*Math.pow(length, alfa1)*Math.pow(capacity, alfa2)*Math.pow(ffSpeed, alfa3));
			//Lei's Model in the Journal of Regional Science paper
				//maintenanceCost = (float)(mu*Math.pow(length, alfa1)*Math.pow(capacity, alfa2));
			if(arcIteration == 0){
				maintenanceCost = 0;
			}
			else{
				maintenanceCost = (float)(mu*Math.pow(length, alfa1)*Math.pow(capacity, alfa2));
			}
		}
	}
	
		
	public void updateExpansionCost(float sgm0, float sgm1, float sgm2, float sgm3){
		//Directly Based on Rama's regression model
			//expansionCost = (float)(Math.pow(ffSpeed/60, 0.5)*Math.exp(Math.log(length) + 10.2));
		//Continuous
			expansionCostCoef = (float)(sgm0*Math.pow(length, sgm1)*Math.pow(capacity, sgm2));  //the third term changes in capacity is not included here
		//Discrete
			expansionCost = (float)(sgm0*Math.pow(length, sgm1)*Math.pow(capacity, sgm2)*Math.pow(plusCapacity, sgm3));
			expansionCost2 = (float)(sgm0*Math.pow(length, sgm1)*Math.pow(capacity, sgm2)*Math.pow(plusCapacity2, sgm3));
			degenerationCost = (float)(sgm0*Math.pow(length, sgm1)*Math.pow(capacity, sgm2)*Math.pow(minusCapacity, sgm3));
	}
	
	
	public void updateRevenue(float phi, float vot){
		if(capacity == 0){
			revenue = 0;
		}
		else{
			generalizedCost = tt + 60*toll/vot;
			vCRatio = flow/(10*capacity);
			tt = (float)( fftt*(1 + theta1*Math.pow(vCRatio, theta2)) );
			speed = 60*length/tt;
			revenue = phi*toll*flow;
			profit = revenue - maintenanceCost;
			profitHistory[arcIteration] = profit;
			tollHistory[arcIteration] = toll;
			demandHistory[arcIteration] = flow;
			gCostHistory[arcIteration] = generalizedCost;
			disposableRevenue = cumRevenue + profit;
			arcIteration++;
		}
	}
	
	public void updateBCRatio(float vot, float x, float y, float r, int id){
		float benefit1 = 0, benefit2 = 0, trafficRate = 1, interestRate = 1;
		float newCapacity1, newFFTT1, newCapacity2, newFFTT2, ttSavings1, ttSavings2, BC1, BC2;
		newCapacity1 = capacity + plusCapacity;
		newCapacity2 = capacity + plusCapacity2;
		newFFTT1 = 60*length/capacitySpeed(newCapacity1);
		newFFTT2 = 60*length/capacitySpeed(newCapacity2);
		for(int i = 0; i < y; i++){
			ttSavings1 = (float)(fftt*(1 + theta1*Math.pow(trafficRate*flow/(10*capacity), theta2))) - (float)(newFFTT1*(1 + theta1*Math.pow(trafficRate*flow/(10*newCapacity1), theta2)));
			ttSavings2 = (float)(fftt*(1 + theta1*Math.pow(trafficRate*flow/(10*capacity), theta2))) - (float)(newFFTT2*(1 + theta1*Math.pow(trafficRate*flow/(10*newCapacity2), theta2)));
			benefit1 += vot*(float)365*trafficRate*flow*ttSavings1/(60*interestRate);
			benefit2 += vot*(float)365*trafficRate*flow*ttSavings2/(60*interestRate);
			trafficRate = trafficRate*(1 + x);
			interestRate = interestRate*(1 + r);
			
		}
		BC1 = benefit1/expansionCost;
		BC2 = benefit2/expansionCost2;
		bCRatio = BC1>BC2? BC1:BC2;
		optimalExpansion = BC1>BC2?1:2;
	}
	
	public void updateInvestment(float capacityChange, int numLanesChange){
		capacity += capacityChange;
		numLanes += numLanesChange;
		ffSpeed = capacitySpeed(capacity);
		fftt = 60*length/ffSpeed;
		vCRatio = flow/(10*capacity);
		plusCapacity = laneCapacity(numLanes + 1) - capacity;
		plusCapacity2 = laneCapacity(numLanes + 2) - capacity;
		minusCapacity = laneCapacity(numLanes - 1) - capacity;
	}
	
	public void saveInitial(){
		this.initCapacity = this.capacity;
		this.initFfSpeed = this.ffSpeed;
		this.initFftt = this.fftt;
		this.initFlow = this.flow;
		this.initSpeed = this.speed;
		this.initToll = this.toll;
		this.initTt = this.tt;
		this.intiCost = this.generalizedCost;
	}
	
	public float capacitySpeed(float capacity){
		float temp;
		temp = (float)(-30.6 + 9.79*Math.log(capacity));
		if(temp > 5){
			return temp;
		}
		else{
			return 5;
		}
	}
	
	public float laneCapacity(int numLanes){
		float temp;
		temp = (float)(341.4 + 272.6*Math.pow(numLanes,2) + 161.5*numLanes);
		return temp;
	}
}
