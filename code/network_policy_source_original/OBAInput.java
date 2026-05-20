/**
 * @author Lei Zhang
 * 			Aug 29, 2003
 */
import java.io.*;
import java.awt.*;
import java.applet.*;
import java.net.*;   
import java.util.*;
import java.lang.*;

public class OBAInput {

	public OBAInput() {}
	
	private Zone[] zone;
	private Arc[] arc;
	private int numZones, numArcs, numNodes, totalODFlow;
	private String dir, OBAArcFile, OBAODFile;
	 
	public OBAInput (Zone[] zone, Arc[] arc, int numZones, int numArcs, int numNodes, int totalODFlow, String dir, String OBAArcFile, String OBAODFile){
		this.zone = zone;
		this.arc = arc;
		this.numZones = numZones;
		this.numArcs = numArcs;
		this.numNodes = numNodes;
		this.totalODFlow = totalODFlow;
		this.dir = dir;
		this.OBAArcFile = OBAArcFile;
		this.OBAODFile = OBAODFile;
	}

	public void outputOBAFiles() throws IOException {
		FileOutputStream foutArc, foutOD;
		PrintWriter outArc, outOD;
		
		try{
			foutArc = new FileOutputStream(dir + OBAArcFile);
		}catch(FileNotFoundException e) {
			System.out.println("ERROR: can not write OBAArcFile");
			return;
		}
		outArc = new PrintWriter(foutArc);
		
		outArc.println("<NUMBER OF ZONES> " + numZones);
		outArc.println("<NUMBER OF NODES> " + numNodes);
		//!!!!!!//CHANGE outArc.println("<FIRST THRU NODE> " + (numZones + 1));
		//outArc.println("<FIRST THRU NODE> " + 1);
		outArc.println("<FIRST THRU NODE> " + 1);
		//outArc.println("<FIRST THRU NODE> " + (numZones + 1));
		outArc.println("<NUMBER OF LINKS> " + numArcs);
		outArc.println("<END OF METADATA>");
		outArc.println();
		outArc.println();
		outArc.println("~ " + "\t" + "Init node" + "\t" + "Term node" + "\t" + "Capacity" + "\t" + "Length" + "\t" + "Free Flow Time" + "\t" + "B" + "\t" + "Power" + "\t" + "Speed limit" + "\t" + "Toll" + "\t" + "Type" + "\t" + ";");
		for(int i = 0; i < numArcs; i++){
			outArc.println("\t" + arc[i].oNode + "\t" + arc[i].dNode + "\t" + arc[i].getOBACapacity() + "\t" + arc[i].length + "\t" + arc[i].fftt + "\t" + arc[i].theta1 + "\t" + arc[i].theta2 + "\t" + 0 + "\t" + arc[i].toll + "\t" + arc[i].type + "\t" + ";");
		}
		outArc.close();
		foutArc.close();
		
		try{
			foutOD = new FileOutputStream(dir + OBAODFile);
		}catch(FileNotFoundException e) {
			System.out.println("ERROR: can not write OBAODFile");
			return;
		}
		outOD = new PrintWriter(foutOD);
		
		outOD.println("<NUMBER OF ZONES> " + numZones);
		outOD.println("<TOTAL OD FLOW> " + totalODFlow);
		outOD.println("<END OF METADATA>");
		outOD.println();
		outOD.println();
		for(int i = 0; i < numZones; i++){
			outOD.println("Origin" + "\t" + (i + 1));
			for(int j = 0; j < numZones; j++){
				outOD.print("\t");
				outOD.print(j + 1);
				outOD.print(" ");
				outOD.print(":");
				outOD.print("\t");
				outOD.print(zone[i].oDFlow[j]);
				outOD.print(";");
				if((j + 1)%5 == 0)outOD.println();
			}
			outOD.println();
		}
		outOD.close();
		foutOD.close();				
	}
}
