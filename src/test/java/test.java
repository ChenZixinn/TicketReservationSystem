import java.text.SimpleDateFormat;
import java.util.Date;

public class test {
    public static void main(String[] args) {
        SimpleDateFormat sdfMin = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Date date = new Date();
        String format = sdfMin.format(date);
        System.out.println(format);
    }
}
