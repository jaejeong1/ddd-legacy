package calculator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringCalculator {

    public int add(final String text) {
        if (text == null) {return 0;}
        if (text.isEmpty()) {return 0;}

        String[] tokens = findTokens(text);
        int sum = 0;

        for (String token : tokens) {
            int num = Integer.parseInt(token);
            validate(num);
            sum += num;
        }

        return sum;
    }

    private void validate(int num) {
        if (num < 0) {
            throw new RuntimeException();
        }
    }

    private String[] findTokens(final String text) {
        Matcher m = Pattern.compile("//(.)\n(.*)").matcher(text);
        if (m.find()) {
            String customDelimiter = m.group(1);
            return m.group(2).split(customDelimiter);
        }

        return text.split(",|:");
    }
}


