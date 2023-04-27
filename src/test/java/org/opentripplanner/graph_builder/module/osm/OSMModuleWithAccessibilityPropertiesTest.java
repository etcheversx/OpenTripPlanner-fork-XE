package org.opentripplanner.graph_builder.module.osm;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.opentripplanner.graph_builder.DataImportIssueStore.noopIssueStore;

import java.io.File;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.opentripplanner.graph_builder.module.osm.tagmapping.DefaultMapper;
import org.opentripplanner.openstreetmap.OpenStreetMapProvider;
import org.opentripplanner.openstreetmap.model.OSMSurface;
import org.opentripplanner.openstreetmap.model.OptionalBoolean;
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
    int succeed = 0;
    IntersectionVertex fromVertex = (IntersectionVertex) grenobleGraph.getVertex(
      "osm:node:" + fromId
    );
    IntersectionVertex toVertex = (IntersectionVertex) grenobleGraph.getVertex("osm:node:" + toId);

    for (StreetEdge edge : grenobleGraph.getStreetEdges()) {
      if (
        (edge.getFromVertex().equals(fromVertex) && edge.getToVertex().equals(toVertex)) ||
        (edge.getFromVertex().equals(toVertex) && edge.getToVertex().equals(fromVertex))
      ) {
        boolean presence = checkedPropertyPresence.apply(edge);
        assertEquals(expectedPresence, presence);
        if (expectedPresence) {
          assertEquals(expectedValue, checkedPropertyValue.apply(edge));
        }
        succeed++;
      }
    }
    assertEquals(2, succeed);
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
  @CsvSource({ "-1659017, -1659280, true, 170.0", "-1656814, -1659965, false, " })
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
    return edge.getAccessibilityProperties().getLit().getAsBoolean();
  }

  @ParameterizedTest(
    name = "On edge from {0} to {1} lit expected presence is {2} and expected value is {3}"
  )
  @CsvSource({ "-1656814, -1659965, true, true", "-1660332, -1661950, true, false" })
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
    return edge.getAccessibilityProperties().getSurface().getAsEnum();
  }

  @ParameterizedTest(
    name = "On edge from {0} to {1} surface expected presence is {2} and expected value is {3}"
  )
  @CsvSource({ "-1656814, -1659965, true, asphalt", "-1660442, -1658768, false, " })
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
    return edge.getAccessibilityProperties().getTactilePaving().getAsBoolean();
  }

  @ParameterizedTest(
    name = "On edge from {0} to {1} tactilePaving expected presence is {2} and expected value is {3}"
  )
  @CsvSource(
    {
      "-1658611, -1659936, true, true",
      "-1660332, -1661950, false, ",
      "-1657652, -1658679, true, false",
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
}