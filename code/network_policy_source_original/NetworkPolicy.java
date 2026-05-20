/**
 * @author Lei Zhang
 * 			Aug 27, 2003
 */

import java.io.*;
import java.awt.*;
import java.applet.*;
import java.net.*;   
import java.util.*;
import java.lang.*;

public class NetworkPolicy {

	private Zone zone[];
	private Arc arc[];
	private InputProcessing initialInput, updatedInput;
	private OutputProcessing iterationOutput;
	private TripDistribution tripDist;
	private OBAInput obaInput;
	private Pricing pricing;
	private Investment investment;
	private MOE moe;
	
	
	private String dir1, dir2, dir3, dir4, arcInputFile, zoneInputFile, arcFlowInputFile, oDCostInputFile, OBAArcFile, OBAODFile, iterationOutputFile;		
	private int numArcs, numZones, numNodes, totalODFlow, executionTime;
	private boolean converged = false, historicalConvergence[], tempBool;
	//Lei's Model: private static final float VOT = 10, DISTBETA = (float)0.01, MU = (float)365*20/180, ALFA1 = 1, ALFA2 = (float)1.25, ALFA3 = (float)1.25, PHI = 365, P0 = (float)1/180, P1 = 1, P3 = (float)0.75, S_BETA = (float)0.75;
	//Bhanu's Model: private static final float VOT = 10, DISTBETA = (float)0.01, MU = (float)365, ALFA1 = 1, ALFA2 = (float)0.75, ALFA3 = (float)0.75, PHI = 365, P0 = (float)1.5, P1 = 1, P3 = (float)0, S_BETA = (float)1;
	private static final float VOT = 10, DISTBETA = (float)0.01, 
		MU = (float)365*20/180, ALFA1 = 1, ALFA2 = (float)1.25, ALFA3 = (float)1.25, 
		PHI = 365, P0 = (float)1/180, P1 = 1, P3 = (float)0.75, 
		SGM0 = 1, SGM1 = (float)0.5, SGM2 = (float)1.25, SGM3 = 1, 
		S_BETA = (float)0.75, X = (float)0.03, Y = 25, R = (float)0.02;

	private static final char INVESTMENTPOLICY = '3'; // See Investment.java for a description of all seven alternative policies
	private static final char PRICINGPOLICY = '1'; // See Pricing.java for a description of all pricing policies	
	private static final int MINIMUM_CONVERGENCE_DURATION = 50, MAXIMUM_ITERATION = 100;
	private float[][] moeList;
	
	public static void main(String[] args) throws IOException, InterruptedException{
		NetworkPolicy networkPolicy = new NetworkPolicy();
		networkPolicy.initialization();
		networkPolicy.iteration();
		networkPolicy.end();		
	}
	
	//constructors
	public NetworkPolicy() {
		this.historicalConvergence = new boolean[MINIMUM_CONVERGENCE_DURATION];
		//input file paths
		this.dir1 = "c:/work/project/network_growth/data/";
		this.dir2 = "c:/work/project/network_growth/OBA/results/";
		this.dir3 = "c:/work/project/network_growth/OBA/data/";
		this.dir4 = "c:/work/project/network_growth/results/TwinCities/";
		this.arcInputFile = "arcs.txt";
		this.zoneInputFile = "zones.txt";
		this.arcFlowInputFile = "arc_flow.txt";
		this.oDCostInputFile = "od_cost.txt";
		this.OBAArcFile = "bsclnk.txt";
		this.OBAODFile = "vehtrp.txt";  
		this.iterationOutputFile = "network";
	}
	
	public void initialization() throws IOException{
		System.out.println("***Initialization***");
						
		//read input files
		initialInput = new InputProcessing(dir1, dir2, arcInputFile, zoneInputFile, arcFlowInputFile, oDCostInputFile);
		//initialize parameters, zones, arcs, OD cost, and arc flows
		numArcs = initialInput.numArcs;
		numZones = initialInput.numZones;
		numNodes = initialInput.numNodes;
		totalODFlow = initialInput.totalODFlows;
		zone = initialInput.zone;
		arc = initialInput.arc;
		for(int i = 0; i < numZones; i++){
			for(int j = 0; j < numZones; j++){
				zone[i].oDCost[j] = initialInput.oDCost[i][j];
			}
		}
		for(int i = 0; i < numArcs; i++){
			arc[i].flow = initialInput.arcFlow[i];
		}
		
		this.moeList = new float[MAXIMUM_ITERATION][13];
	}
	
