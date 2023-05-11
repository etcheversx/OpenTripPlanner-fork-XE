package org.opentripplanner.graph_builder.module.osm;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.opentripplanner.graph_builder.DataImportIssueStore.noopIssueStore;

import java.io.File;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.opentripplanner.graph_builder.module.osm.tagmapping.DefaultMapper;
import org.opentripplanner.openstreetmap.OpenStreetMapProvider;
import org.opentripplanner.openstreetmap.model.OSMFootway;
import org.opentripplanner.openstreetmap.model.OSMHighway;
import org.opentripplanner.openstreetmap.model.OSMSmoothness;
import org.opentripplanner.openstreetmap.model.OSMSurface;
import org.opentripplanner.openstreetmap.model.OptionalEnumAndDouble;
import org.opentripplanner.routing.edgetype.StreetEdge;
import org.opentripplanner.routing.graph.Graph;
import org.opentripplanner.routing.vertextype.IntersectionVertex;
import org.opentripplanner.transit.model.framework.Deduplicator;

public class OSMModuleWithAccessibilityPropertiesTest {

  private static Graph grenobleGraph;

  @BeforeAll
  public static void globalSetup() {
    var deduplicator = new Deduplicator();
    grenobleGraph = new Graph(deduplicator);

    File file = new File(
      URLDecoder.decode(
        Objects
          .requireNonNull(
            OSMModuleWithAccessibilityPropertiesTest.class.getResource(
                "grenoble_secteur_verdun.osm.pbf"
              )
          )
          .getFile(),
        StandardCharsets.UTF_8
      )
    );
    OpenStreetMapProvider provider = new OpenStreetMapProvider(file, true);
    OpenStreetMapModule osmModule = new OpenStreetMapModule(
      List.of(provider),
      Set.of(),
      grenobleGraph,
      noopIssueStore(),
      new DefaultMapper()
    );

    osmModule.buildGraph();
  }

  private static void checkProperty(
    String fromId,
    String toId,
    Function<StreetEdge, Boolean> checkedPropertyPresence,
    Function<StreetEdge, Object> checkedPropertyValue,
    boolean expectedPresence,
    Object expectedValue
  ) {
    IntersectionVertex fromVertex = (IntersectionVertex) grenobleGraph.getVertex(
      "osm:node:" + fromId
    );
    IntersectionVertex toVertex = (IntersectionVertex) grenobleGraph.getVertex("osm:node:" + toId);

    for (StreetEdge edge : grenobleGraph.getStreetEdges()) {
      if (edge.getFromVertex().equals(fromVertex) && edge.getToVertex().equals(toVertex)) {
        boolean presence = checkedPropertyPresence.apply(edge);
        assertEquals(expectedPresence, presence);
        if (expectedPresence) {
          assertEquals(expectedValue, checkedPropertyValue.apply(edge));
        }
        return;
      }
    }
    fail("Edge to test should have been found");
  }

  private static boolean isWidthPresent(StreetEdge edge) {
    return edge.getAccessibilityProperties().getWidth().isPresent();
  }

  private static Object getWidthValue(StreetEdge edge) {
    return edge.getAccessibilityProperties().getWidth().getAsDouble();
  }

  @ParameterizedTest(
    name = "On edge from {0} to {1} width expected presence is {2} and expected value is {3}"
  )
  @CsvSource(
    {
      "-1659017, -1659280, true, 170.0",
      "-1659280, -1659017, true, 170.0",
      "-1656814, -1659965, false, ",
      "-1659965, -1656814, false, ",
    }
  )
  public void testBuildGraphWithWidth(
    String fromId,
    String toId,
    boolean expectedPresence,
    Double expectedValue
  ) {
    checkProperty(
      fromId,
      toId,
      OSMModuleWithAccessibilityPropertiesTest::isWidthPresent,
      OSMModuleWithAccessibilityPropertiesTest::getWidthValue,
      expectedPresence,
      expectedValue
    );
  }

  private static boolean isLitPresent(StreetEdge edge) {
    return edge.getAccessibilityProperties().getLit().isPresent();
  }

  private static Object getLitValue(StreetEdge edge) {
    return edge.getAccessibilityProperties().getLit().getAsTyped();
  }

