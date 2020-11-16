 /**
   * Author " Teja 
   * Java program : Stable Matching Problem.
   * * @exception if data input file is not available system throws exceptions and stop execution
   * @Program Result : Displays the stable matchings between hospitals and students after the execution and will also write output into file.
   */
package com.gs.stma;
import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.Map;
import java.io.PrintWriter;
public class StableMatchingProblem {

	// Collection variables to hold the dataset as per the input data file and they are used for processing the matching 
	private ArrayList hospitals = new ArrayList();
	private ArrayList students = new ArrayList();
	private HashMap hospitalPreferences = new HashMap();
	private HashMap hospitalsPostionsCount = new HashMap();
	private HashMap studentPreferences= new HashMap();
        private String folderPath="c://Temp/";
	
	// initialize the program with constructor
	public StableMatchingProblem() {
		}
	
	/**
	   * Read the Input file and assign the dataset into variables.
	   * Validate data upon reading the file.
	   * @exception - File not found or data format wrong in file
	  */ 
	private void InitializeData() {


		try {
		File dataFile = new File(folderPath+"InputData.txt");
			String row;			
			boolean isStudentFlag=false;
			int pos=0;
			String student;
			String hospital;
			BufferedReader textFileReader = new BufferedReader(new FileReader(dataFile));
			while ((row = textFileReader.readLine()) != null) {
				if (row.trim().isEmpty()|| row.trim().equals("")){ 
					isStudentFlag=true;
		           // System.out.println("No input detected!");                   
				}else {
					String[] data = row.split(",");
					//System.out.println(data.length);
					if(isStudentFlag){
						student=data[0];
						students.add(student);
						pos=1;
						studentPreferences.put(student, CreateList(data,pos));
						
					}else{
						hospital=data[0];
						hospitals.add(hospital);
						hospitalsPostionsCount.put(hospital, data[1]);
						pos=2;
						hospitalPreferences.put(hospital,CreateList(data,pos) );
					}
					
				}
			}// While ends
//			System.out.println("*** students ** -"+ students);
//			System.out.println("*** hospitals ** -"+ hospitals);
//			System.out.println("*** studentPreferences ** -"+ studentPreferences);
//			System.out.println("*** hospitalPreferences ** -"+ hospitalPreferences);
//			System.out.println("*** hospitalPostionCount ** -"+ hospitalPostionCount);
		
			textFileReader.close();
			
		}catch(Exception e){
			System.out.println ("*** - "+e.toString()+"  - **** ");
		}
	}
	
	/**
	   * Read the Input array object and create list based on the input position
	   * @param : data string array ,  pos : starting position of the element  
	   * @return  return list.
	  */ 
	static ArrayList CreateList(String[] data,int pos){
		
		ArrayList dataSet = new ArrayList();
		//System.out.println("** data.length ** -"+data.length);
		for (int i = pos; i < data.length; i++){
			dataSet.add(data[i]);
		}
	    return dataSet;
		
	}
	/**
	   * Execute stable matching based on the data set and create the matching list.
           * then format the matching result to display in right format and write the output into file
	   * @return  none
	  */ 
	
