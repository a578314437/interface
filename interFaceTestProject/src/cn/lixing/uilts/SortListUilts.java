package cn.lixing.uilts;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class SortListUilts {
	public static ArrayList<String> sortList(ArrayList<String> strings){
		Collections.sort(strings, new Comparator<String>() {
			@Override
			public int compare(String o1, String o2) {
				System.out.println("排序字符串：" + o1 + "," + o2);

                int i = o1.compareTo(o2);
                System.out.println("排序结果" + i);
                if ( i > 0 ) {
                    return 1;
                } else {
                    return -1;
                }
			}
		});
		 System.out.println("排序后：" + strings);
		 return strings;
	}
}
