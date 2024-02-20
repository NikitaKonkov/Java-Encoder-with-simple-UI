import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

public class SHUFFLE_CYPHER {
    public static void setPbLen(short pbLen) {
        PB_LEN = pbLen;
    }

    static short PB_LEN = 16;  // public_key length configuration 16 - 128

    public static char[] decoder(char[] arrt, char[] arrk){ // Substitution cipher b
        for (int n = arrt.length-1; n >= 0; n--){
            char k = arrt[arrk[n] % arrt.length];
            arrt[arrk[n] % arrt.length] = arrt[n];
            arrt[n] = k;
        }
        return arrt;
    }
    public static char[] encoder(char[] arrt, char[] arrk){ // Substitution cipher f
        for (short n = 0; n<arrt.length;n++){
            char k = arrt[n];
            arrt[n] = arrt[arrk[n] % arrt.length];
            arrt[arrk[n] % arrt.length] = k;
        }
        return arrt;
    }
    public static int[] matrix(int[] arrt, char[] arrk){
        for (int n =  0; n < arrt.length; n++){
            int k = arrt[n];
            arrt[n] = arrt[arrk[n] % arrt.length];
            arrt[arrk[n] % arrt.length] = k;
        }
        return arrt;
    }

    /**Chen primes
     *Where p is prime and p+2 is either a prime or semi-prime.*/
    static final int[] CHEN_PRIMES = {
            2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 47, 53, 59, 67, 71, 83, 89, 101, 107, 109,
            113, 127, 131, 137, 139, 149, 157, 167, 179, 181, 191, 197, 199, 211, 227, 233, 239, 251,
            257, 263, 269, 281, 293, 307, 311, 317, 337, 347, 353, 359, 379, 389, 401, 409 };
    /**Eisenstein primes without imaginary part
     *Eisenstein integers that are irreducible and real numbers (primes of the form 3n − 1). */
    static final int[] EISENSTEIN_PRIMES = {
            2, 5, 11, 17, 23, 29, 41, 47, 53, 59, 71, 83, 89, 101, 107,
            113, 131, 137, 149, 167, 173, 179, 191, 197, 227, 233, 239,
            251, 257, 263, 269, 281, 293, 311, 317, 347, 353, 359, 383,
            389, 401};

    /** Isolated primes
     *Primes p such that neither p − 2 nor p + 2 is prime.*/
    static final int[] EULER_IRREGULAR_PRIMES = {
            2, 23, 37, 47, 53, 67, 79, 83, 89, 97, 113,
            127, 131, 157, 163, 167, 173, 211, 223, 233,
            251, 257, 263, 277, 293, 307, 317, 331, 337,
            353, 359, 367, 373, 379, 383, 389, 397, 401,
            409, 439, 443, 449, 457, 467, 479, 487, 491,
            499, 503, 509, 541, 547, 557, 563, 577, 587,
            593, 607, 613, 631, 647, 653, 673, 677, 683,
            691, 701, 709, 719, 727, 733, 739, 743, 751,
            757, 761, 769, 773, 787, 797, 839, 853, 863,
            877, 887, 907, 911, 919, 929, 937, 941, 947,
            953, 967, 971, 977, 983, 991, 997};
    /** Harmonic primes
     * Primes p for which there are no solutions to Hk ≡ 0 (mod p) and Hk ≡ −ωp (mod p) for 1 ≤ k ≤ p−2,
     * where Hk denotes the k-th harmonic number and ωp denotes the Wolstenholme quotient*/
    static final int[] HARMONIC_PRIMES = {
            5, 13, 17, 23, 41, 67, 73, 79, 107, 113,
            139, 149, 157, 179, 191, 193, 223, 239,
            241, 251, 263, 277, 281, 293, 307, 311,
            317, 331, 337, 349};

    /**Higgs primes for squares
     *Primes p for which p − 1 divides the square of the product of all earlier terms.*/
    static final int[] HIGGS_PRIMES = {
            2, 3, 5, 7, 11, 13, 19, 23, 29, 31, 37, 43, 47, 53, 59, 61, 67, 71, 79, 101, 107,
            127, 131, 139, 149, 151, 157, 173, 181, 191, 197, 199, 211, 223, 229, 263, 269,
            277, 283, 311, 317, 331, 347, 349 };

