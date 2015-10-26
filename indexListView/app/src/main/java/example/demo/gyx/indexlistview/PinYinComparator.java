package example.demo.gyx.indexlistview;

import java.util.Comparator;

/**
 * Created by gyx on 2015/10/25.
 */
public class PinYinComparator implements Comparator<Model>{

    @Override
    public int compare(Model lhs, Model rhs) {

        if (lhs.getSortLetters().equals("@")
                || rhs.getSortLetters().equals("#")){
            return -1;

        }else if (lhs.getSortLetters().equals("#")
                || rhs.getSortLetters().equals("@")){
            return 1;

        }else {
            return lhs.getSortLetters().compareTo(rhs.getSortLetters());
        }
    }
}
