package org.opentripplanner.routing.impl;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import org.opentripplanner.routing.alertpatch.EntityKey;
import org.opentripplanner.routing.alertpatch.EntitySelector;
import org.opentripplanner.routing.alertpatch.StopCondition;
import org.opentripplanner.routing.alertpatch.StopConditionsHelper;
import org.opentripplanner.routing.alertpatch.TransitAlert;
import org.opentripplanner.routing.services.TransitAlertService;
import org.opentripplanner.transit.model.framework.FeedScopedId;
import org.opentripplanner.transit.model.timetable.Direction;
import org.opentripplanner.transit.service.TransitModel;

/**
 * When an alert is added with more than one transit entity, e.g. a Stop and a Trip, both conditions
 * must be met for the alert to be displayed. This is the case in both the Norwegian interpretation
 * of SIRI, and the GTFS-RT alerts specification.
 */
public class TransitAlertServiceImpl implements TransitAlertService {

  private final TransitModel transitModel;

  private Multimap<EntityKey, TransitAlert> alerts = HashMultimap.create();

  public TransitAlertServiceImpl(TransitModel transitModel) {
    this.transitModel = transitModel;
  }

  @Override
  public void setAlerts(Collection<TransitAlert> alerts) {
    Multimap<EntityKey, TransitAlert> newAlerts = HashMultimap.create();
    for (TransitAlert alert : alerts) {
      for (EntitySelector entity : alert.getEntities()) {
        newAlerts.put(entity.key(), alert);
      }
    }

    this.alerts = newAlerts;
  }

  @Override
  public Collection<TransitAlert> getAllAlerts() {
    return new HashSet<>(alerts.values());
  }

  @Override
  public TransitAlert getAlertById(String id) {
    return alerts
      .values()
      .stream()
      .filter(transitAlert -> transitAlert.getId().equals(id))
      .findAny()
      .orElse(null);
  }

  @Override
  public Collection<TransitAlert> getStopAlerts(
    FeedScopedId stopId,
    Set<StopCondition> stopConditions
  ) {
    Set<TransitAlert> result = new HashSet<>();
    EntitySelector.Stop entitySelector = new EntitySelector.Stop(stopId, stopConditions);
    for (TransitAlert alert : alerts.get(entitySelector.key())) {
      if (alert.getEntities().stream().anyMatch(selector -> selector.matches(entitySelector))) {
        result.add(alert);
      }
    }
    if (result.isEmpty()) {
      // Search for alerts on parent-stop
      if (transitModel != null) {
        var quay = transitModel.getStopModel().getRegularStop(stopId);
        if (quay != null) {
          // TODO - SIRI: Add alerts from parent- and multimodal-stops
          /*
                    if ( quay.isPartOfStation()) {
                        // Add alerts for parent-station
                        result.addAll(patchesByStop.getOrDefault(quay.getParentStationFeedScopedId(), Collections.emptySet()));
                    }
                    if (quay.getMultiModalStation() != null) {
                        // Add alerts for multimodal-station
                        result.addAll(patchesByStop.getOrDefault(new FeedScopedId(stop.getAgencyId(), quay.getMultiModalStation()), Collections.emptySet()));
                    }
                    */
        }
      }
    }
    return result;
  }

  @Override
  public Collection<TransitAlert> getRouteAlerts(FeedScopedId route) {
    return alerts.get(new EntityKey.Route(route));
  }

  @Override
  public Collection<TransitAlert> getTripAlerts(FeedScopedId trip, LocalDate serviceDate) {
    Set<TransitAlert> result = new HashSet<>();
    EntitySelector.Trip entitySelector = new EntitySelector.Trip(trip, serviceDate);
    for (TransitAlert alert : alerts.get(entitySelector.key())) {
      if (alert.getEntities().stream().anyMatch(selector -> selector.matches(entitySelector))) {
        result.add(alert);
      }
    }
    return result;
  }

  @Override
  public Collection<TransitAlert> getAgencyAlerts(FeedScopedId agency) {
    return alerts.get(new EntityKey.Agency(agency));
  }

  @Override
  public Collection<TransitAlert> getStopAndRouteAlerts(
    FeedScopedId stop,
    FeedScopedId route,
    Set<StopCondition> stopConditions
  ) {
    Set<TransitAlert> result = new HashSet<>();
    EntitySelector.StopAndRoute entitySelector = new EntitySelector.StopAndRoute(
      stop,
      route,
      stopConditions
    );
    for (TransitAlert alert : alerts.get(entitySelector.key())) {
      if (alert.getEntities().stream().anyMatch(selector -> selector.matches(entitySelector))) {
        result.add(alert);
      }
    }
    return result;
  }

  @Override
  public Collection<TransitAlert> getStopAndTripAlerts(
    FeedScopedId stop,
    FeedScopedId trip,
    LocalDate serviceDate,
    Set<StopCondition> stopConditions
  ) {
    Set<TransitAlert> result = new HashSet<>();
    EntitySelector.StopAndTrip entitySelector = new EntitySelector.StopAndTrip(
      stop,
      trip,
      serviceDate,
      stopConditions
    );
    for (TransitAlert alert : alerts.get(entitySelector.key())) {
      if (alert.getEntities().stream().anyMatch(selector -> selector.matches(entitySelector))) {
        result.add(alert);
      }
    }
    return result;
  }

  @Override
  public Collection<TransitAlert> getRouteTypeAndAgencyAlerts(int routeType, FeedScopedId agency) {
    return alerts.get(new EntityKey.RouteTypeAndAgency(agency, routeType));
  }

  @Override
  public Collection<TransitAlert> getRouteTypeAlerts(int routeType, String feedId) {
    return alerts.get(new EntityKey.RouteType(feedId, routeType));
  }

  @Override
  public Collection<TransitAlert> getDirectionAndRouteAlerts(
    Direction direction,
    FeedScopedId route
  ) {
    return alerts.get(new EntityKey.DirectionAndRoute(route, direction));
  }
}
