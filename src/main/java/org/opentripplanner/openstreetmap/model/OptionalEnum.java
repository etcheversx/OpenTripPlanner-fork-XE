package org.opentripplanner.openstreetmap.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import javax.validation.constraints.NotNull;

public class OptionalEnum {

  private static final OptionalEnum empty = new OptionalEnum();
  private static final Map<Enum<?>, OptionalEnum> optionalEnums = new HashMap<>();
  private Enum<?> enumerate;

  private OptionalEnum() {}

  private OptionalEnum(Enum<?> enumerate) {
    this.enumerate = enumerate;
  }

  public static OptionalEnum of(Enum<?> value) {
    return optionalEnums.get(value);
  }

  public static OptionalEnum empty() {
    return empty;
  }

  public static OptionalEnum get(String value) throws Exception {
    for (Enum<?> enumerate : optionalEnums.keySet()) {
      if (enumerate.toString().equals(value)) {
        return optionalEnums.get(enumerate);
      }
    }
    throw new Exception("Invalid enum value " + value);
  }

  public static ArrayList<OptionalEnum> parseValues(String values) {
    ArrayList<OptionalEnum> result = new ArrayList<>();
    if (values == null) {
      return result;
    }
    Arrays
      .stream(values.split(";"))
      .forEach(s -> {
        try {
          OptionalEnum optionalEnum = OptionalEnum.get(s);
          if (!result.contains(optionalEnum)) {
            result.add(optionalEnum);
          }
        } catch (Exception ignored) {}
      });
    return result;
  }

  public boolean isEmpty() {
    return this.equals(empty);
  }

  public boolean isPresent() {
    return !isEmpty();
  }

  public Enum<?> getAsEnum() {
    if (!isPresent()) {
      throw new NoSuchElementException("No value present");
    }
    return this.enumerate;
  }

  private static void createTypedOptionalEnum(@NotNull Enum<?>[] values) {
    for (Enum<?> value : values) {
      optionalEnums.put(value, new OptionalEnum(value));
    }
  }

  static {
    try {
      createTypedOptionalEnum(OSMSurface.values());
    } catch (Exception exc) {
      // TODO : nothing done at present time
    }
  }
}