  @ParameterizedTest(
    name = "On edge from {0} to {1} lit expected presence is {2} and expected value is {3}"
  )
  @CsvSource(
    {
      "-1656814, -1659965, true, true",
      "-1659965, -1656814, true, true",
      "-1660332, -1661950, true, false",
      "-1661950, -1660332, true, false",
    }
  )
  public void testBuildGraphWithLit(
    String fromId,
    String toId,
    boolean expectedPresence,
    Boolean expectedValue
  ) {
    checkProperty(
      fromId,
      toId,
      OSMModuleWithAccessibilityPropertiesTest::isLitPresent,
      OSMModuleWithAccessibilityPropertiesTest::getLitValue,
      expectedPresence,
      expectedValue
    );
  }

  private static boolean isSurfacePresent(StreetEdge edge) {
    return edge.getAccessibilityProperties().getSurface().isPresent();
  }

  private static Object getSurfaceValue(StreetEdge edge) {
    return edge.getAccessibilityProperties().getSurface().getAsTyped();
  }

  @ParameterizedTest(
    name = "On edge from {0} to {1} surface expected presence is {2} and expected value is {3}"
  )
  @CsvSource(
    {
      "-1656814, -1659965, true, asphalt",
      "-1659965, -1656814, true, asphalt",
      "-1660442, -1658768, false, ",
      "-1658768, -1660442, false, ",
    }
  )
  public void testBuildGraphWithSurface(
    String fromId,
    String toId,
    boolean expectedPresence,
    OSMSurface expectedValue
  ) {
    checkProperty(
      fromId,
      toId,
      OSMModuleWithAccessibilityPropertiesTest::isSurfacePresent,
      OSMModuleWithAccessibilityPropertiesTest::getSurfaceValue,
      expectedPresence,
      expectedValue
    );
  }

  private static boolean isTactilePavingPresent(StreetEdge edge) {
    return edge.getAccessibilityProperties().getTactilePaving().isPresent();
  }

  private static Object getTactilePavingValue(StreetEdge edge) {
    return edge.getAccessibilityProperties().getTactilePaving().getAsTyped();
  }

  @ParameterizedTest(
    name = "On edge from {0} to {1} tactilePaving expected presence is {2} and expected value is {3}"
  )
  @CsvSource(
    {
      "-1658611, -1659936, true, true",
      "-1659936, -1658611, true, true",
      "-1660332, -1661950, false, ",
      "-1661950, -1660332, false, ",
      "-1657652, -1658679, true, false",
      "-1658679, -1657652, true, false",
    }
  )
  public void testBuildGraphWithTactilePaving(
    String fromId,
    String toId,
    boolean expectedPresence,
    Boolean expectedValue
  ) {
    checkProperty(
      fromId,
      toId,
      OSMModuleWithAccessibilityPropertiesTest::isTactilePavingPresent,
      OSMModuleWithAccessibilityPropertiesTest::getTactilePavingValue,
      expectedPresence,
      expectedValue
    );
  }

  private static boolean isSmoothnessPresent(StreetEdge edge) {
    return edge.getAccessibilityProperties().getSmoothness().isPresent();
  }

  private static Object getSmoothnessValue(StreetEdge edge) {
    return edge.getAccessibilityProperties().getSmoothness().getAsTyped();
  }

  @ParameterizedTest(
    name = "On edge from {0} to {1} smoothness expected presence is {2} and expected value is {3}"
  )
  @CsvSource(
    {
      "-1659001, -1659868, true, good",
      "-1659868, -1659001, true, good",
      "-1660442, -1658768, false, ",
      "-1658768, -1660442, false, ",
    }
  )
  public void testBuildGraphWithSmoothness(
    String fromId,
    String toId,
    boolean expectedPresence,
    OSMSmoothness expectedValue
  ) {
    checkProperty(
      fromId,
      toId,
      OSMModuleWithAccessibilityPropertiesTest::isSmoothnessPresent,
      OSMModuleWithAccessibilityPropertiesTest::getSmoothnessValue,
      expectedPresence,
      expectedValue
    );
  }

  private static boolean isHighwayPresent(StreetEdge edge) {
    return edge.getAccessibilityProperties().getHighway().isPresent();
  }

  private static Object getHighwayValue(StreetEdge edge) {
    return edge.getAccessibilityProperties().getHighway().getAsTyped();
  }

