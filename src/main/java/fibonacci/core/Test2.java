package fibonacci.core;

public class Test2 {
    static int calcAnagram(String a , String b ){
        if (a.length() != b.length()){
            return -1;
        }
        int letterCount = 0;
        int hash1[]  = new int[26];
       // int hash2[] = new int[26];

        for (int x = 0 ; x<a.length();x++){
            hash1[a.charAt(x)-'a']++;
        }
        for (int y = 0 ; y <b.length();y++){
            if (hash1[b.charAt(y)-'a']--<=0){
              letterCount++;
            }

        }

        return letterCount;

    }

    static int[] getMinimumDifference(String[] a, String[] b) {
        if (a.length != b.length){
            System.out.println(" cannot check b/c array not equal length");
            return null;
        }

        int[] resultArray = new int[a.length];

        for (int x  = 0 ; x < a.length ;x++){
            resultArray[x] = calcAnagram(a[x],b[x]);
        }
        return resultArray;


    }

    public static void main(String[] args)
    {
        String str1 = "bcadeh", str2 = "hea";
        System.out.println( calcAnagram(str1, str2));
    }


}
