package org.opentripplanner.openstreetmap.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import javax.validation.constraints.NotNull;

public class OptionalEnum<T extends Enum<T>> implements OptionalValue<T> {

  private static final OptionalEnum<?> empty = new OptionalEnum<>();
  private static final Map<Enum<?>, OptionalEnum<?>> optionalEnums = new HashMap<>();
  private T enumerate;

  private OptionalEnum() {}

  private OptionalEnum(T enumerate) {
    this.enumerate = enumerate;
  }

  public static OptionalEnum empty() {
    return empty;
  }

  public static OptionalEnum<?> get(String value) throws Exception {
    for (Enum<?> enumerate : optionalEnums.keySet()) {
      if (enumerate.toString().equals(value)) {
        return optionalEnums.get(enumerate);
      }
    }
    throw new Exception("Invalid enum value " + value);
  }

  public static <E extends Enum<E>> ArrayList<OptionalEnum<E>> parseValues(String values) {
    ArrayList<OptionalEnum<E>> result = new ArrayList<>();
    if (values == null) {
      return result;
    }
    Arrays
      .stream(values.split(";"))
      .forEach(s -> {
        try {
          OptionalEnum<E> optionalEnum = (OptionalEnum<E>) get(s);
          if (!result.contains(optionalEnum)) {
            result.add(optionalEnum);
          }
        } catch (Exception ignored) {}
      });
    return result;
  }

  @Override
  public boolean isEmpty() {
    return this.equals(empty);
  }

  @Override
  public boolean isPresent() {
    return !isEmpty();
  }

  @Override
  public T getAsTyped() {
    if (!isPresent()) {
      throw new NoSuchElementException("No value present");
    }
    return this.enumerate;
  }

  private static <E extends Enum<E>> void createTypedOptionalEnum(@NotNull E[] values) {
    for (E value : values) {
      optionalEnums.put(value, new OptionalEnum<>(value));
    }
  }

  static {
    createTypedOptionalEnum(OSMSmoothness.values());
    createTypedOptionalEnum(OSMSurface.values());
    createTypedOptionalEnum(OSMHighway.values());
    createTypedOptionalEnum(OSMFootway.values());
    createTypedOptionalEnum(OSMIncline.values());
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    OptionalEnum that = (OptionalEnum) o;
    return Objects.equals(enumerate, that.enumerate);
  }

  @Override
  public int hashCode() {
    return Objects.hash(enumerate);
  }
}
