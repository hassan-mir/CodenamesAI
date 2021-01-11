package Setup;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/*
WordsSetup class will take 25 words and assign them a team
Red and Blue: one team has 8 words and the other 9 (team with 9 words goes first)
Civilian: 7 words
Assassin: 1 word
*/
public class WordsSetup {
    public Team firstGoTeam;

    private final int blueWordsNum;
    private final int redWordsNum;
    private final int civilianWordsNum;
    private final int assassinWordsNum;
    private final int totalWordsNum;

    private List<String> allWords;
    private List<Word> gameWords;

    //default setup
    public WordsSetup() throws Exception {
       this(9, 8, 7, 1, null);
    }

    //give option for custom set up
    public WordsSetup(int firstGoWordsNum, int secondGoWordsNum, int civilianWordsNum, int assassinWordsNum, List<String> customWords) throws Exception {

        boolean isDefault = (customWords == null || customWords.isEmpty());

        Random r = new Random(); //For random first go team

        //Setup card numbers
        this.totalWordsNum = firstGoWordsNum + secondGoWordsNum + civilianWordsNum + assassinWordsNum;
        this.civilianWordsNum = civilianWordsNum;
        this.assassinWordsNum = assassinWordsNum;
        firstGoTeam = r.nextBoolean() ? Team.BLUE : Team.RED;
        if(firstGoTeam == Team.BLUE) {
            this.blueWordsNum = firstGoWordsNum;
            this.redWordsNum = secondGoWordsNum;
        }
        else{
            blueWordsNum = secondGoWordsNum;
            redWordsNum = firstGoWordsNum;
        }

        //generate words for default case/check if custom case valid
        if(isDefault) {
            importAllWords();
            setGameWords();
        }
        else{
            if(customWords.size() != totalWordsNum) throw new IllegalArgumentException();
        }
        assignWordsTeams();
        setAllWordsToVisible();
    }

    private void setAllWordsToVisible() {
        gameWords.stream().forEach(word -> word.setVisible(true));
    }

    private void assignWordsTeams() throws Exception {
        Queue<String> wordsQ = new LinkedList<>(allWords);
        gameWords = new ArrayList<>();
        for (int i = 0; i < blueWordsNum; i++)
            gameWords.add(new Word(wordsQ.poll(), Team.BLUE));
        for (int i = 0; i < redWordsNum; i++)
            gameWords.add(new Word(wordsQ.poll(), Team.RED));
        for (int i = 0; i < civilianWordsNum; i++)
            gameWords.add(new Word(wordsQ.poll(), Team.CIVILIAN));
        for (int i = 0; i < assassinWordsNum; i++)
            gameWords.add(new Word(wordsQ.poll(), Team.ASSASSIN));

        if(!wordsQ.isEmpty())
            throw new Exception("ERROR: words assigned but wordsQ not empty");


    }

    private void setGameWords() {
        Collections.shuffle(allWords);
        allWords = allWords.subList(0,totalWordsNum);
    }

    public List<Word> getGameWords() {
        return gameWords;
    }

    public Team getFirstGoTeam() {
        return firstGoTeam;
    }

    public void importAllWords(){
        try(CSVReader csvReader = new CSVReader(new FileReader("Codenames-AllWords.csv"));) {
            allWords = new LinkedList<>();
            String[] line;
            while((line = csvReader.readNext()) != null)
                allWords.add(String.join(",",line));
        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
        }
    }

    public void printAllGameWords(){
        System.out.println("BLUE:");
        gameWords.stream().filter(word -> word.getTeam() == Team.BLUE)
                .forEach(word -> System.out.println("\t" + word));
        System.out.println("RED:");
        gameWords.stream().filter(word -> word.getTeam() == Team.RED)
                .forEach(word -> System.out.println("\t" + word));
        System.out.println("CIVILIAN:");
        gameWords.stream().filter(word -> word.getTeam() == Team.CIVILIAN)
                .forEach(word -> System.out.println("\t" + word));
        System.out.println("ASSASSIN:");
        gameWords.stream().filter(word -> word.getTeam() == Team.ASSASSIN)
                .forEach(word -> System.out.println("\t" + word));
    }
}