	private void StableMatching(){
		boolean isProcessComplete=false;
		System.out.println("*** students ** -"+ students);
		System.out.println("*** hospitals ** -"+ hospitals);
		System.out.println("*** studentPreferences ** -"+ studentPreferences);
		System.out.println("*** hospitalPreferences ** -"+ hospitalPreferences);
		System.out.println("*** hospitalsPostionsCount ** -"+ hospitalsPostionsCount);
//		ArrayList hospitals = hospitals;
		ArrayList hospitalStudentPrefersList;
		ArrayList studentHospitalPrefersList;
		HashMap matchingList = new HashMap();
		HashMap hospitalsPostionsFilled =new HashMap();
		ArrayList hospitalsComplete = new ArrayList();
		ArrayList studentAssignedList = new ArrayList();
		String hospitalName="";
		String studentName;
		while (isProcessComplete==false){
			//Process all hospital and their preferences until it is done
			for (int i = 0; i < hospitals.size(); i++){
				hospitalName=hospitals.get(i).toString();
				System.out.println("\n\n** Hospital name : "+hospitalName); 
				hospitalStudentPrefersList=(ArrayList)hospitalPreferences.get(hospitalName);
				System.out.println( hospitalName+" hospital student preferences list : "+hospitalStudentPrefersList);
				ArrayList removeStudentList = new ArrayList();
				//Process hospital student preferences List in loop
				for (int j = 0; j < hospitalStudentPrefersList.size(); j++){
					//check if already hospital is filled with their position
				if(!hospitalsComplete.contains(hospitalName)){
					studentName=hospitalStudentPrefersList.get(j).toString();
					System.out.println("** Student Name : "+studentName);
					// check if student is already assigned to hospital  
					// else remove student from list if already assigned.
					if(!studentAssignedList.contains(studentName)){					
						
						studentHospitalPrefersList=(ArrayList)studentPreferences.get(studentName);
						System.out.println(studentName+"  hospitals preferences list : "+studentHospitalPrefersList);
						
						// check if the hospital is in student preference list to remove student from hospital list
						if(studentHospitalPrefersList.indexOf(hospitalName)!= -1){
							boolean isMatched=false;
							// exact match check based on first preference
							if(studentHospitalPrefersList.indexOf(hospitalName)==0){
								isMatched=true;							
							}else{
								System.out.println(studentName+" has other hospitals are in priority list than "+ hospitalName);
								System.out.println("Student first preference is "+studentHospitalPrefersList.get(0)+" so move on.. ");
								// if hospital is not available for student to select remove from student preference list
								if(!hospitals.contains(studentHospitalPrefersList.get(0)) )
								{
									System.out.println(hospitalName+ " hospital is not preffering student "+studentName+" OR hospital is already filled" );
									studentHospitalPrefersList.remove(studentHospitalPrefersList.get(0).toString());
									studentPreferences.put(studentName,studentHospitalPrefersList);
									isMatched=false;
								}
								// if hospital is already filled then remove from student preference list
								if(hospitalsComplete.contains(studentHospitalPrefersList.get(0))){
									System.out.println(studentHospitalPrefersList.get(0)+ " hospital is already filled .. so remove" );
									studentHospitalPrefersList.remove(studentHospitalPrefersList.get(0).toString());
									studentPreferences.put(studentName,studentHospitalPrefersList);
									isMatched=false;
								}
							
							}
							
							if (isMatched){
								matchingList.put(studentName, hospitalName);
								removeStudentList.add(studentName);
								System.out.println("** Matched List :"+matchingList);
								studentAssignedList.add(studentName);
								// Update filled positions and check the available positions are filled or not
								if(hospitalsPostionsFilled.containsKey(hospitalName))
								{
									int count=Integer.parseInt(hospitalsPostionsFilled.get(hospitalName).toString());
									count=count+1;
									hospitalsPostionsFilled.put(hospitalName, String.valueOf(count));
								}else
								{
									hospitalsPostionsFilled.put(hospitalName, "1");
								}
								//System.out.println("** hospitals Postions Filled :"+hospitalsPostionsFilled);
									// checking the available positions are filled or not
								if (hospitalsPostionsFilled.get(hospitalName).equals(hospitalsPostionsCount.get(hospitalName))){
									System.out.println("**filled postions "+hospitalsPostionsFilled.get(hospitalName)+" Total available positions "+hospitalsPostionsCount.get(hospitalName));
									hospitalsComplete.add(hospitalName);
			
								}
								

								
							}
							
						}else {
							System.out.println(studentName+" is not preffering "+hospitalName+" so removed from the hospital list ");
							removeStudentList.add(studentName);
							//hospitalPreferences.remove(studentName);
							
						}
						
						
					}else{
						System.out.println(studentName+ " has already assigned. so removing");
						
						removeStudentList.add(studentName);
					 }
					}	
				}//for (int j = 0; j < hospitalStudentPrefersList.size(); j++){
				
				System.out.println("** hospital student preferences remove list : "+removeStudentList);
				hospitalStudentPrefersList.removeAll(removeStudentList);
				hospitalPreferences.put(hospitalName,hospitalStudentPrefersList);
				System.out.println("** hospital student preferences left : "+hospitalStudentPrefersList);
				//System.out.println("** hospital Preferences left size : "+hospitalPreferences.size());
				//check if all hospital assignments done or not
				if(hospitalStudentPrefersList.size()== 0){
					System.out.println("** hospital "+hospitalName +" Removed from the list");
					if (!hospitalsComplete.contains(hospitalName)){
						hospitalsComplete.add(hospitalName);
					}
				}
				System.out.println( " Student Assigned List : "+studentAssignedList);
			} // End if
			
			System.out.println("** hospitals Complete "+hospitalsComplete);
			hospitals.removeAll(hospitalsComplete);
	
			System.out.println("** hospitals left : "+hospitals+"\n\n");
			if(hospitals.size()== 0){
			isProcessComplete=true;
			}
		
		}//while (isProcessComplete==false)
		
		//System.out.println("** Final Assignments  : "+matchingList+"\n\n");
                
                // formatting the result
                
                Set set = matchingList.entrySet();
                Iterator itr = set.iterator();
                String value="";
                String key="";
                HashMap finalResult = new HashMap();
                while(itr.hasNext()) {
                        Map.Entry element = (Map.Entry)itr.next();                        
                        value=element.getValue().toString();
                        key=element.getKey().toString();
                        ArrayList preferences = new ArrayList();
                        if(finalResult.containsKey(value)){
                           preferences=(ArrayList)finalResult.get(value);
                           preferences.add(key);
                           finalResult.put(value, preferences);
                        }else{
                            
                            preferences.add(key);
                            finalResult.put(value, preferences);
                        }
                }
                // Display the final result and write into output file
                
                 System.out.println("******  Result  ******  \n");
                 set = finalResult.entrySet();
                 itr = set.iterator();
                 
                 try{
                       PrintWriter pw = new PrintWriter(folderPath+"OutputData.txt", "UTF-8");
                       while(itr.hasNext()) {
                              Map.Entry element = (Map.Entry)itr.next();                        
                              ArrayList prefers=(ArrayList)element.getValue();
                              key=element.getKey().toString();
                              String list="";
                              for (int i = 0; i < prefers.size(); i++)
                              {
                                 list=list+prefers.get(i)+", ";
                              }
                              if(list.length()>1) list=list.substring(0,list.length()-2);
                              System.out.println(key+","+list);
                              pw.println(key+","+list);
                       }
                       pw.close();
                  }catch(Exception e){
			System.out.println ("*** - "+e.toString()+"  - **** ");
		 }
                 
	}
	
	public static void main(String[] args) {
		// Initialize and create instance of the class
		StableMatchingProblem sm = new StableMatchingProblem();
                // Load the data
		sm.InitializeData();
                //Excute Stable Matching
		sm.StableMatching();

	}

}
