package org.opentripplanner.api.mapping;

import static org.opentripplanner.api.mapping.AbsoluteDirectionMapper.mapAbsoluteDirection;
import static org.opentripplanner.api.mapping.ElevationMapper.mapElevation;
import static org.opentripplanner.api.mapping.RelativeDirectionMapper.mapRelativeDirection;

import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.OptionalDouble;
import java.util.stream.Collectors;
import org.opentripplanner.api.model.ApiWalkStep;
import org.opentripplanner.graph_builder.module.osm.AccessibilityPropertySet;
import org.opentripplanner.model.plan.WalkStep;
import org.opentripplanner.openstreetmap.model.OptionalBoolean;
import org.opentripplanner.openstreetmap.model.OptionalEnum;
import org.opentripplanner.openstreetmap.model.OptionalEnumAndDouble;

public class WalkStepMapper {

  private final StreetNoteMaperMapper alertsMapper;
  private final Locale locale;

  public WalkStepMapper(Locale locale) {
    this.locale = locale;
    this.alertsMapper = new StreetNoteMaperMapper(locale);
  }

  public List<ApiWalkStep> mapWalkSteps(Collection<WalkStep> domain) {
    if (domain == null) {
      return null;
    }
    return domain.stream().map(this::mapWalkStep).collect(Collectors.toList());
  }

  public ApiWalkStep mapWalkStep(WalkStep domain) {
    if (domain == null) {
      return null;
    }
    ApiWalkStep api = new ApiWalkStep();

    api.distance = domain.getDistance();
    api.relativeDirection = mapRelativeDirection(domain.getRelativeDirection());
    api.streetName = domain.getStreetName().toString(locale);
    api.absoluteDirection = mapAbsoluteDirection(domain.getAbsoluteDirection());
    api.exit = domain.getExit();
    api.stayOn = domain.getStayOn();
    api.area = domain.getArea();
    api.bogusName = domain.getBogusName();
    if (domain.getStartLocation() != null) {
      api.lon = domain.getStartLocation().longitude();
      api.lat = domain.getStartLocation().latitude();
    }
    api.elevation = mapElevation(domain.getRoundedElevation());
    api.walkingBike = domain.isWalkingBike();
    api.alerts = alertsMapper.mapToApi(domain.getStreetNotes());

    enrichWithAccessibilityProperties(api, domain.getAccessibilityProperties());
    return api;
  }

  private static void enrichWithAccessibilityProperties(
    ApiWalkStep api,
    AccessibilityPropertySet accessibilityProperties
  ) {
    OptionalDouble width = accessibilityProperties.getWidth();
    api.width = width.isPresent() ? width.getAsDouble() : null;

    OptionalBoolean lit = accessibilityProperties.getLit();
    api.lit = lit.isPresent() ? lit.getAsTyped() : null;

    OptionalEnum surface = accessibilityProperties.getSurface();
    api.surface = surface.isPresent() ? surface.getAsTyped().name() : null;
    OptionalBoolean tactile_paving = accessibilityProperties.getTactilePaving();

    api.tactilePaving = tactile_paving.isPresent() ? tactile_paving.getAsTyped() : null;
    OptionalEnum smoothness = accessibilityProperties.getSmoothness();
    api.smoothness = smoothness.isPresent() ? smoothness.getAsTyped().name() : null;

    OptionalEnum highway = accessibilityProperties.getHighway();
    api.highway = highway.isPresent() ? highway.getAsTyped().name() : null;
    OptionalEnum footway = accessibilityProperties.getFootway();

    api.footway = footway.isPresent() ? footway.getAsTyped().name() : null;
    OptionalEnumAndDouble incline = accessibilityProperties.getIncline();

    api.incline = incline.isPresent() ? incline.getAsTyped().toString() : null;
    OptionalDouble travHTrt = accessibilityProperties.getTravHTrt();

    api.travHTrt = travHTrt.isPresent() ? travHTrt.getAsDouble() : null;
  }
}
