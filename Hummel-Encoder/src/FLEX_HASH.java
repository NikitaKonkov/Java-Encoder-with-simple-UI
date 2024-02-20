import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import static java.nio.file.Files.newByteChannel;


public class FLEX_HASH {

    public static void main(String[] args) throws IOException {
        long startTime = System.nanoTime(); // gets local time
        System.out.println("HASH: "+ HASH("58269583",1024));
        System.out.println("HASH: "+ HASH("TEST",1024));

        System.out.println("TIME: " + (float) (System.nanoTime() - startTime)/60/60/60/60/6/10); // prints the used time
    }
    static int[] PRIMES =  {53,97,193,389,769,1543,3079,6151,12289,24593,49157,98317,196613,393241,786433,1572869}; // 16 primes
    static int ARRAY_SIZE = 1024; // Size of hash can be between 16 and 1024, smaller hash is for small data and otherwise. 128 for best result
    static long[] HEX_ARRAY = new long[ARRAY_SIZE]; // HEX array for output by given size
    static double SIZE_RATIO = 0.25; // 0.28 for 4GB data saves time if set higher but can also make calculations errors if falsely calibrated
    private static final long[] HASH_ARRAY = new long[ARRAY_SIZE*2];
    private static long HASH = 7L;
    public static String GENERATOR(long[] DATA_ARRAY){
        StringBuilder hashData = new StringBuilder();
        long sum = 0, mean = 0, max = 0, min = DATA_ARRAY[0];
        double deviation = 0.0;
        for (long element : DATA_ARRAY){ // sum, min, max
            max = (max < element) ? element : max; // short way to get max
            min = (min > element) ? element : min; // short way to get min
            sum += element;
        }
        for (long element : DATA_ARRAY){ mean += (element >> 2);}
        mean = mean / ARRAY_SIZE ;
        for(long element : DATA_ARRAY) { deviation += Math.pow(element - mean, 2);}
        deviation = (double) sum / (ARRAY_SIZE * 2);

        for (int index = 0; index < ARRAY_SIZE; index++) {
            HEX_ARRAY[index] = (DATA_ARRAY[index] * mean * (min+1));
            HEX_ARRAY[index] +=  (DATA_ARRAY[ARRAY_SIZE - index - 1] + sum *  max ) * (min-1);
            hashData.append(Long.toHexString((long) Math.abs((HEX_ARRAY[index] / (deviation + 1)) % 16)));
        }return hashData.toString();
    }
    public static String HASH(String input , int hash_size) throws IOException {
        ARRAY_SIZE = hash_size;
        Path pathToFile = Path.of(input);
        SeekableByteChannel ch; // get data channel
        try { ch = newByteChannel( pathToFile, StandardOpenOption.READ ); } catch (IOException e) {
            SMALL_STRING_HASH(String.valueOf(pathToFile));
            //System.out.println("STRING HASHED");
            return GENERATOR(HASH_ARRAY);

            }
        if (ch.size() < ARRAY_SIZE* 2L){   /** data_size < (array_size * 2) */// for small data
            SMALL_FILE_HASH(ch);
        }else {
            BIG_FILE_HASH(ch); // else for big data
        }
        //System.out.println("FILE HASHED");
        return GENERATOR(HASH_ARRAY);
    }
    public static void SMALL_STRING_HASH(String str) { // DATA like psw and messages
        byte[] ram = str.getBytes(StandardCharsets.UTF_8);
        int data = ram.length;
        int i = 0;
        for (byte s: ram) {
            data= PRIMES[ 15 - i % 16 ] * data + s;
            i++;
        }
        for(int n = 0; n < ARRAY_SIZE; n++){
            data += (PRIMES[ n % 16 ] * data + n);
            HASH_ARRAY[ n ] = data + n;
        }
    }
    public static void SMALL_FILE_HASH(SeekableByteChannel ch) throws IOException { // DATA like psw and messages
        ByteBuffer bf = ByteBuffer.allocate((int) ch.size());
        int data = (int) ch.size();
        int i = 0;
        while (ch.read(bf) >= 0) {
            bf.flip();
            for (byte s:bf.array()){
                data= PRIMES[ 15 - i % 16 ] * data + s;
                i++;
            }
            bf.clear();
            i++;
        }
        for(int n = 0; n < ARRAY_SIZE; n++){
            data += (PRIMES[ n % 16 ] * data + n);
            HASH_ARRAY[ n ] = data + n;
        }
    }
    public static void BIG_FILE_HASH(SeekableByteChannel ch) throws IOException { // DATA like RDR 2
        while ( (int) Math.pow( ch.size() , SIZE_RATIO) < ARRAY_SIZE * 2 - 1 ){    // Ration adjuster to fill the whole array
            SIZE_RATIO += 0.000005;
        }
        ByteBuffer bf = ByteBuffer.allocate( (int) (ch.size() / (int) Math.pow( ch.size() , SIZE_RATIO)));  // Adjusted sizes to fit into 511 indexes. The spare data will be fit into the last 512 index
        int i = 0;
        while (ch.read(bf) > 0) {
            if (i >= ARRAY_SIZE * 2){break;} // Overflow handel
            bf.flip();
            for (byte b:bf.array()){
                HASH = 3 * HASH + b ; // low multiplier for slower overflow.
            }
            HASH_ARRAY[i] = HASH;
            HASH = PRIMES[i % 16];
            bf.clear();
            i++;
        }
    }
}
