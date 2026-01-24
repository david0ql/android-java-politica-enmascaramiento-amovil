package co.com.amovil.masking.util;

import java.util.Random;

public final class NameGenerator {
  private static final String[] FIRST_NAMES = {
      "Ana", "Carlos", "Juan", "Maria", "Luis", "Sofia", "Camila", "Miguel",
      "Laura", "Daniel", "Paula", "Andres", "Juliana", "Felipe", "Valentina",
      "Mateo", "Lucia", "Samuel", "Isabella", "Nicolas"
  };

  private static final String[] LAST_NAMES = {
      "Perez", "Gomez", "Rodriguez", "Torres", "Martinez", "Lopez", "Gonzalez",
      "Hernandez", "Diaz", "Castro", "Ramirez", "Sanchez", "Ortega", "Morales",
      "Ruiz", "Vargas", "Suarez", "Rojas", "Navarro", "Cortes"
  };

  private NameGenerator() {
  }

  public static String generateName(int seed) {
    Random random = new Random(seed);
    String first = FIRST_NAMES[Math.abs(random.nextInt()) % FIRST_NAMES.length];
    String last = LAST_NAMES[Math.abs(random.nextInt()) % LAST_NAMES.length];
    return first + " " + last;
  }
}
