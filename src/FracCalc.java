import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
public class FracCalc {
    public static void main(String[] args) 
    {
    	Scanner feed = new Scanner(System.in);
    	String input;
    	
        while(true)
        {
        	System.out.println("Enter a basic arithmetic expression.\n"
        			+ "(use - + / * for arithmetic operators, and _ to separate parts of a mixed fraction.\n"
        			+ "Use spaces to separate the values and the operator.)");
        	input = feed.nextLine();
        	//exit loop on "quit" command
        	if(input.equals("quit")) break;
        	//Print the answer
        	System.out.println("Solution: "+produceAnswer(input));
        }
        feed.close();
    }
    
    public static String produceAnswer(String input)
    {
    	//If the input is not formatted as expected, return an error message
    	if(!input.matches("^-?[0-9]+((_[0-9]+/[1-9][0-9]*)|(/[1-9][0-9]*))?( [\\-\\/*+] -?[0-9]+((_[0-9]+/[1-9][0-9]*)|(/[1-9][0-9]*))?)+$")) return "error: invalid entry";
    	
    	//Split the input into the first operand, the operator, and the second operand
    	ArrayList<String> pieces = new ArrayList<String>();
    	for(String item : input.split(" "))
    	{
    		pieces.add(item);
    	}
    	
    		/*
    		 * I made the program to only do single operations, and I was too lazy to 
    		 * rewrite the whole thing for multiple operations, so what I'm doing
    		 * is creating an ArrayList with all the parts of the expression and just
    		 * passing each individual operation to the performOperation method one by one
    		 * and replacing the three components of that operation in the ArrayList with
    		 * the result. I also use order of operations.
    		 * 
    		 * One of the FracCalc tests does not properly follow order of operations which is why my program fails it.
    		 */
		//do all the multiplication and division pieces
    	for(int i=1; i<pieces.size();i+=2)
    	{
    		if(pieces.get(i).matches("[\\/*]"))
    		{
    			//Replace the three parts of the operation with the result
    			pieces.set(i-1,performOperation(pieces.get(i-1),pieces.get(i),pieces.get(i+1)));
    			pieces.remove(i);
    			pieces.remove(i);
    			pieces.trimToSize();
    			//If the result was a divide by 0 error, return the error message
    			if(pieces.get(i-1).equals("error")) return "error: invalid entry";
    			i-=2;
    		}
    	}
    	//do all the addition and subtraction pieces
    	for(int i=1; i<pieces.size();i+=2)
    	{
    		if(pieces.get(i).matches("[\\-+]"))
    		{
    			//Replace the three parts of the operation with the result
    			pieces.set(i-1,performOperation(pieces.get(i-1),pieces.get(i),pieces.get(i+1)));
    			pieces.remove(i);
    			pieces.remove(i);
    			pieces.trimToSize();
    			//If the result was a divide by 0 error, return the error message
    			if(pieces.get(i-1).equals("error")) return "error: invalid entry";
    			i-=2;
    		}
    	}
    	
    	return pieces.get(0);
    }
    
    public static String performOperation(String num1,String oper,String num2)
    {
    	//		Parsing first operand
    	
    	//Split the number into its separate parts
    	String[] arg1string = num1.split("[\\/_]");
    	//Convert to integers
    	int[] arg1 = new int[arg1string.length];
    	for(int i=0;i<arg1string.length;i++)
    	{
    		arg1[i] = Integer.parseInt(arg1string[i]);
    	}
    	//Convert to numerator and denominator
    	int numerator1;
    	int denominator1 = 1;
    	switch(arg1.length)
    	{
    	case 3:
    		if(arg1[0]<0) arg1[1] *= -1;
    		numerator1 = arg1[0]*arg1[2]+arg1[1];
    		denominator1 = arg1[2];
    		break;
    	case 2:
    		numerator1 = arg1[0];
    		denominator1 = arg1[1];
    		break;
    	default:
    		numerator1 = arg1[0];
    	}
    	
    	
    	//		Parsing second operand

    	//Split the number into its separate parts
    	String[] arg2string = num2.split("[\\/_]");
    	//Convert to integers
    	int[] arg2 = new int[arg2string.length];
    	for(int i=0;i<arg2string.length;i++)
    	{
    		arg2[i] = Integer.parseInt(arg2string[i]);
    	}
    	//Convert to numerator and denominator
    	int numerator2;
    	int denominator2 = 1;
    	switch(arg2.length)
    	{
    	case 3:
    		if(arg2[0]<0) arg2[1] *= -1;
    		numerator2 = arg2[0]*arg2[2]+arg2[1];
    		denominator2 = arg2[2];
    		break;
    	case 2:
    		numerator2 = arg2[0];
    		denominator2 = arg2[1];
    		break;
    	default:
    		numerator2 = arg2[0];
    	}
    	
    	//Print debug info
    	/*
    	System.out.println("expression broken down"+Arrays.toString(pieces));
    	System.out.println("arg1 broken down: "+numerator1+" / "+denominator1);
    	System.out.println("arg2 broken down: "+numerator2+" / "+denominator2);
    	*/
    	
    	
    	//		Performing calculations
    	
    	int numerator3 = 0;
    	int denominator3 = 1;
    	
    	switch(oper.charAt(0))
    	{
    	//Multiplication
    	case '*':
    		numerator3 = numerator1*numerator2;
    		denominator3 = denominator1*denominator2;
    		break;
    	//Division
    	case '/':
    		if(numerator2==0) return "error";
    		numerator3 = numerator1*denominator2;
    		denominator3 = denominator1*numerator2;
    		break;
    	//Subtraction
    	case '-':
    		numerator2 *= -1;
    	//Addition
    	case '+':
    		numerator3 = numerator1*denominator2+numerator2*denominator1;
    		denominator3 = denominator1*denominator2;
    		
    	}
    	
    	
    	//		Formatting output
    	
    	//Reduce the fraction
    	int factor = gcd(numerator3,denominator3);
    	numerator3 /= factor;
    	denominator3 /= factor;
    	if(denominator3<0)
		{
			numerator3 *=-1;
			denominator3 *=-1;
		}
    	//Whole number result
    	if(denominator3 == 1) return numerator3+"";
    	//Mixed fraction result
    	if(denominator3 < Math.abs(numerator3)) return numerator3 / denominator3 + "_" + Math.abs(numerator3 % denominator3) + "/" + denominator3;
        //Proper fraction result
    	return numerator3+"/"+denominator3;
    }
    
    public static int gcd(int a, int b)
    {
        a = Math.abs(a);
        b = Math.abs(b);
        int max = Math.max(a, b);
        int min = Math.min(a, b);
        while (min != 0) {
            int tmp = min;
            min = max % min;
            max = tmp;
        }
        return max;
    }
    
    public static int lcm(int a, int b)
    {
        int gcd = gcd(a, b);
        return (a*b)/gcd;
    }
}
