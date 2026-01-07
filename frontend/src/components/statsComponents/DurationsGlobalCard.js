import React from 'react';
import { Card, CardBody, CardTitle, ListGroup, ListGroupItem } from 'reactstrap';
import { fmtMins } from '../../util/formatters';

export default function DurationsGlobalCard({ avgDurationMinutes, maxDurationMinutes, minDurationMinutes }) {
  return (
    <Card className="mb-3 stat-card">
      <CardBody>
        <CardTitle tag="h6" className="stat-title">Durations — Global</CardTitle>
        <ListGroup flush>
          <ListGroupItem className="d-flex justify-content-between align-items-center">
            <span>Avg duration per game (min)</span>
            <strong>{fmtMins(avgDurationMinutes)}</strong>
          </ListGroupItem>
          <ListGroupItem className="d-flex justify-content-between align-items-center">
            <span>Max duration (min)</span>
            <strong>{fmtMins(maxDurationMinutes)}</strong>
          </ListGroupItem>
          <ListGroupItem className="d-flex justify-content-between align-items-center">
            <span>Min duration (min)</span>
            <strong>{fmtMins(minDurationMinutes)}</strong>
          </ListGroupItem>
        </ListGroup>
      </CardBody>
    </Card>
  );
}
