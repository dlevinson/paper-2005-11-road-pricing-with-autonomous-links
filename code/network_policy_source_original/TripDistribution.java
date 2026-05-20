/**
 * @author Lei Zhang
 * 			Aug 28, 2003
 */
public class TripDistribution {

	private float[] a, b, currentProduction, currentAttraction, denominator;
	private float percentError;
	private int iterationCounter;
	
	public TripDistribution() {}
	
	//ODij = aibjexp(-beta*COSTij)
	
	public TripDistribution(Zone[] zone, int numZones, float beta, int iteration){
		
		this.percentError = 99;
		this.iterationCounter = 0;
		this.a = new float[numZones];
		this.b = new float[numZones];
		this.currentAttraction = new float[numZones];
		this.currentProduction = new float[numZones];
		this.denominator = new float[numZones];
		for(int i = 0; i <  numZones; i++){
			b[i] = 1;
		}
		
		for(int i = 0; i < numZones; i++){
			for(int j = 0; j < numZones; j++){
				zone[i].oDCost[j] = (float)Math.exp(-beta*zone[i].oDCost[j]);
			}
		}
		
		//convention in the next block of codes: i denotes production, j denotes attraction
		while(percentError > 1){ //allow for 1 percent error
			percentError = 0;
			if(iterationCounter%2 == 0){ //keep b constant, update a
				//initialization
				for(int i = 0; i <  numZones; i++){
					denominator[i] = 0;
					currentAttraction[i] = 0;
				}
				//update a by satisfying production constraints
				//System.out.println("denominator");
				for(int i = 0; i < numZones; i++){
					for(int j = 0; j < numZones; j++){
						denominator[i] += b[j]*zone[i].oDCost[j]; 
					}
					//System.out.println(denominator[i]);
				}
				for(int i = 0; i < numZones; i++){
					a[i] = zone[i].production/denominator[i];
				}
				//claculate MSE of attractions
				
				for(int j = 0; j < numZones; j++){
					for(int i = 0; i < numZones; i++){
						currentAttraction[j] += a[i]*b[j]*zone[i].oDCost[j];
					}
				}
				for(int j = 0; j < numZones; j++){
					percentError += Math.abs((zone[j].attraction - currentAttraction[j])/zone[j].attraction);
				}
				percentError = 100*percentError/(float)numZones;
			}	
			else{ //keep a constant, update b
				//initialization
				for(int j = 0; j <  numZones; j++){
					denominator[j] = 0;
					currentProduction[j] = 0;
				}
				//update b by satisfying attraction constraints
				//System.out.println("denominator");
				for(int j = 0; j < numZones; j++){
					for(int i = 0; i < numZones; i++){
						denominator[j] += a[i]*zone[i].oDCost[j]; 
					}
					//System.out.println(denominator[j]);
				}
				//System.out.println("B");
				for(int j = 0; j < numZones; j++){
					b[j] = zone[j].attraction/denominator[j];
					//System.out.println(b[j]);
				}
				//claculate MSE of productions
				for(int i = 0; i < numZones; i++){
					for(int j = 0; j < numZones; j++){
						currentProduction[i] += a[i]*b[j]*zone[i].oDCost[j];
					}
				}
				for(int i = 0; i < numZones; i++){
					percentError += Math.abs((zone[i].production - currentProduction[i])/zone[i].production);
				}
				percentError = 100*percentError/(float)numZones;
			}
			iterationCounter++;
			
		}
		
		//Compute OD table 
		
		//No historical impacts
		for(int i = 0; i < numZones; i++){
			for(int j = 0; j < numZones; j++){
				zone[i].oDFlow[j] = a[i]*b[j]*zone[i].oDCost[j];
			}
		}
		
		
		/*
		//Eoluationary updating procudrue-MSA
		for(int i = 0; i < numZones; i++){
			for(int j = 0; j < numZones; j++){
				if(iteration == 0){
					zone[i].oDFlow[j] = a[i]*b[j]*zone[i].oDCost[j];
				}
				else{ //method of succesive averages
					zone[i].oDFlow[j] = (1 - 1/((float)iteration + 1))*zone[i].oDFlow[j] + (1/((float)iteration + 1))*(a[i]*b[j]*zone[i].oDCost[j]);
				}
			}
		}
		*/
		
		

	}

}
