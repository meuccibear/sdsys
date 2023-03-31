import com.adsys.util.DateUtil;
import org.apache.shiro.crypto.hash.SimpleHash;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class Test {


    public static void main(String[] args) {
//            System.out.println(new SimpleHash("SHA-1", "admin", "123456").toString());

        LocalDateTime now = LocalDateTime.now();
        try {
            Thread.sleep(1000 * 60);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        int mins = DateUtil.getDiffMin(DateUtil.formatSdfTimes(Timestamp.valueOf(now)), DateUtil.getTime());
        System.out.println(mins);
    }
}
