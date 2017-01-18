package spamcop;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class SpamCopOptions {
    
    //Variables
    
    public static String[] defaults = {"Spam Cop Options and Settings!", "UseWarnTimer: true", "UseMessageRepeatTimer: true" ,"UseMaxSingleWordMessageLength: true", "UseRepeatLetters: true", "UseMatchingLettersPercent: true", "UseNoRepeatMessage: true", "MaxWordLength: 25" , "CharacterPercent: 80", "RepeatLetters: 4", "CorrectionsWordLeght: 2", "CorrectionsMessageLength: 10", "UseCorrectionFilter: true", "KickTimerSpeed (Minutes): 5", "RepeatTimerSpeed (Seconds): 5", "TimesToSpamUntilKicked: 5", "UseMatchingWordsPercent: true", "WordPercent: 80", "RepeatWords: 3", "UseRepeatWords: true"};
    public static String[] optionArguments = new String[19];
    public static boolean useWarnTimer, useMessageRepeatTimer, useMaxSingleWordMessageLength, useRepeatLetters, useMatchingLetters, useNoRepeatMessage, useCorrectionFilter, useMatchingWords, useRepeatWords;
    public static int maxWordLength, repeatLetters , correctionsWordLeght, correctionsMessageLength, kickTimerSpeed, repeatTimerSpeed, spamTimesToKick, repeatWords;
    public static double characterPercent, wordPercent;
    
    //Writes default options to the options file
    
    public static void writeDefaults(File f) throws IOException{
        
        FileWriter fstream = new FileWriter(f, true);
        BufferedWriter output = new BufferedWriter(fstream);
        for (String option : defaults) {
            output.write(option);
            output.newLine();
        }
        output.close();
    }
    
    //Reads the options in the options files
    
    public static void parseOptionArguments(File f) throws IOException{
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            int iterations = 0;
            while ((line = br.readLine()) != null) {
                if(iterations > 0){
                    try{
                        String[] split = line.split(":");
                        optionArguments[iterations - 1] = split[1].substring(1);
                    }catch (Exception e){
                        String[] split = defaults[iterations].split(":");
                        optionArguments[iterations - 1] = split[1].substring(1);
                        System.out.println("[SpamCop] Option not found for " + split[iterations] + ", using default setting!");
                    }
                    
                }
                iterations++;
            }
        }
    }
        
    
        //Trys to set the options to the options in the config, if there is an error it goes to default settings
    
        public static void setOptions(){
            try{
            useWarnTimer = Boolean.parseBoolean(optionArguments[0]);
            useMessageRepeatTimer = Boolean.parseBoolean(optionArguments[1]);
            useMaxSingleWordMessageLength = Boolean.parseBoolean(optionArguments[2]);
            useRepeatLetters = Boolean.parseBoolean(optionArguments[3]);
            useMatchingLetters = Boolean.parseBoolean(optionArguments[4]);
            useNoRepeatMessage = Boolean.parseBoolean(optionArguments[5]);
            maxWordLength = Integer.parseInt(optionArguments[6]);
            characterPercent = Double.parseDouble(optionArguments[7]);
            repeatLetters = Integer.parseInt(optionArguments[8]);
            correctionsWordLeght = Integer.parseInt(optionArguments[9]);
            correctionsMessageLength = Integer.parseInt(optionArguments[10]);
            useCorrectionFilter = Boolean.parseBoolean(optionArguments[11]);
            kickTimerSpeed = Integer.parseInt(optionArguments[12]);
            repeatTimerSpeed = Integer.parseInt(optionArguments[13]);
            spamTimesToKick = Integer.parseInt(optionArguments[14]);
            useMatchingWords = Boolean.parseBoolean(optionArguments[15]);
            wordPercent = Double.parseDouble(optionArguments[16]);
            repeatWords = Integer.parseInt(optionArguments[17]);
            useRepeatWords = Boolean.parseBoolean(optionArguments[18]);
            }catch(Exception e){
            System.out.println("[SpamCop] Error in options file, switching to defaults!");
            useWarnTimer = true;
            useMessageRepeatTimer = true;
            useMaxSingleWordMessageLength = true;
            useRepeatLetters = true;
            useMatchingLetters = true;
            useNoRepeatMessage = true;
            maxWordLength = 25;
            characterPercent = 80;
            repeatLetters = 4;
            correctionsWordLeght = 2;
            correctionsMessageLength = 10;
            useCorrectionFilter = true;
            kickTimerSpeed = 5;
            repeatTimerSpeed = 5;
            spamTimesToKick = 5;
            useMatchingWords = true;
            useRepeatWords = true;
            }
        }
    
        
}
