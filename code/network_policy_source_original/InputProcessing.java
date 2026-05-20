/**
 * @author Lei Zhang
 * 			Aug 28, 2003
 */

import java.io.*;
import java.awt.*;
import java.applet.*;
import java.net.*;   
import java.util.*;
import java.lang.*;

public class InputProcessing {
	
	public int numArcs, numZones, numNodes, totalODFlows;
	public Arc arc[];
	public Zone zone[];
	public float arcFlow[], oDCost[][];
	
	private int oNode, dNode, type, numLanes, zoneId, production, attraction;
	private float length, fftt, capacity; 
	
	private static final float THETA1 = (float)0.15, THETA2 = 4, TOLL = 0;
		
	public InputProcessing() {}
	
	public InputProcessing(String dir1, String dir2, String arcFileName, String zoneFileName, String arcFlowFileName, String oDCostFileName) throws IOException {
		
		ReadAFile readArc = new ReadAFile(dir1, arcFileName);
		ReadAFile readZone = new ReadAFile(dir1, zoneFileName);
		ReadAFile readArcFlow = new ReadAFile(dir2, arcFlowFileName);
		ReadAFile readODCost = new ReadAFile(dir2, oDCostFileName);
		
		//arc file
		this.numArcs = readArc.readint();
		this.arc = new Arc[numArcs];
		readArc.readLine();
		//readArc.readLine();
		for(int i = 0; i < numArcs; i++){
			this.oNode = readArc.readint();
			//System.out.print(oNode + "\t");
			this.dNode = readArc.readint();
			this.type = readArc.readint();
			this.numLanes = readArc.readint();
			this.length = readArc.readfloat();
			this.fftt = readArc.readfloat();
			this.capacity = readArc.readfloat();
			//System.out.println(capacity);
			this.arc[i] = new Arc(oNode, dNode, length, capacity, fftt, THETA1, THETA2, TOLL, type, numLanes);
		}
		System.out.println("-- Arc File");
		//zone file
		this.numZones = readZone.readint();
		this.numNodes = readZone.readint();
		this.zone = new Zone[numZones];
		readZone.readLine();
		//readZone.readLine();
		this.totalODFlows = 0;
		for(int i = 0; i < numZones; i++){
			this.zoneId = readZone.readint();
			this.production = readZone.readint();
			this.attraction = readZone.readint();
			totalODFlows += production;
			//System.out.println(zoneId);
			this.zone[i] = new Zone(zoneId, production, attraction, numZones);
		}
		System.out.println("-- Zone File");
		//OBA link flow file
		readArcFlow.readLine();
		this.arcFlow = new float[numArcs];
		for(int i = 0; i < numArcs; i++){
			readArcFlow.readint();
			readArcFlow.readint();
			this.arcFlow[i] = readArcFlow.readfloat();
			readArcFlow.readfloat();
		}
		System.out.println("-- OBA Link File");
		//OBA OD cost table file
		readODCost.readLine();
		this.oDCost = new float[numZones][numZones];
		for(int i = 0; i < numZones; i++){
			for(int j = 0; j < numZones; j++){
				/*
				readODCost.readint();
				readODCost.readint();
				this.oDCost[i][j] = readODCost.readfloat();
				*/
				//!!!!//CHAGNE TO THIS
				
				if(i == j){
					this.oDCost[i][j] = 99999;
				}
				else{
					readODCost.readint();
					readODCost.readint();
					this.oDCost[i][j] = readODCost.readfloat();
				}
				
			}
			//System.out.println(i);
		}
		System.out.println("-- OBA OD File");
	
	}
	
	public InputProcessing(String dir2, Arc[] arc, Zone[] zone, String arcFlowFileName, String oDCostFileName, int numArcs, int numZones) throws IOException {
		
		ReadAFile readArcFlow = new ReadAFile(dir2, arcFlowFileName);
		ReadAFile readODCost = new ReadAFile(dir2, oDCostFileName);
		
		//OBA link flow file
		readArcFlow.readLine();
		this.arcFlow = new float[numArcs];
		for(int i = 0; i < numArcs; i++){
			readArcFlow.readint();
			readArcFlow.readint();
			arc[i].flow = readArcFlow.readfloat();
			readArcFlow.readfloat();
		}
		
		//OBA OD cost table file
		readODCost.readLine();
		this.oDCost = new float[numZones][numZones];
		for(int i = 0; i < numZones; i++){
			for(int j = 0; j < numZones; j++){
				if(i == j){
					zone[i].oDCost[j] = 0;
				}
				else{
					readODCost.readint();
					readODCost.readint();
					zone[i].oDCost[j] = readODCost.readfloat();
				}
			}
		}
		
	}
		
}
