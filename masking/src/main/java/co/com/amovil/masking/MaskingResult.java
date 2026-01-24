package co.com.amovil.masking;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public final class MaskingResult {
  private final String masked;
  private final Map<String, String> metadata;

  public MaskingResult(String masked) {
    this(masked, null);
  }

  public MaskingResult(String masked, Map<String, String> metadata) {
    this.masked = masked;
    if (metadata == null || metadata.isEmpty()) {
      this.metadata = Collections.emptyMap();
    } else {
      this.metadata = Collections.unmodifiableMap(new HashMap<>(metadata));
    }
  }

  public String getMasked() {
    return masked;
  }

  public Map<String, String> getMetadata() {
    return metadata;
  }
}
