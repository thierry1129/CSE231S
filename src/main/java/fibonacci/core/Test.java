//package fibonacci.core;
//
//import java.util.HashMap;
//import java.util.HashSet;
//import java.util.Map;
//import java.util.Set;
//
//public class Test {
//    // you can also use imports, for example:
//// import java.util.*;
//
//// you can write to stdout for debugging purposes, e.g.
//// System.out.println("this is a debug message");
//
//        public static int solution(int[] A) {
//
//            int inputLength = A.length;
//            int[] locArray = new int[inputLength];
//            Map<Integer, Integer> resMap = new HashMap<>();
//
//            for (int a : A) {
//                if (resMap.get(a) != null) {
//                    resMap.put(a, resMap.get(a) + 1);
//                } else {
//                    resMap.put(a, 1);
//                }
//            }
//            int startChecker = 0;
//            int l = 0;
//            int res = A.length + 1;
//            int end = -1;
//            int current = 0 ;
//            HashMap<Integer,Integer > ans = new HashMap<Integer,Integer>();
//            while(end < A.length){
//
//                if (current < resMap.keySet().size()){
//                    end++;
//                    if (ans.get(A[end])==null){
//                        current ++;
//                        ans.put(A[end],1);
//                    }else{
//                        ans.put(A[end],ans.get(A[end])+1);
//                    }
//                    continue;
//                }
//                while (c)
//
//
//
//
//
//
//            }
//            for (int b = 0; b < A.length; b++) {
//                int cur = A[b];
//                if (resMap.get(cur) != null) {
//                    resMap.put(cur, resMap.get(cur) - 1);
//                    if (resMap.get(cur) >= 0) {
//                        startChecker++;
//                    }
//
//                }
//
//                while (startChecker == resMap.keySet().size()) {
//                    if (b - l + 1 < res) {
//                        res = b - l + 2;
//                    }
//
//                    int currentLeft = A[l];
//                    if (resMap.containsKey(currentLeft)) {
//                        int clftVal = resMap.get(currentLeft);
//                        resMap.put(currentLeft, clftVal + 1);
//                        if (resMap.get(currentLeft) > 0) {
//                            startChecker -= 1;
//                        }
//
//                    }
//
//                }
//                l++;
//            }
//            if (res == A.length + 1) {
//                return 0;
//            } else {
//                return res;
//            }
//
//        }
//    public static int solution2(int[] A) {
//
//        int inputLength = A.length;
//        int[] locArray = new int[inputLength];
//        Map<Integer, Integer> resMap = new HashMap<>();
//
//        for (int a : A) {
//            if (resMap.get(a) != null) {
//                resMap.put(a, resMap.get(a) + 1);
//            } else {
//                resMap.put(a, 1);
//            }
//        }
//
//        int sizeDis = resMap.keySet().size();
//
//        int leftPt = 0 ;
//        int rightPt = leftPt+sizeDis;
//        Set<Integer>  s = new HashSet<>();
//        boolean isStart = false;
//        for (int b = 0; b< A.length;b++){
//            while(rightPt<A.length){
//
//                s.add(A[leftPt]);
//                leftPt++;
//                rightPt = leftPt+sizeDis;
//            }
//            if( s.size() !=  sizeDis ){
//                continue;
//            }else{
//                return
//            }
//
//        }
//
//    }
//    public static void main(String args[]) {
//        int[] A = {7, 3, 7, 3, 1, 3, 4, 1};
//        System.out.println(solution(A));
//
//    }
//}