    public static void setPRIMES(String psw) throws IOException {
        psw = FLEX_HASH.HASH(psw,1024);
        for(int x = 0; x < 5; x++){
        SHUFFLE_CYPHER.PRIMES[x] = matrix(PRIMES[x], psw.toCharArray());
    }}

    public static int[][] PRIMES = {CHEN_PRIMES, EISENSTEIN_PRIMES, EULER_IRREGULAR_PRIMES, HARMONIC_PRIMES, HIGGS_PRIMES};


    public static List<String> Hummelnest_Crypter(String text, String psw) throws IOException {
        int sum_psw = 0;
        for (char c: psw.toCharArray()){
            sum_psw+=c;
        }
        String micro_psw = "";
        int count = 0;
        for (char c: psw.toCharArray()){
            micro_psw += (char) (c*PRIMES[sum_psw%5][count%PRIMES[sum_psw%5].length]);
            count++;
        }
        String public_psw =  FLEX_HASH.HASH(micro_psw,PB_LEN);  // generates public psw
        String public_psw_original = public_psw;
        while (public_psw.length() < text.length()){    // psw extender
            public_psw+=FLEX_HASH.HASH(public_psw,16);}
        int public_psw_sum = 0;
        for (char n: public_psw_original.toCharArray()){
            public_psw_sum+=n;
        }
        public_psw_sum %= 100;
        char[] cypher_msg = encoder(text.toCharArray(),public_psw.toCharArray());  // cypher msg
        count = 0;
        String encoded_msg = "";
        for (char n: cypher_msg){
            encoded_msg+=(char)(n+PRIMES[public_psw_sum%5][count%PRIMES[public_psw_sum%5].length]-public_psw_sum%5);
            count++;
        }
        char[] final_msg = decoder(encoded_msg.toCharArray(), public_psw.toCharArray());
        String final_destination = "";
        for (char n: final_msg) {
            final_destination+=n;
        }
        final_destination+=(char)public_psw_sum;
        return List.of(final_destination,public_psw_original);
    }
    public static String Hummelnest_Dectrypter(String text, String psw) throws IOException {
        char[] final_decryption = text.toCharArray();
        int hash_index = final_decryption[final_decryption.length-1]%5;
        int check = 0;
        char[] arr = psw.toCharArray();
        for (char n: arr ){
            check+=n;
        }
        if (check%5 != hash_index){
            return Privat_Hummel(text,psw);
        }
        while (psw.length() < text.length()){    // psw extender
            psw+=FLEX_HASH.HASH(psw,16);}
        final_decryption = Arrays.copyOf(final_decryption, final_decryption.length - 1);
        char[] decrypted_msg = encoder(final_decryption, psw.toCharArray());
        int count = 0;
        String decryption_msg = "";
        for (char n: decrypted_msg){
            decryption_msg+=(char)(n-PRIMES[hash_index][count%PRIMES[hash_index].length]+hash_index);
            count++;
        }
        String end_msg = "";
        for(char n : decoder(decryption_msg.toCharArray(), psw.toCharArray())){
            end_msg+=n;
        }
        return end_msg;
    }

    public static String Privat_Hummel(String text, String psw) throws IOException {
        int sum_psw = 0;
        for (char c: psw.toCharArray()){
            sum_psw+=c;
        }
        String micro_psw = "";
        int count = 0;
        for (char c: psw.toCharArray()){
            micro_psw += (char) (c*PRIMES[sum_psw%5][count%PRIMES[sum_psw%5].length]);
            count++;
        }
        psw = FLEX_HASH.HASH(micro_psw,PB_LEN);
        char[] final_decryption = text.toCharArray();
        int hash_index = final_decryption[final_decryption.length-1];
        while (psw.length() < text.length()){    // psw extender
            psw+=FLEX_HASH.HASH(psw,16);}
        final_decryption = Arrays.copyOf(final_decryption, final_decryption.length - 1);
        char[] decrypted_msg = encoder(final_decryption, psw.toCharArray());
        count = 0;
        String decryption_msg = "";
        for (char n: decrypted_msg){
            decryption_msg+=(char)(n-PRIMES[hash_index%5][count%PRIMES[hash_index%5].length]+hash_index%5);
            count++;
        }
        String end_msg = "";
        for(char n : decoder(decryption_msg.toCharArray(), psw.toCharArray())){
            end_msg+=n;
        }
        return end_msg;
    }

    public static void main(String[] args) throws IOException {

    }
}



