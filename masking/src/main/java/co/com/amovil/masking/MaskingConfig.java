package co.com.amovil.masking;

import android.content.Context;

/**
 * Global configuration for the masking library.
 *
 * <p>Must be initialized once in your {@link android.app.Application#onCreate()}:
 * <pre>
 *   public class MyApp extends Application {
 *     {@literal @}Override
 *     public void onCreate() {
 *       super.onCreate();
 *       MaskingConfig.init(this, true);
 *     }
 *   }
 * </pre>
 *
 * <p>Register it in {@code AndroidManifest.xml}:
 * <pre>
 *   &lt;application android:name=".MyApp" ...&gt;
 * </pre>
 *
 * <p>If {@code enabled} is {@code false}, {@link MaskingEngine#mask} returns the original
 * value unmodified — useful to disable masking in debug builds.
 *
 * <p>If {@link MaskingEngine#mask} is called before {@code init()}, the app will crash
 * with an {@link IllegalStateException}.
 */
public final class MaskingConfig {

  private static volatile MaskingConfig instance;

  private final boolean enabled;

  private MaskingConfig(boolean enabled) {
    this.enabled = enabled;
  }

  /**
   * Initializes the library. Call this once from {@code Application.onCreate()}.
   *
   * @param context application context (must not be null)
   * @param enabled {@code true} to apply masking; {@code false} to return originals as-is
   */
  public static void init(Context context, boolean enabled) {
    if (context == null) {
      throw new IllegalArgumentException("Context must not be null");
    }
    instance = new MaskingConfig(enabled);
  }

  /**
   * Returns the current config, or throws if {@link #init} was never called.
   */
  static MaskingConfig require() {
    MaskingConfig config = instance;
    if (config == null) {
      throw new IllegalStateException(
          "MaskingConfig has not been initialized.\n"
              + "Call MaskingConfig.init(context, enabled) in your Application.onCreate().\n\n"
              + "Example:\n"
              + "  public class MyApp extends Application {\n"
              + "    @Override public void onCreate() {\n"
              + "      super.onCreate();\n"
              + "      MaskingConfig.init(this, true);\n"
              + "    }\n"
              + "  }\n\n"
              + "Then register it in AndroidManifest.xml:\n"
              + "  <application android:name=\".MyApp\" ...>"
      );
    }
    return config;
  }

  /** Returns whether masking is currently enabled. */
  public boolean isEnabled() {
    return enabled;
  }

  /** Returns whether {@link #init} has been called. */
  public static boolean isInitialized() {
    return instance != null;
  }
}
