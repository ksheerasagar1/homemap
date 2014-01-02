import java.util.Arrays;
import java.util.List;


public class StopWords {

        public static List<String> stopWordsList(){
                String stopWordsString = "a,able,about,across,after,all,almost,also,am,among,an,and,any,are,as,at,be,because,been,but,by,can,cannot,could,dear,did,do,does,either,else,ever,every,for,from,get,got,had,has,have,he,her,hers,him,his,how,however,i,if,in,into,is,it,its,just,least,let,like,likely,may,me,might,most,must,my,neither,no,nor,not,of,off,often,on,only,or,other,our,own,rather,said,say,says,she,should,since,so,some,than,that,the,their,them,then,there,these,they,this,tis,to,too,twas,us,wants,was,we,were,what,when,where,which,while,who,whom,why,will,with,would,yet,you,your";
                
                List<String> stopWordsList = Arrays.asList(stopWordsString.split(",")) ;
                
                return stopWordsList;
        }
        
        public static String removeStopWords(String aString){
                StringBuilder temp = new StringBuilder();
                
                List<String> inputList = Arrays.asList(aString.split("\\s+"));
                
                for( int i = 0; i < inputList.size() ; i++){
                        if(!StopWords.stopWordsList().contains(inputList.get(i).toLowerCase())){
                                temp.append(inputList.get(i)) ;
                                temp.append(" ") ;
                        }
                }
                return temp.toString();
        }
        
}