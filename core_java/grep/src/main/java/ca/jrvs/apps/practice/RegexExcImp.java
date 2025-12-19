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

    public static void main(String[] args) {
        if (args.length < 2){
            throw new IllegalArgumentException("USAGE: regexType, line \nregexType: jpeg, ip, empty");
        }
        RegexExc exc = new RegexExcImp();
        switch (args[0]) {
            case "jpeg":
                System.out.println(exc.matchJpeg(args[1]));
                break;

            case "ip":
                System.out.println(exc.matchIp(args[1]));
                break;

            case "empty":
                System.out.println(exc.isEmptyLine(args[1]));
                break;

            default:
                throw new IllegalArgumentException("USAGE: regexType, line \nregexType: jpeg, ip, empty");
        }
    }
}
