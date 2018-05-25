package util

import java.math.RoundingMode
import java.text.NumberFormat
import java.text.SimpleDateFormat

/**
 * Created by solax on 2017-1-19.
 */
class CommonUtil {

    static int StringToInt (def object) {
        if (!object) return 0
        return Integer.parseInt(object)
    }

    static  String toPercent (Double num) {
        if (!num) return '0%'
        NumberFormat fmt = NumberFormat.getPercentInstance();
        fmt.setMaximumFractionDigits(6);//最多两位百分小数，如25.23%
        return fmt.format(num)
    }

     static double formatDouble2(double d) {
        BigDecimal bg = new BigDecimal(d).setScale(2, RoundingMode.UP);
        return bg.doubleValue();
    }

    static Long formatLong (String string){
        if (!string)  return  0
        try {
            Double d = Double.parseDouble(string)
            return d.longValue()
        } catch (Exception e){
            e.printStackTrace()
            return 0
        }
    }

    static Long formatLong (Double d) {
        return d ? d.longValue() : 0
    }

    static subString (String string, int length = 4) {
        if (!string) return ''
        if (string.length() > length) {
            return string.substring(0, length)
        }
        return string
    }

    static String  percentPlus (String p1, String p2) {
        try {
            p1 = p1.substring(0,p1.indexOf('%'))
            p2 = p2.substring(0,p2.indexOf('%'))
            return formatDouble2(Double.parseDouble(p1) + Double.parseDouble(p2) ) + '%'
        } catch (Exception e) {
            e.printStackTrace()
            return '0%'
        }
    }
}
