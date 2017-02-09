package no.haagensoftware.contentice.util;

import java.security.SecureRandom;
import java.util.UUID;

/**
 * Created by jhsmbp on 08/10/16.
 */
public class UUIDUtil {

    public static String getUniqueString(int length) {
        UUID idOne = UUID.randomUUID();
        UUID idTwo = UUID.randomUUID();
        UUID idThree = UUID.randomUUID();
        UUID idFour = UUID.randomUUID();

        String time = idOne.toString().replace("-", "");
        String time2 = idTwo.toString().replace("-", "");
        String time3 = idThree.toString().replace("-", "");
        String time4 = idFour.toString().replace("-", "");

        StringBuffer data = new StringBuffer();
        data.append(time);
        data.append(time2);
        data.append(time3);
        data.append(time4);

        SecureRandom random = new SecureRandom();
        int beginIndex = random.nextInt(50);       //Begin index + length of your string < data length
        int endIndex = beginIndex + length;            //Length of string which you want

        return data.substring(beginIndex, endIndex);
    }
}
