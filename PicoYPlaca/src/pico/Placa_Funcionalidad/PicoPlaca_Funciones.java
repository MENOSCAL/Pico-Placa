/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pico.Placa_Funcionalidad;

import com.toedter.calendar.JDateChooser;
import static java.awt.Color.black;
import static java.awt.Color.red;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextField;

/**
 *
 * @author victor
 */
public class PicoPlaca_Funciones {
    
   //This function allows to validate the plate entered by the user because it must be only 6 or 7 digits
   public void plateValidator(JTextField TextField_plate, JLabel Label_error1, JLabel Label_error2, JLabel Label_results) {
        String plateNumber=(TextField_plate.getText());//we get the string of the plate entered by the user
            if(plateNumber.length()==6){ //we increased a character to the string to identify when the length of the plate was only 6 digits
               plateNumber=plateNumber+"0";
            }
        char [] plateCharacters= plateNumber.toCharArray();// we transform the text of the plate into an array in order to validate the board
            if(plateCharacters.length==6){ //if the plate was only 6 digits then it has a 0 at the end of the array which must be replaced by the previous digit which will serve to determine whether or not there is pico y placa
               plateCharacters[6]=plateCharacters[5];        
            } 
            if (plateCharacters.length==7){  // this condition will only be executed when the user types a complete plate, otherwise the error messages will be displayed
                validatorWithPatterns(0,0,2,Label_error1,Label_error2,TextField_plate,"[a-zA-Z]", Label_results); //We call validatorPatterns function
                validatorWithPatterns(0,3,6,Label_error1,Label_error2,TextField_plate,"[0-9]", Label_results);
            }
            else {
             Label_error1.setVisible(true);
             Label_error2.setVisible(true);
            }
    }
   //this function will validate that the 3 first digits be letters, and the rest numbers
   public void validatorWithPatterns(int iterator, int firstValue, int lastValue,JLabel Label_error1, JLabel Label_error2,JTextField TextField_plate, String patternNumbersOrLetters, JLabel Label_results ){
       String[] temporalPlateNumber = new String[7]; //this array will be to store the plate number and make the comparisons
       String plateNumber=(TextField_plate.getText());//we get the plate from the text field
       if(plateNumber.length()==6){ //here add a letter when the plate is only with 6 digits
          plateNumber=plateNumber+"0";
        }    
       char [] plateCharacters= plateNumber.toCharArray();// we transform the text of the plate into an array in order to validate the board
       Pattern patternLettersAndNumbers= Pattern.compile(patternNumbersOrLetters); //we will use the patterns function to make sure that the first 3 digits of the plate are uppercase or lowercase letters and the rest are only numbers through regular expressions
       Matcher matcher;// the matcher will compare the string
       for(iterator= firstValue; iterator <= lastValue;iterator++){ //we will compare each digit of the plate
           temporalPlateNumber[iterator]=Character.toString(plateCharacters[iterator]);// we transform the plate to string because the matcher function only accepts string as parameters
           matcher = patternLettersAndNumbers.matcher(temporalPlateNumber[iterator]);
               if(!matcher.find()){ //if regular expressions are not found, error messages are displayed
                  Label_error1.setVisible(true);
                  Label_error2.setVisible(true);
                  break;
                }
                else { //restablish the error messages in case of new queries
                   Label_error1.setVisible(false);
                   Label_error2.setVisible(false);
                }
        }
   }
    //This function will verify the conditions of pico y placa according with the last digit of the plate
   public void verificacionDeRestriccion(JDateChooser Calendar, JComboBox<String> ComboBox_schedules,JTextField TextField_plate,JLabel Label_results, JLabel Label_error1, JLabel Label_error2) {
        int dayOfWeek=Calendar.getCalendar().getTime().getDay();//the day of week is an int value from 0 to 6, where 0 is sunday, 1 is monday, 2 is tuesday, 3 is wednesday, 4 is thursday, 5 is friday and 6 is saturday
        String plateNumber=(TextField_plate.getText());//we obtain the plate of the text field
        int lastPlateNumber=0;// here is stored te last number of the plate
        if(plateNumber.length()==6){ //we add a digit if the plate was only to 6 digits
           plateNumber=plateNumber+"b";
        }
        char [] plateCharacters= plateNumber.toCharArray();// we transform the plate in array for its validation 
        if(plateCharacters.length!=7){
           Label_error1.setVisible(true);
           Label_error2.setVisible(true);
        }
        else{
        if(plateCharacters[6]=='b'){ //if the plate has only 6 digits then it has a b at the end which must be replaced by the previous digit which will serve to determine whether or not there is pico y placa
           plateCharacters[6]=plateCharacters[5];
           }
        }  
        if(plateCharacters.length==7){ //if the plate has 7 digits we take the last digit to compare, otherwise it will not enter to the switch case
           lastPlateNumber=Character.getNumericValue(plateCharacters[6]);
        }
        else{
           dayOfWeek=8;
           Label_results.setText("");//returns the result text to white when the plate length is difrent of 6
        }
        // switch function will set the messages when the cars are able or not to road
        switch (dayOfWeek){
           case 0:// Case 0 corresponds to sunday
               if(lastPlateNumber>9){ //if the user writes a leter as the last digit by mistake it will be transformed to int, if this happens the result makes blank and the error message appear
                  Label_results.setText("");
               }
               else{
                Label_results.setForeground(black);
                Label_results.setText("USTED PUEDE CIRCULAR");}
           break;
           case 1://Case 1 corresponds to monday
                verificationResults(1,2,Label_results,ComboBox_schedules,Label_error1,Label_error2,TextField_plate,Calendar);
           break;
           case 2:// Case 2 corresponds to tuesday
                 verificationResults(3,4,Label_results,ComboBox_schedules,Label_error1,Label_error2,TextField_plate,Calendar);
                
           break;
           case 3://Case 3 corresponds to wednesday
                 verificationResults(5,6,Label_results,ComboBox_schedules,Label_error1,Label_error2,TextField_plate,Calendar);
           break;
           case 4://Case 4 corresponds to thursday
               verificationResults(7,8,Label_results,ComboBox_schedules,Label_error1,Label_error2,TextField_plate,Calendar);
           break;
           case 5:// Case 5 corresponds to friday
                verificationResults(9,0,Label_results,ComboBox_schedules,Label_error1,Label_error2,TextField_plate,Calendar);
           break;
           case 6:// Case 6 corresponds to saturday and has the same function than sunday
               if(lastPlateNumber>9){
                  Label_results.setText("");
               }
               else{
                Label_results.setForeground(black);
                Label_results.setText("USTED PUEDE CIRCULAR");}
           break;   
 }
  
 }
   //This function will establish the relations between the plates an days of restrictions
   public void verificationResults(int lastPlateNumber1, int lastPlateNumber2, JLabel Label_results, JComboBox<String> ComboBox_schedules, JLabel Label_error1,JLabel Label_error2, JTextField TextField_plate,JDateChooser Calendar){
        String plateNumber=(TextField_plate.getText());
        int lastPlateNumber=0;// here it is stored te last number of the plate
        if(plateNumber.length()==6){ 
           plateNumber=plateNumber+"b";
        }
        char [] plateCharacters= plateNumber.toCharArray();//transform the string into array to be able to compare
        if(plateCharacters.length!=7){ //if the plate has not 7 digits the program will show the errors
           Label_error1.setVisible(true);
           Label_error2.setVisible(true);
        }
        else{
            if(plateCharacters[6]=='b'){
               plateCharacters[6]=plateCharacters[5];
            }
        }  
        if(plateCharacters.length==7){ //the plate must have 7 digits to continue
           lastPlateNumber=Character.getNumericValue(plateCharacters[6]);}
        if(lastPlateNumber==lastPlateNumber1||lastPlateNumber==lastPlateNumber2){ //compare the last digit of the plate in pairs 1-2, 3-4, 5-6, 7-8, 9-0
            if(ComboBox_schedules.getSelectedItem()=="07h00-09h30"||ComboBox_schedules.getSelectedItem()=="16h00-19h30"){ //pico y placa apply in mornings from 7h00 to 9h30 and in the afternoon from 16h00 to 19h30
               Label_results.setForeground(red);
               Label_results.setText("USTED NO PUEDE CIRCULAR");
            }
            else{  
                Label_error1.setVisible(false);
                Label_results.setForeground(black);
                Label_results.setText("USTED PUEDE CIRCULAR");
            }
        }
        else{
            if(lastPlateNumber>9){//if the user writes a leter as the last digit by mistake it will be transformed to int, if this happens the result makes blank and the error message appear
            Label_results.setText("");
            } else{
            Label_results.setForeground(black);//if the plate not corresponds to the pico y placa day it will be able to road any time
            Label_results.setText("USTED PUEDE CIRCULAR");
            }  
        }
   }
}