  @ParameterizedTest(
    name = "On edge from {0} to {1} highway expected presence is {2} and expected value is {3}"
  )
  @CsvSource(
    {
      "-1659001, -1659868, true, footway",
      "-1659868, -1659001, true, footway",
      "-1660442, -1658768, false, ",
      "-1658768, -1660442, false, ",
    }
  )
  public void testBuildGraphWithHighway(
    String fromId,
    String toId,
    boolean expectedPresence,
    OSMHighway expectedValue
  ) {
    checkProperty(
      fromId,
      toId,
      OSMModuleWithAccessibilityPropertiesTest::isHighwayPresent,
      OSMModuleWithAccessibilityPropertiesTest::getHighwayValue,
      expectedPresence,
      expectedValue
    );
  }

  private static boolean isFootwayPresent(StreetEdge edge) {
    return edge.getAccessibilityProperties().getFootway().isPresent();
  }

  private static Object getFootwayValue(StreetEdge edge) {
    return edge.getAccessibilityProperties().getFootway().getAsTyped();
  }

  @ParameterizedTest(
    name = "On edge from {0} to {1} footway expected presence is {2} and expected value is {3}"
  )
  @CsvSource(
    {
      "-1659001, -1659868, true, sidewalk",
      "-1659868, -1659001, true, sidewalk",
      "-1660442, -1658768, false, ",
      "-1658768, -1660442, false, ",
    }
  )
  public void testBuildGraphWithFootway(
    String fromId,
    String toId,
    boolean expectedPresence,
    OSMFootway expectedValue
  ) {
    checkProperty(
      fromId,
      toId,
      OSMModuleWithAccessibilityPropertiesTest::isFootwayPresent,
      OSMModuleWithAccessibilityPropertiesTest::getFootwayValue,
      expectedPresence,
      expectedValue
    );
  }

  private static boolean isInclinePresent(StreetEdge edge) {
    return edge.getAccessibilityProperties().getIncline().isPresent();
  }

  private static Object getInclineValue(StreetEdge edge) {
    OptionalEnumAndDouble incline = edge.getAccessibilityProperties().getIncline();
    try {
      return incline.getAsObject().toString();
    } catch (Exception exc) {
      return incline.toString();
    }
  }

  @ParameterizedTest(
    name = "On edge from {0} to {1} incline expected presence is {2} and expected value is {3}"
  )
  @CsvSource(
    {
      "-1659001, -1659868, false, ",
      "-1659868, -1659001, false, ",
      "-1656494, -1661526, true, up",
      "-1661526, -1656494, true, down",
      "-1660538, -1660804, true, down",
      "-1660804, -1660538, true, up",
      "-1659017, -1659280, true, 2.0",
      "-1659280, -1659017, true, -2.0",
    }
  )
  public void testBuildGraphWithIncline(
    String fromId,
    String toId,
    boolean expectedPresence,
    String expectedValue
  ) {
    checkProperty(
      fromId,
      toId,
      OSMModuleWithAccessibilityPropertiesTest::isInclinePresent,
      OSMModuleWithAccessibilityPropertiesTest::getInclineValue,
      expectedPresence,
      expectedValue
    );
  }

  private static boolean isTravHTrtPresent(StreetEdge edge) {
    return edge.getAccessibilityProperties().getTravHTrt().isPresent();
  }

  private static Object getTravHTrtValue(StreetEdge edge) {
    return edge.getAccessibilityProperties().getTravHTrt().getAsDouble();
  }

  @ParameterizedTest(
    name = "On edge from {0} to {1} trav_h_trt expected presence is {2} and expected value is {3}"
  )
  @CsvSource(
    {
      "-1659017, -1659280, true, 0.17",
      "-1659280, -1659017, true, 0.17",
      "-1656814, -1659965, false, ",
      "-1659965, -1656814, false, ",
    }
  )
  public void testBuildGraphWithTravHTrt(
    String fromId,
    String toId,
    boolean expectedPresence,
    Double expectedValue
  ) {
    checkProperty(
      fromId,
      toId,
      OSMModuleWithAccessibilityPropertiesTest::isTravHTrtPresent,
      OSMModuleWithAccessibilityPropertiesTest::getTravHTrtValue,
      expectedPresence,
      expectedValue
    );
  }
}
