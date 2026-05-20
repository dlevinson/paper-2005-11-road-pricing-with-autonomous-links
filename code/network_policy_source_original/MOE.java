/**
 * @author Lei Zhang
 * 			Dec 13, 2003
 */
public class MOE {
	
	private float [] moeList, accessListAct, accessListPop; 
	private float numTrips, vht, vkt, cs, prodVKT, prodTrips, prodCS, input, output, accessAct, accessPop, giniAct, giniPop, revenue, hierarchy;
	private float initInput;
	public MOE() {
		this.moeList = new float[13];
		this.numTrips = 0;
		this.vht = 0;
		this.vkt = 0;
		this.cs = 0;
		this.input = 0;
		this.output = 0;
		this.accessAct = 0;
		this.accessPop = 0;
		this.revenue = 0;
		this.hierarchy = 0;
	}
	
	public float [] computeMOE(Arc arc[], Zone zone[], int numArcs, int numZones, float distBeta, int iteration){
		this.accessListAct = new float[numZones];
		this.accessListPop = new float[numZones]; 
			
		for(int i = 0; i < numArcs; i++){
			vht += arc[i].flow*arc[i].tt/60;
			revenue += arc[i].flow*arc[i].toll;
			input += arc[i].flow*arc[i].generalizedCost/60;
			vkt += arc[i].flow*arc[i].length*1.6;
			cs += 0.5*(arc[i].flow + arc[i].initFlow)*(arc[i].intiCost - arc[i].generalizedCost)/60;
		}
		
		if(iteration == 0)initInput = input;
		
		for(int i = 0; i < numZones; i++){
			for(int j = 0; j < numZones; j++){
				numTrips += zone[i].oDFlow[j];
				zone[i].accessAct += zone[j].attraction*Math.exp(-distBeta*zone[i].oDCost[j]);
				zone[i].accessPop += zone[j].production*Math.exp(-distBeta*zone[j].oDCost[i]);
			}
			accessAct += zone[i].accessAct;
			accessPop += zone[i].accessPop;
			accessListAct[i] = zone[i].accessAct;
			accessListPop[i] = zone[i].accessPop;
			zone[i].accessAct = 0;
			zone[i].accessPop = 0;
		}
		
		output = vkt;
		prodVKT = output/input;
		output = numTrips;
		prodTrips = output/input;
		output = cs;
		prodCS = output/(input - initInput);
		
		moeList[0] = vht;
		moeList[1] = vkt;
		moeList[2] = revenue;
		moeList[3] = cs;
		moeList[4] = input;
		moeList[5] = numTrips;
		moeList[6] = accessAct;
		moeList[7] = accessPop;
		moeList[8] = gini(accessListAct);
		moeList[9] = gini(accessListPop);
		moeList[10] = prodVKT;
		moeList[11] = prodTrips;
		moeList[12] = prodCS;

		return moeList;
	}
		
	public float gini(float [] list){
		int n;
		float gini = 0, average;
		n = list.length;
		average = average(list);
		
		for(int i = 0; i < n; i++){
			for(int j = 0; j < n; j++){
				gini += Math.abs(list[i] - list[j]);
			}
		}
		gini = gini/(2*n*n*average);
		return gini;
	}

	public float average(float [] list){
		float ave = 0;
		for(int i = 0; i < list.length; i++)ave += ave + list[i];
		ave = ave/list.length;
		return ave;
	}
}
