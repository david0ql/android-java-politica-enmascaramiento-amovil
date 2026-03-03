package co.com.amovil.masking;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

public class MaskingEngineTest {
  @Test
  public void cifradoUsesBase64() {
    MaskingRequest request = MaskingRequest.builder(MaskingType.CIFRADO, "ABC").build();
    MaskingResult result = MaskingEngine.mask(request);
    assertEquals("QUJD", result.getMasked());
  }

  @Test
  public void enmascaramientoParcialMasksAllButVisibleChars() {
    MaskingRequest request = MaskingRequest.builder(MaskingType.ENMASCARAMIENTO_PARCIAL, "1023456789")
        .visibleChars(4)
        .maskChar("*")
        .build();
    MaskingResult result = MaskingEngine.mask(request);
    assertEquals("******6789", result.getMasked());
  }

  @Test
  public void envejecimientoFechasShiftsDate() {
    MaskingRequest request = MaskingRequest.builder(MaskingType.ENVEJECIMIENTO_FECHAS, "1990-05-12")
        .originalDate("1990-05-12")
        .offsetDays(-180)
        .build();
    MaskingResult result = MaskingEngine.mask(request);
    assertEquals("1989-11-13", result.getMasked());
    assertEquals("1989-11-13", result.getMetadata().get("maskedDate"));
  }

  @Test
  public void tokenizacionGeneratesToken() {
    MaskingRequest request = MaskingRequest.builder(MaskingType.TOKENIZACION, "4111123456789012")
        .tokenPrefix("TKN")
        .build();
    MaskingResult result = MaskingEngine.mask(request);
    assertTrue(result.getMasked().matches("TKN-[0-9A-F]{4}-[0-9A-F]{4}"));
    assertEquals(result.getMasked(), result.getMetadata().get("token"));
  }

  @Test
  public void sustitucionGeneratesSyntheticName() {
    MaskingRequest request = MaskingRequest.builder(MaskingType.SUSTITUCION, "Juan Perez").build();
    MaskingResult result = MaskingEngine.mask(request);
    assertNotNull(result.getMasked());
    assertNotSame("Juan Perez", result.getMasked());
  }

  @Test
  public void permutacionRespectsSwappedWith() {
    MaskingRequest request = MaskingRequest.builder(MaskingType.PERMUTACION, "Ana")
        .swappedWith("Luis (Bogota)")
        .build();
    MaskingResult result = MaskingEngine.mask(request);
    assertEquals("Luis (Bogota)", result.getMasked());
  }

  @Test
  public void datosSinteticosGenerateValue() {
    MaskingRequest request = MaskingRequest.builder(MaskingType.DATOS_SINTETICOS, "Maria Lopez").build();
    MaskingResult result = MaskingEngine.mask(request);
    assertNotNull(result.getMasked());
    assertNotSame("Maria Lopez", result.getMasked());
  }
}
