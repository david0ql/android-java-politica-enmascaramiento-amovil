package co.com.amovil.masking;

import co.com.amovil.masking.util.DateUtils;
import co.com.amovil.masking.util.HashingUtils;
import co.com.amovil.masking.util.NameGenerator;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public final class MaskingEngine {
  private static final Map<MaskingType, MaskingStrategy> STRATEGIES;

  static {
    Map<MaskingType, MaskingStrategy> strategies = new EnumMap<>(MaskingType.class);
    strategies.put(MaskingType.SUSTITUCION, new SustitucionStrategy());
    strategies.put(MaskingType.PERMUTACION, new PermutacionStrategy());
    strategies.put(MaskingType.CIFRADO, new CifradoStrategy());
    strategies.put(MaskingType.ENMASCARAMIENTO_PARCIAL, new EnmascaramientoParcialStrategy());
    strategies.put(MaskingType.ENVEJECIMIENTO_FECHAS, new EnvejecimientoFechasStrategy());
    strategies.put(MaskingType.TOKENIZACION, new TokenizacionStrategy());
    strategies.put(MaskingType.DATOS_SINTETICOS, new DatosSinteticosStrategy());
    STRATEGIES = Collections.unmodifiableMap(strategies);
  }

  private MaskingEngine() {
  }

  public static MaskingResult mask(MaskingRequest request) {
    if (request == null) {
      throw new IllegalArgumentException("Masking request is required");
    }
    MaskingStrategy strategy = STRATEGIES.get(request.getType());
    if (strategy == null) {
      throw new IllegalArgumentException("Unsupported masking type: " + request.getType());
    }
    return strategy.apply(request);
  }

  interface MaskingStrategy {
    MaskingResult apply(MaskingRequest request);
  }

  private static final class SustitucionStrategy implements MaskingStrategy {
    @Override
    public MaskingResult apply(MaskingRequest request) {
      int seed = HashingUtils.hashString(request.getOriginal());
      String masked = NameGenerator.generateName(seed);
      return new MaskingResult(masked);
    }
  }

  private static final class PermutacionStrategy implements MaskingStrategy {
    @Override
    public MaskingResult apply(MaskingRequest request) {
      if (request.getSwappedWith() != null && !request.getSwappedWith().isEmpty()) {
        return new MaskingResult(request.getSwappedWith());
      }
      int seed = HashingUtils.hashString(request.getOriginal()) + 1;
      String masked = NameGenerator.generateName(seed);
      return new MaskingResult(masked);
    }
  }

  private static final class CifradoStrategy implements MaskingStrategy {
    @Override
    public MaskingResult apply(MaskingRequest request) {
      String original = safeString(request.getOriginal());
      String masked = Base64.getEncoder().encodeToString(original.getBytes(StandardCharsets.UTF_8));
      return new MaskingResult(masked);
    }
  }

  private static final class EnmascaramientoParcialStrategy implements MaskingStrategy {
    @Override
    public MaskingResult apply(MaskingRequest request) {
      String original = safeString(request.getOriginal());
      int visibleChars = request.getVisibleChars() != null ? request.getVisibleChars() : 4;
      if (visibleChars < 0) {
        visibleChars = 0;
      }
      String maskChar = request.getMaskChar();
      if (maskChar == null || maskChar.isEmpty()) {
        maskChar = "*";
      }
      if (original.length() <= visibleChars) {
        return new MaskingResult(original);
      }
      String visiblePart = original.substring(original.length() - visibleChars);
      String maskedPart = repeat(maskChar, original.length() - visibleChars);
      return new MaskingResult(maskedPart + visiblePart);
    }
  }

  private static final class EnvejecimientoFechasStrategy implements MaskingStrategy {
    @Override
    public MaskingResult apply(MaskingRequest request) {
      String originalDate = request.getOriginalDate();
      if (originalDate == null) {
        originalDate = request.getOriginal();
      }
      int offsetDays = request.getOffsetDays() != null ? request.getOffsetDays() : -180;
      String maskedDate = DateUtils.shiftDate(originalDate, offsetDays);
      Map<String, String> metadata = new HashMap<>();
      metadata.put("maskedDate", maskedDate);
      return new MaskingResult(maskedDate, metadata);
    }
  }

  private static final class TokenizacionStrategy implements MaskingStrategy {
    @Override
    public MaskingResult apply(MaskingRequest request) {
      String prefix = request.getTokenPrefix();
      if (prefix == null || prefix.trim().isEmpty()) {
        prefix = "TKN";
      }
      int hash = HashingUtils.hashString(request.getOriginal());
      String hex = Integer.toHexString(Math.abs(hash)).toUpperCase(Locale.US);
      if (hex.length() < 8) {
        hex = String.format(Locale.US, "%8s", hex).replace(' ', '0');
      }
      hex = hex.substring(0, 8);
      String token = prefix + "-" + hex.substring(0, 4) + "-" + hex.substring(4, 8);
      Map<String, String> metadata = new HashMap<>();
      metadata.put("token", token);
      return new MaskingResult(token, metadata);
    }
  }

  private static final class DatosSinteticosStrategy implements MaskingStrategy {
    @Override
    public MaskingResult apply(MaskingRequest request) {
      int seed = HashingUtils.hashString(request.getOriginal()) + 2;
      String masked = NameGenerator.generateName(seed);
      Map<String, String> metadata = new HashMap<>();
      metadata.put("syntheticValue", masked);
      return new MaskingResult(masked, metadata);
    }
  }

  private static String repeat(String value, int count) {
    StringBuilder builder = new StringBuilder();
    for (int i = 0; i < count; i++) {
      builder.append(value);
    }
    return builder.toString();
  }

  private static String safeString(String value) {
    return value == null ? "" : value;
  }
}
