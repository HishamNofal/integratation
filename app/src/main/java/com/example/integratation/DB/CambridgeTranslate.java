package com.example.integratation.DB;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;

 class CambridgeTranslate {

    private String germanWord;
    private String englishTranslation ="";
     private LinkedHashSet<String> properGermanFormWithRepetitionWithoutRepetition;


     CambridgeTranslate(String germanWord){

    this.germanWord = germanWord;

}

     String[] theWords() {


         String[] properGermanFormFinalAndEnglishTranslationFinalStringList = new String[2];
         StringBuilder properGermanFormTempLines = new StringBuilder();
        try {
            Document translationHTMLDoc = Jsoup.connect("https://dictionary.cambridge.org/dictionary/german-english/"+ germanWord).get();
            Element englishTranslationRaw = translationHTMLDoc.select("meta").select("[name=description]").first();
            Elements properGermanFormRawList = translationHTMLDoc.select("div").select("[class=dpos-h di-head normal-entry]");

            String englishTranslationRawContent = englishTranslationRaw.attr("content");

            if (englishTranslationRawContent.contains(" translate:")){

                englishTranslation = englishTranslationRawContent.replaceAll("^.*:|\\..*","").trim() ;
                String[] englishTranslationRawContentSplit = englishTranslation.split(", ");
                LinkedHashSet<String> englishTranslationRawContentSplitRepetition = new LinkedHashSet<>(Arrays.asList(englishTranslationRawContentSplit));
                englishTranslation = englishTranslationRawContentSplitRepetition.toString().replaceAll("([\\[\\]])","");

            }

            for (Element properGermanFormRawLine : properGermanFormRawList){
                //the type
                if (properGermanFormRawLine.select("span").select("[class=pos dpos]").text().equals("noun")) {

                    properGermanFormTempLines.append(properGermanFormRawLine.text()).append(" ");
                }
            }

            String properGermanFormWithRepetition = properGermanFormTempLines.toString().replaceAll("/\\S*/|]|\\[|\\||oder|noun|also|\\(|\\)", "");

            properGermanFormWithRepetitionWithoutRepetition = new LinkedHashSet<>(Arrays.asList(properGermanFormWithRepetition.split("\\s+")));

            properGermanFormFinalAndEnglishTranslationFinalStringList[0]= englishTranslation;
            properGermanFormFinalAndEnglishTranslationFinalStringList[1]= fixArticles(properGermanFormWithRepetitionWithoutRepetition);

        }
        catch (Exception e){}


        return properGermanFormFinalAndEnglishTranslationFinalStringList;}


        //it replaces the articles names with the articles themselves 
        // and it makes them advance in front of the noun
        private String fixArticles(LinkedHashSet<String> properGermanFormHashListWithoutRepetitionWords){

        ArrayList<String> properGermanFormListTempHolder = new ArrayList<>();

        for (String properGermanFormWithoutRepetitionWords: properGermanFormHashListWithoutRepetitionWords ){

            switch (properGermanFormWithoutRepetitionWords) {
                case "masculine":
                    properGermanFormListTempHolder.add(
                            properGermanFormListTempHolder.size()-1,"der");
                    break;
                case "feminine":
                    properGermanFormListTempHolder.add(
                            properGermanFormListTempHolder.size()-1,"die");
                    break;
                case "neuter":
                    properGermanFormListTempHolder.add(
                            properGermanFormListTempHolder.size()-1,"das");
                    break;
                default: properGermanFormListTempHolder.add(properGermanFormWithoutRepetitionWords);break;
            }


        }
        return properGermanFormListTempHolder.toString().replaceAll("[]\\[,]","");
    }


}