	public void iteration() throws InterruptedException, IOException{
		System.out.println("***Iteration***");
		int iteration = 0;
		
		do{
			System.out.println("[Iteration] " + iteration);
			
			//Trip distribution according to the current OD cost matrix
				tripDist = new TripDistribution(zone, numZones, DISTBETA, iteration);		
				System.out.println("	Trip Distribution");
			//Traffic assignment (OBA)
				//Generate input files for the origin-based assignment program
				obaInput = new OBAInput(zone, arc, numZones, numArcs, numNodes, totalODFlow, dir3, OBAArcFile, OBAODFile);
				obaInput.outputOBAFiles();
				System.out.println("	OBA Input");
				//Run the OBA program
				Process p = Runtime.getRuntime().exec("c:/work/project/network_growth/OBA/tap_ob c:/work/project/network_growth/OBA/sf_tap_ob.tui");
				p.waitFor();
				p.destroy();
				System.out.println("	OBA Done");
				//Read new link flows and OD costs from the output files of the OBA program
				updatedInput = new InputProcessing(dir2, arc, zone, arcFlowInputFile, oDCostInputFile, numArcs, numZones);
				System.out.println("	Update Flows and OD Costs");
			//Apply cost and revenue models
				for(int i = 0; i < numArcs; i++){
					arc[i].updateMaintenanceCost(MU, ALFA1, ALFA2, ALFA3);
					arc[i].updateExpansionCost(SGM0, SGM1, SGM2, SGM3);
					arc[i].updateRevenue(PHI, VOT);
					arc[i].updateBCRatio(VOT, X, Y, R, i);
				}
			//Calculate MOEs
				if(iteration == 0){ for(int i = 0; i < numArcs; i++)arc[i].saveInitial();}
				moe = new MOE();
				moeList[iteration] = moe.computeMOE(arc, zone, numArcs, numZones, DISTBETA, iteration);
			//Apply investment model - new link capacity and free-flow speed
				investment = new Investment(arc, numArcs, INVESTMENTPOLICY, SGM3, S_BETA);
				System.out.println("	Investment");
			//Apply the pricing model
				pricing = new Pricing(arc, numArcs, PRICINGPOLICY, P0, P1, P3, VOT);
				//System.out.println("	Pricing");			
			//Evaluate convergence
				converged = evaluateConvergence(iteration);
			//Output the current network including link flows, capacity and free-flow speeds
				iterationOutput = new OutputProcessing(arc, numArcs, moeList[iteration], dir4, iterationOutputFile, iteration);
				System.out.println("	Output");
			iteration ++;
			
		}while(converged == false);
	}
	
	public void end() throws IOException {
		System.out.println("***End***");
	}
	
	public boolean evaluateConvergence (int iteration){
		//minimum number of iterations 
		if(iteration < MINIMUM_CONVERGENCE_DURATION){
			historicalConvergence[iteration] = investment.getConvergence();
			return false;
		}
		//maximum number of iterations
		else if(iteration > 100){
			return true;
		}
		//if the network does not change in the past MINIMUM_CONVERGENCE_DURATION, a convergence is achived
		else{
			tempBool = true;
			for(int i = 0; i < MINIMUM_CONVERGENCE_DURATION - 1; i++){
				historicalConvergence[i] = historicalConvergence[i + 1];
				if(historicalConvergence[i] == false)tempBool = false;
			}
			historicalConvergence[MINIMUM_CONVERGENCE_DURATION - 1] = investment.getConvergence();
			if(historicalConvergence[MINIMUM_CONVERGENCE_DURATION - 1] == false)tempBool = false;
			return tempBool;		
		}
	}
	
}
