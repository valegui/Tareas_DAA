public class Utils {
    /*
     * from https://stackoverflow.com/questions/11437203/how-to-convert-a-byte-array-to-an-int-array#11438071
     */
    public static int[] convertByteToIntArray(byte buf[]) {
        int intArr[] = new int[buf.length / 4];
        int offset = 0;
        for(int i = 0; i < intArr.length; i++) {
            intArr[i] = (buf[3 + offset] & 0xFF) | ((buf[2 + offset] & 0xFF) << 8) |
                    ((buf[1 + offset] & 0xFF) << 16) | ((buf[0 + offset] & 0xFF) << 24);
            offset += 4;
        }
        return intArr;
    }
}
