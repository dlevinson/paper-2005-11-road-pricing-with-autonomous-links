/**
 * @author Lei Zhang
 * 			Sep 1, 2003
 */
import java.io.*;
import java.awt.*;
import java.applet.*;
import java.net.*;   
import java. util.*;
import java.lang.*;

public class OutputProcessing {

	/**
	 * Constructor for OutputProcessing.
	 */
	public OutputProcessing(Arc[] arc, int numArcs, float [] moeList, String dir4, String fileName, int iteration) throws IOException {
		
		FileOutputStream foutArc;
		PrintWriter outArc;
		Integer count = new Integer(iteration);
		
		try{
			foutArc = new FileOutputStream(dir4 + fileName + count.toString() + ".txt");
		}catch(FileNotFoundException e) {
			System.out.println("ERROR: can not write iterationOutputFile");
			return;
		}
		outArc = new PrintWriter(foutArc);

		outArc.println("VHT" + "\t" + moeList[0]);
		outArc.println("VKT" + "\t" + moeList[1]);
		outArc.println("Revenue" + "\t" + moeList[2]);
		outArc.println("Consumers's Surplus" + "\t" + moeList[3]);
		outArc.println("Total Input: VHT + Revenue/VoT" + "\t" + moeList[4]);
		outArc.println("NumOfTrips" + "\t" + moeList[5]);
		outArc.println("Accessibility-Activity" + "\t" + moeList[6]);
		outArc.println("Accessibility-Population" + "\t" + moeList[7]);
		outArc.println("AccessAct Gini" + "\t" + moeList[8]);
		outArc.println("AccessPop Gini" + "\t" + moeList[9]);
		outArc.println("Productivity-VKT/Input" + "\t" + moeList[10]);
		outArc.println("Productivity-Trips" + "\t" + moeList[11]);
		outArc.println("Productivity-CS Change/Input Change" + "\t" + moeList[12]);
					
		outArc.println("<NUMBER OF LINKS> " + numArcs);
		outArc.println("Init node" + "\t" + "Term node" + "\t" + "FFSpeed" + "\t" + "Free Flow Time(min)" + "\t" + "Flow(veh/day)" + "\t" + "Capacity(veh/hour)" + "\t" + "Length(mile)" + "\t" + "Toll(min)" + "\t" + "Gen.Cost(min)" + "\t" + "Profit($)" + "\t" + "Type");
		for(int i = 0; i < numArcs; i++){
			outArc.println(arc[i].oNode + "\t" + arc[i].dNode + "\t" + arc[i].ffSpeed + "\t" + arc[i].fftt + "\t" + arc[i].flow + "\t" + arc[i].capacity + "\t" + arc[i].length + "\t" + arc[i].toll + "\t" + arc[i].generalizedCost + "\t" + arc[i].profit + "\t" + arc[i].type);
		}
		
		outArc.close();
		foutArc.close();
	}
}
