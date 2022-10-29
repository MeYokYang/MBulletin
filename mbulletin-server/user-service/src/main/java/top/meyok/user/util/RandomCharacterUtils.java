package top.meyok.user.util;

import java.util.Random;

/**
 * 生成随机校验码工具类
 * @author meyok@meyok.org
 * @date 2022/8/29 8:37
 */
public class RandomCharacterUtils {

    private final static String FIGURE_SET = "0123456789";
    private final static String CAPITAL_LETTER_SET = "ABCDEFGHIJKLMNOPQRESTUVWXYZ";
    private final static String SMALL_LETTER_SET = "abcdefghijklmnopqresuvwxyz";
    private final static String SPECIAL_CHARACTER_SET = "!@#$%^&*()";


    public static String getPictureCheckCode(int length) {
        Random random = new Random();
        String pictureCheckCodeSet = FIGURE_SET+CAPITAL_LETTER_SET+SMALL_LETTER_SET;
        return getString(length, random, pictureCheckCodeSet);
    }

    public static String getMailCheckCode(int length) {
        Random random = new Random();
        return getString(length, random, FIGURE_SET);
    }

    public static String getSalt(int length) {
        Random random = new Random();
        String saltCodeSet = FIGURE_SET+CAPITAL_LETTER_SET+SMALL_LETTER_SET+SPECIAL_CHARACTER_SET;
        return getString(length, random, saltCodeSet);
    }

    private static String getString(int length, Random random, String codeSet) {
        int setLength = codeSet.length();
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 1; i <= length; i++) {
            int index = random.nextInt(setLength);
            char c = codeSet.charAt(index);
            stringBuilder.append(c);
        }
        return stringBuilder.toString();
    }


}
