
package springboot.core.common;


import springboot.core.util.ConvertUtil;
import springboot.core.util.DateUtil;
import springboot.core.util.StringUtil;

import java.security.SecureRandom;
import java.util.Date;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author : hermit
 */
public class Identities
{
  private static SecureRandom random = new SecureRandom();
  private static AtomicLong atomicLongID = new AtomicLong(10000L);

  public static String uuid()
  {
    return UUID.randomUUID().toString();
  }

  public static String uuid2()
  {
    return UUID.randomUUID().toString().replaceAll("-", "");
  }

  public static long randomLong()
  {
    return Math.abs(random.nextLong());
  }

  public static String randomBase62(int length)
  {
    byte[] randomBytes = new byte[length];
    random.nextBytes(randomBytes);
    return ConvertUtil.convertEncodeBase62(randomBytes);
  }

  public static String getTimeMillisSequence()
  {
    long nanoTime = System.nanoTime();
    String preFix = null;
    if (nanoTime < 0L)
    {
      preFix = "A";
      nanoTime = nanoTime + 9223372036854775807L + 1L;
    } else {
      preFix = "Z";
    }
    String nanoTimeStr = String.valueOf(nanoTime);
    int difBit = String.valueOf(9223372036854775807L).length() - nanoTimeStr.length();
    for (int i = 0; i < difBit; i++) {
      preFix = StringUtil.concat(new Object[] { preFix, "0" });
    }
    long last = atomicLongID.getAndIncrement() % 10000L + 10000L;
    return StringUtil.concat(new Object[] { DateUtil.changeDateTOStr5(new Date(), "yyyyMMddHHmmssSSS"), preFix, nanoTimeStr, Long.valueOf(last) });
  }
  /**
   * 获取一定长度的随机字符串
   * @param length 指定字符串长度
   * @return 一定长度的字符串
   */
  public static String getRandomString(int length) {
    String base = "abcdefghijklmnopqrstuvwxyz0123456789";
    Random random = new Random();
    StringBuffer sb = new StringBuffer();
    for (int i = 0; i < length; i++) {
      int number = random.nextInt(base.length());
      sb.append(base.charAt(number));
    }
    return sb.toString();
  }
}
