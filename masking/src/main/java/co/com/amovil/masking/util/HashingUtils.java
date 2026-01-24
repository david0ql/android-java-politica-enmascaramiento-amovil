package co.com.amovil.masking.util;

public final class HashingUtils {
  private HashingUtils() {
  }

  public static int hashString(String value) {
    if (value == null) {
      return 0;
    }
    int hash = 0;
    for (int i = 0; i < value.length(); i++) {
      hash = ((hash << 5) - hash) + value.charAt(i);
    }
    return hash;
  }
}
