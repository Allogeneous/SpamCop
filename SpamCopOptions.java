package spamcop;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

public class SpamCopOptions {
    
    //Variables
    
    public static String[] defaults = {"Spam Cop Options and Settings!", "UseWarnTimer: true", "UseMessageRepeatTimer: true" ,"UseMaxSingleWordMessageLength: true", "UseRepeatLetters: true", "UseMatchingLetters: true", "UseMatchingWords: true", "UseNoRepeatMessage: true", "MaxWordLength: 25" , "CharacterPercent: 80", "WordPercent: 80", "RepeatLetters: 4", "CorrectionsWordLeght: 2", "CorrectionsMessageLength: 10", "UseCorrectionFilter: true", "KickTimerSpeed (Minutes): 5", "RepeatTimerSpeed (Seconds): 5, TimesToSpamUntilKicked: 5"};
    public static String[] optionArguments = new String[17];
    public static boolean useWarnTimer, useMessageRepeatTimer, useMaxSingleWordMessageLength, useRepeatLetters, useMatchingLetters, useMatchingWords, useNoRepeatMessage, useCorrectionFilter;
    public static int maxWordLength, characterPercent, wordPercent, repeatLetters , correctionsWordLeght, correctionsMessageLength, kickTimerSpeed, repeatTimerSpeed, spamTimesToKick;
    
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
                        optionArguments[iterations - 1] = split[1];
                    }catch (Exception e){
                        String[] split = defaults[iterations].split(":");
                        optionArguments[iterations - 1] = split[1].substring(1);
                        System.out.println("[SpamCop] Option not found for " + split[0] + ", using default setting!");
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
            useMatchingWords = Boolean.parseBoolean(optionArguments[5]);
            useNoRepeatMessage = Boolean.parseBoolean(optionArguments[6]);
            maxWordLength = Integer.parseInt(optionArguments[7]);
            characterPercent = Integer.parseInt(optionArguments[8]);
            wordPercent = Integer.parseInt(optionArguments[9]);
            repeatLetters = Integer.parseInt(optionArguments[10]);
            correctionsWordLeght = Integer.parseInt(optionArguments[11]);
            correctionsMessageLength = Integer.parseInt(optionArguments[12]);
            useCorrectionFilter = Boolean.parseBoolean(optionArguments[13]);
            kickTimerSpeed = Integer.parseInt(optionArguments[14]);
            repeatTimerSpeed = Integer.parseInt(optionArguments[15]);
            spamTimesToKick = Integer.parseInt(optionArguments[16]);
            }catch(Exception e){
            System.out.println("[SpamCop] Error in options file, switching to defaults!");
            useWarnTimer = true;
            useMessageRepeatTimer = true;
            useMaxSingleWordMessageLength = true;
            useRepeatLetters = true;
            useMatchingLetters = true;
            useMatchingWords = true;
            useNoRepeatMessage = true;
            maxWordLength = 25;
            characterPercent = 80;
            wordPercent = 80;
            repeatLetters = 4;
            correctionsWordLeght = 2;
            correctionsMessageLength = 10;
            useCorrectionFilter = true;
            kickTimerSpeed = 5;
            repeatTimerSpeed = 5;
            spamTimesToKick = 5;
            }
        }
    
        
}
