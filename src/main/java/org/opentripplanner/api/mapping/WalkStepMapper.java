package org.opentripplanner.api.mapping;

import static org.opentripplanner.api.mapping.AbsoluteDirectionMapper.mapAbsoluteDirection;
import static org.opentripplanner.api.mapping.ElevationMapper.mapElevation;
import static org.opentripplanner.api.mapping.RelativeDirectionMapper.mapRelativeDirection;

import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.opentripplanner.api.model.ApiWalkStep;
import org.opentripplanner.graph_builder.module.osm.AccessibilityPropertySet;
import org.opentripplanner.model.plan.WalkStep;
import org.opentripplanner.routing.graph.Edge;
import org.opentripplanner.routing.graph.Vertex;

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

    enrichWithAccessibilityProperties(api, domain);
    return api;
  }

  private static void enrichWithAccessibilityProperties(ApiWalkStep api, WalkStep domain) {
    AccessibilityPropertySet accessibilityProperties = domain.getAccessibilityProperties();
    var width = accessibilityProperties.getWidth();
    api.width = width.isPresent() ? width.getAsTyped() : null;

    var lit = accessibilityProperties.getLit();
    api.lit = lit.isPresent() ? lit.getAsTyped() : null;

    var surface = accessibilityProperties.getEnumValue("surface");
    api.surface = surface.isPresent() ? surface.getAsTyped().name() : null;

    var tactile_paving = accessibilityProperties.getTactilePaving();
    api.tactilePaving = tactile_paving.isPresent() ? tactile_paving.getAsTyped() : null;

    var smoothness = accessibilityProperties.getEnumValue("smoothness");
    api.smoothness = smoothness.isPresent() ? smoothness.getAsTyped().name() : null;

    var highway = accessibilityProperties.getEnumValue("highway");
    api.highway = highway.isPresent() ? highway.getAsTyped().name() : null;

    var footway = accessibilityProperties.getEnumValue("footway");
    api.footway = footway.isPresent() ? footway.getAsTyped().name() : null;

    var incline = accessibilityProperties.getIncline();
    api.incline = incline.isPresent() ? incline.getAsTyped().toString() : null;

    var ressautMax = accessibilityProperties.getRessautMax();
    api.ressautMax = ressautMax.isPresent() ? ressautMax.getAsTyped() : null;

    var ressautMin = accessibilityProperties.getRessautMin();
    api.ressautMin = ressautMin.isPresent() ? ressautMin.getAsTyped() : null;

    var bevEtat = accessibilityProperties.getEnumValue("wgt:bev_etat");
    api.bevEtat = bevEtat.isPresent() ? bevEtat.getAsTyped().toString() : null;

    var bevCtrast = accessibilityProperties.getBevCtrast();
    api.bevCtrast = bevCtrast.isPresent() ? bevCtrast.getAsTyped() : null;

    api.edges = domain.getEdges().stream().map((Function<? super Edge, String>) edge -> {
      Vertex fromVertex = edge.getFromVertex();
      Vertex toVertex = edge.getToVertex();
      return "from: " + (fromVertex != null ? fromVertex.toString() : "null") + ", " +
        "to: " + (toVertex != null ? toVertex.toString() : "null");
    }).toList();
  }
}
