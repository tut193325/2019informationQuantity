package s4.B193325; // Please modify to s4.Bnnnnnn, where nnnnnn is your student ID. 
import java.lang.*;
import s4.specification.*;

/* What is imported from s4.specification
package s4.specification;
public interface InformationEstimatorInterface{
    void setTarget(byte target[]); // set the data for computing the information quantities
    void setSpace(byte space[]); // set data for sample space to computer probability
    double estimation(); // It returns 0.0 when the target is not set or Target's length is zero;
// It returns Double.MAX_VALUE, when the true value is infinite, or space is not set.
// The behavior is undefined, if the true value is finete but larger than Double.MAX_VALUE.
// Note that this happens only when the space is unreasonably large. We will encounter other problem anyway.
// Otherwise, estimation of information quantity, 
}                        
*/

public class InformationEstimator implements InformationEstimatorInterface{
    // Code to tet, *warning: This code condtains intentional problem*
    byte [] myTarget; // data to compute its information quantity
    byte [] mySpace;  // Sample space to compute the probability
    FrequencerInterface myFrequencer;  // Object for counting frequency
	boolean targetReady = false;
    boolean spaceReady = false;

    byte [] subBytes(byte [] x, int start, int end) {
	// corresponding to substring of String for  byte[] ,
	// It is not implement in class library because internal structure of byte[] requires copy.
	byte [] result = new byte[end - start];
	for(int i = 0; i<end - start; i++) { result[i] = x[start + i]; };
	return result;
    }

    // IQ: information quantity for a count,  -log2(count/sizeof(space))
    double iq(int freq) {
	return  - Math.log10((double) freq / (double) mySpace.length)/ Math.log10((double) 2.0);
    }

    public void setTarget(byte [] target) {
	myTarget = target;
	if(myTarget.length>0) targetReady = true;
	}
    public void setSpace(byte []space) { 
	myFrequencer = new Frequencer();
	mySpace = space;
	myFrequencer.setSpace(space); 
	if(space.length>0) spaceReady = true;
    }

    public double estimation(){
	if(targetReady == false) return 0.0;
    if(spaceReady == false) return Double.MAX_VALUE;
	double [] Iq = new double[myTarget.length];
	double value1 = (double) 0.0;
	for( int i = 0 ; i < myTarget.length ; i++ ){
		for( int j = 0 ; j <= i ; j++ ){
			if(j == 0){
				myFrequencer.setTarget(subBytes(myTarget, j, i+1));
				value1 = iq(myFrequencer.frequency());
				Iq[i] = value1;
			}else{
				myFrequencer.setTarget(subBytes(myTarget, j, i+1));
				value1 = Iq[j-1] + iq(myFrequencer.frequency());
				if(Iq[i] > value1) Iq[i] = value1;
			}
		}
		// System.out.printf("i = %d, Iq[i] = %f\n",i,Iq[i]);
	}
	return Iq[myTarget.length-1];
    }

    public static void main(String[] args) {
	InformationEstimator myObject;
	double value;
	myObject = new InformationEstimator();
	myObject.setSpace("3210321001230123".getBytes());
	myObject.setTarget("0".getBytes());
	value = myObject.estimation();
	System.out.println(">0 "+value);
	myObject.setTarget("01".getBytes());
	value = myObject.estimation();
	System.out.println(">01 "+value);
	myObject.setTarget("0123".getBytes());
	value = myObject.estimation();
	System.out.println(">0123 "+value);
	myObject.setTarget("00".getBytes());
	value = myObject.estimation();
	System.out.println(">00 "+value);
    }
}
				  
			       

	
    
