package ca.jrvs.apps.practice;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class RegexExcImp implements RegexExc{
    private static final Pattern JPEG_EXTENSION = Pattern.compile(".+\\.jpe?g$", Pattern.CASE_INSENSITIVE);
    private static final Pattern IP_ADDRESS = Pattern.compile("^(\\d{1,3}\\.){3}\\d{1,3}$");
    private static final Pattern EMPTY_LINE = Pattern.compile("^\\s*$");

    @Override
    public boolean matchJpeg(String filename) {
        if (filename == null) {
            return false;
        }
        Matcher matcher = JPEG_EXTENSION.matcher(filename);
        return matcher.matches();
    }

    @Override
    public boolean matchIp(String ip) {
        if (ip == null) {
            return false;
        }
        Matcher matcher = IP_ADDRESS.matcher(ip);
        return matcher.matches();
    }

    @Override
    public boolean isEmptyLine(String line) {
        // Security to not match a null String
        if (line == null) {
            return true;
        }
        Matcher matcher = EMPTY_LINE.matcher(line);
        return matcher.matches();
    }

    /*
    public static void main(String[] args) {
        RegexExcImp exc = new RegexExcImp();
        String[] jpgTest = {"image.jpg", "image.JPEG", "imapge.jpg.png", ".jpg", ""};
        String[] ipTest = {"0.0.0.0", "1234.123.123.123", "z.f.d.09", "12.34.frz.2", "12.123.9.123", ""};
        String[] emptyTest = {"           ", "   ", "", "  fefe  ", "must return false"};

        System.out.println("JPG test");
        for(String test : jpgTest) {System.out.println(test + ": " + exc.matchJpeg(test));}
        System.out.println("\nIP test");
        for(String test : ipTest) {System.out.println(test + ": " + exc.matchIp(test));}
        System.out.println("\nEmpty test");
        for(String test : emptyTest) {System.out.println(test + ": " + exc.isEmptyLine(test));}
    }
     */
}
