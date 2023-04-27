package org.opentripplanner.graph_builder.module.osm;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.opentripplanner.graph_builder.DataImportIssueStore.noopIssueStore;

import java.io.File;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;
import java.util.OptionalDouble;
import java.util.Set;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.opentripplanner.graph_builder.module.osm.tagmapping.DefaultMapper;
import org.opentripplanner.openstreetmap.OpenStreetMapProvider;
import org.opentripplanner.openstreetmap.model.OSMSurface;
import org.opentripplanner.openstreetmap.model.OptionalBoolean;
import org.opentripplanner.openstreetmap.model.OptionalEnum;
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

  @Test
  public void testBuildGraphWithWidth() {
    int succeed = 0;
    IntersectionVertex edgeFromWithWidth = (IntersectionVertex) grenobleGraph.getVertex(
      "osm:node:-1659017"
    );
    IntersectionVertex edgeToWithWidth = (IntersectionVertex) grenobleGraph.getVertex(
      "osm:node:-1659280"
    );

    for (StreetEdge edge : grenobleGraph.getStreetEdges()) {
      OptionalDouble width = edge.getAccessibilityProperties().getWidth();
      if (
        (
          edge.getFromVertex().equals(edgeFromWithWidth) &&
            edge.getToVertex().equals(edgeToWithWidth)
        ) ||
          (
            edge.getFromVertex().equals(edgeToWithWidth) &&
              edge.getToVertex().equals(edgeFromWithWidth)
          )
      ) {
        assertTrue(width.isPresent());
        assertEquals(170.0, width.getAsDouble());
        succeed++;
      }
    }
    assertEquals(2, succeed);
  }

  @Test
  public void testBuildGraphWithoutWidth() {
    int succeed = 0;
    IntersectionVertex edgeFromWithoutWidth = (IntersectionVertex) grenobleGraph.getVertex(
      "osm:node:-1656814"
    );
    IntersectionVertex edgeToWithoutWidth = (IntersectionVertex) grenobleGraph.getVertex(
      "osm:node:-1659965"
    );

    for (StreetEdge edge : grenobleGraph.getStreetEdges()) {
      OptionalDouble width = edge.getAccessibilityProperties().getWidth();
      if (
        (
          edge.getFromVertex().equals(edgeFromWithoutWidth) &&
            edge.getToVertex().equals(edgeToWithoutWidth)
        ) ||
          (
            edge.getFromVertex().equals(edgeToWithoutWidth) &&
              edge.getToVertex().equals(edgeFromWithoutWidth)
          )
      ) {
        assertTrue(width.isEmpty());
        succeed++;
      }
    }
    assertEquals(2, succeed);
  }

  @Test
  public void testBuildGraphWithLit() {
    int succeed = 0;
    IntersectionVertex edgeFromWithLit = (IntersectionVertex) grenobleGraph.getVertex(
      "osm:node:-1656814"
    );
    IntersectionVertex edgeToWithLit = (IntersectionVertex) grenobleGraph.getVertex(
      "osm:node:-1659965"
    );

    for (StreetEdge edge : grenobleGraph.getStreetEdges()) {
      OptionalBoolean lit = edge.getAccessibilityProperties().getLit();
      if (
        (
          edge.getFromVertex().equals(edgeFromWithLit) && edge.getToVertex().equals(edgeToWithLit)
        ) ||
          (edge.getFromVertex().equals(edgeToWithLit) && edge.getToVertex().equals(edgeFromWithLit))
      ) {
        assertTrue(lit.isPresent());
        assertTrue(lit.getAsBoolean());
        succeed++;
      }
    }
    assertEquals(2, succeed);
  }

  @Test
  public void testBuildGraphWithoutLit() {
    int succeed = 0;
    IntersectionVertex edgeFromWithoutLight = (IntersectionVertex) grenobleGraph.getVertex(
      "osm:node:-1660332"
    );
    IntersectionVertex edgeToWithoutLight = (IntersectionVertex) grenobleGraph.getVertex(
      "osm:node:-1661950"
    );

    for (StreetEdge edge : grenobleGraph.getStreetEdges()) {
      OptionalBoolean lit = edge.getAccessibilityProperties().getLit();
      if (
        (
          edge.getFromVertex().equals(edgeFromWithoutLight) &&
            edge.getToVertex().equals(edgeToWithoutLight)
        ) ||
          (
            edge.getFromVertex().equals(edgeToWithoutLight) &&
              edge.getToVertex().equals(edgeFromWithoutLight)
          )
      ) {
        assertTrue(lit.isPresent());
        assertFalse(lit.getAsBoolean());
        succeed++;
      }
    }
    assertEquals(2, succeed);
  }

  @Test
  public void testBuildGraphWithSurface() {
    int succeed = 0;
    IntersectionVertex edgeFromWithSurface = (IntersectionVertex) grenobleGraph.getVertex(
      "osm:node:-1656814"
    );
    IntersectionVertex edgeToWithSurface = (IntersectionVertex) grenobleGraph.getVertex(
      "osm:node:-1659965"
    );

    for (StreetEdge edge : grenobleGraph.getStreetEdges()) {
      OptionalEnum surface = edge.getAccessibilityProperties().getSurface();
      if (
        (
          edge.getFromVertex().equals(edgeFromWithSurface) &&
            edge.getToVertex().equals(edgeToWithSurface)
        ) ||
          (
            edge.getFromVertex().equals(edgeToWithSurface) &&
              edge.getToVertex().equals(edgeFromWithSurface)
          )
      ) {
        assertTrue(surface.isPresent());
        assertSame(OSMSurface.asphalt, surface.getAsEnum());
        succeed++;
      }
    }
    assertEquals(2, succeed);
  }

  @Test
  public void testBuildGraphWithoutSurface() {
    int succeed = 0;
    IntersectionVertex edgeFromWithoutSurface = (IntersectionVertex) grenobleGraph.getVertex(
      "osm:node:-1660442"
    );
    IntersectionVertex edgeToWithoutSurface = (IntersectionVertex) grenobleGraph.getVertex(
      "osm:node:-1658768"
    );

    for (StreetEdge edge : grenobleGraph.getStreetEdges()) {
      OptionalEnum surface = edge.getAccessibilityProperties().getSurface();
      if (
        (
          edge.getFromVertex().equals(edgeFromWithoutSurface) &&
            edge.getToVertex().equals(edgeToWithoutSurface)
        ) ||
          (
            edge.getFromVertex().equals(edgeToWithoutSurface) &&
              edge.getToVertex().equals(edgeFromWithoutSurface)
          )
      ) {
        assertTrue(surface.isEmpty());
        succeed++;
      }
    }
    assertEquals(2, succeed);
  }

  @Test
  public void testBuildGraphWithTactilePaving() {
    int succeed = 0;
    IntersectionVertex edgeFromWithTactilePaving = (IntersectionVertex) grenobleGraph.getVertex(
      "osm:node:-1658611"
    );
    IntersectionVertex edgeToWithTactilePaving = (IntersectionVertex) grenobleGraph.getVertex(
      "osm:node:-1659936"
    );

    for (StreetEdge edge : grenobleGraph.getStreetEdges()) {
      OptionalBoolean tactilePaving = edge.getAccessibilityProperties().getTactilePaving();
      if (
        (edge.getFromVertex().equals(edgeFromWithTactilePaving) && edge.getToVertex().equals(edgeToWithTactilePaving))
          || (edge.getFromVertex().equals(edgeToWithTactilePaving) && edge.getToVertex().equals(edgeFromWithTactilePaving))
      ) {
        assertTrue(tactilePaving.isPresent());
        assertTrue(tactilePaving.getAsBoolean());
        succeed++;
      }
    }
    assertEquals(2, succeed);
  }

  @Test
  public void testBuildGraphWithoutTactilePaving() {
    int succeed = 0;
    IntersectionVertex edgeFromWithoutTactilePaving = (IntersectionVertex) grenobleGraph.getVertex(
      "osm:node:-1660332"
    );
    IntersectionVertex edgeToWithoutTactilePaving = (IntersectionVertex) grenobleGraph.getVertex(
      "osm:node:-1661950"
    );
    IntersectionVertex edgeFromWithFalseTactilePaving = (IntersectionVertex) grenobleGraph.getVertex(
      "osm:node:-1657652"
    );
    IntersectionVertex edgeToWithFalseTactilePaving = (IntersectionVertex) grenobleGraph.getVertex(
      "osm:node:-1658679"
    );


    for (StreetEdge edge : grenobleGraph.getStreetEdges()) {
      OptionalBoolean tactilePaving = edge.getAccessibilityProperties().getTactilePaving();
      if (
        (
          edge.getFromVertex().equals(edgeFromWithoutTactilePaving) &&
            edge.getToVertex().equals(edgeToWithoutTactilePaving)
        ) ||
          (
            edge.getFromVertex().equals(edgeToWithoutTactilePaving) &&
              edge.getToVertex().equals(edgeFromWithoutTactilePaving)
          )
      ) {
        assertTrue(tactilePaving.isEmpty());
        succeed++;
      }
      if (
        (
          edge.getFromVertex().equals(edgeFromWithFalseTactilePaving) &&
            edge.getToVertex().equals(edgeToWithFalseTactilePaving)
        ) ||
          (
            edge.getFromVertex().equals(edgeToWithFalseTactilePaving) &&
              edge.getToVertex().equals(edgeFromWithFalseTactilePaving)
          )
      ) {
        assertTrue(tactilePaving.isPresent());
        assertFalse(tactilePaving.getAsBoolean());
        succeed++;
      }
    }
    assertEquals(4, succeed);
  }
}
